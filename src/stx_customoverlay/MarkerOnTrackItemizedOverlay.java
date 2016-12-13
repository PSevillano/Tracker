package stx_customoverlay;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;


public class MarkerOnTrackItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	 
	 private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	 
	 public MarkerOnTrackItemizedOverlay(Drawable defaultMarker) {
	 super(boundCenter(defaultMarker));
	 //super(boundCenterBottom(defaultMarker));
	 // TODO Auto-generated constructor stub
	 }
	 
	 @Override
	 protected OverlayItem createItem(int i) {
	 return mOverlays.get(i);
	 }
	 
	 @Override
	 public int size() {
	 return mOverlays.size();
	 }
	
	 @Override
	 public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	        if(!shadow) {
	            super.draw(canvas, mapView, false);

	        }
	    }
	
	 public void addOverlayItem(OverlayItem overlayItem) {
	 mOverlays.add(overlayItem);
	 populate();
	 }
	 
	 }