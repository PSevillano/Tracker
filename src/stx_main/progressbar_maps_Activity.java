package stx_main;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.stx_downhill_tracker.R;

import stx_commons.stx_GPS;
import stx_commons.stx_calculations;
import stx_commons.stx_commons;
import stx_customoverlay.CustomMyLocationOverlay;
import stx_customoverlay.MarkerOnTrackItemizedOverlay;
import stx_customoverlay.MarkerOnTrackOverlay;
import stx_customoverlay.PathOverlay;
import stx_customoverlay.ShapeOnTrackOverlay;

public class progressbar_maps_Activity extends MapActivity implements SensorEventListener
{
	
	private MapView mapView;//vista por defecto del Google Maps
	private MapController mapController;//inicializcion del core de la API de Google
	
	private ImageButton button_start_tracking;//imagen como boton
	private TextView text_total_distance;//mostrar distancia
	
	CustomMyLocationOverlay currentLocationOverlay;//capa asociada por defecto a la vista de google maps(Punto de localizcion)
	
	// Check FastCalculations
	public GeoPoint last_Geopoint=null;//almacena el posicion lat y long
	public GeoPoint new_Geopoint=null;
	
	public MarkerOnTrackItemizedOverlay itemizedOverlay;
	
	public int current_distance_beetween_check_points;
	
	// variables de la brujula
	 public float currentDegree = 0f;
	 public double x,y,z;
	 public SensorManager mSensorManager;
	 public SensorManager mSensorACC;
	 private ImageView image;

   // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   // Processo de refresco del posicionamiento actual de la marca de donde estoy
   // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	// Lista de puntos a a�adir por cada segmento
	
   // Icono de inicio de recorrido
   private  Drawable icon_new_geopoint;//texturas
   private  Drawable icon_new_checkpoint;
   private  Drawable icon_new_jump;
   
