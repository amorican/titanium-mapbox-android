# Mapbox Android SDK Wrapper for Titanium

Uses the [mbpages branch of the Mapbox Android SDK](https://github.com/mapbox/mapbox-android-sdk/tree/mb-pages).

## Current State of Development
This is a work in progress, 
- Uses the example map `examples.map-i87786ca`. 
- For the current iteration, the goal is to expose some API’s from ti.map for setting the location, zoom, and markers.

## Source & Test App
The root of this repo contains the module source code and the test app respectively.

## Example

```javascript
var mapbox = require('flg.mapbox');

var mapView = mapbox.createMapView({
	width : Titanium.UI.FILL,
	height : Titanium.UI.FILL,
	top : 3,
	bottom : 3,
	left : 3,
	right : 3,
	region : {
		latitude : 39.743878,
		longitude :  -105.020118,
		latitudeDelta : 0.0005,
		longitudeDelta : 0.0005
	},
	userLocation : showUserLocation
});

win.add(mapView);
win.open();
```

## Properties
### region
Dictionary with keys:
- latitude
- longitude
- latitudeDelta
- longitudeDelta

### userLocation
Boolean, show or hide the current location on the map.

### annotations
Array of dictionaries with keys:
- title
- subtitle
- latitude
- longitude

## Methods
### setRegion
### setUserLocation
### setAnnotations
