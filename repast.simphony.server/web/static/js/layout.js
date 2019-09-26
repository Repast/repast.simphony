import { Display2D } from '/js/display.js';
import { LeafletDisplay } from '/js/display_leaflet.js';
import { empty } from '/js/utils.js';
import { ITownsDisplay } from '/js/display_itowns.js';

let current_layout = "tabs";
let tabs = [];
let displays = new Map();
let selected = null;
let tab_display_attributes = new Map();
let ff_display_attributes = new Map();

export function createLayout(display_data, display_updates, on_picked) {
    console.log(current_layout);
    if (display_data.length > 0) {
        if (current_layout === 'tabs') {
            $("#display-freeform-container").hide();
            createTabs(display_data);
            let parents = []
            tabs.forEach ( (tab) => {
                parents.push(tab.tab_content);
            });
            createDisplays(display_data, display_updates, on_picked, parents);
            tabs.forEach( (tab) => {
                let display = displays.get(tab.display_id);
                $('#display-tabs a[href="#' + tab.tab_id + '"]').on('shown.bs.tab', display.checkResize.bind(display));
            });
            if (tab_display_attributes.size > 0) {
                displays.forEach((display) => {
                    updateInteractAttributes(display.container, tab_display_attributes, display.display_id);
                });
            }
            $("#display-tab-container").show();
            displays.forEach((display) => {
                display.windowResize();
            });

        } else {
            $("#display-tab-container").hide();
            createFreeformLayout(display_data, display_updates, on_picked);
            if (ff_display_attributes.size > 0) {
                displays.forEach((display) => {
                    updateInteractAttributes(display.container, ff_display_attributes, display.display_id);
                });
            }
            $("#display-freeform-container").show();

            displays.forEach((display) => {
                display.windowResize();
            }); 
        }
    }
    return displays;
}

export function layoutSelected(val) {
    // display-tab-container
    // display-freeform-container
    if (this.value === 'freeform') {
        $("#display-tab-container").hide();
        let parent = document.getElementById("display-freeform-container");
        tabs.forEach((tab) => {
            let display = displays.get(tab.display_id);
            display.container.parentElement.removeChild(display.container);
            saveInteractAttributes(display.container, tab_display_attributes, tab.display_id);
            updateInteractAttributes(display.container, ff_display_attributes, tab.display_id);
            parent.appendChild(display.container);
        });
       
        $("#display-freeform-container").show();
         displays.forEach( (display) => {
            display.windowResize();
        }); 
    } else {
        if (tabs.length == 0) {
            let disp_data = [];
            displays.forEach( (display) => {
                disp_data.push({
                    display_id: display.display_id,
                    name: display.name
                });
            });
            createTabs(disp_data);
            tabs.forEach((tab) => {
                let display = displays.get(tab.display_id);
                $('#display-tabs a[href="#' + tab.tab_id + '"]').on('shown.bs.tab', display.checkResize.bind(display));
            });
        }
        $("#display-freeform-container").hide();

        tabs.forEach( (tab) => {
            let display = displays.get(tab.display_id);
            display.container.parentElement.removeChild(display.container);
            saveInteractAttributes(display.container, ff_display_attributes, tab.display_id);
            updateInteractAttributes(display.container, tab_display_attributes, tab.display_id);
            tab.tab_content.appendChild(display.container);
        });
        
        $("#display-tab-container").show();

        displays.forEach((display) => {
            display.windowResize();
            display.resize = true;
        });
    }
    current_layout = this.value;
}

function saveInteractAttributes(target, display_attributes, display_id) {
    display_attributes.set(display_id, {
        transform: (target.style.transform ? target.style.transform : ""),
        data_x: (target.hasAttribute("data-x") ? target.getAttribute("data-x") : 0),
        data_y: (target.hasAttribute("data-y") ? target.getAttribute("data-y") : 0),
        width: (target.style.width ? target.style.width : ""),
        height: (target.style.height ? target.style.height : "")
    });
}

function updateInteractAttributes(target, display_attributes, display_id) {
    if (display_attributes.has(display_id)) {
        let attribs = display_attributes.get(display_id);
        target.style.webkitTransform = target.style.transform = attribs.transform;
        target.setAttribute('data-x', attribs.data_x);
        target.setAttribute('data-y', attribs.data_y);
        target.style.width = attribs.width;
        target.style.height = attribs.height;
    } else {
        target.style.webkitTransform = target.style.transform = "";
        target.removeAttribute('data-x');
        target.removeAttribute('data-y');
        target.style.width = "";
        target.style.height = "";
    }

}

function createFreeformLayout(display_data, display_updates, on_picked) {
    let parent = document.getElementById("display-freeform-container");
    // let width = parent.clientWidth;
    // // 620 based on hard coded CSS values
    // let cols = Math.floor(width / 620);
    // parent.style.gridTemplateColumns = `repeat(${cols}, 1fr)`;
    // parent.style.gridGap = "15px";
    // parent.style.padding = "15px";
    
    let parents = Array(display_data.length).fill(parent);
    createDisplays(display_data, display_updates, on_picked, parents);
}


