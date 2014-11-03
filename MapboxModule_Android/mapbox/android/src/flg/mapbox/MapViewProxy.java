package flg.mapbox;

import java.util.HashMap;
import java.util.ArrayList;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.AsyncResult;
import org.appcelerator.kroll.common.TiMessenger;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiContext;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiUIView;
import org.appcelerator.titanium.proxy.TiViewProxy;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;


@Kroll.proxy(creatableInModule = MapboxModule.class)
public class MapViewProxy extends TiViewProxy 
{
	// Standard Debugging variables
	private static final String LCAT = "MapboxModule";

	private static final int MSG_FIRST_ID = TiViewProxy.MSG_LAST_ID + 1;
	private static final int MSG_SET_REGION = MSG_FIRST_ID + 500;
	private static final int MSG_SET_ANNOTATIONS = MSG_FIRST_ID + 501;
	private static final int MSG_SET_USER_LOCATION = MSG_FIRST_ID + 502;

	public MapViewProxy() {
		super();
		
		Log.d(LCAT, "[VIEWPROXY LIFECYCLE EVENT] init");
	}
	
	@Override
	public TiUIView createView(Activity activity) 
	{
		MapView view = new MapView(this);
		view.getLayoutParams().autoFillsHeight = true;
		view.getLayoutParams().autoFillsWidth = true;
		
		return view;
	}

	// Handle creation options
	@Override
	public void handleCreationDict(KrollDict options) 
	{
		// This method is called from handleCreationArgs if there is at least
		// argument specified for the proxy creation call and the first argument
		// is a KrollDict object.

		Log.d(LCAT, "VIEWPROXY LIFECYCLE EVENT] handleCreationDict " + options);

		// Calling the superclass method ensures that the properties specified
		// in the dictionary are properly set on the proxy object.
		super.handleCreationDict(options);
	}

	public void handleCreationArgs(KrollModule createdInModule, Object[] args) 
	{
		// This method is one of the initializers for the proxy class. The arguments
		// for the create call are passed as an array of objects. If your proxy 
		// simply needs to handle a single KrollDict argument, use handleCreationDict.
		// The superclass method calls the handleCreationDict if the first argument
		// to the create method is a dictionary object.

		Log.d(LCAT, "VIEWPROXY LIFECYCLE EVENT] handleCreationArgs ");
	
		for (int i = 0; i < args.length; i++) {
			Log.d(LCAT, "VIEWPROXY LIFECYCLE EVENT] args[" + i + "] " + args[i]);
		}

		super.handleCreationArgs(createdInModule, args);
	}

	@Override
	public boolean handleMessage(Message msg) {
		AsyncResult result = null;
		switch (msg.what) {
			case MSG_SET_REGION: {
				result = (AsyncResult)msg.obj;
				handleSetRegion((HashMap)result.getArg());
				result.setResult(null);
//				handleSetRegion((HashMap)msg.obj);
				return true;
			}
			case MSG_SET_ANNOTATIONS: {
				result = (AsyncResult)msg.obj;
				handleSetAnnotations((Object[])result.getArg());
				result.setResult(null);
				return true;
			}
			case MSG_SET_USER_LOCATION: {
				result = (AsyncResult)msg.obj;
				handleSetUserLocation((Boolean)result.getArg());
				result.setResult(null);
				return true;
			}
			default : {
				return super.handleMessage(msg);
			}
		}
	}
	// Proxy properties are forwarded to the view in the propertyChanged
	// notification. If the property update needs to occur on the UI thread
	// then create the setProperty method in the proxy and forward to the view.
	@Kroll.setProperty(retain=false) @Kroll.method
	public void setRegion(final Object region) 
	{
		Log.d(LCAT,"[VIEWPROXY LIFECYCLE EVENT] Property Set: setRegion");
		if (!(region instanceof HashMap)) {
			Log.e(LCAT,"Invalid type passed to method setRegion.");
			return;
		}
		
		HashMap regionDict = (HashMap)region;
		if (!regionDict.containsKey(MapView.PROPERTY_LATITUDE) || !regionDict.containsKey(MapView.PROPERTY_LONGITUDE)) {
			Log.e(LCAT,"setRegion was called with a parameter missing either the latitude or longitude key; Unable to set the region.");
			return;
		}
		
		if (TiApplication.isUIThread()) {
			handleSetRegion(regionDict);
		} else {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_REGION), regionDict);
//			getMainHandler().obtainMessage(MSG_SET_REGION, regionDict).sendToTarget();
		}
		
		// Call setProperty if you want the property set on the proxy and
		// to signal the propertyChanged notification
		setProperty("region", regionDict, true);
	}
	
	@Kroll.setProperty(retain=false) @Kroll.method
	public void setAnnotations(final Object annotations)
	{
		Log.i(LCAT,"[VIEWPROXY LIFECYCLE EVENT] Property Set: setAnnotations " + annotations);
		if (!(annotations instanceof Object[])) {
			Log.e(LCAT, "Object parameter passed to setAnnotation is not an array.");
			return;
		}

		Object[] annotationsArray = (Object[])annotations;
		if (TiApplication.isUIThread()) {
			handleSetAnnotations(annotationsArray);
		} else {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_ANNOTATIONS), annotationsArray);
		}
				
		setProperty("annotations", annotations, true);
	}
	
	@Kroll.setProperty(retain=false) @Kroll.method
	public void setUserLocation(final Object flag) {
		Log.i(LCAT,"[VIEWPROXY LIFECYCLE EVENT] Property Set: setUserLocation " + flag);
		if (!(flag instanceof Boolean)) {
			Log.e(LCAT, "Object parameter passed to setUserLocation is not a boolean.");
			return;
		}
		Boolean showLocation = TiConvert.toBoolean(flag);

		if (TiApplication.isUIThread()) {
			handleSetUserLocation(showLocation);
		} else {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_USER_LOCATION), showLocation);
		}
		
		setProperty("userLocation", flag, true);
	}

	public void handleSetRegion(HashMap<String, Object> regionDict)
	{
		MapView mapView = (MapView)view;
		if (!(mapView instanceof MapView)) {
			Log.e(LCAT,"MapView View Object hasn't been instantiated yet; Unable to set region.");
			return;
		}
		mapView.setRegion(regionDict);
	}
	
	public void handleSetAnnotations(Object[] annotations)
	{
		MapView mapView = (MapView)view;
		if (!(mapView instanceof MapView)) {
			Log.e(LCAT,"MapView View Object hasn't been instantiated yet; Unable to set annotations.");
			return;
		}
		mapView.setAnnotations(annotations);
	}
	
	public void handleSetUserLocation(Boolean flag)
	{
		MapView mapView = (MapView)view;
		if (!(mapView instanceof MapView)) {
			Log.e(LCAT,"MapView View Object hasn't been instantiated yet; Unable to set user location.");
			return;
		}
		mapView.setUserLocation(flag);
	}
	
	@Kroll.getProperty @Kroll.method
	public HashMap getRegion() {
		return (HashMap)getProperty("region");
	}
}