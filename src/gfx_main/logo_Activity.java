package gfx_main;

import org.andengine.engine.options.ScreenOrientation;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;

import stx_engine.CustomBaseSplashActivity;
import android.app.Activity;

public class logo_Activity extends CustomBaseSplashActivity 
{
	
	// define el numero de segundos del splash
	private static final int SPLASH_DURATION = 3;

	@Override
	protected ScreenOrientation getScreenOrientation() {
		//debemos devolver la orientación de la pantalla, en este caso es apaisada.
		return ScreenOrientation.PORTRAIT_FIXED;
	}

	@Override
	protected String onGetSplashTexturePath() {
		// TODO Auto-generated method stub
		return "stx_images/logo/logointro.png";
	}

	@Override
	protected float onGetSplashDuration() {
		// TODO Auto-generated method stub
		return SPLASH_DURATION;
	}

	@Override
	protected Class<? extends Activity> getFollowUpActivity() {
		// IMPORTANT!!!!
		//lo que debemos devolver es la Actividad que se va a ejecutar después del splash. En este caso main.
		return stx_main.progressbar_menu_Activity.class ;
	}


}
