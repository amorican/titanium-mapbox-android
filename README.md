# Mapbox Android SDK Wrapper for Titanium

Uses the [mbpages branch of the Mapbox Android SDK](https://github.com/mapbox/mapbox-android-sdk/tree/mb-pages).

This is a work in progress, which uses the example map `examples.map-i87786ca`. For the current iteration, the goal is to expose some API’s from ti.map for setting the location, zoom, and markers.

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
- Latitude
- Longitude
- LatitudeDelta
- LongitudeDelta

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

# Current Roadblock
The Mapbox Android SDK project is set up to build a `.aar` which contains some resources. I was not able to reference the `.aar` from the Titanium module project, so I built the Mapbox SDK as a `.jar`.
From there everything looks fine, we can load the map, set different locations with different zoom levels, and add markers.
Unfortunately the first sign of trouble is the user location not showing at all, and the log doesn’t show anything about it. Second, clicking on a marker (where you would expect the marker popup to show) causes a crash (see console log below).

I found that [someone was getting the exact same error](https://github.com/mapbox/mapbox-android-sdk/issues/429), and the diagnostic was that the layout xml files where missing, with the recommended solution to use a `.apklib` instead of a `.aar`.

But the problem with Titanium is that I couldn’t reference a `.apklib` either (and never found documentation on whether this is even possible at all), so I added the Mapbox SDK resources to my module project in <module root>/android/platform/android/res and added the line `respackage: com.mapbox.mapboxsdk` in the manifest [as it is explained here](https://jira.appcelerator.org/browse/TC-1328) and [here](https://jira.appcelerator.org/browse/TIMOB-11360). Unfortunately this didn’t fix anything.



