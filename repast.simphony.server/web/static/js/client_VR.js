
// these need to be accessed inside more than one function so we'll declare them first
let container;
let camera;
let renderer;
let scene;
let stats;
let light;

let updates = [];

let meshes = [];
let color_array = [
    0x1f78b4,
    0xb2df8a,
    0x33a02c,
    0xfb9a99,
    0xe31a1c,
    0xfdbf6f,
    0xff7f00,
    0x6a3d9a,
    0xcab2d6,
    0xffff99
]

function getRandomFloat(min, max) {
    return Math.random() * (max - min) + min;
}

function getRandomInt(min, max) {
    return min + Math.floor(Math.random() * (max - min + 1));
}


function init() {

    // Get a reference to the container element that will hold our scene
    container = document.querySelector('#scene-container');

    // create a Scene
    scene = new THREE.Scene();

    scene.background = new THREE.Color(0x8FBCD4);

    // set up the options for a perspective camera
    const fov = 90; // fov = Field Of View
    const aspect = container.clientWidth / container.clientHeight;

    const near = 0.1;
    const far = 10;

    camera = new THREE.PerspectiveCamera(fov, aspect, near, far);

//    controls = new THREE.OrbitControls(camera);
    
    // every object is initially created at ( 0, 0, 0 )
    // we'll move the camera back a bit so that we can view the scene
    camera.position.set(0, 0, 50);
//    controls.update();
    
    var num_obj = 20 * 20;
    
    var geometry = new THREE.SphereBufferGeometry(0.1, 32, 32);
    
    for (var i = 0; i < num_obj; ++i) {
        // create a geometry
//        var geometry = new THREE.SphereBufferGeometry(0.1, 32, 32);
        // create a default (white) Basic material        
        var material = new THREE.MeshStandardMaterial( { color: color_array[getRandomInt(0, 9)] } );
        // create a Mesh containing the geometry and material
        mesh = new THREE.Mesh(geometry, material);
        
        // add the mesh to the scene object
        scene.add(mesh);
        var x = getRandomFloat(-5, 5);
        var y = getRandomFloat(-3, 3);
        var z = getRandomFloat(-3, 3);
        mesh.position.set(x, y, z);
//        mesh.position.set(0, 0, 0);
        meshes.push(mesh);
    }
    
    // Create a directional light
    light = new THREE.DirectionalLight(0xffffff, 3.0);

    // move the light back and up a bit
    light.position.set(10, 10, 10);

    // remember to add the light to the scene
    scene.add(light);
//    camera.add(light)

    // create a WebGLRenderer and set its width and height
    renderer = new THREE.WebGLRenderer(  { antialias: true } );
    renderer.setSize(container.clientWidth, container.clientHeight);

    renderer.setPixelRatio(window.devicePixelRatio);
    
    renderer.vr.enabled = true;
    document.body.appendChild( WEBVR.createButton( renderer ) );
    renderer.setAnimationLoop( render );
    update();
    
//    renderer.setAnimationLoop( () => {        
//        stats.begin();
//        update();
//
//    
//        controls.update();
//        light.position.copy( camera.getWorldPosition() );
//        render();
//        stats.end();
//    });

    // add the automatically created <canvas> element to the page
    container.appendChild(renderer.domElement);

    stats = new Stats();
    container.appendChild(stats.dom);
  
//    render();  
//    animate();

//    controls.addEventListener( 'change', render );
}

function update() {
    // increase the mesh's rotation each frame
    // mesh.rotation.z += 0.01;
    // mesh.rotation.x += 0.01;
    // mesh.rotation.y += 0.01;

    // meshes.forEach((mesh, index, array) => {
    //     var pos = mesh.position;
    //     var adj_x = getRandomFloat(-0.1, 0.1);
    //     var adj_y = getRandomFloat(-0.1, 0.1);
    //     pos.set(pos.x + adj_x, pos.y + adj_y, pos.z);
    //     mesh.material.color.setHex(color_array[getRandomInt(0, 9)]);
    // });

    console.log("update");
   
    updates.forEach((update, index, array) => {
        mesh = meshes[update.id]
        
        // Move the mesh
        mesh.position.set(update.x, update.y, update.z);
        
        // Color the mesh
        mesh.material.color.setHex(update.color);
        
        // scale the mesh
        scale = getRandomFloat(0,1);
        mesh.scale.set(scale, scale, scale);
    });
}

// function animate(){
//    update();
//
//    setTimeout( function() {
//        requestAnimationFrame( animate );
//    }, 1000 / 15 );
//
//    controls.update();
//    light.position.copy( camera.getWorldPosition() );
//   render();
//    stats.update();
//}

function render() {
//    update();
//    controls.update();
    light.position.copy( camera.getWorldPosition() );
    stats.update();
    
    renderer.render(scene, camera);
}

// this can get called alot!
function onWindowResize() {
    // set the aspect ratio to match the new browser window aspect ratio
    camera.aspect = container.clientWidth / container.clientHeight;

    // update the camera's frustum
    camera.updateProjectionMatrix();

    // update the size of the renderer AND the canvas
    renderer.setSize(container.clientWidth, container.clientHeight);

}

function initSocket() {
    var socket = io.connect('http://localhost:5000');
    socket.on('data', (msg) => {
        //console.log(`Data: ${msg}`);
        updates = msg;
        
        update();
//        render();
        
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