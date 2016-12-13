package gfx_main;


import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;

import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

public class progressbar_menu_Activity extends SimpleBaseGameActivity 
{

	private Camera camera;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mFaceTextureRegion;
    private static final int CAMERA_WIDTH = 720;
    private static final int CAMERA_HEIGHT = 480;
	
   
    public EngineOptions onCreateEngineOptions()
    {
    	camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), camera);
        return engineOptions;
    }


    protected void onCreateResources()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("images/");
    	this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 1024,1024, TextureOptions.BILINEAR);
    	this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "gfx_images/face.png", 0, 0, 2, 1);
    	this.mBitmapTextureAtlas.load();
    }


    protected Scene onCreateScene()
    {
    	Scene scene = new Scene();
    	scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
    	return scene;
    }

}
	




