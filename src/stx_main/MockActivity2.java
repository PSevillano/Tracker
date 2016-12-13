package stx_main;

import android.app.Activity;
import android.os.Bundle;

public class MockActivity2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.exit(0);
	}

}