package flg.mapbox;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import org.appcelerator.titanium.view.TiCompositeLayout;
import org.appcelerator.titanium.view.TiUIView;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.util.TiConvert;
import android.view.View;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RatingBar;
import android.graphics.Color;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.GpsLocationProvider;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.tileprovider.tilesource.*;
//import com.mapbox.mapboxsdk.views;
import com.mapbox.mapboxsdk.views.util.TilesLoadedListener;


public class MapView extends TiUIView 
{
	// Standard Debugging variables
	private static final String LCAT = "MapboxModule";

	// Dictionary key names
	private static final String PROPERTY_REGION = "region";
	public static final String PROPERTY_LATITUDE = "latitude";
	public static final String PROPERTY_LONGITUDE = "longitude";
	private static final String PROPERTY_ANNOTATIONS = "annotations";
	private static final String PROPERTY_LATITUDE_DELTA = "latitudeDelta";
	private static final String PROPERTY_LONGITUDE_DELTA = "longitudeDelta";
	private static final String PROPERTY_USER_LOCATION = "userLocation";
	private static final String PROPERTY_ANNOTATION_TITLE = "title";
	private static final String PROPERTY_ANNOTATION_SUBTITLE = "subtitle";
	
	private static final int DEFAULT_ZOOM_LEVEL = 15;
	
	private com.mapbox.mapboxsdk.views.MapView mapView;
	private ArrayList<Marker> markers = new ArrayList<Marker>();

	private UserLocationOverlay userLocationOverlay;

	public MapView(TiViewProxy proxy) 
	{
		super(proxy);
		
		Log.d(LCAT, "[VIEW LIFECYCLE EVENT] view");
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout holder = new LinearLayout(proxy.getActivity());
		holder.setLayoutParams(lp);
		
		mapView = new com.mapbox.mapboxsdk.views.MapView(proxy.getActivity());
		mapView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		String currentMap ="examples.map-i87786ca";
		ITileLayer source = new MapboxTileLayer(currentMap);
		mapView.setTileSource(source);

		mapView.setMinZoomLevel(mapView.getTileProvider().getMinimumZoomLevel());
		mapView.setMaxZoomLevel(mapView.getTileProvider().getMaximumZoomLevel());
		mapView.setCenter(mapView.getTileProvider().getCenterCoordinate());
		
		addLocationOverlay();

        holder.addView(mapView);
		this.setNativeView(holder);
	}
		
	// The view is automatically registered as a model listener when the view
	// is realized by the view proxy. That means that the processProperties
	// method will be called during creation and that propertiesChanged and 
	// propertyChanged will be called when properties are changed on the proxy.

