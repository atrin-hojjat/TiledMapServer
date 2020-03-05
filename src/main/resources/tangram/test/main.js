var source="http://0.0.0.0:1111/map/tiles/tests/tehran_roads/{z}/{x}/{y}/vtile.geojson";
var simplified_land_polygons_source = "http://0.0.0.0:1111/map/tiles/simplified_land_polygons/{z}/{x}/{y}/vtile.geojson";
var scene_url = "scene.yaml";
var map = map = L.map('map', {
        maxZoom: 20,
        center: [35.767057861965,51.4151550343379],
        zoom: 14
    }),

    layer = Tangram.leafletLayer({
        scene: scene_url,
        attribution: '&copy Atrin Hojjat'
    });

layer.scene.subscribe({
    load: function (msg) {
        var config = msg.config;
        // If no source was set in scene definition, set one based on the URL
        if(!config.sources ){
            config.sources = config.sources || {};
            if (!config.sources['tehran_roads']) {
                config.sources['tehran_roads'] = source;
            }
            if (!config.sources['simplified_land_polygons']) {
                config.sources['simplified_land_polygons'] = simplified_land_polygons_source;
            }
        }
    },
    error: function (msg) {
        // debugger;
    },
    warning: function (msg) {
        // debugger;
    }
});


window.layer = layer;
window.map = map;
var scene = layer.scene;
window.scene = scene;

function initFeatureSelection(){
    // Selection info shown on hover
    var selection_info = document.createElement('div');
    selection_info.setAttribute('class', 'label');
    selection_info.style.display = 'block';

    var test = document.getElementById('div');

    // Show selected feature on hover
    scene.container.addEventListener('mousemove', function (event) {
        var pixel = { x: event.clientX, y: event.clientY };

        scene.getFeatureAt(pixel).then(function(selection) {
            //test.innerHTML = "<h1>Mouse is Moving</h1>";
            if (!selection) {
                return;
            }
            var feature = selection.feature;
            if (feature != null) {
                // console.log("selection map: " + JSON.stringify(feature));

                var label = '';
                if (feature.properties.name != null) {
                    label = feature.properties.name;
                }

                if (label != '') {
                    selection_info.style.left = (pixel.x + 5) + 'px';
                    selection_info.style.top = (pixel.y + 15) + 'px';
                    selection_info.innerHTML = '<span class="labelInner">' + label + '</span>';
                    scene.container.appendChild(selection_info);
                }
                else if (selection_info.parentNode != null) {
                    selection_info.parentNode.removeChild(selection_info);
                }
            }
            else if (selection_info.parentNode != null) {
                selection_info.parentNode.removeChild(selection_info);
            }
        });

        // Don't show labels while panning
        if (scene.panning == true) {
            if (selection_info.parentNode != null) {
                selection_info.parentNode.removeChild(selection_info);
            }
        }
    });

    scene.container.addEventListener('click', function (event) {
        // if (key.isPressed('e')) {
        var pixel = {x: event.clientX, y: event.clientY};
        if (true /*&& key.isPressed('d')*/) {
            scene.getFeatureAt(pixel).then(function (selection) {
                var feature=selection.feature;
                if(feature && feature.properties){
                    var text = "";
                    for( var x in feature.properties){
                        text += "\n"+x+" : "+feature.properties[x];
                    }
                    alert(text)
                }
            })
        }
    });
}

window.addEventListener('load', function () {
    // Scene initialized
    layer.on('init', function () {
        initFeatureSelection();
    });
});

layer.addTo(map);