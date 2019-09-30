
// these need to be accessed inside more than one function so we'll declare them first
let map;

let updates = [];

let markers = [];

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

    map = L.map('map');
	let myRenderer = L.canvas({ padding: 0.5 });
	
	
	let OpenStreetMap_Mapnik = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		maxZoom: 19,
		attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
		});
	
	OpenStreetMap_Mapnik.addTo(map);

	let num_obj = 1000;
	
    for (var i = 0; i < num_obj; ++i) {
		let marker = L.circleMarker(getRandomLatLng(), {
			renderer: myRenderer
			}).addTo(map).bindPopup('marker ' + i);
		
		marker.setStyle({
			fillColor: color_array[getRandomInt(0, 9)]
		});
		
		markers.push(marker);
	}

	map.setView([41.5, -87.6], 13);
	
//	L.control.layers(baseLayers, overlays).addTo(map);
}

function getRandomLatLng() {
	return [
  	41.5 + Math.random() - 0.5,
    -87.6 + Math.random() - 0.5
  ];
}

function update() {
  
    updates.forEach((update, index, array) => {
		let marker = markers[update.id];
		
		let loc = marker.getLatLng();
			
		let newlat = loc.lat.valueOf() + 0.001 * (Math.random() - 0.5);
		let newlng = loc.lng.valueOf() + 0.001 * (Math.random() - 0.5);
		
		marker.setLatLng([newlat, newlng]);
		
    });
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