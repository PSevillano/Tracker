package stx_main;

import com.stx_downhill_tracker.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import stx_commons.stx_commons;

public class showData extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.data);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		
		String lat,lon;
		
		for(int i=0; i<stx_commons.geoPointsArray.size();i++){
			TextView txt = new TextView(this);
			lat=String.valueOf(stx_commons.geoPointsArray.get(i).getLatitudeE6());
			lon=String.valueOf(stx_commons.geoPointsArray.get(i).getLongitudeE6());
			
			
			txt.setText("Latitud: "+lat+", Longitud: "+lon);
			layout.addView(txt);
		}
		
		
		
		
	}
	
	

}