	@Override
	public void processProperties(KrollDict props) 
	{
		super.processProperties(props);
		
		Log.d(LCAT,"[VIEW LIFECYCLE EVENT] processProperties " + props);

		if (props.containsKey(PROPERTY_REGION)) {
			HashMap regionDict = (HashMap)props.get(PROPERTY_REGION);
			this.setRegion(regionDict);
			notifyOfRegionChange(regionDict);
		}
		if (props.containsKey(PROPERTY_USER_LOCATION)) {
			Object userLocationFlag = props.get(PROPERTY_USER_LOCATION);
			Boolean show = TiConvert.toBoolean(userLocationFlag);
			this.setUserLocation(show);
			notifyOfUserLocationChange(show);
		}
		if (props.containsKey(PROPERTY_ANNOTATIONS)) {
			Object[] annotations = (Object[])props.get(PROPERTY_ANNOTATIONS);
			this.setAnnotations(annotations);
			notifyOAnnotationsChange(annotations);
		}
	}
	
	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue, KrollProxy proxy)
	{
		// This method is called whenever a proxy property value is updated. Note that this 
		// method is only called if the new value is different than the current value.

		super.propertyChanged(key, oldValue, newValue, proxy);
		
		Log.d(LCAT,"[VIEW LIFECYCLE EVENT] propertyChanged: " + key + ' ' + oldValue + ' ' + newValue);
	}
	
	//
	// Local helper methods to fire the somethingChange events
	//
	private void notifyOfRegionChange(HashMap newRegion)
	{
		// The event listeners for a view are actually attached to the view proxy.
		// You must reference 'proxy' to get the proxy for this view.
		
		Log.d(LCAT,"[VIEW LIFECYCLE EVENT] notifyOfRegionChange");
		
		// It is a good idea to check if there are listeners for the event that
		// is about to be fired. There could be zero or multiple listeners for the
		// specified event.
		if (proxy.hasListeners("regionChange")) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("region", newRegion);
			proxy.fireEvent("regionChange", hm);
		}
	}
	
	private void notifyOfUserLocationChange(Boolean flag)
	{
		if (proxy.hasListeners("userLocationChange")) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("userLocation", flag);
			proxy.fireEvent("userLocationChange", hm);
		}
	}
	
	private void notifyOAnnotationsChange(Object[] annotations)
	{
		if (proxy.hasListeners("userLocationChange")) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("annotations", annotations);
			proxy.fireEvent("annotationsChange", hm);
		}
	}
	
	// Setter method called by the proxy when the property is
	// set. This could also be handled in the propertyChanged handler.
	public void setRegion(final HashMap regionDict) {
		float latitude = 0;
		float longitude = 0;
		if (regionDict.containsKey(PROPERTY_LATITUDE)) {
			latitude = (TiConvert.toFloat(regionDict, PROPERTY_LATITUDE));
		}
		if (regionDict.containsKey(PROPERTY_LONGITUDE)) {
			longitude = (TiConvert.toFloat(regionDict, PROPERTY_LONGITUDE));
		}
		ILatLng latlong = new LatLng(latitude, longitude);
		this.setZoom(regionDict);
		mapView.setCenter(latlong);
	}
	
	public void setZoom(final HashMap regionDict) {
		float latitudeDelta = 0;
		float longitudeDelta = 0;
		Boolean hasZoomInfo = false;
		
		if (regionDict.containsKey(PROPERTY_LATITUDE_DELTA)) {
			latitudeDelta = (TiConvert.toFloat(regionDict, PROPERTY_LATITUDE_DELTA));
			hasZoomInfo = true;
			
		}
		if (regionDict.containsKey(PROPERTY_LONGITUDE_DELTA)) {
			longitudeDelta = (TiConvert.toFloat(regionDict, PROPERTY_LONGITUDE_DELTA));
			hasZoomInfo = true;
		}
		
		if (hasZoomInfo) {
			mapView.setZoom(convertCoordinatesDeltaToZoomLevel(latitudeDelta, longitudeDelta));
		}
	}
	
	public void setAnnotations(final Object[] annotations) {
		removeMarkers();
		for (Object anAnnotation : annotations) {
			if (!(anAnnotation instanceof HashMap)) {
				Log.e(LCAT, "An object in the array parameter passed to setAnnotation is not an dictionary.");
				continue;
			}
			
			HashMap<String, Object> theAnnotation = (HashMap<String, Object>)anAnnotation;
			float latitude = 0;
			float longitude = 0;
			String annotationTitle = "";
			String annotationSubtitle = "";
			
			if (theAnnotation.containsKey(PROPERTY_LATITUDE)) {
				latitude = (TiConvert.toFloat(theAnnotation, PROPERTY_LATITUDE));
			}
			if (theAnnotation.containsKey(PROPERTY_LONGITUDE)) {
				longitude = (TiConvert.toFloat(theAnnotation, PROPERTY_LONGITUDE));
			}
			if (theAnnotation.containsKey(PROPERTY_ANNOTATION_TITLE)) {
				annotationTitle = TiConvert.toString(theAnnotation, PROPERTY_ANNOTATION_TITLE);
			}
			if (theAnnotation.containsKey(PROPERTY_ANNOTATION_SUBTITLE)) {
				annotationSubtitle = TiConvert.toString(theAnnotation, PROPERTY_ANNOTATION_SUBTITLE);
			}
			LatLng latlong = new LatLng(latitude, longitude);

			Marker m = new Marker(mapView, annotationTitle, annotationSubtitle, latlong);
	        m.setIcon(new Icon(proxy.getActivity(), Icon.Size.SMALL, "marker-stroked", "FF0000"));
	        markers.add(m);
	        mapView.addMarker(m);
		}
	}
	
	public void setUserLocation(final Boolean flag) {
		Log.i(LCAT, ((flag) ? "Add " : "Remove ") + "user location overlay on map.");
		if (flag) {
			addLocationOverlay();
		}
		else {
			removeLocationOverlay();
		}
	}
	
	private void removeMarkers() {
		for (Marker aMarker : markers) {
			mapView.removeMarker(aMarker);
		}
		markers = new ArrayList<Marker>();
	}
	
	private void addLocationOverlay() {
		removeLocationOverlay();
		userLocationOverlay = new UserLocationOverlay(new GpsLocationProvider(proxy.getActivity()), mapView);
		userLocationOverlay.setDrawAccuracyEnabled(true);
		mapView.getOverlays().add(userLocationOverlay);
	}
	
	private void removeLocationOverlay() {
		if (userLocationOverlay != null) {
			mapView.getOverlays().remove(userLocationOverlay);
			userLocationOverlay = null;
		}
	}
	
	private int convertCoordinatesDeltaToZoomLevel(float latitudeDelta, float longitudeDelta) {
		float averageDelta = (latitudeDelta + longitudeDelta) / 2;

		// Set the zoom level according to the passed lat/lng deltas using the table at
		// http://wiki.openstreetmap.org/wiki/Zoom_levels
		int zoomLevel = DEFAULT_ZOOM_LEVEL;
		
		if (averageDelta <= 0) {
			zoomLevel = 20;
		}
		else if (averageDelta < 0.0005) {
			zoomLevel = 19;
		}
		else if (averageDelta < 0.001) {
			zoomLevel = 18;
		}
		else if (averageDelta < 0.003) {
			zoomLevel = 17;
		}
		else if (averageDelta < 0.005) {
			zoomLevel = 16;
		}
		else if (averageDelta < 0.011) {
			zoomLevel = 15;
		}
		else if (averageDelta < 0.022) {
			zoomLevel = 14;
		}
		else if (averageDelta < 0.044) {
			zoomLevel = 13;
		}
		else if (averageDelta < 0.088) {
			zoomLevel = 12;
		}
		else if (averageDelta < 0.176) {
			zoomLevel = 11;
		}
		else if (averageDelta < 0.352) {
			zoomLevel = 10;
		}
		else if (averageDelta < 0.703) {
			zoomLevel = 9;
		}
		else if (averageDelta < 1.406) {
			zoomLevel = 8;
		}
		else if (averageDelta < 2.813) {
			zoomLevel = 7;
		}
		else if (averageDelta < 5.625) {
			zoomLevel = 6;
		}
		else if (averageDelta < 11.25) {
			zoomLevel = 5;
		}
		else if (averageDelta < 22.5) {
			zoomLevel = 4;
		}
		else if (averageDelta < 45) {
			zoomLevel = 3;
		}
		else if (averageDelta < 90) {
			zoomLevel = 2;
		}
		else if (averageDelta < 180) {
			zoomLevel = 1;
		}
		else if (averageDelta < 360) {
			zoomLevel = 0;
		}
		return zoomLevel;
	}
}