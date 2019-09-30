import { promisifyLoader } from '/js/utils.js';

// we use to set a valid width / height for a display
// the display's tab may not be visible yet if its not the
// first tab and so its clientWidth / clientHeight will be 0
// When that's the case, we can use these.
let uwidth = -1;
let uheight = -1;

const textureLoader = promisifyLoader(new THREE.TextureLoader());
let raycaster = new THREE.Raycaster();

function getElementPosition(element) {
    let top = 0;
    let left = 0;
    do {
        top += element.offsetTop || 0;
        left += element.offsetLeft || 0;
        element = element.offsetParent;
    }
    while (element);
    return { top: top, left: left };
}

class SpriteCreator {
    constructor(texture_file, scale) {
        this.promise = textureLoader.load('textures/' + texture_file).then(this.onLoad.bind(this)).catch( (err ) => {
            console.log(err);
        });
        this.scale = scale;
    }

    onLoad(texture) {
        this.material = new THREE.SpriteMaterial({ map: texture });
        this.img_width = texture.image.width;
        this.img_height = texture.image.height;
    }

    create() {
        let material = this.material.clone();
        let sprite = new THREE.Sprite(material);
        sprite.scale.set(this.img_width * this.scale, this.img_height * this.scale, 1);
        return sprite;
    }

    update(item, r, g, b) {
        // no op as we expect color not to change for icons
    }
}

const star_geom = [-0.30900001525878906, -0.5, -0.25, -0.15599998831748962, -0.5, 0.08799999952316284, 
    -0.15400001406669617, 0.1380000114440918, 0.0, 0.4509999752044678, 0.15399998426437378, 0.1380000114440918, 
    0.5, 0.08799999952316284, 0.25, -0.15599998831748962, 0.38999998569488525, -0.5, 0.0, -0.33799999952316284, 
    -0.30900001525878906, -0.5];

const cross_geom = [0.5, 0.125, 0.125, 0.125, 0.125, 0.5, -0.125, 0.5, -0.125, 
    0.125, -0.5, 0.125, -0.5, -0.125, -0.125, -0.125, -0.125, -0.5, 0.125, -0.5, 
    0.125, -0.125, 0.5, -0.125, 0.5, 0.125];

const x_geom = [0.2651650309562683, 0.4419417381286621, 1.3877787807814457E-17, 0.1767766922712326, 
    -0.2651650309562683, 0.4419417381286621, -0.4419417381286621, 0.2651650309562683, 
    -0.1767766922712326, 1.3877787807814457E-17, -0.4419417381286621, -0.2651650309562683,
    -0.2651650309562683, -0.4419417381286621, -1.3877787807814457E-17, -0.1767766922712326, 
    0.2651650309562683, -0.4419417381286621, 0.4419417381286621, -0.2651650309562683, 0.1767766922712326, 
    -1.3877787807814457E-17, 0.4419417381286621, 0.2651650309562683, 0.2651650309562683, 0.4419417381286621];

const arrow_geom = [0.0, -0.5, 0.5, 0.0, 0.0, 0.5, 0.0, 0.10000000149011612, -0.5, 0.10000000149011612, 
    -0.5, -0.10000000149011612, 0.0, -0.10000000149011612, 0.0, -0.5];

function createShapeGeometry(points, size) {
    var shape = new THREE.Shape();
    shape.moveTo(points[0] * size, points[1] * size);
    for (let i = 2; i < points.length; i += 2) {
        shape.lineTo(points[i] * size, points[i + 1] * size);
    }

    return new THREE.ShapeGeometry(shape);
}

class ShapeCreator {

