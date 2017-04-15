package location;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.tannguyen.project2017.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by TanNguyen on 04/08/2017.
 */

public class get_Location /*implements LocationListener*/ {
    private LocationManager locationManager;
    private Context activity;

    public get_Location(Context activity) {
        this.activity = activity;
    }

    //chosse provider to take location
    public List<String> getProvider() {
        //take service Location
        locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        List<String> locationprovider = locationManager.getAllProviders();
        return locationprovider;
    }


    //take a my location
    //take a my location
    public LatLng myLocation() {//request update location constantly
        //when location change , funtion is called
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }


            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
        LatLng latlngMyLocation;
        if (getProvider().size() != 0) {

            Location location = null;
            List<String> providers = getProvider();
            //check provider to take location
            for (String provider : providers) {
                locationManager.requestLocationUpdates(provider, 1000, 1000, listener);
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return null;
                }
                Location location2 = locationManager.getLastKnownLocation(provider);
                if (location2 != null) {
                    if (location == null && location2 != null) location = location2;
                    else {
                        if (location.getAccuracy() > location2.getAccuracy()) location = location2;
                        //Date time = new Date(location.getTime());
                        // Log.d("sd", time.toString());
                    }
                }

            }
            if (location == null) return null;
            latlngMyLocation = new LatLng(location.getLatitude(), location.getLongitude());
            return latlngMyLocation;
        } else {
            return null;
        }
    }
    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(activity);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null || address.size()==0) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}