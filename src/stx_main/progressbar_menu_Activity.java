package stx_main;

import com.stx_downhill_tracker.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class progressbar_menu_Activity extends TabActivity{
	private TabHost tabHost;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        setTabs();
    }
    
	private void setTabs() {
		tabHost = getTabHost();
		
		addTab(R.string.tab_1, R.drawable.tab_info,MockActivity.class);
		addTab(R.string.tab_2, R.drawable.tab_info,progressbar_maps_Activity.class);
		addTab(R.string.tab_3, R.drawable.tab_info,stx_main.showData.class);
		addTab(R.string.tab_4, R.drawable.exiticon,MockActivity2.class);
		addTab(R.string.tab_2, R.drawable.newicon,progressbar_maps_Activity.class);
	}
	
	private void addTab(int labelId, int drawableId , Class<? extends Activity> activity_to_Run) {

		
		Intent intent = new Intent(this, activity_to_Run);
		
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);		
		
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
		
	}
}