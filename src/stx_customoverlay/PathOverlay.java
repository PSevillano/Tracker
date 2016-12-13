package stx_customoverlay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
	
public class PathOverlay extends Overlay{
		 
		private GeoPoint gP1;
		private GeoPoint gP2;
		
		public PathOverlay(GeoPoint tgP1,GeoPoint tgP2)
		{
			this.gP1=tgP1;
			this.gP2=tgP2; 
			
		}
		
		public void draw(Canvas canvas, MapView mapv, boolean shadow)
		{
		     super.draw(canvas, mapv, shadow);
		     //Configuring the paint brush
		 	 Paint mPaint = new Paint();
			 mPaint.setDither(true);
			 mPaint.setColor(0x30e68c00);
			 mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			 mPaint.setStrokeJoin(Paint.Join.ROUND);
			 mPaint.setStrokeCap(Paint.Cap.ROUND);
			 mPaint.setStrokeWidth(5);
			 
		     Point p1 = new Point();
			 Point p2 = new Point();
			 Path path1 = new Path();
			 stx_commons.stx_commons.projection.toPixels(gP1, p1);
			 stx_commons.stx_commons.projection.toPixels(gP2, p2);
		 
			 path1.moveTo(p1.x,p1.y);
			 path1.lineTo(p2.x,p2.y);
			 
			 canvas.drawPath(path1,mPaint);
	     }
	}