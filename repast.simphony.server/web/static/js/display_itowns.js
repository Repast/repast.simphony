
import { empty } from '/js/utils.js';
import { GuiTools } from '/js/GuiTools.js';

const ext = new itowns.Extent('EPSG:4326', [0, 0, 0, 0]);


   function dynamicDataExecuteCommand(command) {
        const promises = [];
        const layer = command.layer;
        const requester = command.requester;
        const extentsSource = command.extentsSource;

        console.log("dynamic data execute command");
        
        if (requester &&
            !requester.material) {
            return Promise.reject(new CancelledCommandException(command));
        }

        for (let i = 0, size = extentsSource.length; i < size; i++) {
            promises.push(layer.convert(requester, extentsSource[i]));
        }

        return Promise.all(promises);
    }


class FeatureSource extends itowns.Source {
    
    constructor(){
        var source = { url:'fake-file-url', 
                       projection: "EPSG:4326",
                       extent: new itowns.Extent('EPSG:4326', -180, 180, -90, 90)  // CRS, W, E, S, N
                     };
        
        super(source);
        
        //this.fetchedData = null; // Once the file has been loaded, the resulting data is stored in this property.
        //this.parsedData = [];  // - Once the file has been loaded and parsed, the resulting data is stored in this property.
        this.zoom = source.zoom || { min: 5, max: 21 };    
        
        this.protocol = "dynamic-data";
        //this.provider = null;
    }
    
    extentInsideLimit(extent) {
        // Fix me => may be not
        const localExtent = this.extent.crs == extent.crs ? extent : extent.as(this.extent.crs, ext);
        return (extent.zoom == undefined || !(extent.zoom < this.zoom.min || extent.zoom > this.zoom.max)) &&
            this.extent.intersectsExtent(localExtent);
    }
    
}

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

