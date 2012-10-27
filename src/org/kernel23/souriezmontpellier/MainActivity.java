package org.kernel23.souriezmontpellier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class MainActivity extends  MapActivity {

	MapView mapView;
	MapController mc;
	GeoPoint p;
	GeoPoint cctv;
	
	LocationManager lm;
	LocationListener locationListener;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     
        
       	LocationManager locationManager;
    	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	Location loc;
    	if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
    		loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	}else{
    		loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	}
    	
        mapView = (MapView) findViewById(R.id.mapView);
        mc = mapView.getController();
    
        double lat = loc.getLatitude();
        double lng = loc.getLongitude();
        p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
        
        mc.animateTo(p);
        mc.setZoom(15);
        mapView.invalidate();
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        
        Drawable Mydrawable = this.getResources().getDrawable(R.drawable.me);
        HelloItemizedOverlay Myitemizedoverlay = new HelloItemizedOverlay(Mydrawable,this);
        OverlayItem Myoverlayitem = new OverlayItem(p, "", "Vous �tes ici !");
        Myitemizedoverlay.addOverlay(Myoverlayitem);       
        
        mapOverlays.add(Myitemizedoverlay);
        
       
        Drawable drawable = this.getResources().getDrawable(R.drawable.cctv);
        HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable,this);    

        InputStream is = getResources().openRawResource(R.raw.montpellier_cctv_gps_database);
        try
        {
       	 BufferedReader reader = new BufferedReader(new InputStreamReader(is));
       	 String line;
       
        while ((line = reader.readLine()) != null )
        {
       	 	String [] rowData = line.split(";");

   				Double myLat = Double.parseDouble(rowData[6]);
   				Double myLong = Double.parseDouble(rowData[7]);
   				GeoPoint geopoint = new GeoPoint((int)(myLat*1E6), (int)(myLong*1E6));
   				OverlayItem overlayitem2 = new OverlayItem(geopoint, "Informations", rowData[2]+"\n"+rowData[3]+"\n"+rowData[4]+"\n"+rowData[5]);
   				itemizedoverlay.addOverlay(overlayitem2);
        }
        
        mapOverlays.add(itemizedoverlay);
	       
        }
        catch (FileNotFoundException e)
        {
       	 e.printStackTrace();

        } catch (IOException e) {
       	 e.printStackTrace();
        }
     
        /*
        Button next = (Button) findViewById(R.id.button1);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
            }

        });
        */
        
         
    }

    public void MystartService(View view) {
        startService(new Intent(getBaseContext(), LocationService.class));
    }

    public void MystopService(View view) {
        stopService(new Intent(getBaseContext(), LocationService.class));
    }

    
    @Override
    public void onStop() {
    	super.onStop();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    }     
    
    @Override
    public void onPause() {
    	super.onPause();
    }     

    @Override
	public void onResume() {
    	super.onResume();
    	
    	LocationManager locationManager;
    	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	Location loc;
    	if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
    		loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	}else{
    		loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	}
    	
/*    	
  		TextView lat = (TextView)findViewById(R.id.textView1);
        lat.setText(String.format("%f",loc.getLatitude()));
  		TextView longi = (TextView)findViewById(R.id.textView2);
  		longi.setText(String.format("%f",loc.getLongitude()));
*/
   }
  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.startApp:
        	this.MystartService(mapView);
            return true;
        case R.id.stopApp:
        	this.MystopService(mapView);
            return true;       
        case R.id.submitApp:
            return true;               
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
    
}