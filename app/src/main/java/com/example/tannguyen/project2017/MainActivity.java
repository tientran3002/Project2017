package com.example.tannguyen.project2017;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import class_Customer.Customer;
import custom_listview.MyArrayAdapter;

public class MainActivity extends AppCompatActivity {

    private Button btnShowMap;
    private LinearLayout layoutMap;
    private ArrayList<Customer> customerArrayList;
    private MyArrayAdapter arrayAdapterCustomer;
    private ListView lvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        loadListView();
        //loadData();
       event();
       // test_datafirebase();
        test_load_data();
    }
    public void  anhXa(){
        btnShowMap=(Button)findViewById(R.id.btnShow);
        layoutMap=(LinearLayout)findViewById(R.id.layoutMap);
        lvData=(ListView)findViewById(R.id.lvData);
        customerArrayList=new ArrayList<Customer>();
    }

    public void event(){
        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("select_custormer",customerArrayList.get(position));
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
    }
    public void loadListView() {

        arrayAdapterCustomer=new MyArrayAdapter(MainActivity.this,customerArrayList,R.layout.custom_listview);
        lvData.setAdapter(arrayAdapterCustomer);
    }
    public void loadData () {
        Firebase.setAndroidContext(MainActivity.this);
        Firebase myFireBase=new Firebase("https://doan-63316.firebaseio.com/");
        myFireBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //a[0] =  dataSnapshot.getValue();
                customerArrayList.clear();
                for(DataSnapshot dataS : dataSnapshot.getChildren()) {
                    // listCustomer.add(dataSnapshot.child("Ho Chi Minh").child("Binh Thanh").child("Customer"+i).getValue(Customer.class));
                    for(DataSnapshot datachild : dataSnapshot.child(dataS.getKey()).getChildren()) {
                        String b=datachild.getKey();
                        for(int i=1;i<=dataSnapshot.child(dataS.getKey()).child(datachild.getKey()).getChildrenCount();i++) {
                            customerArrayList.add(dataSnapshot.child(dataS.getKey()).child(datachild.getKey()).child("Customer"+i).getValue(Customer.class));
                             arrayAdapterCustomer.notifyDataSetChanged();
                            //SystemClock.sleep(1000);
                            //publishProgress(i);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        myFireBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("sd","sad");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void test_datafirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;

        for (int i=0;i<10;i++)
        {   myRef = database.getReference("doan-63316").getParent().child("Ho Chi Minh").child("Binh Thanh").child("Customer"+i);
            Customer customer=new Customer("Tan"+i,"Vo Van Ngan Thu Duc","Bach Dang","Binh Thanh","Ho Chi Minh", (double) (12+i),900+i);
            myRef.setValue(customer);}
        myRef = database.getReference("doan-63316").getParent().child("Ho Chi Minh").child("quan 9");
        for(int i=0;i<10;i++) {
            myRef = database.getReference("doan-63316").getParent().child("Ho Chi Minh").child("quan 9").child("Customer"+i);
            Customer customer=new Customer("Tan"+i,"Bach Dang Binh Thanh","Le Van Viet","quan 9","Ho Chi Minh", (double) (12+i),900+i);
            myRef.setValue(customer);
        }
    }
    public void test_load_data() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("doan-63316").getParent();
        myRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                customerArrayList.clear();
                for(com.google.firebase.database.DataSnapshot dataS : dataSnapshot.getChildren()) {
                    // listCustomer.add(dataSnapshot.child("Ho Chi Minh").child("Binh Thanh").child("Customer"+i).getValue(Customer.class));
                    for(com.google.firebase.database.DataSnapshot datachild : dataSnapshot.child(dataS.getKey()).getChildren()) {
                        String b=datachild.getKey();
                        for(int i=1;i<=dataSnapshot.child(dataS.getKey()).child(datachild.getKey()).getChildrenCount();i++) {
                            customerArrayList.add(dataSnapshot.child(dataS.getKey()).child(datachild.getKey()).child("Customer"+i).getValue(Customer.class));
                            arrayAdapterCustomer.notifyDataSetChanged();
                            //SystemClock.sleep(1000);
                            //publishProgress(i);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("ads","sad");
    }
}
