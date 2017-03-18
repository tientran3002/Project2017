package com.example.tannguyen.project2017;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Destination.Des;
import class_Customer.Customer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView tvName,tvAddressStart,tvAddressDestination,tvNumPhone,tvWeight,tvDuration,tvDistance;
    private Button btnselect;
    private Customer data;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        anhxa();
        event();
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
        btnselect=(Button)findViewById(R.id.btnselect);
        btnselect.setVisibility(View.VISIBLE);
        img=(ImageView)findViewById(R.id.imageView);
    }
    public void load_intent() {
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("bundle");
        data= (Customer) bundle.getSerializable("select_custormer");
        tvName.setText(data.getName());
        tvWeight.setText(String.valueOf(data.getWeight()));
        tvNumPhone.setText(String.valueOf(data.getPhoneNumber()));
        tvAddressStart.setText(data.getAddressStart());
        tvAddressDestination.setText(data.getAddress()+", "+data.getDistrict()+", "+data.getCity());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference store_image=storage.getReferenceFromUrl("gs://doan-63316.appspot.com/hh.jpg");
        Glide.with(MapsActivity.this)
                .using(new FirebaseImageLoader())
                .load(store_image)
                .into(img);
        Des des=new Des(mMap,MapsActivity.this,tvDistance,tvDuration);
        des.execute(data);
    }
    public void event(){
        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef= database.getReference("doan-63316").getParent().child("Customers").child(data.getCity()).child(data.getDistrict()).child(String.valueOf(data.getId()));
                myRef.removeValue();
                DatabaseReference myRef2=database.getReference("doan-63316").getParent().child(SignInActivity.user).child(data.getCity()).child(data.getDistrict()).child(String.valueOf(data.getId()));
                myRef2.setValue(data);
            }
        });
    }
}