    constructor(shape_name, color, scale, size) {
        this.size = size;
        if (shape_name == 'circle') {
            this.geometry = new THREE.CircleBufferGeometry(this.size / 2, 32);
        } else if (shape_name == 'square') {
            this.geometry = new THREE.PlaneBufferGeometry(this.size, this.size, 1);
        } else if (shape_name == 'cross') {
            this.geometry = createShapeGeometry(cross_geom, this.size);

        } else if (shape_name == 'star') {
            this.geometry = createShapeGeometry(star_geom, this.size);

        } else if (shape_name == 'triangle') {
            this.geometry = new THREE.Geometry();
            // (0, a/sqrt(3)) | (-a/2, -a/(2 sqrt(3))) | (a/2, -a/(2 sqrt(3)))
            let v1 = new THREE.Vector3(0, 1.0 / 1.73, 0).multiplyScalar(this.size);
            let v2 = new THREE.Vector3(- 1.0 / 2, - 1 / (2 * 1.73), 0).multiplyScalar(this.size);
            let v3 = new THREE.Vector3(1.0 / 2, - 1 / (2 * 1.73), 0).multiplyScalar(this.size);
            
            this.geometry.vertices.push(v1, v2, v3);
            this.geometry.faces.push(new THREE.Face3(0, 1, 2));

        } else if (shape_name == 'x') {
            this.geometry = createShapeGeometry(x_geom, this.size);
        } else if (shape_name == 'arrow') {
            this.geometry = createShapeGeometry(arrow_geom, this.size);
        }

        this.color = color;
        this.scale = scale;
    }

    create() {
        // Lambert is for non-shiny material, good for simple 2D shapes  
        let material = new THREE.MeshLambertMaterial();
        // create a Mesh containing the geometry and material
        let mesh = new THREE.Mesh(this.geometry, material);
        mesh.material.color.set(this.color);
        mesh.scale.set(this.scale, this.scale, 1);
        return mesh;
    }

    update(item, r, g, b) {
        item.material.color.setRGB(r, g, b);
    }
}

// href = "#home" role = "tab" aria - controls="home"
// aria - selected="true" > Home</a >
//                     </li >

export class Display2D {
    constructor(name, display_id, bbox, tab_content) {
        this.name = name;
        this.resize = false;
        this.display_id = display_id;
        this.mouse = new THREE.Vector2();
        this.picked = function(){};

        //let outer_cont = document.createElement("div");
        //outer_cont.style.display = 'inline-block';    // allows multiple displays per row
        //outer_cont.style.width = "100%";

        // Create the container that will hold this display
        this.container = document.createElement("div");
        this.container.className = "card-display-container";
        this.container.setAttribute("display_id", display_id);

        // let card = document.createElement("div");
        // card.className = "card";
        // card.style.width = "600px";
        // card.style.height = "620px";
        let header = document.createElement("h5");
        header.className = "card-header";
        header.textContent = name;
        this.container.appendChild(header);
        this.card_body = document.createElement("div");
        this.card_body.className = "card-block-display w-100 p-0";
        this.container.appendChild(this.card_body);

        // tab_content.appendChild(this.container);
        tab_content.appendChild(this.container);
      
        this.name = name;
        this.objectMap = new Map();

        // create a Scene
        this.scene = new THREE.Scene();
        // group to hold all viz objects
        this.objGroup = new THREE.Group();
        this.scene.add(this.objGroup);
        
        // set up the options for a perspective camera
        // const fov = 35; // fov = Field Of View
        // let width = this.container.clientWidth == 0 ? uwidth : this.container.clientWidth;
        // let height = this.container.clientHeight == 0 ? uheight : this.container.clientHeight;

        let width = this.card_body.clientWidth == 0 ? uwidth : this.card_body.clientWidth;
        let height = this.card_body.clientHeight == 0 ? uheight : this.card_body.clientHeight;

        // should be > 0 for first tab 
        uwidth = width;
        uheight = height;

        // origin at center and 1 pixel = 1 three.js unit
        const left = -width / 2;
        const right = width / 2;
        const top = height / 2;
        const bottom = -height / 2;
        const near = -1;
        const far = 1;
        const zoom = 1;

        this.camera = new THREE.OrthographicCamera(left, right, top, bottom, near, far);
        this.camera.zoom = zoom;

        console.log(`pw: ${bbox[0]}, width: ${width}, height: ${height}`);
        this.objGroup.position.set(-bbox[0] / 2,  -bbox[1] / 2, 0);

        this.camera.zoom = Math.min(uwidth / bbox[0] * 0.9, uheight / bbox[1] * 0.9);
        this.bbox = bbox;
        this.camera.updateProjectionMatrix();
        this.camera.updateMatrix();

        // We do not need a directional light for 2D display
        // this.light = new THREE.DirectionalLight(0xffffff, 3.0);
        this.light = new THREE.AmbientLight();
        this.scene.add(this.light);

        // move the light back and up a bit
        // let wpVector = new THREE.Vector3();
        // this.camera.getWorldPosition(wpVector);
        // this.light.position.copy(wpVector);
        // this.camera.add(this.light)

        // // remember to add the light to the scene
        // this.scene.add(this.light);

        // create a WebGLRenderer and set its width and height
        this.renderer = new THREE.WebGLRenderer({ antialias: true });
        this.renderer.setSize(width, height);

        this.renderer.setPixelRatio(window.devicePixelRatio);

        // add the automatically created <canvas> element to the page
        //this.container.appendChild(this.renderer.domElement);
        this.card_body.appendChild(this.renderer.domElement);
        this.controls = new THREE.OrbitControls(this.camera, this.renderer.domElement);
        this.controls.addEventListener('change', this.render.bind(this));
        this.renderer.domElement.addEventListener('click', this.onClick.bind(this));
        window.addEventListener('resize', this.windowResize.bind(this));
    }

