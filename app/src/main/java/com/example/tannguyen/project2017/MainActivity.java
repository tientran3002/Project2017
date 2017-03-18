package com.example.tannguyen.project2017;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import class_Customer.Customer;
import custom_listview.MyArrayAdapter;

public class MainActivity extends AppCompatActivity {

    private Button btnSearch, btndelete;
    private LinearLayout layoutMap;
    private ArrayList<Customer> customerArrayList;
    private String[] array_district, array_city;
    private MyArrayAdapter arrayAdapterCustomer;
    private ListView lvData, lvHistory;
    private AutoCompleteTextView autoeditDistrict, autoeditCity;
    private Boolean check_history = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        progressDialog.show();
        loadListView();
        loadData("", "", "", false);
        event();
        //test_datafirebase();
        //test_load_data();
    }

    public void anhXa() {
        btnSearch = (Button) findViewById(R.id.btnSearch);
        layoutMap = (LinearLayout) findViewById(R.id.layoutMap);
        lvData = (ListView) findViewById(R.id.lvData);
        customerArrayList = new ArrayList<Customer>();
        autoeditCity = (AutoCompleteTextView) findViewById(R.id.autoeditCity);
        autoeditDistrict = (AutoCompleteTextView) findViewById(R.id.autoeditDistrict);
        array_district = new String[]{"Quan 1", "Quan 2", "Quan 3", "Quan 4", "Quan 5", "Quan 6", "Quan 7", "Quan 8", "Quan 9", "Quan 10", "Quan 11", "Quan 12", "Thu Duc", "Binh Thanh", "Go Vap"};
        array_city = new String[]{"Ho Chi Minh", "Ha Noi"};
        lvHistory = (ListView) findViewById(R.id.lvHistory);
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Handling..");
    }
    //bat cac su kien
    public void event() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                lvHistory.setVisibility(View.GONE);
                lvData.setVisibility(View.VISIBLE);
                String search_district = autoeditDistrict.getText().toString();
                String search_city = autoeditCity.getText().toString();
                loadData(search_district, search_city, "", false);
                check_history = false;
            }
        });
        //chon vao du lieu
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progressDialog.show();
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("select_custormer", customerArrayList.get(position));
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                check_history = false;
                progressDialog.cancel();
            }
        });
        //chon du lieu trong my order
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View customDialogView = li.inflate(R.layout.custom_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setView(customDialogView);

                TextView tvName=(TextView)customDialogView.findViewById(R.id.tvName);
                TextView tvAddressStart=(TextView)customDialogView.findViewById(R.id.tvAddressStart);
                TextView tvAddressDestination=(TextView)customDialogView.findViewById(R.id.tvAddressDestination);
                TextView tvNumPhone=(TextView)customDialogView.findViewById(R.id.tvNumPhone);
                TextView tvWeight=(TextView)customDialogView.findViewById(R.id.tvWeight);
                ImageView img=(ImageView)customDialogView.findViewById(R.id.imageView);
                tvName.setText(customerArrayList.get(position).getName());
                tvWeight.setText(String.valueOf(customerArrayList.get(position).getWeight()));
                tvNumPhone.setText(String.valueOf(customerArrayList.get(position).getPhoneNumber()));
                tvAddressStart.setText(customerArrayList.get(position).getAddressStart());
                tvAddressDestination.setText(customerArrayList.get(position).getAddress()+", "+customerArrayList.get(position).getDistrict()+", "+customerArrayList.get(position).getCity());
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference store_image=storage.getReferenceFromUrl("gs://doan-63316.appspot.com/hh.jpg");
                Glide.with(MainActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(store_image)
                        .into(img);

                alertDialogBuilder.setCancelable(false).setPositiveButton("Show",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                progressDialog.show();
                                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("select_custormer", customerArrayList.get(position));
                                intent.putExtra("bundle", bundle);
                                startActivity(intent);
                                progressDialog.cancel();
                            }
                        })
                        .setNeutralButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("doan-63316").getParent().child(SignInActivity.user + "-history").child(customerArrayList.get(position).getCity()).child(customerArrayList.get(position).getDistrict()).child(String.valueOf(customerArrayList.get(position).getId()));
                                myRef.removeValue();
                                if (!check_history) {
                                    DatabaseReference myRef2 = database.getReference("doan-63316").getParent().child(SignInActivity.user + "-history").child(customerArrayList.get(position).getCity()).child(customerArrayList.get(position).getDistrict()).child(String.valueOf(customerArrayList.get(position).getId()));
                                    myRef2.setValue(customerArrayList.get(position));
                                    check_history=false;
                                }
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }


    public void loadListView() {

        arrayAdapterCustomer = new MyArrayAdapter(MainActivity.this, customerArrayList, R.layout.custom_listview);
        lvData.setAdapter(arrayAdapterCustomer);
    }
    //load du lieu tu firebase
    public void loadData(final String district, final String city, final String order, final Boolean history) {

        autoeditDistrict.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.custom_auto,R.id.autoCompleteItem, array_district));
        autoeditCity.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.custom_auto,R.id.autoCompleteItem, array_city));

        Firebase.setAndroidContext(MainActivity.this);
        Firebase myFireBase = new Firebase("https://doan-63316.firebaseio.com/");
        //load hinh
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        final StorageReference store_image=storage.getReferenceFromUrl("gs://doan-63316.appspot.com/");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("doan-63316").getParent().child("Customers").child("Ho Chi Minh").child("Binh Thanh").child("Customer1");
        myRef.setValue(null);
        myFireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //a[0] =  dataSnapshot.getValue();
                customerArrayList.clear();
                if (!order.equals("")) {
                    for (DataSnapshot dataS : dataSnapshot.child(order).getChildren()) {
                        // listCustomer.add(dataSnapshot.child("Ho Chi Minh").child("Binh Thanh").child("Customer"+i).getValue(Customer.class));
                        for (DataSnapshot datachild : dataSnapshot.child(order).child(dataS.getKey()).getChildren()) {
                            for (DataSnapshot datachild2 : dataSnapshot.child(order).child(dataS.getKey()).child(datachild.getKey()).getChildren()) {
                                Customer customer = datachild2.getValue(Customer.class);
                                //String s=datachild2.getKey();
                                customerArrayList.add(customer);
                                arrayAdapterCustomer.notifyDataSetChanged();

                                //Log.d("sd","1");
                            }
                        }
                    }
                    progressDialog.cancel();
                } else if (history) {
                    for (DataSnapshot dataS : dataSnapshot.child(SignInActivity.user + "-history").getChildren()) {
                        // listCustomer.add(dataSnapshot.child("Ho Chi Minh").child("Binh Thanh").child("Customer"+i).getValue(Customer.class));
                        for (DataSnapshot datachild : dataSnapshot.child(SignInActivity.user + "-history").child(dataS.getKey()).getChildren()) {
                            for (DataSnapshot datachild2 : dataSnapshot.child(SignInActivity.user + "-history").child(dataS.getKey()).child(datachild.getKey()).getChildren()) {
                                Customer customer = datachild2.getValue(Customer.class);
                                //String s=datachild2.getKey();
                                customerArrayList.add(customer);
                                arrayAdapterCustomer.notifyDataSetChanged();

                                //Log.d("sd","1");
                            }
                        }
                    }
                    progressDialog.cancel();
                } else if (district.equals("") || city.equals("")) {
                    for (DataSnapshot dataS : dataSnapshot.child("Customers").getChildren()) {
                        // listCustomer.add(dataSnapshot.child("Ho Chi Minh").child("Binh Thanh").child("Customer"+i).getValue(Customer.class));
                        for (DataSnapshot datachild : dataSnapshot.child("Customers").child(dataS.getKey()).getChildren()) {
                            //String b = datachild.getKey();
//                            for (int i = 1; i <= dataSnapshot.child("Customers").child(dataS.getKey()).child(datachild.getKey()).getChildrenCount(); i++) {
//                                Customer customer=(dataSnapshot.child("Customers").child(dataS.getKey()).child(datachild.getKey()).child("Customer" + i).getValue(Customer.class));
//                                customerArrayList.add(customer);
//                                arrayAdapterCustomer.notifyDataSetChanged();
//
//
//                                //SystemClock.sleep(1000);
//                                //publishProgress(i);
//                            }
                            for (DataSnapshot datachild2 : dataSnapshot.child("Customers").child(dataS.getKey()).child(datachild.getKey()).getChildren()) {
                                Customer customer = datachild2.getValue(Customer.class);
                                //String s=datachild2.getKey();
                                customerArrayList.add(customer);
                                arrayAdapterCustomer.notifyDataSetChanged();
                                //Log.d("sd","1");
                            }

                        }
                    }
                    progressDialog.cancel();
                } else {
                    for (DataSnapshot data : dataSnapshot.child("Customers").child(city).child(district).getChildren()) {

                        Customer customer = data.getValue(Customer.class);
                        customerArrayList.add(customer);
                        arrayAdapterCustomer.notifyDataSetChanged();

                    }
                    progressDialog.cancel();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
//        myFireBase.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.d("add", "add");
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                Log.d("sd", "sad");
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
    }

    public void test_datafirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;

        for (int i = 1; i <= 10; i++) {
            myRef = database.getReference("doan-63316").getParent().child("Customers").child("Ho Chi Minh").child("Binh Thanh").child(String.valueOf(i));
            Customer customer = new Customer("Tan" + i, "Vo Van Ngan Thu Duc", "Bach Dang", "Binh Thanh", "Ho Chi Minh", (double) (12 + i), 900 + i, null, i);
            myRef.setValue(customer);
        }
        //myRef = database.getReference("doan-63316").getParent().child("Customer").child("Ho Chi Minh").child("Quan 9");
        for (int i = 1; i <= 10; i++) {
            myRef = database.getReference("doan-63316").getParent().child("Customers").child("Ho Chi Minh").child("Quan 9").child(String.valueOf(i));
            Customer customer = new Customer("Tan" + i, "Bach Dang Binh Thanh", "Le Van Viet", "Quan 9", "Ho Chi Minh", (double) (12 + i), 900 + i, null, i);
            myRef.setValue(customer);
        }
    }
