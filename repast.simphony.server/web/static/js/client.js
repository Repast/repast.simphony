import { TSChart } from '/js/chart.js';
import { createTab } from '/js/chart.js';
import { Parameters } from '/js/parameters.js';
import { DataViewWrapper } from '/js/utils.js';
import { layoutSelected, createLayout, destroyLayout } from '/js/layout.js';
import { createProbes, getPickedIds, clearProbes } from '/js/probe.js';

let charts = new Map();
let params = new Parameters();
let displays = new Map();
let chart_data = new Array();
let display_data = new Array();
let socket = null;
let tick_span = null;
let display_updates = new Map();

var runner = (function() {
    // Structure is a JS module organization pattern
    // https://addyosmani.com/resources/essentialjsdesignpatterns/book/#modulepatternjavascript
    var obj = {};

    var SimState = {
        INITIALIZED: 0,
        STARTED: 1,
        PAUSED: 2,
        STOPPED: 3,
        QUIT: 4
    };

    var sim_state = SimState.STOPPED;

    obj.sendParams = (socket) => {
        if (sim_state == SimState.STOPPED) {
            params.clear_errors();
            let msg = {
                type: 'cmd',
                value: 'init_params',
                params: params.get_values()
            };
            console.log("Sending Params");
            socket.send(JSON.stringify(msg));
            return true;
        }
        return false;
    };

    obj.shutdownModel = (socket) => {
        console.log("shutdown model");
        sim_state = SimState.STOPPED;
        $("#start").html("Start");
        $("#init").prop('disabled', false);
        $("#stop").prop('disabled', true);
        obj.sendCommand("shutdown_model", socket);
    };

    // Initialize Run
    obj.initClicked = (socket) => {
        console.log("init");
        if (sim_state == SimState.STOPPED) {
            obj.init();
            $("#stop").prop('disabled', false);
            $("#init").prop('disabled', true);
            $("#start").prop('disabled', false);
            $("#step").prop('disabled', false);
            obj.sendCommand('init_run', socket);
            sim_state = SimState.INITIALIZED;
            tick_span.textContent = "0";
        } 
    };

    obj.stepClicked = (socket) => {
        console.log("step");
        if (sim_state == SimState.STOPPED || sim_state == SimState.INITIALIZED) {
            if (sim_state != SimState.INITIALIZED) {
                obj.init();
                tick_span.textContent = "0";
            }
            $("#init").prop('disabled', true);
            $("#stop").prop('disabled', false);
            obj.sendCommand('step', socket);
            sim_state = SimState.PAUSED;
        }
        else if (sim_state == SimState.STARTED || sim_state == SimState.PAUSED) {
            // "Paused"
            // $("#start").html("Start");
            // sim_state = SimState.PAUSED;
            obj.sendCommand('step', socket);
            updatePicked();
        }
    };
    
    // Start Run
    obj.startClicked = (socket) => {
        console.log("start");
        if (sim_state == SimState.STOPPED || sim_state == SimState.INITIALIZED) {
            if (sim_state != SimState.INITIALIZED) {
                obj.init();
                tick_span.textContent = "0";
            }
            $("#start").html("Pause");
            $("#stop").prop('disabled', false);
            $("#init").prop('disabled', true);
            $("#step").prop('disabled', true);
            obj.sendCommand('start', socket);
            sim_state = SimState.STARTED;
        } 
        else if (sim_state == SimState.STARTED) {
            // "Paused"
            $("#start").html("Start");
            $("#step").prop('disabled', false);
            sim_state = SimState.PAUSED;
            obj.sendCommand('pause', socket);
            updatePicked();
        } 
        else if (sim_state == SimState.PAUSED) {
            sim_state = SimState.STARTED;
            $("#start").html("Pause");
            obj.sendCommand('start', socket);
        }
    };

    // Stop Run
    obj.stopClicked = (socket) => {
        if (sim_state != SimState.STOPPED) {
            sim_state = SimState.STOPPED;
            $("#start").html("Start");
            $("#init").prop('disabled', false);
            $("#start").prop('disabled', false);
            $("#step").prop('disabled', false);
            $("#stop").prop('disabled', true);
            obj.sendCommand('stop', socket);
        }
    };

    obj.quitClicked = (socket) => {
        sim_state = SimState.QUIT;
        obj.sendCommand('quit', socket);
        $("#init").prop('disabled', true);
        $("#stop").prop('disabled', true);
        $("#step").prop('disabled', true);
        $("#quit").prop('disabled', true);
        $("#start").prop('disabled', true);
    };

    obj.init = () => {
        // Destroy existing displays
        destroyLayout();

        charts.forEach( (value) => {
            value.forEach( (chart) => {
                chart.clear();
            });
        })
        charts.clear();
        chart_data = [];
        clearProbes();
        // Any other initialization
    }

    obj.updateStatus = (msg) => {
        if (msg == 'stopped' && sim_state != SimState.QUIT) {
            sim_state = SimState.STOPPED;
            $("#start").html("Start");
            $("#init").prop('disabled', false);
            $("#stop").prop('disabled', true);
            updatePicked();
        }
    }

    obj.sendCommand = (cmd, socket) => {
        var msg = {
            type: 'cmd',
            value: cmd
        };
        socket.send(JSON.stringify(msg));
    }

    obj.next = null;

    return obj;
})();



