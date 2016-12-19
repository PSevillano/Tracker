package stx_customoverlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Projection;
import com.stx_downhill_tracker.R;


public class CustomMyLocationOverlay extends MyLocationOverlay {
    private Context mContext;
    private float   mOrientation;
    
    // NEW ADD
    private float accuracy=10.0f;

    public CustomMyLocationOverlay(Context context, MapView mapView) {
        super(context, mapView);
        mContext = context;
    }
    
    @Override 
    protected void drawMyLocation(Canvas canvas, MapView mapView, Location lastFix, GeoPoint myLocation, long when) {
        // translate the GeoPoint to screen pixels
        Point screenPts = mapView.getProjection().toPixels(myLocation, null);

        // create a rotated copy of the marker
        Bitmap arrowBitmap = BitmapFactory.decodeResource( mContext.getResources(), R.drawable.mycurrentlocation_icon);
        Matrix matrix = new Matrix();
        matrix.postRotate(mOrientation);
        Bitmap rotatedBmp = Bitmap.createBitmap(
            arrowBitmap, 
            0, 0, 
            arrowBitmap.getWidth(), 
            arrowBitmap.getHeight(), 
            matrix, 
            true
        );
        // add the rotated marker to the canvas
        canvas.drawBitmap(
            rotatedBmp, 
            screenPts.x - (rotatedBmp.getWidth()  / 2), 
            screenPts.y - (rotatedBmp.getHeight() / 2 +14), // +14 Correction from center
            null
        );
        
        // NEW ADD
        Projection projection = mapView.getProjection();
        Point center = new Point();
 
        int radius = (int) (projection.metersToEquatorPixels(accuracy));
        projection.toPixels(myLocation, center);
 
        Paint accuracyPaint = new Paint();
        accuracyPaint.setAntiAlias(true);
        accuracyPaint.setStrokeWidth(2.0f);
        accuracyPaint.setColor(0xffa50000);
        accuracyPaint.setStyle(Style.STROKE);
 
        canvas.drawCircle(center.x, center.y, radius, accuracyPaint);
 
        accuracyPaint.setColor(0x18e68c00); // 18 es com de transparent fins ff
        accuracyPaint.setStyle(Style.FILL);
        canvas.drawCircle(center.x, center.y, radius, accuracyPaint);
        
    }

    public void setAccuracy(float tacc){
    	this.accuracy=tacc;
    }
    
    public void setOrientation(float newOrientation) {
         mOrientation = newOrientation;
    }
}



