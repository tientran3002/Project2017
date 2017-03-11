package com.example.tannguyen.project2017;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import Destination.Des;
import class_Customer.Customer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView tvName,tvAddressStart,tvAddressDestination,tvNumPhone,tvWeight,tvDuration,tvDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        anhxa();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        load_intent();
    }
    public void anhxa() {
        tvName=(TextView)findViewById(R.id.tvName);
        tvAddressStart=(TextView)findViewById(R.id.tvAddressStart);
        tvAddressDestination=(TextView)findViewById(R.id.tvAddressDestination);
        tvNumPhone=(TextView)findViewById(R.id.tvNumPhone);
        tvWeight=(TextView)findViewById(R.id.tvWeight);
        tvDistance=(TextView)findViewById(R.id.tvDistance);
        tvDuration=(TextView)findViewById(R.id.tvDuration);
    }
    public void load_intent() {
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("bundle");
        Customer data= (Customer) bundle.getSerializable("select_custormer");
        tvName.setText(data.getName());
        tvWeight.setText(String.valueOf(data.getWeight()));
        tvNumPhone.setText(String.valueOf(data.getPhoneNumber()));
        tvAddressStart.setText(data.getAddressStart());
        tvAddressDestination.setText(data.getAddress()+", "+data.getDistrict()+", "+data.getCity());
        Des des=new Des(mMap,MapsActivity.this,tvDistance,tvDuration);
        des.execute(data);
    }
}
