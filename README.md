# Mapbox Android SDK Wrapper for Titanium

Uses the [mbpages branch of the Mapbox Android SDK](https://github.com/mapbox/mapbox-android-sdk/tree/mb-pages).

## Current State of Development
This is a work in progress, 
- Uses the example map `examples.map-i87786ca`. 
- For the current iteration, the goal is to expose some API’s from ti.map for setting the location, zoom, and markers.
- See the "Current Roadblock' section below, a click on a marker is causing a crash which seems to be due to a problem with loading the mapbox's tooltip.xml layout resource file, maybe this is a build issue that can be easily fixed, but everything I've tried so far has failed.

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

# Current Roadblock
The Mapbox Android SDK project is set up to build a `.aar` which contains some resources. I was not able to reference the `.aar` from the Titanium module project, so I built the Mapbox SDK as a `.jar`.
From there everything looks fine, we can load the map, set different locations with different zoom levels, and add markers.
Unfortunately the first sign of trouble is the user location not showing at all, and the log doesn’t show anything about it. Second, clicking on a marker causes the following crash, which happens while trying to create the popup from tooltip.xml, a layout resource file.

```
[ERROR] :  TiApplication: (main) [1484,41251] Sending event: exception on thread: main msg:android.view.InflateException: Binary XML file line #25: Error inflating class android.support.v7.internal.widget.ActionBarView; Titanium 3.4.0,2014/09/25 16:42,b54c467
[ERROR] :  TiApplication: android.view.InflateException: Binary XML file line #25: Error inflating class android.support.v7.internal.widget.ActionBarView
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.createView(LayoutInflater.java:620)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.createViewFromTag(LayoutInflater.java:696)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.rInflate(LayoutInflater.java:755)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.rInflate(LayoutInflater.java:758)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.parseInclude(LayoutInflater.java:814)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.rInflate(LayoutInflater.java:745)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.inflate(LayoutInflater.java:492)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.inflate(LayoutInflater.java:397)
[ERROR] :  TiApplication: 	at com.mapbox.mapboxsdk.views.InfoWindow.<init>(InfoWindow.java:41)
[ERROR] :  TiApplication: 	at com.mapbox.mapboxsdk.overlay.Marker.createTooltip(Marker.java:110)
[ERROR] :  TiApplication: 	at com.mapbox.mapboxsdk.overlay.Marker.getToolTip(Marker.java:122)
[ERROR] :  TiApplication: 	at com.mapbox.mapboxsdk.views.MapView.selectMarker(MapView.java:409)
[ERROR] :  TiApplication: 	at com.mapbox.mapboxsdk.views.MapView$1.onItemSingleTapUp(MapView.java:503)
[ERROR] :  TiApplication: 	at com.mapbox.mapboxsdk.views.MapView$1.onItemSingleTapUp(MapView.java:501)
[ERROR] :  TiApplication: 	at com.mapbox.mapboxsdk.overlay.ItemizedIconOverlay.onSingleTapUpHelper(ItemizedIconOverlay.java:191)
[ERROR] :  TiApplication: 	at com.mapbox.mapboxsdk.overlay.ItemizedIconOverlay$2.run(ItemizedIconOverlay.java:184)
[ERROR] :  TiApplication: 	at com.mapbox.mapboxsdk.overlay.ItemizedIconOverlay.activateSelectedItems(ItemizedIconOverlay.java:99)
[ERROR] :  TiApplication: 	at com.mapbox.mapboxsdk.overlay.ItemizedIconOverlay.onSingleTapConfirmed(ItemizedIconOverlay.java:177)
[ERROR] :  TiApplication: 	at com.mapbox.mapboxsdk.overlay.OverlayManager.onSingleTapConfirmed(OverlayManager.java:271)
[ERROR] :  TiApplication: 	at com.mapbox.mapboxsdk.views.MapViewGestureDetectorListener.onSingleTapConfirmed(MapViewGestureDetectorListener.java:91)
[ERROR] :  TiApplication: 	at android.view.GestureDetector$GestureHandler.handleMessage(GestureDetector.java:273)
[ERROR] :  TiApplication: 	at android.os.Handler.dispatchMessage(Handler.java:99)
[ERROR] :  TiApplication: 	at android.os.Looper.loop(Looper.java:137)
[ERROR] :  TiApplication: 	at android.app.ActivityThread.main(ActivityThread.java:5103)
[ERROR] :  TiApplication: 	at java.lang.reflect.Method.invokeNative(Native Method)
[ERROR] :  TiApplication: 	at java.lang.reflect.Method.invoke(Method.java:525)
[ERROR] :  TiApplication: 	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:737)
[ERROR] :  TiApplication: 	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:553)
[ERROR] :  TiApplication: 	at dalvik.system.NativeStart.main(Native Method)
[ERROR] :  TiApplication: Caused by: java.lang.reflect.InvocationTargetException
[ERROR] :  TiApplication: 	at java.lang.reflect.Constructor.constructNative(Native Method)
[ERROR] :  TiApplication: 	at java.lang.reflect.Constructor.newInstance(Constructor.java:417)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.createView(LayoutInflater.java:594)
[ERROR] :  TiApplication: 	... 28 more
[ERROR] :  TiApplication: Caused by: android.view.InflateException: Binary XML file line #22: Error inflating class <unknown>
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.createView(LayoutInflater.java:620)
[ERROR] :  TiApplication: 	at com.android.internal.policy.impl.PhoneLayoutInflater.onCreateView(PhoneLayoutInflater.java:56)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.onCreateView(Layou
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.createViewFromTag(LayoutInflater.java:694)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.rInflate(LayoutInflater.java:755)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.inflate(LayoutInflater.java:492)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.inflate(LayoutInflater.java:397)
[ERROR] :  TiApplication: 	at android.support.v7.internal.widget.ActionBarView.<init>(ActionBarView.java:218)
[ERROR] :  TiApplication: 	... 31 more
[ERROR] :  TiApplication: Caused by: java.lang.reflect.InvocationTargetException
[ERROR] :  TiApplication: 	at java.lang.reflect.Constructor.constructNative(Native Method)
[ERROR] :  TiApplication: 	at java.lang.reflect.Constructor.newInstance(Constructor.java:417)
[ERROR] :  TiApplication: 	at android.view.LayoutInflater.createView(LayoutInflater.java:594)
[ERROR] :  TiApplication: 	... 38 more
[ERROR] :  TiApplication: Caused by: android.content.res.Resources$NotFoundException: Resource is not a Drawable (color or path): TypedValue{t=0x2/d=0x7f010013 a=-1}
[ERROR] :  TiApplication: 	at android.content.res.Resources.loadDrawable(Resources.java:2063)
[ERROR] :  TiApplication: 	at android.content.res.TypedArray.getDrawable(TypedArray.java:601)
[ERROR] :  TiApplication: 	at android.widget.ImageView.<
```

I found that [someone was getting the exact same error](https://github.com/mapbox/mapbox-android-sdk/issues/429), and the diagnostic was that the layout xml files where missing, with the recommended solution to use a `.apklib` instead of a `.aar`.

But the problem with Titanium is that I couldn’t reference a `.apklib` either (and never found documentation on whether this is even possible at all), so I added the Mapbox SDK resources to my module project in <module root>/android/platform/android/res [as it is explained here](https://jira.appcelerator.org/browse/TC-1328) and [here](https://jira.appcelerator.org/browse/TIMOB-11360). Unfortunately this didn’t fix anything. I’ve also tried to add the line `respackage: com.mapbox.mapboxsdk` in the module’s manifest, but this would make the build of the test app fail:

```
[INFO] :   Running dexer: [...]
[ERROR] Application Installer abnormal process termination. Process exit value was 1
[ERROR] :  Failed to run dexer:
[ERROR] :  
[ERROR] :  UNEXPECTED TOP-LEVEL EXCEPTION:
[ERROR] :  java.lang.IllegalArgumentException: already added: Lcom/mapbox/mapboxsdk/R$attr;
[ERROR] :  	at com.android.dx.dex.file.ClassDefsSection.add(ClassDefsSection.java:122)
[ERROR] :  	at com.android.dx.dex.file.DexFile.add(DexFile.java:161)
[ERROR] :  	at com.android.dx.command.dexer.Main.processClass(Main.java:685)
[ERROR] :  	at com.android.dx.command.dexer.Main.processFileBytes(Main.java:634)
[ERROR] :  	at com.android.dx.command.dexer.Main.access$600(Main.java:78)
[ERROR] :  	at com.android.dx.command.dexer.Main$1.processFileBytes(Main.java:572)
[ERROR] :  	at com.android.dx.cf.direct.ClassPathOpener.processArchive(ClassPathOpener.java:284)
[ERROR] :  	at com.android.dx.cf.direct.ClassPathOpener.processOne(ClassPathOpener.java:166)
[ERROR] :  	at com.android.dx.cf.direct.ClassPathOpener.process(ClassPathOpener.java:144)
[ERROR] :  	at com.android.dx.command.dexer.Main.processOne(Main.java:596)
[ERROR] :  	at com.android.dx.command.dexer.Main.processAllFiles(Main.java:498)
[ERROR] :  	at com.android.dx.command.dexer.Main.runMonoDex(Main.java:264)
[ERROR] :  	at com.android.dx.command.dexer.Main.run(Main.java:230)
[ERROR] :  	at com.android.dx.command.dexer.Main.main(Main.java:199)
[ERROR] :  	at com.android.dx.command.Main.main(Main.java:103)
[ERROR] :  1 error; aborting
```

So the project is currently stuck there, any help would be very appreciated!



