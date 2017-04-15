package com.example.tannguyen.project2017;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.LocaleDisplayNames;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.MainThread;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;


import java.security.spec.ECField;

import class_Customer.Customer;
import location.get_Location;

public class service_notify extends Service {
    private boolean check_toast=false;
    NotificationCompat.Builder notify;
    NotificationManager mNotifyMgr;
    public service_notify() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Firebase.setAndroidContext(getApplicationContext());
        Firebase myFireBase = new Firebase("https://doan-72e1b.firebaseio.com/Customers");
        myFireBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    SharedPreferences pre=getSharedPreferences
                            ("account", MODE_PRIVATE);
                    int maxid=pre.getInt("maxid",0);
                    Log.d("max", String.valueOf(maxid));
                    if (maxid >= dataSnapshot.getValue(Customer.class).getId()) {
                        return;
                    }
                    Log.d("2max", String.valueOf(maxid));
                    //get my location
                    final get_Location get_location = new get_Location(getApplicationContext());
                    LatLng my_location = get_location.myLocation();
                    LatLng from_address = get_location.getLocationFromAddress(dataSnapshot.getValue(Customer.class).getAddressStart());
                    if (check_toast) {
                        return;
                    }
                    float[] results = new float[1];
                    if (my_location == null || from_address == null) {
                        Toast.makeText(getApplicationContext(), "No looking for my location", Toast.LENGTH_LONG).show();
                        check_toast = true;
                        return;
                    } else {
                        //get distance
                        Location.distanceBetween(
                                my_location.latitude,
                                my_location.longitude,
                                from_address.latitude,
                                from_address.longitude,
                                results);
                        if (results[0] < 1000) {
                            notify = new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
                                    .setContentTitle("New Order")
                                    .setAutoCancel(true)
                                    .setContentText(dataSnapshot.getValue(Customer.class).getName());
                            mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            Intent resultIntent = new Intent(getApplicationContext(), MapsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("select_custormer", dataSnapshot.getValue(Customer.class));
                            resultIntent.putExtra("bundle", bundle);
                            resultIntent.putExtra("button", true);

                            PendingIntent resultPendingIntent =
                                    PendingIntent.getActivity(
                                            getApplicationContext(),
                                            0,
                                            resultIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );
                            notify.setContentIntent(resultPendingIntent);
                            mNotifyMgr.notify(Integer.parseInt(dataSnapshot.getKey()), notify.build());
                        }
                        SharedPreferences.Editor editor = pre.edit();
                        editor.putBoolean("running", true);
                        editor.putInt("maxid",dataSnapshot.getValue(Customer.class).getId());
                        editor.commit();
                        check_toast = false;
                    }
                } catch (Exception e){}
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                try{mNotifyMgr.cancel(Integer.parseInt(dataSnapshot.getKey()));}
                catch (Exception e){}
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
