
// these need to be accessed inside more than one function so we'll declare them first
let map;
let vector_layer;


let updates = [];

let myfeatures = [];

let color_array = [
    '#1f78b4',
    '#b2df8a',
    '#33a02c',
    '#fb9a99',
    '#e31a1c',
    '#fdbf6f',
    '#ff7f00',
    '#6a3d9a',
    '#cab2d6',
    '#ffff99'
]

function getRandomFloat(min, max) {
    return Math.random() * (max - min) + min;
}

function getRandomInt(min, max) {
    return min + Math.floor(Math.random() * (max - min + 1));
}


function init() {

    map = new ol.Map({
        loadTilesWhileInteracting: true,
        target: 'map',
        renderer: 'webgl'   // webgl or canvas
      });
      
    let num_obj = 1000;
	
    for (var i = 0; i < num_obj; ++i) {
      
        var point_feature = new ol.Feature({ });
        var point_geom = new ol.geom.Point( ol.proj.fromLonLat(getRandomLonLat()));
        point_feature.setGeometry(point_geom);
        
        myfeatures.push(point_feature);
    }  
    
    vector_layer = new ol.layer.Vector({
//        renderMode: 'image',
        source: new ol.source.Vector({
            features: myfeatures
        })
      })
      
    
    var fill = new ol.style.Fill({
        color: [0, 0, 256, 0.3]
    });
 
    var stroke = new ol.style.Stroke({
        color: [0, 0, 0, 1],
        width: 1
    });
    
    var style = new ol.style.Style({
        image: new ol.style.Circle({
            fill: fill,
            stroke: stroke,
            radius: 8
        }),
        fill: fill,
        stroke: stroke
    });
    vector_layer.setStyle(style);
    
    // Open Street Map tiles
    var osm_layer = new ol.layer.Tile({
        source: new ol.source.OSM()
    });
      
/*     var bing_layer = new ol.layer.Tile({
          visible: true,
          preload: Infinity,
          source: new ol.source.BingMaps({
            key: 'Your Bing Maps Key from http://www.bingmapsportal.com/ here'
          })
        });   */
      
    map.setView( new ol.View({
        center: ol.proj.fromLonLat([-87.6, 41.5]),
        zoom: 6
        })
    );  
  
//    map.addLayer(bing_layer);  
    map.addLayer(osm_layer);
    map.addLayer(vector_layer);
    
//     map.on('postcompose', animate);
      
}

function getRandomLonLat() {
	return [
  	 -87.6 + Math.random() - 0.5,
    41.5 + Math.random() - 0.5
  ];
}

function getRandomLatLng() {
	return [
  	41.5 + Math.random() - 0.5,
    -87.6 + Math.random() - 0.5
  ];
}

function update() {
  
//    vector_layer.setVisible(false);

    updates.forEach((update, index, array) => {
        let feature = myfeatures[update.id];

        let loc = feature.getGeometry().getCoordinates();
			
		loc[0] = loc[0] + 1000 * (Math.random() - 0.5);
		loc[1] = loc[1] + 1000 * (Math.random() - 0.5);
        
//        feature.getGeometry().setCoordinates(ol.proj.fromLonLat(getRandomLonLat()));

        feature.getGeometry().setCoordinates(loc);
        
    });
    
//    vector_layer.setVisible(true);
}



// this can get called alot!
function onWindowResize() {
 

}

function initSocket() {
    var socket = io.connect('http://localhost:5000');
    socket.on('data', (msg) => {
        //console.log(`Data: ${msg}`);
        updates = msg;
        
        update();
        
    });

    socket.on('status', (msg) => {
        console.log(`Status: ${msg}`);
    });

    $("#start").on('click', () => {
        console.log("start");
        socket.emit('command', 'start');
    });

    $("#stop").on('click', () => {
        console.log("stop");
        socket.emit('command', 'stop');
    });
}

$(document).ready(function () {
    initSocket();
    init();
});

window.addEventListener('resize', onWindowResize);