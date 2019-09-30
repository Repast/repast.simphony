import { empty } from '/js/utils.js';

let chart_idx = 0;

export function fixChartTabs() {
    let c = $("#chart-tabs").children().length;
    for (let j = 1; j <= c; ++j) {
        $(`#chart-tabs li:nth-child(${j}) a`).tab('show');
    }
}


export function createTab(title, idx) {

    let charts_container = document.getElementById("chart-tabs")
    let tab = document.createElement("li");
    tab.className = "nav-item";
    let tab_anchor = document.createElement("a");
    tab_anchor.className = "nav-link"
    
    let tab_anchor_id = `c${idx}-tab`;
    let cid = `c${idx}`;
    tab_anchor.id = tab_anchor_id;

    tab_anchor.setAttribute("data-toggle", "tab");
    tab_anchor.setAttribute("role", "tab");
    tab_anchor.setAttribute("aria-controls", cid);
    tab_anchor.setAttribute("href", `#${cid}`);

    tab_anchor.setAttribute("aria-selected", "false");
    tab_anchor.innerHTML = title;

    let chart_content = document.getElementById("chart-content");
    let tab_content = document.createElement("div");
    tab_content.className = "tab-pane fade";
    
    tab_content.id = cid;
    tab_content.setAttribute("role", "tabpanel");
    tab_content.setAttribute("aria-labelledby", tab_anchor_id);
    //tab_content.innerHTML = cid;

    let canvas = document.createElement("canvas");
    canvas.className = "my-4 w-100";
    canvas.setAttribute("width", 900);
    canvas.setAttribute("height", 380);
    
    tab_content.appendChild(canvas);

    chart_content.appendChild(tab_content);
    tab.appendChild(tab_anchor);
    charts_container.appendChild(tab);

    return {
        canvas: canvas,
        content: tab_content,
        anchor: tab_anchor,
        tab: tab
    };
}


export class TSChart {
    constructor(chartDef, tab) {
        this.tab = tab;
        this.container = this.tab.canvas.getContext("2d");
        this.chartDef = chartDef;
        this.chart_id = chart_idx;
        ++chart_idx;
        this.name = chartDef.title;
        //this.createTab();

        this.chart_data = {
            labels: [],
            datasets: []
        };
        this.ids = [];

        chartDef.items.forEach((item, i) => {
            this.ids.push(item.id);
            let color = "rgba(" + item.color[0] + "," + item.color[1] + "," +
                item.color[2] + "," + item.color[3] + ")";
            this.chart_data.datasets.push(
                {
                    label: item.label,
                    fill: true,
                    borderColor: color,
                    pointBackgroundColor: color,
                    pointBorderColor: color,
                    data: []
                }
            );
        });
        
        this.chart = new Chart(this.container, {
            type: 'line',
            data: this.chart_data,
            options: {
                legend: {
                    position: 'bottom',
                    labels: {
                        usePointStyle: true
                    }
                },
                title: {
                    display: true,
                    text: this.chartDef.title
                },
                scales: {
                    yAxes: [{
                        scaleLabel: {
                            display: true,
                            labelString: this.chartDef.yAxisLabel
                        },
                        ticks: {
                            suggestedMax: 30,
                            suggestedMin: 0
                        }
                    }],

                    xAxes: [{
                        scaleLabel: {
                            display: true,
                            labelString: this.chartDef.xAxisLabel
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
    }

    update(rows) {
        rows.forEach((row) => {
            this.ids.forEach((val, i) => {
                this.chart_data.datasets[i].data.push(row[val]);
            });
            this.chart_data.labels.push(row.tick);
        });
        this.chart.update(0);
    }

    clear() {
        this.chart_data.labels = [];
        this.chart_data.datasets.forEach((ds) => {
            ds.data = [];
        });
        empty(this.container);
        empty(this.tab.tab);
        empty(this.tab.content);
        document.getElementById("chart-tabs").removeChild(this.tab.tab);
        document.getElementById("chart-content").removeChild(this.tab.content);
    }
}