    onClick(evt) {
        // (-1 to +1) for both components
        let rect = this.renderer.domElement.getBoundingClientRect();
        this.mouse.x = ((event.clientX - rect.left) / (rect.right - rect.left)) * 2 - 1;
        this.mouse.y = - ((event.clientY - rect.top) / (rect.bottom - rect.top)) * 2 + 1;
        
        //console.log(this.mouse);
        raycaster.setFromCamera(this.mouse, this.camera);
        let intersects = raycaster.intersectObjects(this.objGroup.children, true);
        let picked_ids = [];
        for (let i = 0; i < intersects.length; ++i) {
            let obj = intersects[i];
            picked_ids.push(obj.object.userData);
        }

        if (picked_ids.length > 0) {
            this.picked(picked_ids, this.display_id, "2D");
        }
    }
    
    init(display_msg) {
        let bg_color = new THREE.Color(display_msg.background_color[0], display_msg.background_color[1],
            display_msg.background_color[2]);
        this.scene.background = bg_color;

        this.layers = new Map();

        // Init display layers
        // layer def:
        // color: (3)[0, 0, 1]
        // icon: "person.png"
        // name: "jzombies.Human"
        // scale: 1
        // shape_wkt: "circle"
        // size: -1

        let promises = [];

        for (var i = 0; i < display_msg.layers.length; i++) {
            let lay = display_msg.layers[i];
            let creator = null;
            if (lay.icon != "") {
                creator = new SpriteCreator(lay.icon, lay.scale);
                promises.push(creator.promise);
            } else {
                let shape_color = new THREE.Color(lay.color[0], lay.color[1], lay.color[2]);
                creator = new ShapeCreator(lay.shape_wkt, shape_color, lay.scale, lay.size);
            }
            this.layers.set(lay.layer_id, creator);
        }

        this.render();
        return promises;
    }

    remove(objid) {
        let item = this.objectMap.get(objid);
        this.objGroup.remove(item);
        this.objectMap.delete(objid);
        item.material.dispose();
        item = null;
    }