   private Handler handler = new Handler();
   private Runnable refreshTask = new Runnable()
   {
     public void run()
     {
       handler.removeCallbacks(this);
       // Posiciona el mapa
       if (currentLocationOverlay.getMyLocation() != null)
       {
    	   mapController.animateTo(currentLocationOverlay.getMyLocation());
       }
       // Force redraw
       mapView.postInvalidate();
       // Delay 2 seconds
       handler.postDelayed(this, stx_commons.myCurrent_position_time_refresh);

     }
   };


   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.google_maps);//Me falta implementar el imageview en el xml googlemaps
      
      mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
      mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
      
      mSensorACC = (SensorManager) getSystemService(SENSOR_SERVICE);
      mSensorACC.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
      
      //stx_commons.appStatus = (TextView)findViewById(R.id.textViewMap);
      image = (ImageView)findViewById(R.id.minibrujula);
      // Images init
      button_start_tracking=(ImageButton)findViewById(R.id.button_start_tracking);
      // Texto de distancia total
      text_total_distance = (TextView) findViewById(R.id.tracking_distance);
      
	  // Icono GEO i CHECK
      icon_new_geopoint = getResources().getDrawable(R.drawable.pin_point);
	  icon_new_checkpoint = getResources().getDrawable(R.drawable.pin_check);
	  icon_new_jump = getResources().getDrawable(R.drawable.icon_jump);
	  
	  icon_new_geopoint.setBounds(0, 0, icon_new_geopoint.getIntrinsicWidth(), icon_new_geopoint.getIntrinsicHeight());//tamaño
	  icon_new_checkpoint.setBounds(0, 0, icon_new_checkpoint.getIntrinsicWidth(), icon_new_checkpoint.getIntrinsicHeight());
	  
	  // Puntos entre linias tipo itemized sin shadow
	  itemizedOverlay = new MarkerOnTrackItemizedOverlay(icon_new_geopoint);

      // Check GPS Signal
      stx_GPS.locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
      stx_GPS.locationManager.addGpsStatusListener(stx_GPS.onGpsStatusChange) ;
      stx_GPS.onGpsStatusChange.onGpsStatusChanged(GpsStatus.GPS_EVENT_SATELLITE_STATUS); 
      
      initMapView();
      initMyLocation();
      initMyLocationTracker();
      
      // Listener del botton de inicio de tracking
      button_start_tracking.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) 
			{
				if(stx_commons.is_start_tracking)
				{
					// Fins que no tinguem com a minim dos punts valids no le permitas al usuario parar el tracking
					if (last_Geopoint!=null)
					{
						button_start_tracking.setImageResource(R.drawable.tracking_record_0);
						stx_commons.is_start_tracking=false;
						stx_commons.showToast(getApplicationContext(), "Track recording is stopped.");
						
						// LAST LINE
						PathOverlay pathOverlay = new PathOverlay(last_Geopoint,new_Geopoint);
						// A�ade otra layer que tiene una linia entre dos puntos.
						mapView.getOverlays().add(pathOverlay);
						
						// FINISH
						MarkerOnTrackOverlay marker = new MarkerOnTrackOverlay(new_Geopoint,progressbar_maps_Activity.this,R.drawable.pin_finish,14);
						mapView.getOverlays().add(marker);
						mapView.invalidate();
					}
						
				}
				else
				{
					button_start_tracking.setImageResource(R.drawable.tracking_record_1);
					stx_commons.is_start_tracking=true;
					stx_commons.showToast(getApplicationContext(), "Track recording is started.");
				}
				
			}
	  });
      
      // Por defecto esta disabled por que el listener lo activa con un buen GPS accuracy
	  button_start_tracking.setImageResource(R.drawable.tracking_record_0_dis);
	  button_start_tracking.setClickable(false);

   }
   
   // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   // Inicializa el Mapa 
   // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   private void initMapView() 
   {
      mapView = (MapView) findViewById(R.id.mapview);//vincular el maps con el xml
      mapController = mapView.getController();//Le añadimos el controlador
      mapView.setSatellite(false);//Satelites
      mapView.setBuiltInZoomControls(true);//botones de zoom
   }

   // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   // Posicioname en mi mapa
   // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   private void initMyLocation() 
   {
      
	   stx_GPS.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	   
	   currentLocationOverlay= new CustomMyLocationOverlay(this, mapView);//vinculamos el punto azul con el mapa
	   currentLocationOverlay.enableMyLocation();//arrancar el sistema de posicionamiento
	   
	   
       //overlay.enableCompass(); // does not work in emulator
      
	   currentLocationOverlay.runOnFirstFix(new Runnable() {
         public void run() {
            // Zoom in to current location
        	 mapController.setZoom(24);
        	 mapController.animateTo(currentLocationOverlay.getMyLocation());
         }
      });
      
      stx_commons.projection = mapView.getProjection();
      
      // A�ade la layer pricipal de mi posicion actual 
      mapView.getOverlays().add(currentLocationOverlay);
      
      // Activa la tarea de actualizacion de mi posicion cada X tiempo
      Thread t = new Thread(refreshTask);
      t.start();
      
   }
   
   // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   // Inicializa el listener que pinta la linias entre dos puntos detectados solo cuando:
   // - La localizacion ha cambiado
   // - La senyal GPs es buena
   // - 
   // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   private void initMyLocationTracker()
   {
	      //LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);  
	      //button_start_tracking=(ImageButton)findViewById(R.id.button_start_tracking);
	      
	      LocationListener locationListener = new LocationListener() 
	      {
				public void onLocationChanged(Location location) //se ejecuta cuando la posicion del GPS cambia
				{
					
					// Only try if thre's Accuracy
					if (location.hasAccuracy())//buena precicon
					{
						
						// Change radius on accuracy
						currentLocationOverlay.setAccuracy(location.getAccuracy());
						
						// Check the accuracy if not discard location
						if ((int) location.getAccuracy()<stx_commons.minium_GPS_accuracy) 
						{
							
							if (!button_start_tracking.isClickable() && !stx_commons.is_start_tracking)
							{
								button_start_tracking.setImageResource(R.drawable.tracking_record_0);
								button_start_tracking.setClickable(true);
							}
							
							if (stx_commons.is_start_tracking)
							{
								// Es igual no que tingui o no bona accuracy (si la te guardem si no res)
								new_Geopoint = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
								
								// Como minimo hay dos puntos para una linia
								//if(stx_commons.geoPointsArray.size()>=1)
								if(last_Geopoint!=null)
								{
									 // Si hay mas de dos puntos proponemos este para a�adir a la lista de puntos pero 
									 // lo haremos solo si hay una distancia minima de X metros
									 
									 // Ahora que tenemos los dos geopoints calcula la distacia entre ellos en metros
									 float distance_beetween_two_points=stx_calculations.getDistanceInMeters(new_Geopoint,last_Geopoint);
									 
									 // Si hay mas de X metros entre los puntos accion si no descartamos
									 if (distance_beetween_two_points>=stx_commons.distance_beetween_track_points)
									 {
										 
										 // +++ A�ade a la lista de puntos en tracking visible//////////////////////////////////////
										 geoPosition geoPosition = new geoPosition(x,y,z,currentDegree,new_Geopoint);
										 stx_commons.geoPoints_info.add(new geoPosition(new_Geopoint));
										 //////////////////////////////////////////////////////////////////////////////////////////////
										 // ***********
										 // LINEAS
										 // ***********								
	
										PathOverlay pathOverlay = new PathOverlay(last_Geopoint,new_Geopoint);
										// A�ade otra layer que tiene una linia entre dos puntos.
										mapView.getOverlays().add(pathOverlay);
										 
										 
										 // ***********
										 // P U N T O S   O   C H E C K P O I N T S
										 // ***********
										 
										// Ahora que tenemos los dos geopoints calcula la distacia entre ellos en metros
										stx_calculations.ct_total_distance+=stx_calculations.getDistanceInMeters(new_Geopoint,last_Geopoint);
										text_total_distance.setText(Integer.toString((int) Math.floor(stx_calculations.ct_total_distance))+" m");
										
										 // OVERLAY
										 OverlayItem overlayItem = new OverlayItem(new_Geopoint, "Point", null);
										 overlayItem.setMarker(icon_new_geopoint);
										 itemizedOverlay.addOverlayItem(overlayItem);
										 itemizedOverlay.getFocus();
										 mapView.getOverlays().add(itemizedOverlay);
										 
										 // DETERMINA si es CHECKPOINT 
										 if ((stx_calculations.ct_total_distance-current_distance_beetween_check_points)> stx_commons.distance_beetween_check_points)
										 {
											 current_distance_beetween_check_points=(int) Math.floor(stx_calculations.ct_total_distance);
										     // Captura todos los puntos que puedas para tener una max reloucion para checkpoints
											 // puntos en tracking interno para deteccion de checkpoints
											// +++ A�ade a la lista de puntos en checkpoint visible
											 stx_commons.checkPointsArray.add(new_Geopoint);
											 
											 ShapeOnTrackOverlay checkpointArea = new ShapeOnTrackOverlay(progressbar_maps_Activity.this,new_Geopoint,location.getAccuracy());
											 mapView.getOverlays().add(checkpointArea);
											 mapView.invalidate();
										
										 }
										
										// Add to LASTPOINT
										last_Geopoint=new_Geopoint;
									 }
										 
							    }
								else
								{			
									 stx_commons.geoPoints_info.add(new geoPosition(new_Geopoint));//Me da un error y no se porque///////////////////////////////
									 // Es nuestro primer geopoint de la captura
									 MarkerOnTrackOverlay marker = new MarkerOnTrackOverlay(new_Geopoint,progressbar_maps_Activity.this,R.drawable.pin_start,14);
									 mapView.getOverlays().add(marker);
									 mapView.invalidate();
									 
									 //Add to LASTPOINT
									 last_Geopoint=new_Geopoint;
								}
							}
							
						}
						else
						{
							// Si hay mala se�al GPS i aun no hemos empezado el tracking deshabilita el botton
							if (!stx_commons.is_start_tracking)
							{
								button_start_tracking.setImageResource(R.drawable.tracking_record_0_dis);
								button_start_tracking.setClickable(false);
							}
						}

					}
					
					
				}
				
				public void onStatusChanged(String provider, int status, Bundle extras) {
					// TODO Auto-generated method stub
				}
				
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					
				}
				
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
					
				}


			};
			
			stx_GPS.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
			stx_GPS.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);

	        //LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);  

   }