function createDisplayTab(tab_id, name) {
    // Get a reference to the container element that will hold our scene
    let displays_container = document.getElementById("display-tabs")
    let selected = false; //displays_container.childElementCount == 0;
    let tab = document.createElement("li");
    tab.className = "nav-item";
    let tab_anchor = document.createElement("a");
    tab_anchor.className = (selected ? "nav-link active" : "nav-link");
    let tab_anchor_id = `${tab_id}-tab`;
    tab_anchor.id = tab_anchor_id;

    tab_anchor.setAttribute("data-toggle", "tab");
    tab_anchor.setAttribute("role", "tab");
    tab_anchor.setAttribute("aria-controls", tab_id);
    tab_anchor.setAttribute("href", `#${tab_id}`);

    tab_anchor.setAttribute("aria-selected", (selected ? "true" : "false"));
    tab_anchor.innerHTML = name;

    let display_content = document.getElementById("display-content");
    let tab_content = document.createElement("div");
    tab_content.className = (selected ? "tab-pane fade show active" : "tab-pane fade");
    tab_content.id = tab_id;
    tab_content.setAttribute("role", "tabpanel");
    tab_content.setAttribute("aria-labelledby", tab_anchor_id);

    display_content.appendChild(tab_content);
    tab.appendChild(tab_anchor);
    displays_container.appendChild(tab);

    return {
        tab_content: tab_content,
        tab_li: tab,
        tab_id: tab_id
    };
}

function createTabs(display_data) {
    tabs = [];
    display_data.forEach((msg) => {
        let tab_id = `t${msg.display_id}`.replace(' ', '_');
        let t = createDisplayTab(tab_id, msg.name);
        t.display_id = msg.display_id;
        tabs.push(t);
    });

    if (display_data.length > 0) {
        $('#display-tabs a[href="#' + tabs[0].tab_id + '"]').tab('show');
    }
}

function createDisplays(display_data, display_updates, on_picked, parents) {
    display_data.forEach((msg, i) => {
        let name = msg.name;
        let type = msg.type;
        let disp_id = msg.display_id;
        let disp_bbox = msg.bbox;

        if (type === "2D") {
            let display = new Display2D(name, disp_id, disp_bbox, parents[i]);
            const promises = display.init(msg);
            displays.set(disp_id, display);
            display.picked = on_picked;
            Promise.all(promises).then(() => {
                if (display_updates.has(disp_id)) {
                    let dv = display_updates.get(disp_id);
                    display_updates.delete(disp_id);
                    display.updateBinary(dv);
                }
            });
        }
        else if (type === "GIS 3D") {
            let display = new LeafletDisplay(name, parents[i], disp_id);
            //let display = new ITownsDisplay(name, tabs[i], disp_id);
            display.init(msg);
            displays.set(disp_id, display);
            if (display_updates.has(disp_id)) {
                let msg = display_updates.get(disp_id);
                display_updates.delete(disp_id);
                display.update(msg);
            }
        }
    });
    interact('.card-display-container').draggable({
        onmove: window.dragMoveListener,
        allowFrom: '.card-header',
        listeners: {
            start(event) {
                if (selected) {
                    selected.style.zIndex = 1;
                }
                selected = event.target;
                event.target.style.zIndex = 10;
            }
        },
        modifiers: [
            interact.modifiers.restrictRect({
                restriction: 'parent',
                endOnly: true
            })
        ],

        // cursorChecker: (action, interatable, element, interacting) => {
        //     return 'default';
        // }
    })
        .resizable({
            edges: { left: true, right: true, bottom: true, top: true },

            modifiers: [
                // keep the edges inside the parent
                interact.modifiers.restrictEdges({
                    outer: 'parent',
                    endOnly: true
                })
            ],
            inertia: true
        })
        .on('resizemove', function (event) {
            var target = event.target
            var x = (parseFloat(target.getAttribute('data-x')) || 0)
            var y = (parseFloat(target.getAttribute('data-y')) || 0)

            // update the element's style
            target.style.width = event.rect.width + 'px'
            target.style.height = event.rect.height + 'px'

            // translate when resizing from top or left edges
            x += event.deltaRect.left
            y += event.deltaRect.top

            target.style.webkitTransform = target.style.transform =
                'translate(' + x + 'px,' + y + 'px)'

            target.setAttribute('data-x', x)
            target.setAttribute('data-y', y)

            let did = target.getAttribute("display_id");
            displays.get(parseInt(did, 10)).windowResize();
        });
}

export function destroyLayout() {
    if (current_layout == 'tabs') {
        displays.forEach((display) => {
            saveInteractAttributes(display.container, tab_display_attributes, display.display_id);
        });
    } else {
        displays.forEach((display) => {
            saveInteractAttributes(display.container, ff_display_attributes, display.display_id);
        });
    }

    displays.forEach((disp) => { disp.destroy() });
    displays.clear();

    tabs.forEach( (tab) => {
        empty(tab.tab_li);
        empty(tab.tab_content);
        document.getElementById("display-tabs").removeChild(tab.tab_li);
        document.getElementById("display-content").removeChild(tab.tab_content);
    });
    tabs = [];

    empty(document.getElementById("display-freeform-container"));
}
