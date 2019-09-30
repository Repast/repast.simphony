
import { empty } from '/js/utils.js';

class ShapeCreator {

    constructor(shape_name){
        this.shape_name = shape_name;
    }
    
    create(data) {
        
        var latlons = data.latlons;  // The latlon point array
        var item = null;
        
        // Line geometry
        if (this.shape_name === "line"){
            item = L.polyline(latlons, {});
        }
        
        // Polygon geometry
        else if (this.shape_name === "polygon"){
            item = L.polygon(latlons, {}); 
        }
        
        // Point geometry
        // single coord pair [x,y] for point symbols
        else {
            item = L.shapeMarker(latlons[0], {
					shape: this.shape_name,
                    radius: 10
			});
        }
     
        // Apply styling     
        this.updateStyle(item,data);
     
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
           
            item.setStyle({
                fillColor: data.color,
                weight: 1,
                fillOpacity: 0.5,
                color: '#000000'
            });
        }
        
        // Point geometry
        // single coord pair [x,y] for point symbols
        else {
            // TODO How to check for what style data is passed in ??
            
            item.setStyle({
                fillColor: data.color,
                weight: 1,
                fillOpacity: 0.5,
                color: '#000000'
            });
             
        }
    }
    
    updateLocation(item, data) { 
        var latlons = data.latlons;  // The latlon point array
        
        // Line geometry
        if (this.shape_name === "line"){
           item.setLatLngs(latlons);
        }
        
        // Polygon geometry
        else if (this.shape_name === "polygon"){
           item.setLatLngs(latlons);
        }
        
        // Point geometry
        // single coord pair [x,y] for point symbols
        else {
            item.setLatLng(latlons[0]);
        }
    }

}

