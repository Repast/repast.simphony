var data = {
    labels: [],
    datasets: [{
            label: "Zombie Count",
            fill: true,
            borderColor: "rgba(127,63,191,1)",
            pointBackgroundColor: "rgba(127,63,191,1)",
            pointBorderColor: "#7F3FBF",
            data: [],
        },
        {
            label: "Human Count",
            fill: false,
            borderColor: "rgba(63,191,191,1)",
            pointBackgroundColor: "rgba(63,191,191,1)",
            pointBorderColor: "#fff",
            data: [],
        }
    ]
};

var ui = {};

var charts = (function() {

    var obj = {};

    obj.update = function(chart, row) {
        var tick = row.tick;
        var new_data = [row['Zombie Count'], row['Human Count']]
        obj.addData(chart, tick, new_data);
    }

    obj.addData = function(chart, tick, new_data) {
        data.labels.push(tick);
        data.datasets[0].data.push(new_data[0]);
        data.datasets[1].data.push(new_data[1]);
        chart.update();
    }

    obj.clear = function(chart) {
      data.labels = [];
      data.datasets[0].data = [];
      data.datasets[1].data = [];
      chart.update();
    }

    return obj;
})();

var repast_ws = (function() {

    var SimState = {
        STARTED: 0,
        PAUSED: 1,
        STOPPED: 2
    };

    var simState = SimState.STOPPED;

    var obj = {};

    obj.startClicked = function(socket) {
      if (simState == SimState.STOPPED) {
          obj.init();
          $("#start").html("Pause");
          obj.start(socket);
          simState = SimState.STARTED;
      } else if (simState == SimState.STARTED) {
        $("#start").html("Start");
        simState = SimState.PAUSED;
        var msg = {command: 'pause'};
        socket.send(JSON.stringify(msg));
      } else if (simState == SimState.PAUSED) {
        simState = SimState.STARTED;
        $("#start").html("Pause");
        var msg = {command: 'resume'};
        socket.send(JSON.stringify(msg));
      }
    };

    obj.stopClicked = function(socket) {
      if (simState == SimState.STARTED || simState == SimState.PAUSED) {
        var msg = {command: 'stop'};
        socket.send(JSON.stringify(msg));
      }
    }

    obj.init = function() {
      charts.clear(ui.chart1);
    }

    obj.start = function(socket) {
        //console.log('start');
        var msg = {
            command: 'start',
            scenario: '/Users/nick/Documents/workspace/RepastSimphony2/jzombies/jzombies.rs',
            classpath: '/Users/nick/Documents/workspace/RepastSimphony2/jzombies/bin',
            //scenario: '/Users/nick/Documents/repos/gcmat2/rareEarths/rareEarths_large_outputs.rs',
            //classpath: '/Users/nick/Documents/repos/gcmat2/rareEarths/bin:/Users/nick/Documents/repos/gcmat2/rareEarths/lib/joptimizer-3.5.1.jar',
        };
        socket.send(JSON.stringify(msg));
    };

    obj.updateStatus = function(status) {
      if (status == "DONE") {
        simState = SimState.STOPPED;
        $("#start").html("Start");
      }
    }

    return obj;
})();


$(document).ready(function() {

    var socket = new WebSocket('ws://localhost:8080/events/');

    $("#start").on('click', function() {
      repast_ws.startClicked(socket);
    });

    $("#stop").on('click', function() {
      repast_ws.stopClicked(socket);
    });

    var ctx = document.getElementById("chart1").getContext("2d");
    ui.chart1 = new Chart(ctx, {
        type: 'line',
        data: data
    });

    socket.onmessage = function(evt) {
        console.log(event.data);
        var obj = JSON.parse(event.data);
        if (obj.id == "row") {
            charts.update(ui.chart1, obj.data);
        } else if (obj.id == "status") {
          repast_ws.updateStatus(obj.data);
        }
    }
});
