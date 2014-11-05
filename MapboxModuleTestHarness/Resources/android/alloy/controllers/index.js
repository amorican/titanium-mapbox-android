function __processArg(obj, key) {
    var arg = null;
    if (obj) {
        arg = obj[key] || null;
        delete obj[key];
    }
    return arg;
}

function Controller() {
    function init() {
        $.index.open();
        var mapbox = require("flg.mapbox");
        Ti.API.info("module is => " + mapbox);
        mapView = mapbox.createMapView({
            width: Titanium.UI.FILL,
            height: Titanium.UI.FILL,
            top: 3,
            bottom: 3,
            left: 3,
            right: 3,
            region: {
                latitude: 50,
                longitude: 50,
                latitudeDelta: 180,
                longitudeDelta: 180
            },
            userLocation: showUserLocation
        });
        $.mapContainer.add(mapView);
    }
    function helloWorldButtonTapped() {
        var region = {
            latitude: 50,
            longitude: 50,
            latitudeDelta: 180,
            longitudeDelta: 180
        };
        mapView.region = region;
    }
    function helloUSAButtonTapped() {
        var region = {
            latitude: 42,
            longitude: -97,
            latitudeDelta: 10,
            longitudeDelta: 10
        };
        mapView.setRegion(region);
    }
    function helloBroncosButtonTapped() {
        var region = {
            latitude: 39.743878,
            longitude: -105.020118,
            latitudeDelta: 5e-4,
            longitudeDelta: 5e-4
        };
        mapView.setRegion(region);
    }
    function markersButtonTapped() {
        mapView.setAnnotations(getAnnotations());
    }
    function zoomInButtonTapped() {
        var region = mapView.region;
        Ti.API.info("Region on map is => latitude: " + region.latitude + ", longitude: " + region.longitude + ", latitudeDelta: " + region.latitudeDelta + ", longitudeDelta: " + region.longitudeDelta);
        var avgDelta = (region.latitudeDelta + region.longitudeDelta) / 2;
        var nextDelta = getNextDeltaValue(avgDelta, "in");
        region.latitudeDelta = nextDelta;
        region.longitudeDelta = nextDelta;
        mapView.region = region;
    }
    function zoomOutButtonTapped() {
        var region = mapView.getRegion();
        Ti.API.info("Region on map is => latitude: " + region.latitude + ", longitude: " + region.longitude + ", latitudeDelta: " + region.latitudeDelta + ", longitudeDelta: " + region.longitudeDelta);
        var avgDelta = (region.latitudeDelta + region.longitudeDelta) / 2;
        var nextDelta = getNextDeltaValue(avgDelta, "out");
        region.latitudeDelta = nextDelta;
        region.longitudeDelta = nextDelta;
        mapView.setRegion(region);
    }
    function locationButtonTapped() {
        showUserLocation = !showUserLocation;
        mapView.userLocation = showUserLocation;
    }
    function getAnnotations() {
        annotations = 0 === annotations.length ? [ {
            title: "Annotation 1",
            subtitle: "The first annotation",
            latitude: 55.94629,
            longitude: -3.20777
        }, {
            title: "Annotation 2",
            subtitle: "The second annotation",
            latitude: -33.852222,
            longitude: 151.210556
        } ] : [];
        return annotations;
    }
    function getNextDeltaValue(currentValue, inOrOut) {
        if (currentValue >= 360) return "in" === inOrOut ? 180 : 360;
        if (currentValue >= 180) return "in" === inOrOut ? 90 : 360;
        if (currentValue >= 90) return "in" === inOrOut ? 45 : 180;
        if (currentValue >= 45) return "in" === inOrOut ? 22.5 : 90;
        if (currentValue >= 22.5) return "in" === inOrOut ? 11.25 : 45;
        if (currentValue >= 11.25) return "in" === inOrOut ? 5.625 : 22.5;
        if (currentValue >= 5.625) return "in" === inOrOut ? 2.813 : 11.25;
        if (currentValue >= 2.813) return "in" === inOrOut ? 1.406 : 5.625;
        if (currentValue >= 1.406) return "in" === inOrOut ? .703 : 2.813;
        if (currentValue >= .703) return "in" === inOrOut ? .352 : 1.406;
        if (currentValue >= .352) return "in" === inOrOut ? .176 : .703;
        if (currentValue >= .176) return "in" === inOrOut ? .088 : .352;
        if (currentValue >= .088) return "in" === inOrOut ? .044 : .176;
        if (currentValue >= .044) return "in" === inOrOut ? .022 : .088;
        if (currentValue >= .022) return "in" === inOrOut ? .011 : .044;
        if (currentValue >= .011) return "in" === inOrOut ? .005 : .022;
        if (currentValue >= .005) return "in" === inOrOut ? .003 : .011;
        if (currentValue >= .003) return "in" === inOrOut ? .001 : .005;
        if (currentValue >= .001) return "in" === inOrOut ? 5e-4 : .003;
        if (currentValue >= 5e-4) return "in" === inOrOut ? 5e-4 : .001;
    }
    require("alloy/controllers/BaseController").apply(this, Array.prototype.slice.call(arguments));
    this.__controllerPath = "index";
    if (arguments[0]) {
        {
            __processArg(arguments[0], "__parentSymbol");
        }
        {
            __processArg(arguments[0], "$model");
        }
        {
            __processArg(arguments[0], "__itemTemplate");
        }
    }
    var $ = this;
    var exports = {};
    var __defers = {};
    $.__views.index = Ti.UI.createWindow({
        backgroundColor: "white",
        id: "index"
    });
    $.__views.index && $.addTopLevelView($.__views.index);
    $.__views.buttonsRow = Ti.UI.createView({
        top: 0,
        left: 0,
        id: "buttonsRow",
        layout: "horizontal"
    });
    $.__views.index.add($.__views.buttonsRow);
    $.__views.__alloyId0 = Ti.UI.createButton({
        title: "Hello World",
        id: "__alloyId0"
    });
    $.__views.buttonsRow.add($.__views.__alloyId0);
    helloWorldButtonTapped ? $.__views.__alloyId0.addEventListener("click", helloWorldButtonTapped) : __defers["$.__views.__alloyId0!click!helloWorldButtonTapped"] = true;
    $.__views.__alloyId1 = Ti.UI.createButton({
        title: "Hello USA",
        id: "__alloyId1"
    });
    $.__views.buttonsRow.add($.__views.__alloyId1);
    helloUSAButtonTapped ? $.__views.__alloyId1.addEventListener("click", helloUSAButtonTapped) : __defers["$.__views.__alloyId1!click!helloUSAButtonTapped"] = true;
    $.__views.__alloyId2 = Ti.UI.createButton({
        title: "Hello Broncos",
        id: "__alloyId2"
    });
    $.__views.buttonsRow.add($.__views.__alloyId2);
    helloBroncosButtonTapped ? $.__views.__alloyId2.addEventListener("click", helloBroncosButtonTapped) : __defers["$.__views.__alloyId2!click!helloBroncosButtonTapped"] = true;
    $.__views.__alloyId3 = Ti.UI.createButton({
        title: "IN",
        id: "__alloyId3"
    });
    $.__views.buttonsRow.add($.__views.__alloyId3);
    zoomInButtonTapped ? $.__views.__alloyId3.addEventListener("click", zoomInButtonTapped) : __defers["$.__views.__alloyId3!click!zoomInButtonTapped"] = true;
    $.__views.__alloyId4 = Ti.UI.createButton({
        title: "OUT",
        id: "__alloyId4"
    });
    $.__views.buttonsRow.add($.__views.__alloyId4);
    zoomOutButtonTapped ? $.__views.__alloyId4.addEventListener("click", zoomOutButtonTapped) : __defers["$.__views.__alloyId4!click!zoomOutButtonTapped"] = true;
    $.__views.__alloyId5 = Ti.UI.createButton({
        title: "Markers",
        id: "__alloyId5"
    });
    $.__views.buttonsRow.add($.__views.__alloyId5);
    markersButtonTapped ? $.__views.__alloyId5.addEventListener("click", markersButtonTapped) : __defers["$.__views.__alloyId5!click!markersButtonTapped"] = true;
    $.__views.__alloyId6 = Ti.UI.createButton({
        title: "Location",
        id: "__alloyId6"
    });
    $.__views.buttonsRow.add($.__views.__alloyId6);
    locationButtonTapped ? $.__views.__alloyId6.addEventListener("click", locationButtonTapped) : __defers["$.__views.__alloyId6!click!locationButtonTapped"] = true;
    $.__views.mapContainer = Ti.UI.createView({
        top: 100,
        bottom: 20,
        left: 20,
        right: 20,
        width: Titanium.UI.FILL,
        height: Titanium.UI.FILL,
        backgroundColor: "black",
        id: "mapContainer"
    });
    $.__views.index.add($.__views.mapContainer);
    exports.destroy = function() {};
    _.extend($, $.__views);
    var mapView;
    var annotations = [];
    var showUserLocation = true;
    init();
    __defers["$.__views.__alloyId0!click!helloWorldButtonTapped"] && $.__views.__alloyId0.addEventListener("click", helloWorldButtonTapped);
    __defers["$.__views.__alloyId1!click!helloUSAButtonTapped"] && $.__views.__alloyId1.addEventListener("click", helloUSAButtonTapped);
    __defers["$.__views.__alloyId2!click!helloBroncosButtonTapped"] && $.__views.__alloyId2.addEventListener("click", helloBroncosButtonTapped);
    __defers["$.__views.__alloyId3!click!zoomInButtonTapped"] && $.__views.__alloyId3.addEventListener("click", zoomInButtonTapped);
    __defers["$.__views.__alloyId4!click!zoomOutButtonTapped"] && $.__views.__alloyId4.addEventListener("click", zoomOutButtonTapped);
    __defers["$.__views.__alloyId5!click!markersButtonTapped"] && $.__views.__alloyId5.addEventListener("click", markersButtonTapped);
    __defers["$.__views.__alloyId6!click!locationButtonTapped"] && $.__views.__alloyId6.addEventListener("click", locationButtonTapped);
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;