export class LeafletDisplay {
    constructor(name, tab_content, display_id) {
        this.name = name;
        this.objectMap = new Map();  
        this.tab_content = tab_content;
        
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

        // tab_content.appendChild(this.container);
        tab_content.appendChild(this.container);
        
        // Use the display id which is unique, since the display name can be duplicate
        this.card_body.id = display_id;
        
        this.map = L.map(this.card_body.id, { // constructor needs unique container ID
            preferCanvas: true
        });

		this.renderer = L.canvas();
        
        // TODO move background layes creation to function
        
        // OpenSteetMap tile layer is cached locally
        // Maximum age of the cache, in milliseconds
        var cacheAge = 30 * 24 * 3600 * 1000;
        var osm = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            useCache: true,
            crossOrigin: true,
            cacheMaxAge: cacheAge,
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            });    
        osm.addTo(this.map);
        
        
        var Esri_WorldGrayCanvas = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/{z}/{y}/{x}', {
            attribution: 'Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ',
            maxZoom: 16
        });
        Esri_WorldGrayCanvas.addTo(this.map);
        
        var Esri_WorldImagery = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
            attribution: 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community'
        });
        Esri_WorldImagery.addTo(this.map);
        
        // Scale bar
        L.control.scale().addTo(this.map); 

        // TODO blank colored base layer
    
        // Layer legend and control
        var baseLayers = {'Empty': L.tileLayer(''), 'Esri Imagery': Esri_WorldImagery,
            'OpenStreetMap': osm, 'Esri Gray': Esri_WorldGrayCanvas};
            
        this.layerControl = L.control.layers(baseLayers,{});
        this.layerControl.addTo(this.map);  
        
        // TODO zoom to layer bounds
        
        this.map.setView([41.5, -87.6], 10);
        
        window.addEventListener('resize', this.windowResize.bind(this));
    }
    
    // Initialize the display
    init(msg) {
        
        console.log(msg);
        
        this.mapLayers = new Map();     // Holds a L.layerGroup for each layer
        this.itemCreators = new Map();  // Holds an item creator for each layer

        for (var i = 0; i < msg.agent_layers.length; i++) {
            var lay = msg.agent_layers[i];
            
            var maplayer = L.layerGroup();
            maplayer.addTo(this.map);
            
            this.mapLayers.set(lay.layer_id, maplayer);
            
            this.layerControl.addOverlay(maplayer, lay.name);
                        
            this.itemCreators.set(lay.layer_id, new ShapeCreator(lay.symbol));
                
                //new SpriteCreator('person.png'));;
        }

         for (var i = 0; i < msg.net_layers.length; i++) {
            var lay = msg.net_layers[i];
            var maplayer = L.layerGroup();
            maplayer.addTo(this.map);
            
            this.mapLayers.set(lay.layer_id, maplayer);
            
            this.layerControl.addOverlay(maplayer, lay.name);
         }
        
        // TODO add markers to feature groups and set bounds to zoom
       
    }
   

    update(msg) {
        //console.log(msg);
        
        for (var i = 0; i < msg.agent_layers.length; i++) {
            var lay = msg.agent_layers[i];
            var mapLayer = this.mapLayers.get(lay.layer_id);
            var shapeCreator = this.itemCreators.get(lay.layer_id);
            
            // Display update add new objects
            for (var j = 0; j < lay.data.add.length; j++) {
                var add_data = lay.data.add[j];
                var item = shapeCreator.create(add_data);
  
                item.addTo(mapLayer);
                
                // TODO Label
                
                item.bindPopup('marker ' + add_data.id);
            
                this.objectMap.set(add_data.id, item);
            }

            // Display update remove objects
            for (var j = 0; j < lay.data.remove.length; j++) {
                var id = lay.data.remove[j];
                var item = this.objectMap.get(id);
                
                item.removeFrom(mapLayer);
                this.objectMap.delete(id);
                item = null;
            }

            // Display update existing objects styles
            for (var j = 0; j < lay.data.update.length; j++) {
                var data = lay.data.update[j];
                var item = this.objectMap.get(data.id);
               
                shapeCreator.updateStyle(item, data);
            }
            
            // Display update existing objects locations
            for (var j = 0; j < lay.data.move.length; j++) {
                var data = lay.data.move[j];
                var item = this.objectMap.get(data.id);
               
                shapeCreator.updateLocation(item, data);
            }
        }
        
        for (var i = 0; i < msg.net_layers.length; i++) {
            var lay = msg.net_layers[i];
            
            var mapLayer = this.mapLayers.get(lay.layer_id);
            
            //let creator = this.layers.get(lay.layer_id);

            // Display update add new objects
            for (var j = 0; j < lay.data.add.length; j++) {
                var add_data = lay.data.add[j];
                
                // TODO creator
                
                var line = L.polyline([add_data.source,add_data.target], {});
                    
                line.addTo(mapLayer);
                          
                line.setStyle({
                    color: add_data.color,
                    weight: add_data.size
                });
            
                // TODO use different map for network objects
                this.objectMap.set(add_data.id, line);
            }

            // Display update remove objects
            for (var j = 0; j < lay.data.remove.length; j++) {
                var id = lay.data.remove[j];
                var item = this.objectMap.get(id);
                
                item.removeFrom(mapLayer);
                this.objectMap.delete(id);
                item = null;
            }

            // Display update existing objects
            for (var j = 0; j < lay.data.update.length; j++) {
                var data = lay.data.update[j];
                var line = this.objectMap.get(data.id);
              
                // TODO styling
              
                line.setLatLngs([data.source, data.target]);
                
                line.setStyle({
                    color: data.color,
                    weight: data.size                    
                 });
            }
        }
    }

    destroy() {
        this.map.remove();
        this.map = null;
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
            this.map.invalidateSize();
        }
    }

}

// Extends leaflet CircleMarker to provide additional shapes
L.ShapeMarker = L.CircleMarker.extend({
	options: {
		fill: true,
		shape: 'circle',
		radius: 10
	},

	_updatePath: function () {
		this._renderer._updateShape(this);
	},

});

// @factory L.shapeMarker(latlng: LatLng, options? ShapeMarker options)
//
L.shapeMarker = function shapeMarker(latlng, options) {
	return new L.ShapeMarker(latlng, options);
};

