package com.yoonkim.bestime.Map;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yoonkim.bestime.City.Airport;
import com.yoonkim.bestime.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener{
    private GoogleMap map;
    private LatLng myLocation;
    private LatLng latLng;
    private double lat1,lon1;
    private List<Airport> cities;
    private String country;  //= "unknown country";

    Geocoder geocoder = null;
    ArrayList<String> locations; //will contain all the locations

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mapper");
        actionBar.setDisplayHomeAsUpEnabled(true);  //helps with returning to our MainActivity

        geocoder = new Geocoder(this);
        locations = new ArrayList<String>();

        // This fetches the addresses from a bundle and places them in an ArrayList
        // ArrayList will be used later by GeoCoder
        Intent arts = getIntent();
        Bundle bundle = arts.getExtras();

        cities = (List<Airport>) bundle.getSerializable("cities");

        lat1 = cities.get(0).getLat();
        lon1 = cities.get(0).getLng();

        String msg = "Lng: " + lon1 + " Lat: " + lat1;
        Log.v("in map",msg);
        System.out.println(msg);
        //gets the maps to load
        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.the_map);
        mf.getMapAsync(this);

        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap map) {    // map is loaded but not laid out yet
        this.map = map;
        map.setOnMapLoadedCallback(this);      // calls onMapLoaded when layout done
        UiSettings mapSettings;
        mapSettings = map.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        map.setOnMarkerClickListener(this);


    }
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Integer c = (Integer) marker.getTag();
        marker.setTag(++c);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(c%2 == 1? BitmapDescriptorFactory.HUE_GREEN: BitmapDescriptorFactory.HUE_BLUE) );
        marker.showInfoWindow();
        Toast.makeText(getApplicationContext(), "Please use the IATA code for ticket search", Toast.LENGTH_LONG).show();
        return true;
    }
    // maps are loaded and this is where I should perform the getMoreInfo() to grab more data
    //note use of geocoder.getFromLocationName() to find LonLat from address
    @Override
    public void onMapLoaded() {
        // code to run when the map has loaded
        getMoreInfo(); // call this --> use a geoCoder to find the location of the eq
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), "Please use the IATA code for ticket search", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getMoreInfo() {
        System.out.println("in getMoreInfo " + lat1 + " " + lon1);
        latLng = new LatLng(lat1, lon1);  //used in addMarker below for placing a marker at the Longitude/Latitude spot
        Geocoder gcd = new Geocoder(this);
        try {
            List<Address> list = gcd.getFromLocation(lat1, lon1, 10);
            if (list != null & list.size() > 0) {
                country = list.get(0).getCountryName(); //grab country name from GeoCoder data from Google
                if (country==null)
                    country = "unknown country";
                System.out.println("in map getMoreInfo country " + country);
            }
            else { //no location found
                country = "unknown country";
                System.out.println("in getMoreInfo no location found");
            }
        } catch (IOException e) //no geo address found
        {
            country = "unknown country";
            Log.v("in map new test","hhhh");
        }
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));

        // puts marker icons at airports
        for(int i = 1; i < cities.size();i++) {
            Airport ap = cities.get(i);
            LatLng apLatLng = new LatLng(ap.getLat(), ap.getLng());

            Marker m = map.addMarker(new MarkerOptions()
                    .position(apLatLng)
                    .snippet("IATA code: " + ap.getIATA())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(ap.getName() + " Airport"));
            m.setTag(new Integer(0));
        }
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17), 3000, null);
    }
}