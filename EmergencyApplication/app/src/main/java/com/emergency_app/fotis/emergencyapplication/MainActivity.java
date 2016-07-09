package com.emergency_app.fotis.emergencyapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        boolean gpsEnabled = false;
        try{
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!gpsEnabled ) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            dialog.setMessage(getApplicationContext().getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getApplicationContext().getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(getApplicationContext().getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
//                makeUseOfNewLocation(location);
                Toast.makeText(getApplicationContext(),location.toString(),Toast.LENGTH_SHORT).show();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(),provider,Toast.LENGTH_SHORT).show();

            }

            public void onProviderDisabled(String provider) {
                Toast.makeText(getApplicationContext(),provider,Toast.LENGTH_SHORT).show();

            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(),"not available",Toast.LENGTH_SHORT).show();

            return;
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                new PostRequest().execute();


            }
        });


        ImageView imgFire = (ImageView) findViewById(R.id.fire);
        imgFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"FIRE",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Other.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class PostRequest extends AsyncTask<URL, Integer, Void>{

        private String getQuery(HashMap<String,String> params) throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (Map.Entry<String, String> pair : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }

            return result.toString();
        }

        @Override
        protected Void doInBackground(URL... urls) {
            try{
                URL url = new URL("http://192.168.10.62:8000/category/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                String userCredentials = "fotis:Apoel788";
                String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(),0));
                conn.setRequestProperty ("Authorization", basicAuth);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                HashMap<String, String> params = new HashMap<>();
                params.put("name", "yiota");


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                Log.w("TAG",conn.getResponseMessage());
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;

        }
//        protected void onPostExecute(Long result) {
//            showDialog("Downloaded " + result + " bytes");
//        }

    }

}