// Add a method to the leaflet canvas to provide support for adddtional shapes
L.Canvas.include({
	_updateShape: function _updateShape(layer) {

		if (!this._drawing || layer._empty()) { return; }

		var p = layer._point;
		var	ctx = this._ctx;
		var	r = Math.max(Math.round(layer._radius), 1);
		var	s = (Math.max(Math.round(layer._radiusY), 1) || r) / r;

		if (s !== 1) {
			ctx.save();
			ctx.scale(1, s);
		}

		ctx.beginPath();

		var shape = layer.options.shape;

		if(shape === "diamond"){
			ctx.moveTo(p.x-r, p.y);
			ctx.lineTo(p.x, p.y-r);
			ctx.lineTo(p.x+r, p.y);
			ctx.lineTo(p.x, p.y+r);
			ctx.lineTo(p.x-r, p.y);
		}
		else if(shape === "square"){
			ctx.moveTo(p.x-r, p.y-r);
			ctx.lineTo(p.x+r, p.y-r);
			ctx.lineTo(p.x+r, p.y+r);
			ctx.lineTo(p.x-r, p.y+r);
			ctx.lineTo(p.x-r, p.y-r);
		}
		else if (shape === "triangle" || shape === "triangle-up") {
            ctx.moveTo(p.x, p.y-r);
			ctx.lineTo(p.x+r, p.y+r);
			ctx.lineTo(p.x-r, p.y+r);
			ctx.lineTo(p.x, p.y-r);
		}
		else if (shape === "cross") {
            ctx.moveTo(p.x-r, p.y-0.25*r);
			ctx.lineTo(p.x-0.25*r, p.y-0.25*r);           
			ctx.lineTo(p.x-0.25*r, p.y-r);            
			ctx.lineTo(p.x+0.25*r, p.y-r);			
            ctx.lineTo(p.x+0.25*r, p.y-0.25*r);            
            ctx.lineTo(p.x+r, p.y-0.25*r);          
            ctx.lineTo(p.x+r, p.y+0.25*r);           
            ctx.lineTo(p.x+0.25*r, p.y+0.25*r);         
            ctx.lineTo(p.x+0.25*r, p.y+r);           
            ctx.lineTo(p.x-0.25*r, p.y+r);         
            ctx.lineTo(p.x-0.25*r, p.y+0.25*r);           
            ctx.lineTo(p.x-r, p.y+0.25*r);          
            ctx.lineTo(p.x-r, p.y-0.25*r);

		}
        else if (shape === "star") {
            var rot = Math.PI/2*3;
            var spikes = 5;
            var r0 = r/2;
            var x = p.x;
            var y = p.y;
            var step = Math.PI/spikes;
            

            ctx.moveTo(p.x, p.y - r)
            for(var i=0; i<spikes; i++){
                x = p.x + Math.cos(rot) * r;
                y = p.y + Math.sin(rot) * r;
                ctx.lineTo(x,y)
                rot += step;

                x = p.x + Math.cos(rot) * r0;
                y = p.y + Math.sin(rot) * r0;
                ctx.lineTo(x,y);
                rot += step;
            }
            ctx.lineTo(p.x, p.y - r);
        }
		else if (shape === "X") {
           /*  ctx.translate(p.x+r/2, p.y+r/2 );
            ctx.rotate(45 * Math.PI / 180);
             
            // same as cross, but rotated 45 degree
			ctx.moveTo(p.x-r, p.y-0.25*r);
			ctx.lineTo(p.x-0.25*r, p.y-0.25*r);           
			ctx.lineTo(p.x-0.25*r, p.y-r);            
			ctx.lineTo(p.x+0.25*r, p.y-r);			
            ctx.lineTo(p.x+0.25*r, p.y-0.25*r);            
            ctx.lineTo(p.x+r, p.y-0.25*r);          
            ctx.lineTo(p.x+r, p.y+0.25*r);           
            ctx.lineTo(p.x+0.25*r, p.y+0.25*r);         
            ctx.lineTo(p.x+0.25*r, p.y+r);           
            ctx.lineTo(p.x-0.25*r, p.y+r);         
            ctx.lineTo(p.x-0.25*r, p.y+0.25*r);           
            ctx.lineTo(p.x-r, p.y+0.25*r);          
            ctx.lineTo(p.x-r, p.y-0.25*r);
            
            ctx.rotate(-45 * Math.PI / 180);
            ctx.translate(-(p.x+r/2), -(p.y+r/2) ); */
		}
        
        // Just use the buiilt int circle draw
        else { // (shape === "circle") {
			this._updateCircle(layer)
		}
		
		if (s !== 1) {
			ctx.restore();
		}

		this._fillStroke(ctx, layer);
	}
});
