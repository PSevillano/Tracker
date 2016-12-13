package stx_commons;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Projection;

public class stx_commons {
	public static ArrayList<GeoPoint> geoPointsArray = new ArrayList<GeoPoint>();
	public static ArrayList<GeoPoint> checkPointsArray = new ArrayList<GeoPoint>();
	public static Projection projection;
	
	public static TextView appStatus;
	
	public static long myCurrent_position_time_refresh=2000; // milliseconds
	
	public static boolean is_start_tracking=false;
	
	// USER CONFIGURATION VARS
	public static float distance_beetween_track_points=7.0f; // In meters
	public static int minium_GPS_accuracy=10; // In meters
	public static int distance_beetween_check_points=20; // In meters
	
	public static void showToast(Context context,String message)
	{
		Toast toast= Toast.makeText(context,message, Toast.LENGTH_SHORT);  
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
}