export class ITownsDisplay {
    constructor(name, tab, display_id) {
        this.name = name;
        this.objectMap = new Map();

        this.tab_content = tab.tab_content;
        this.tab = tab.tab_li;
        //this.createTab();
        
        // TODO perhaps refactor the container setup to an abstract parent class
        // Get a reference to the container element that will hold our scene
      
        let outer_cont = document.createElement("div");
        outer_cont.style.display = 'inline-block';    // allows multiple displays per row

        // Create the container that will hold this display
        this.container = document.createElement("div");

        // TO DISABLE DRAGGING AND RESIZING, COMMENT OUT THESE
        // TWO LINES AND UNCOMMENT THE 4 STYLE LINES BELOW
        this.container.className = "display-container";
        this.container.setAttribute("display_id", display_id);
        
        // this.container.style.width = "600px";
        // this.container.style.height = "600px";
        // this.container.style.border = "solid black";
        // this.container.style.margin = "30px";

        outer_cont.appendChild(this.container);
        this.tab_content.appendChild(outer_cont);

        this.container.id = this.name;  // needed by L.map()
        
        //var position = new itowns.Coordinates('WGS84', -87.6, 45.5, 20e6);
        
        var positionOnGlobe = {longitude: -87.6, latitude: 41.5, altitude: 20e6 };
        this.view = new itowns.GlobeView(this.container, positionOnGlobe);
  
        var provider = {
            executeCommand: dynamicDataExecuteCommand,
            id: 'EricProvider'};
  
        this.view.mainLoop.scheduler.addProtocolProvider('dynamic-data', provider);
  
  
    // GuiTools.js menu
        var menuGlobe = new GuiTools(this.container, 'menuDiv', this.view);
  
        // TODO not sure if we need a loading screen per the demos?  
//        setupLoadingScreen(this.container, view);
        
        var orthoSource = new itowns.WMTSSource({
            url: 'http://wxs.ign.fr/3ht7xcw6f7nciopo16etuqp2/geoportail/wmts',
            name: 'ORTHOIMAGERY.ORTHOPHOTOS',
            tileMatrixSet: 'PM',
            format: 'image/jpeg',
            projection: "EPSG:3857"
        });

        var orthoLayer = new itowns.ColorLayer('Ortho', {
            source: orthoSource,
        });

        this.view.addLayer(orthoLayer).then(menuGlobe.addLayerGUI.bind(menuGlobe));
        
        var elevatSource = new itowns.WMTSSource({
            url: 'https://wxs.ign.fr/3ht7xcw6f7nciopo16etuqp2/geoportail/wmts',
            name: 'ELEVATION.ELEVATIONGRIDCOVERAGE.SRTM3',
            tileMatrixSet: 'WGS84G',
            format: 'image/x-bil;bits=32',
            projection: "EPSG:4326"
        });

        var elevatLayer = new itowns.ElevationLayer('MNT_WORLD_SRTM3', {
            source: elevatSource,
        });

//        this.view.addLayer(elevatLayer).then(menuGlobe.addLayerGUI.bind(menuGlobe));

       /*  var elevatSource2 = new itowns.WMTSSource({
            url: 'https://wxs.ign.fr/3ht7xcw6f7nciopo16etuqp2/geoportail/wmts',
            name: 'ELEVATION.ELEVATIONGRIDCOVERAGE.HIGHRES',
            tileMatrixSet: 'WGS84G',
            format: 'image/x-bil;bits=32',
            projection: "EPSG:4326"
        });

        var elevatLayer2 = new itowns.ElevationLayer('IGN_MNT_HIGHRES', {
            source: elevatSource2,
        });

        this.view.addLayer(elevatLayer2).then(menuGlobe.addLayerGUI.bind(menuGlobe));
         */
        
        
        
        
        var agentSource = new FeatureSource();
        
        this.geometryLayer = new itowns.GeometryLayer('Agents', new itowns.THREE.Group(), {
           
            source: agentSource,

            }
        
        );
        
        this.geometryLayer.projection = 'EPSG:4326';
        this.geometryLayer.mergeFeatures = false;
        
       // this.geometryLayer.protocol = "Eric Nutz";
        
        this.geometryLayer.update = itowns.FeatureProcessing.update;
        this.geometryLayer.convert = itowns.Feature2Mesh.convert({
              
                altitude: this.setAltitude 
                });
                
       
        
        this.geometryLayer.onMeshCreated = function scaleZ(mesh) {
                    mesh.scale.z = 0.01;
                    meshes.push(mesh);
                    
                    console.log("Heyoo " + str(mesh));
                };
        
        
      
       var coord = new itowns.Coordinates('EPSG:4326');
        coord.setFromValues(-87.618952, 41.875817);  // Buckingham Fountain
        coord.altitude = 10;
                 
        var featureType = itowns.FEATURE_TYPES.POINT;
        var crs = 'EPSG:4326';
        var options = {buildExtent: true };
        
        var feature = new itowns.Feature(featureType, crs, options);
        var geometry = feature.bindNewGeometry();
        
		
		geometry.startSubGeometry(1);
        geometry.pushCoordinates(coord);
        feature.updateExtent(geometry);
        
        var features = new itowns.FeatureCollection(crs, {buildExtent: true });
        
        features.pushFeature(feature);
        //features.updateExtent();
        
        this.geometryLayer.source.parsedData = features; 
        
            
        //this.view.addLayer(this.geometryLayer).then(console.log("Added geom layer"));
	  
        var p = this.view.addLayer(this.geometryLayer);
      
        
        
      
		// Add a geometry layer, which will contain the multipolygon to display
        this.marne = new itowns.GeometryLayer('Marne', new itowns.THREE.Group());
        this.marne.update = itowns.FeatureProcessing.update;
        this.marne.convert = itowns.Feature2Mesh.convert({ 
                                altitude: 1,
                                extrude: 100});
        this.marne.transparent = true;
        this.marne.opacity = 0.7;
        
		// Use a FileSource to load a single file once
        this.marne.source = new itowns.FileSource({
                url: 'https://raw.githubusercontent.com/iTowns/iTowns2-sample-data/master/multipolygon.geojson',
                projection: 'EPSG:4326',
                format: 'application/json',
                zoom: { min: 10, max: 10 },
        });
		
		this.view.addLayer(this.marne).then(menuGlobe.addLayerGUI.bind(menuGlobe));
	  
        window.addEventListener('resize', this.windowResize.bind(this));

        // Tab Events doc: https://getbootstrap.com/docs/4.0/components/navs/#events
        $('#display-tabs a[href="#' + tab.tab_id + '"]').on('shown.bs.tab', this.checkResize.bind(this));
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
        
        var THREE = itowns.THREE;
        
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
        
        this.view.controls.setTilt(60, true);
       
       
	   
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
        
        this.view.scene.dispose();
        this.view.scene = null;
        this.view.camera = null;
        this.view = null;
        
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
            this.view.mainLoop.gfxEngine.onWindowResize(this.container.clientWidth, this.container.clientHeight);
            this.view.camera.resize(this.container.clientWidth, this.container.clientHeight);
            this.view.notifyChange(this.view.camera.camera3D);
        }
    }

}