//    public void test_load_data() {
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef=database.getReference("doan-63316").getParent();
//        myRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//                customerArrayList.clear();
//                for(com.google.firebase.database.DataSnapshot dataS : dataSnapshot.child("Customers").getChildren()) {
//                    // listCustomer.add(dataSnapshot.child("Ho Chi Minh").child("Binh Thanh").child("Customer"+i).getValue(Customer.class));
//                    for(com.google.firebase.database.DataSnapshot datachild : dataSnapshot.child("Customers").child(dataS.getKey()).getChildren()) {
//                        String b=datachild.getKey();
//                        for(int i=0;i< (dataSnapshot.child(dataS.getKey()).child(datachild.getKey())).getChildrenCount();i++) {
//                            customerArrayList.add(dataSnapshot.child("Customers").child(dataS.getKey()).child(datachild.getKey()).child("Customer"+i).getValue(Customer.class));
//                            arrayAdapterCustomer.notifyDataSetChanged();
//                            //SystemClock.sleep(1000);
//                            //publishProgress(i);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        myRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
//            @Override
//            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
//
//                Log.d("sd","sda");
//            }
//
//            @Override
//            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        Log.d("ads","sad");
//    }
    //tao menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_my_order) {
            check_history = false;
            lvHistory.setVisibility(View.VISIBLE);
            lvData.setVisibility(View.GONE);
            lvHistory.setAdapter(arrayAdapterCustomer);
            loadData("", "", SignInActivity.user, false);
            Log.d("check",check_history.toString());
        }
        if (id == R.id.item_history) {
            check_history = true;
            lvHistory.setVisibility(View.VISIBLE);
            lvData.setVisibility(View.GONE);
            lvHistory.setAdapter(arrayAdapterCustomer);
            loadData("", "", "", true);
        }
        return super.onOptionsItemSelected(item);
    }
}