function initSocket() {
    //var socket = io.connect('http://localhost:5000');
    socket = new WebSocket('ws://localhost:5000/simphony/simsocket');

    socket.onmessage = function(evt) {
        if (evt.data instanceof Blob) {
            let fr = new FileReader();
            fr.onload = function(evt) {
                let dv = new DataViewWrapper(new DataView(fr.result), 0);
                let msg_type = dv.getInt32();
                if (msg_type === 0) {
                    let display_id = dv.getInt32();
                    if (displays.has(display_id)) {
                        displays.get(display_id).updateBinary(dv);
                    } else {
                        display_updates.set(display_id, dv);
                    }
                } else if (msg_type === 1) {
                    let tick = dv.getFloat64().toFixed(4);
                    tick_span.textContent = tick.toString();
                }
            };
            fr.readAsArrayBuffer(evt.data);
        
        } else {
            let msg = JSON.parse(evt.data);
            
            // console.log(msg);
            
            if (msg.id == 'data') { 
                if (msg.type == 'update') {
                    // console.log(msg);
                    let chart_list = charts.get(msg.dataset);
                    chart_list.forEach( (chart) => {
                        chart.update(msg.value);
                    });
                }
            } 
            else if (msg.id == 'display_update') {
                //displays.get(msg.display_id).update(msg);
                if (displays.has(msg.display_id)) {
                    displays.get(msg.display_id).update(msg);
                } else {
                    display_updates.set(msg.display_id, msg);
                }
            }
            else if (msg.id == 'status') {
                // e.g. {"id": "status", "value": "stopped"}
                if (msg.value === 'initialized') {
                    initDisplays();
                    chart_data.forEach( (item, i) => {
                        // console.log(item);
                        let tab = createTab(item.title, i);
                        let chart = new TSChart(item, tab);
                        if (charts.has(item.dataset)) {
                            charts.get(item.dataset).push(chart);
                        } else {
                            charts.set(item.dataset, [chart]);
                        }
                    });

                    if (chart_data.length > 0) {
                        // show first tab
                        $(`#chart-tabs li:first-child a`).tab('show');
                    }
                    
                } else {
                    runner.updateStatus(msg.value);
                }
            } else if (msg.id == 'display_init') {
                // initDisplay(msg);
                display_data.push(msg);

            } else if (msg.id == 'chart_init') {
                chart_data.push(msg.value);

            } else if (msg.id == 'parameters') {
                console.log(msg);
                params.reset(msg);

            } else if (msg.id == 'parameters_status') {
                if (msg.errors.length == 0) {
                    runner.next(socket);
                } else {
                    params.set_errors(msg.errors);
                }

            } else if (msg.id === 'name') {
                let name_element = document.getElementById("model_name");
                name_element.innerHTML = msg.value;

            } else if (msg.id === 'probed') {
                createProbes(msg.value, msg.display_id, msg.display_type);
            } else if (msg.id == 'heartbeat') {
                console.log(msg);
            }
        }
    }
    
    return socket;
}

function initDisplays() {
    displays = createLayout(display_data, display_updates, picked);
    display_data = [];
}

function updatePicked() {
    let pick_data = getPickedIds();
    pick_data.forEach( (p) => {
        picked(p.picked_ids, p.display_id, p.display_type);
    });
}

function picked(picked_ids, display_id, display_type) {
    console.log(`picked: ${picked_ids}`);
    var msg = {
        type: 'cmd',
        value: 'picked',
        ids: picked_ids,
        display_id: display_id,
        display_type: display_type
    };
    socket.send(JSON.stringify(msg));
}

function dragMoveListener(event) {
    var target = event.target
    // keep the dragged position in the data-x/data-y attributes
    var x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx
    var y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy

    // translate the element
    target.style.webkitTransform =
        target.style.transform =
        'translate(' + x + 'px, ' + y + 'px)'

    // update the posiion attributes
    target.setAttribute('data-x', x)
    target.setAttribute('data-y', y)
}

window.dragMoveListener = dragMoveListener;

$(document).ready(function () {
    $("#stop").prop('disabled', true);
    //$("#start").prop('disabled', true);
    //$("#step").prop('disabled', true);

    initSocket();
    
    $("#init").on('click', () => {
        runner.next = runner.initClicked;
        runner.sendParams(socket);
    });
    
    $("#start").on('click', () => {
        runner.next = runner.startClicked;
        if (!runner.sendParams(socket)) {
            // no need to wait for params OK message as they've
            // already been sent
            runner.startClicked(socket);
        }
    });

    $("#stop").on('click', () => {
        runner.stopClicked(socket);
    });

    $("#step").on('click', () => {
        runner.next = runner.stepClicked;
        if (!runner.sendParams(socket)) {
            // no need to wait for params OK message as they've
            // already been sent
            runner.stepClicked(socket);
        }
    });

    $("#quit").on('click', () => {
        runner.quitClicked(socket);
    });

    $("#shutdown-model").click( (e) => {
        e.preventDefault();
        runner.shutdownModel(socket);
    });

    tick_span = document.getElementById("tick_count");
    let layout_select = document.getElementById("layout-select");
    layout_select.onchange = layoutSelected;
    layout_select.value = 'tabs';
});
