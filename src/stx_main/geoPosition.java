package stx_main;

import com.google.android.maps.GeoPoint;

public class geoPosition {
	double x,y,z,forceG;
	float degree;
	GeoPoint geoPoint;
	
	public geoPosition(GeoPoint geoPoint) {
		super();
		this.geoPoint = geoPoint;
	}
	
	public geoPosition(double x, double y, double z, float degree,
			GeoPoint geoPoint) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.degree = degree;
		this.geoPoint = geoPoint;
	}

	public geoPosition(double x, double y, double z, double forceG,
			float degree, GeoPoint geoPoint) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.forceG = forceG;
		this.degree = degree;
		this.geoPoint = geoPoint;
	}
	
	

	
	
	
	
	
	
	
	
	

}
