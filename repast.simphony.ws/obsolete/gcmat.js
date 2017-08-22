//Nd oxide historical price China Internal":19.581786558231315,
//"Nd oxide historical price China FOB":22.802985948951186,
//"Nd oxide buyer price China":19.59493975202638,
//"Nd oxide buyer price non-China":23.29861542065978


var data = {
    labels: [],
    datasets: [{
            label: "Nd oxide historical price China Internal",
            fill: false,
            borderColor: "rgba(127,63,191,1)",
            pointBackgroundColor: "rgba(127,63,191,1)",
            //pointBorderColor: "#7F3FBF",
            data: [],
        },
        {
            label: "Nd oxide historical price China FOB",
            fill: false,
            borderColor: "rgb(244, 104, 66)",
            pointBackgroundColor: "rgb(244, 104, 66)",
            //pointBorderColor: "#fff",
            data: [],
        },
        {
            label: "Nd oxide buyer price China",
            fill: false,
            borderColor: "rgb(110, 244, 65)",
            pointBackgroundColor: "rgb(110, 244, 65)",
            //pointBorderColor: "#fff",
            data: [],
        },
        {
            label: "Nd oxide buyer price non-China",
            fill: false,
            borderColor: "rgb(65, 118, 244)",
            pointBackgroundColor: "rgb(65, 118, 244)",
            //pointBorderColor: "#fff",
            data: [],
        }
    ]
};

var ui = {};

var charts = (function() {

    var obj = {};

    obj.update = function(chart, row) {
        var tick = row.tick;
        var new_data = [row['Nd oxide historical price China Internal'],
          row['Nd oxide historical price China FOB'],
          row['Nd oxide buyer price China'],
          row['Nd oxide buyer price non-China']]
        obj.addData(chart, tick, new_data);
    }

    obj.addData = function(chart, tick, new_data) {
        data.labels.push(tick);
        for (i = 0; i < 4; i++) {
            data.datasets[i].data.push(new_data[i]);
        }
        chart.update();
    }

    obj.clear = function(chart) {
      data.labels = [];
      for (i = 0; i < 4; i++) {
        data.datasets[i].data = [];
      }
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
            //scenario: '/Users/nick/Documents/workspace/RepastSimphony2/jzombies/jzombies.rs',
            //classpath: '/Users/nick/Documents/workspace/RepastSimphony2/jzombies/bin',
            scenario: '${root}/gcmat/rareEarths_large_outputs.rs',
            classpath: '${root}/gcmat/bin:${root}/gcmat/lib/joptimizer-3.5.1.jar',
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

    var host = window.location.host;
    console.log(host)
    var socket = new WebSocket('ws://' + host + '/simphony/SimSocket');

    $("#start").on('click', function() {
      repast_ws.startClicked(socket);
    });

    $("#stop").on('click', function() {
      repast_ws.stopClicked(socket);
    });

    var ctx = document.getElementById("chart1").getContext("2d");
    ui.chart1 = new Chart(ctx, {
        type: 'line',
        data: data,
        options: {
          legend: {
            position: 'bottom',
            labels: {
              usePointStyle: true
            }
          },
          title: {
            display: true,
            text: 'Nd Oxide Prices'
        },
          scales: {
            yAxes: [{
              scaleLabel: {
                display: true,
                labelString: 'Price ($/kg)'
              },
              ticks: {
                suggestedMax: 30,
                suggestedMin: 0
              }
            }],

            xAxes: [{
              scaleLabel: {
                display: true,
                labelString: 'Week'
              },
              ticks: {
                suggestedMax: 10,
                suggestedMin: 0,
                maxTicksLimit: 20
              }
            }]
          }
        }
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