    updateBinary(dv) {
        let layerCount = dv.getInt32();
        for (let i = 0; i < layerCount; ++i) {
            let layerid = dv.getInt32();
            //console.log("layer id: " + layerid);
            let creator = this.layers.get(layerid);
            // remove is first
            let numToRemove = dv.getInt32();
            //console.log("To remove: " + numToRemove);
            for (let j = 0; j < numToRemove; ++j) {
                this.remove(dv.getInt32());
            }

            let numToUpdate = dv.getInt32();
            // console.log("To update: " + numToUpdate);
            // id (int), location (3 float32), color (4 int)
            for (let j = 0; j < numToUpdate; ++j) {
                let objid = dv.getInt32();
                let r = dv.getFloat32();
                let g = dv.getFloat32();
                let b = dv.getFloat32();
                let x = dv.getFloat32();
                let y = dv.getFloat32();
                let z = dv.getFloat32();
                var item = this.objectMap.get(objid);
                // TODO proper location scaling
                item.position.set(x, y, z);
                creator.update(item, r, g, b);
                //console.log(`obj id: ${objid}, location: ${x}, ${y}, ${z}, color: ${r}, ${g}, ${b}`);
            }

            let numToAdd = dv.getInt32();
            //console.log("To add: " + numToAdd);
            // id (int), location (3 float32), color (3 float)
            for (let j = 0; j < numToAdd; ++j) {
                let objid = dv.getInt32();
                let r = dv.getFloat32();
                let g = dv.getFloat32();
                let b = dv.getFloat32();
                let x = dv.getFloat32();
                let y = dv.getFloat32();
                let z = dv.getFloat32();
                let item = creator.create();
                // TODO proper location scaling
                item.position.set(x, y, z);
                creator.update(item, r, g, b);
                this.objectMap.set(objid, item);
                item.userData = objid;
                this.objGroup.add(item);
                //console.log(`obj id: ${objid}, location: ${x}, ${y}, ${z}, color: ${r}, ${g}, ${b}`);
            }
        }
        this.render();
        //var box = new THREE.Box3().setFromObject(this.objGroup);
        //console.log(box);
    }

    update(msg) {
        //console.log(msg);
        for (var i = 0; i < msg.layers.length; i++) {
            let lay = msg.layers[i];
            let creator = this.layers.get(lay.layer_id);

            // Display update add new objects
            for (var j = 0; j < lay.data.add.length; j++) {
                let add_data = lay.data.add[j];
                let item = creator.create();
                item.position.set(add_data.pt[0] / 10, add_data.pt[1] / 10, add_data.pt[2] / 10);
                this.scene.add(item);
                creator.update(item, add_data.color);
               
                // store item in object map with agent id as the key
                this.objectMap.set(add_data.id, item);
            }

            // Display update remove objects
            for (var j = 0; j < lay.data.remove.length; j++) {
                let id = lay.data.remove[j];
                let item = this.objectMap.get(id);
                this.scene.remove(item);
                this.objectMap.delete(id);
                item.material.dispose();
                item = null;
            }

            // Display update existing objects
            for (var j = 0; j < lay.data.update.length; j++) {
                var data = lay.data.update[j];
                var item = this.objectMap.get(data.id);
                // TODO proper location scaling
                item.position.set(data.pt[0] / 10, data.pt[1] / 10, data.pt[2] / 10);
                creator.update(item, data.color);
            }
        }

        this.render();
    }

    render() {
        // let wpVector = new THREE.Vector3();
        // this.camera.getWorldPosition(wpVector);
        // this.light.position.copy(wpVector);
        this.renderer.render(this.scene, this.camera);
    }

    checkResize() {
        let sz = new THREE.Vector2();
        this.renderer.getSize(sz);
        if (this.resize || sz[0] != this.card_body.clientWidth || 
            sz[1] != this.card_body.clientHeight) {
            
            this.windowResize();
            this.resize = false;
        }
    }

    windowResize() {
        // These aren't needed for ortho camera
        // this.camera.aspect = this.container.clientWidth / this.container.clientHeight;
        // this.camera.updateProjectionMatrix();
        if (this.container.clientHeight == 0) {
            this.resize = true;
        } else {
            // update the size of the renderer AND the canvas
            let w = this.card_body.clientWidth;
            let h = this.card_body.clientHeight;
            this.renderer.setSize(w, h);

            this.camera.right = w / 2;
            this.camera.left = -w / 2;
            this.camera.top = h / 2;
            this.camera.bottom = -h / 2;

            this.camera.updateProjectionMatrix();
            this.camera.updateMatrix();
            this.render();
        }
    }

    destroy() {
        //        this.renderer.domElement.addEventListener('dblclick', null, false);
        console.log("destroy " + this.name);
        this.scene.dispose();
        this.scene = null;
        //        this.projector = null;
        this.camera = null;
        this.controls = null;
        window.removeEventListener('resize', this.windowResize.bind(this));
    }
}
