package stx_commons;

import java.util.Iterator;

import stx_customoverlay.PathOverlay;

import com.google.android.maps.GeoPoint;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.util.Log;

public class stx_GPS 
{
	
	public static LocationManager locationManager;
	public static LocationListener locationListener;
	public static Location Location;
	
	// Number of meters of accurary to pin geopoint to track path arraylist
	// public static int minium_GPS_accuracy=10;
	
	   public static final Listener onGpsStatusChange=new GpsStatus.Listener()
	   {
	           public void onGpsStatusChanged(int event)
	           {
	               switch(event)
	                   {
	                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
	                    	
	                         GpsStatus xGpsStatus=locationManager.getGpsStatus(null) ;

	                         Iterable<GpsSatellite> iSatellites=xGpsStatus.getSatellites();
	                         Iterator<GpsSatellite> it=iSatellites.iterator();
	                          int count=0;
	                          while(it.hasNext())
	                          {
		                          count=count+1;
		                          GpsSatellite oSat=(GpsSatellite) it.next();
	                          } 
	                    break;
	                   }
	           }
	   };
	   
	   
}
