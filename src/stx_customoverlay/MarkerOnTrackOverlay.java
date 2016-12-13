package stx_customoverlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.stx_downhill_tracker.R;

/*My overlay Class starts*/
public class MarkerOnTrackOverlay extends com.google.android.maps.Overlay
{
	private GeoPoint location = null;
	private MapActivity mapActivity;
	private int type_of_mark;
	private int offset=0;
    private float mOrientation;
	
	public MarkerOnTrackOverlay(GeoPoint location,MapActivity tmapActivity,int ttype_of_mark,int toffset)
	{
		super();
		this.location = location;
		this.mapActivity=tmapActivity;
		this.type_of_mark=ttype_of_mark;
		this.offset=toffset;
	}

	@Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{

	super.draw(canvas, mapView, shadow);
	//translate the screen pixels
	Point screenPoint = new Point();
	Point screenPts = mapView.getProjection().toPixels(this.location, screenPoint);
	
    // create a rotated copy of the marker
    Bitmap arrowBitmap = BitmapFactory.decodeResource( this.mapActivity.getResources(), this.type_of_mark);
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
        screenPts.y - (rotatedBmp.getHeight() / 2 +this.offset), // +14 Correction from center
        null
    );
	
/**	
	//add the image
	canvas.drawBitmap(BitmapFactory.decodeResource(mapActivity.getResources(), R.drawable.pin_start),
	screenPoint.x+10, screenPoint.y , null); //Setting the image  location on the screen (x,y). // +10 Correction from center
*/
	}
}
/*My overlay Class ends*/