@Override
protected boolean isRouteDisplayed() {
	// TODO Auto-generated method stub
	return false;
}

public void onSensorChanged(SensorEvent event) {
	  float degree = Math.round(event.values[0]);
	  
	  
	 
	if(event.sensor.getType()==mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION).getType()){
		 RotateAnimation ra = new RotateAnimation(currentDegree,-degree,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);

		 // how long the animation will take place
		  ra.setDuration(210);
			
		  // set the animation after the end of the reservation status
		  ra.setFillAfter(true);
				
		  // Start the animation
		  image.startAnimation(ra);
		  currentDegree = -degree;
		 
		  
	}else if(event.sensor.getType()==mSensorACC.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).getType()){
		
		x=event.values[0];
		y=event.values[1];
		z=event.values[2];

	
		
		if((Math.sqrt((x*x)+(y*y)+(z*z))/SensorManager.GRAVITY_EARTH)>5){
			double forceG = Math.sqrt((x*x)+(y*y)+(z*z))/SensorManager.GRAVITY_EARTH;
			Location location = stx_GPS.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			GeoPoint geoJump = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
			geoPosition geoPosition = new geoPosition(x,y,z,forceG,currentDegree,geoJump);
			 stx_commons.geoPoints_info.add(geoPosition);
			 
			 MarkerOnTrackOverlay jump = new MarkerOnTrackOverlay(geoPosition.geoPoint,progressbar_maps_Activity.this,R.drawable.icon_jump,14);
			 mapView.getOverlays().add(jump);
			 mapView.invalidate();

		}
	}
}

public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// TODO Auto-generated method stub
}


}