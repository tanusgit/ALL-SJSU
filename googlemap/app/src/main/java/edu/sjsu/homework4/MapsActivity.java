package edu.sjsu.homework4;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.view.View;

import com.google.android.gms.maps.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private final LatLng LOCATION_UNIV = new LatLng(37.335371, -121.881050);
    private final LatLng LOCATION_CS = new LatLng(37.333714, -121.881860);

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(LOCATION_CS).title("Find Me Here!"));

        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                drawMarker(point);
                ContentValues contentValues = new ContentValues();
                contentValues.put(LocationsDB.FIELD_LAT, point.latitude );
                contentValues.put(LocationsDB.FIELD_LNG, point.longitude);
                contentValues.put(LocationsDB.FIELD_ZOOM, map.getCameraPosition().zoom);
                LocationInsertTask insertTask = new LocationInsertTask();
                insertTask.execute(contentValues);

                Toast.makeText(getBaseContext(), "Added marker", Toast.LENGTH_SHORT).show();
            }
        });

        googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                map.clear();
                LocationDeleteTask deleteTask = new LocationDeleteTask();
                deleteTask.execute();

                Toast.makeText(getBaseContext(), "Deleted all markers", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClick_CS(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_CS, 18);
        map.animateCamera(update);
    }

    public void onClick_Univ(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 14);
        map.animateCamera(update);
    }

    public void onClick_City(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 10);
        map.animateCamera(update);
    }

    ///

    private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void>{
        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);
            return null;
        }
    }

    private class LocationDeleteTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);
            return null;
        }
    }

    public Loader<Cursor> onCreateLoader(int arg0,
                                         Bundle arg1) {
        Uri uri = LocationsContentProvider.CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }


    private void drawMarker(LatLng point){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        map.addMarker(markerOptions);
    }

    @SuppressLint("Range")
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        int locationCount = 0;
        double lat=0;
        double lng=0;
        float zoom=0;

        locationCount = arg1.getCount();
        arg1.moveToFirst();

        for(int i=0;i<locationCount;i++){
            lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT));
            lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG));
            zoom = arg1.getFloat(arg1.getColumnIndex(LocationsDB.FIELD_ZOOM));
            LatLng location = new LatLng(lat, lng);
            drawMarker(location);
            arg1.moveToNext();
        }

        if(locationCount>0){
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));
            map.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }
    }

    public void onLoaderReset(Loader<Cursor> arg0) {
    }




}

