var mapView;
var annotations = [];
var showUserLocation = true;

function init() {
	$.index.open();

	var mapbox = require('flg.mapbox');
	Ti.API.info("module is => " + mapbox);

	mapView = mapbox.createMapView({
		width : Titanium.UI.FILL,
		height : Titanium.UI.FILL,
		top : 3,
		bottom : 3,
		left : 3,
		right : 3,
		region : {
			latitude : 50,
			longitude : 50,
			latitudeDelta : 180,
			longitudeDelta : 180
		},
		userLocation : showUserLocation
	});

	$.mapContainer.add(mapView);
}

//
// Test region & zoom
//
function helloWorldButtonTapped(e) {
	var region = {
		latitude : 50,
		longitude : 50,
		latitudeDelta : 180,
		longitudeDelta : 180
	};

	mapView.region = region;
}

function helloUSAButtonTapped(e) {
	var region = {
		latitude : 42,
		longitude : -97,
		latitudeDelta : 10,
		longitudeDelta : 10
	};

	mapView.setRegion(region);
}

function helloBroncosButtonTapped(e) {
	var region = {
		latitude : 39.743878,
		longitude :  -105.020118,
		latitudeDelta : 0.0005,
		longitudeDelta : 0.0005
	};

	mapView.setRegion(region);
}

//
// Test markers
//
function markersButtonTapped(e) {
	mapView.setAnnotations(getAnnotations());
}

//
// Test zoom in & out
//
function zoomInButtonTapped(e) {
	var region = mapView.region;
	
	Ti.API.info("Region on map is => latitude: " + region.latitude + ", longitude: " + region.longitude + ", latitudeDelta: " + region.latitudeDelta + ", longitudeDelta: " + region.longitudeDelta);
	
	var avgDelta = (region.latitudeDelta + region.longitudeDelta) / 2;
	var nextDelta = getNextDeltaValue(avgDelta, "in");
	
	region.latitudeDelta = nextDelta;
	region.longitudeDelta = nextDelta;
	mapView.region = region;
}

function zoomOutButtonTapped(e) {
	var region = mapView.getRegion();

	Ti.API.info("Region on map is => latitude: " + region.latitude + ", longitude: " + region.longitude + ", latitudeDelta: " + region.latitudeDelta + ", longitudeDelta: " + region.longitudeDelta);

	var avgDelta = (region.latitudeDelta + region.longitudeDelta) / 2;
	var nextDelta = getNextDeltaValue(avgDelta, "out");
	
	region.latitudeDelta = nextDelta;
	region.longitudeDelta = nextDelta;
	mapView.setRegion(region);
}

//
// Test user location
//
function locationButtonTapped(e) {
	showUserLocation = !showUserLocation;
	mapView.userLocation = showUserLocation;
}


function getAnnotations() {
	if (annotations.length === 0) {
		annotations = [
			{
				title : "Annotation 1",
				subtitle : "The first annotation",
				latitude : 55.94629,
				longitude : -3.20777
			},
			{
				title : "Annotation 2",
				subtitle : "The second annotation",
			    latitude: -33.852222,
			    longitude: 151.210556,
			}
		];
	}
	else {
		annotations = [];
	}
	return annotations;
}

function getNextDeltaValue(currentValue, inOrOut) {
	if (currentValue >= 360) {
		return ((inOrOut === "in") ? 180 : 360);
	}
	else if (currentValue >= 180) {
		return ((inOrOut === "in") ? 90 : 360);
	}
	else if (currentValue >= 90) {
		return ((inOrOut === "in") ? 45 : 180);
	}
	else if (currentValue >= 45) {
		return ((inOrOut === "in") ? 22.5 : 90);
	}
	else if (currentValue >= 22.5) {
		return ((inOrOut === "in") ? 11.25 : 45);
	}
	else if (currentValue >= 11.25) {
		return ((inOrOut === "in") ? 5.625 : 22.5);
	}
	else if (currentValue >= 5.625) {
		return ((inOrOut === "in") ? 2.813 : 11.25);
	}
	else if (currentValue >= 2.813) {
		return ((inOrOut === "in") ? 1.406 : 5.625);
	}
	else if (currentValue >= 1.406) {
		return ((inOrOut === "in") ? 0.703 : 2.813);
	}
	else if (currentValue >= 0.703) {
		return ((inOrOut === "in") ? 0.352 : 1.406);
	}
	else if (currentValue >= 0.352) {
		return ((inOrOut === "in") ? 0.176 : 0.703);
	}
	else if (currentValue >= 0.176) {
		return ((inOrOut === "in") ? 0.088 : 0.352);
	}
	else if (currentValue >= 0.088) {
		return ((inOrOut === "in") ? 0.044 : 0.176);
	}
	else if (currentValue >= 0.044) {
		return ((inOrOut === "in") ? 0.022 : 0.088);
	}
	else if (currentValue >= 0.022) {
		return ((inOrOut === "in") ? 0.011 : 0.044);
	}
	else if (currentValue >= 0.011) {
		return ((inOrOut === "in") ? 0.005 : 0.022);
	}
	else if (currentValue >= 0.005) {
		return ((inOrOut === "in") ? 0.003 : 0.011);
	}
	else if (currentValue >= 0.003) {
		return ((inOrOut === "in") ? 0.001 : 0.005);
	}
	else if (currentValue >= 0.001) {
		return ((inOrOut === "in") ? 0.0005 : 0.003);
	}
	else if (currentValue >= 0.0005) {
		return ((inOrOut === "in") ? 0.0005 : 0.001);
	}
}

init();

