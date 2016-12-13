package stx_commons;

import android.location.Location;

import com.google.android.maps.GeoPoint;

public class stx_calculations {
	
	// ct = current_tracking
	public static float ct_total_distance=0;
	
	public static float getDistanceInMeters(GeoPoint p1, GeoPoint p2) 
	{
	    double lat1 = ((double)p1.getLatitudeE6()) / 1e6;
	    double lng1 = ((double)p1.getLongitudeE6()) / 1e6;
	    double lat2 = ((double)p2.getLatitudeE6()) / 1e6;
	    double lng2 = ((double)p2.getLongitudeE6()) / 1e6;
	    float [] dist = new float[1];
	    Location.distanceBetween(lat1, lng1, lat2, lng2, dist);
	    return dist[0];
	}
	
}
