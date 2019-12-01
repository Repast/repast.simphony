
import { empty } from '/js/utils.js';
import { VRButton } from '/vendor/VRButton.js';

//import RGlobeView from '/js/RGlobeView.js';

//we use to set a valid width / height for a display
//the display's tab may not be visible yet if its not the
//first tab and so its clientWidth / clientHeight will be 0
//When that's the case, we can use these.
let uwidth = -1;
let uheight = -1;



class ShapeCreator {

    constructor(shape_name){
        this.shape_name = shape_name;
    }
    
    create(data) {
        
        var latlons = data.latlons;  // The latlon point array
        var item = null;
        
        // Line geometry
        if (this.shape_name === "line"){
//            item = L.polyline(latlons, {});
        }
        
        // Polygon geometry
        else if (this.shape_name === "polygon"){
//            item = L.polygon(latlons, {}); 
        }
        
        // Point geometry
        // single coord pair [x,y] for point symbols
        else {
//            item = L.shapeMarker(latlons[0], {
//					shape: this.shape_name,
//                    radius: 10
//			});
        }
     
        // Apply styling     
//        this.updateStyle(item,data);
     
        return item;
    }
    
    updateStyle(item, data) {
           
        // Line geometry
        if (this.shape_name === "line"){
           // TODO update styles
        }
        
        // Polygon geometry
        else if (this.shape_name === "polygon"){
             // TODO update styles
           
//            item.setStyle({
//                fillColor: data.color,
//                weight: 1,
//                fillOpacity: 0.5,
//                color: '#000000'
//            });
        }
        
        // Point geometry
        // single coord pair [x,y] for point symbols
        else {
            // TODO How to check for what style data is passed in ??
            
//            item.setStyle({
//                fillColor: data.color,
//                weight: 1,
//                fillOpacity: 0.5,
//                color: '#000000'
//            });
             
        }
    }
    
    updateLocation(item, data) { 
        var latlons = data.latlons;  // The latlon point array
        
        // Line geometry
        if (this.shape_name === "line"){
//           item.setLatLngs(latlons);
        }
        
        // Polygon geometry
        else if (this.shape_name === "polygon"){
//           item.setLatLngs(latlons);
        }
        
        // Point geometry
        // single coord pair [x,y] for point symbols
        else {
//            item.setLatLng(latlons[0]);
        }
    }

}

export class ViziCitiesDisplay {
    constructor(name, tab_content, display_id) {
        this.name = name;
        this.objectMap = new Map();

        this.tab_content = tab_content;
        
        TweenLite.lagSmoothing(0);
        
        // TODO perhaps refactor the container setup to an abstract parent class
        // Get a reference to the container element that will hold our scene
      
        // Create the container that will hold this display
        this.container = document.createElement("div");
        this.container.className = "card-display-container";
        this.container.setAttribute("display_id", display_id);
        
        let header = document.createElement("h5");
        header.className = "card-header";
        header.textContent = name;
        this.container.appendChild(header);
        this.card_body = document.createElement("div");
        this.card_body.className = "card-block-display w-100 p-0";
        this.container.appendChild(this.card_body);
        
        tab_content.appendChild(this.container);
        
        // Use the display id which is unique, since the display name can be duplicate
        this.card_body.id = display_id;
        
        let width = this.card_body.clientWidth == 0 ? uwidth : this.card_body.clientWidth;
        let height = this.card_body.clientHeight == 0 ? uheight : this.card_body.clientHeight;
 
        // should be > 0 for first tab 
        uwidth = width;
        uheight = height;
                
//        var positionOnGlobe = {longitude: -87.6, latitude: 41.5, altitude: 20e6 };
       
        
        var coords = [40.739940, -73.988801];
        this.world = VIZI.world(this.card_body.id).setView(coords);
        
        // Enable for VR
        this.world._engine._renderer.vr.enabled = true; 
        var camera = this.world.getCamera();
       
//        var camera = this.world._engine._renderer.vr.getCamera();
//        camera.position.y = 1.6;

        // This works setting the non-VR camera position
        camera.position.set( 0, 1000, 0 );

        this.world._engine._scene.translateY(-100);
        
        // Hack for testing VR, we should update orbit controls instead
//        camera.zoom = 10;
//        camera.updateProjectionMatrix();
//        camera.updateMatrix();
        
//        VIZI.Controls.orbit().addTo(this.world);
        
        VIZI.imageTileLayer('http://{s}.basemaps.cartocdn.com/light_nolabels/{z}/{x}/{y}.png').addTo(this.world);
        
     // Buildings from Tilezen
        VIZI.geoJSONTileLayer('https://tile.nextzen.org/tilezen/vector/v1/all/{z}/{x}/{y}.json?api_key=-P8vfoBlQHWiTrDduihXhA', {
          interactive: false,
          style: function(feature) {
            var height;

            if (feature.properties.height) {
              height = feature.properties.height;
            } else {
              height = 10 + Math.random() * 10;
            }

            return {
              height: height
            };
          },
          layers: ['buildings'],
          filter: function(feature) {
            // Don't show points
            return feature.geometry.type !== 'Point';
          }
        }).addTo(this.world);
        
        // create a WebGLRenderer and set its width and height
        
//        this.renderer = new THREE.WebGLRenderer({ 
//        	canvas: document.createElement('canvas'),
//        	antialias: true,
//        	alpha: true,
//        	logarithmicDepthBuffer: true
////        	autoClear: false
//        	});
//
////        this.renderer.vr.enabled = true;      
//        
//        this.renderer.setPixelRatio(window.devicePixelRatio);
//        this.renderer.setSize(width, height);
//        this.card_body.appendChild(this.renderer.domElement);
        
//        console.log(this.renderer);
                
//        this.card_body.appendChild( VRButton.createButton( this.renderer ) );

//        window.addEventListener('resize', this.windowResize.bind(this));

        if (this.world._engine._renderer.vr.enabled){
        	this.container.appendChild( VRButton.createButton(this.world._engine._renderer ) );   
        }
    }
    
