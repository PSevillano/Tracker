package stx_customoverlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.util.FloatMath;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class ShapeOnTrackOverlay extends Overlay 
{

    Context context;
    float accuracy;
    GeoPoint newGeopoint;

     public ShapeOnTrackOverlay(Context tcontext, GeoPoint tnewGeopoint, float tacc ) 
     {
            context = tcontext;
            newGeopoint=tnewGeopoint;
            accuracy = tacc;
     }

     public void draw(Canvas canvas, MapView mapView, boolean shadow) 
     {
         // NEW ADD
         Projection projection = mapView.getProjection();
         Point center = new Point();
  
         int radius = (int) (projection.metersToEquatorPixels(accuracy));
         projection.toPixels(newGeopoint, center);
  
         Paint accuracyPaint = new Paint();
         accuracyPaint.setAntiAlias(true);
         accuracyPaint.setStrokeWidth(2.0f);
         accuracyPaint.setColor(0x30e68c00);
         accuracyPaint.setStyle(Style.STROKE);
  
         canvas.drawCircle(center.x, center.y, radius, accuracyPaint);
  
         accuracyPaint.setColor(0x18e68c00); // 18 es com de transparent fins ff
         accuracyPaint.setStyle(Style.FILL);
         canvas.drawCircle(center.x, center.y, radius, accuracyPaint);
 
    }
}