    setAltitude(properties) {
        console.log(properties);
    }
    
     acceptFeature(properties) {
         return true;
     }
    
    // Initialize the display
    init(msg) {
        
        console.log(msg);
        
        this.mapLayers = new Map();     // Holds a L.layerGroup for each layer
        this.itemCreators = new Map();  // Holds an item creator for each layer

        for (var i = 0; i < msg.agent_layers.length; i++) {
            var lay = msg.agent_layers[i];
          
        }

         for (var i = 0; i < msg.net_layers.length; i++) {
            var lay = msg.net_layers[i];
            var maplayer = L.layerGroup();
            
         }
        
        // TODO add markers to feature groups and set bounds to zoom
                
//        var material = new THREE.MeshLambertMaterial();
//        var geometry = new THREE.CircleBufferGeometry(1000, 32);
        
        // create a Mesh containing the geometry and material
     
       // mesh.material.color.set(0xff0000);
        
//        var geometry = new THREE.CylinderGeometry(1000, 1000, 2000, 8);
//        var material = new THREE.MeshBasicMaterial({ color: 0xff0000 });
//        var mesh = new THREE.Mesh(geometry, material);
        
        
        /* var coord = new itowns.Coordinates('EPSG:4326');
        coord.setFromValues(-87.618952, 41.875817);  // Buckingham Fountain
        coord.altitude = 1000;
         
         // position of the mesh
//         var meshCoord = {longitude: -87.6, latitude: 41.5};
//         var meshCoord = coord;
        // meshCoord.altitude = 1000;
       
        // position and orientation of the mesh
//        mesh.position.copy(meshCoord.as(this.view.referenceCrs));
//        mesh.lookAt(new THREE.Vector3(0, 0, 0));
//        mesh.rotateX(Math.PI / 1);
        
 //       mesh.scale.z = 0.01;
        
        // update coordinate of the mesh
//        mesh.updateMatrixWorld();
       
//        this.group.add(mesh);
        
//        this.view.mesh = mesh;
        
        var featureType = itowns.FEATURE_TYPES.POINT;
        var crs = 'EPSG:4326';
        var options = {buildExtent: true };
        
        var feature = new itowns.Feature(featureType, crs, options);
        var geometry = feature.bindNewGeometry();
        
		
		/* geometry.properties.style = new itowns.Style({
          fill: 'red',
          fillOpacity: 0.5,
          stroke: 'white',
		  'point.color': 'green',
		  'point.line': 'gray',
		  'point.radius': 10,
		  'point.opacity': 1.0
      });
		
		geometry.startSubGeometry(1);
        geometry.pushCoordinates(coord);
        feature.updateExtent(geometry);
        
        var features = new itowns.FeatureCollection(crs, {buildExtent: true });
        
        features.pushFeature(feature);
        features.updateExtent();
        
        this.geometryLayer.source.parsedData = features; 
        
		this.geometryLayer.object3d.updateMatrixWorld();
		this.view.notifyChange(this.geometryLayer);
		//this.geometryLayer.update(); */
		
//        console.log(this.geometryLayer);
//        console.log(this.view);
        
//        this.view.controls.setTilt(60, true);
       
       
	   
//	   console.log(this.marne.object3d);
    }
   
    

    update(msg) {
        console.log(msg);
        
         //this.view.mesh.updateMatrixWorld();
        
        for (var i = 0; i < msg.agent_layers.length; i++) {
            var lay = msg.agent_layers[i];
 //           var mapLayer = this.mapLayers.get(lay.layer_id);
 //           var shapeCreator = this.itemCreators.get(lay.layer_id);
            
            // Display update add new objects
            for (var j = 0; j < lay.data.add.length; j++) {
                var add_data = lay.data.add[j];
//                var item = shapeCreator.create(add_data);
  
//                item.addTo(mapLayer);
                
                // TODO Label
                
 //               item.bindPopup('marker ' + add_data.id);
            
//                this.objectMap.set(add_data.id, item);
            }

            // Display update remove objects
            for (var j = 0; j < lay.data.remove.length; j++) {
                var id = lay.data.remove[j];
//                var item = this.objectMap.get(id);
                
//                item.removeFrom(mapLayer);
//                this.objectMap.delete(id);
//                item = null;
            }

            // Display update existing objects styles
            for (var j = 0; j < lay.data.update.length; j++) {
                var data = lay.data.update[j];
//                var item = this.objectMap.get(data.id);
               
//                shapeCreator.updateStyle(item, data);
            }
            
            // Display update existing objects locations
            for (var j = 0; j < lay.data.move.length; j++) {
                var data = lay.data.move[j];
//                var item = this.objectMap.get(data.id);
               
//                shapeCreator.updateLocation(item, data);
            }
        }
        
        for (var i = 0; i < msg.net_layers.length; i++) {
            var lay = msg.net_layers[i];
            
//            var mapLayer = this.mapLayers.get(lay.layer_id);
            
            //let creator = this.layers.get(lay.layer_id);

            // Display update add new objects
            for (var j = 0; j < lay.data.add.length; j++) {
                var add_data = lay.data.add[j];
                
                // TODO creator
                
//                var line = L.polyline([add_data.source,add_data.target], {});
                    
//                line.addTo(mapLayer);
                          
//                line.setStyle({
//                    color: add_data.color,
//                   weight: add_data.size
//                });
            
                // TODO use different map for network objects
//                this.objectMap.set(add_data.id, line);
            }

            // Display update remove objects
            for (var j = 0; j < lay.data.remove.length; j++) {
//                var id = lay.data.remove[j];
//                var item = this.objectMap.get(id);
                
//                item.removeFrom(mapLayer);
//                this.objectMap.delete(id);
//                item = null;
            }

            // Display update existing objects
            for (var j = 0; j < lay.data.update.length; j++) {
//                var data = lay.data.update[j];
//                var line = this.objectMap.get(data.id);
              
                // TODO styling
              
//                line.setLatLngs([data.source, data.target]);
                
//                line.setStyle({
//                    color: data.color,
//                    weight: data.size                    
//                 });
            }
        }
    }

    destroy() {
//        this.map.remove();
//        this.map = null;
        
    	this.world.destroy();
    	this.world = null;
        
        empty(this.tab);
        empty(this.tab_content);
        document.getElementById("display-tabs").removeChild(this.tab);
        document.getElementById("display-content").removeChild(this.tab_content);
        
        window.removeEventListener('resize', this.windowResize.bind(this));
    }

    checkResize() {
        if (this.resize) {
            this.windowResize();
            this.resize = false;
        }
    }

    // this can get called alot!
    windowResize() {
     if (this.container.clientHeight == 0) {
            this.resize = true;
        } else {
//            this.view.mainLoop.gfxEngine.onWindowResize(this.container.clientWidth, this.container.clientHeight);
//            this.view.camera.resize(this.container.clientWidth, this.container.clientHeight);
//            this.view.notifyChange(this.view.camera.camera3D);
        }
    }

}

