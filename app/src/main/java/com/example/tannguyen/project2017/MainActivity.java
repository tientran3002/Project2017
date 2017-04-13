package com.example.tannguyen.project2017;

import android.app.Activity;
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
import android.widget.AbsListView;
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
import java.util.HashMap;

import class_Customer.Customer;
import custom_listview.MyArrayAdapter;
import location.get_Location;
import read_data.get_data;

public class MainActivity extends AppCompatActivity {

    private Button btnSearch;
    private ArrayList<Customer> customerArrayList;
    private String[] array_district, array_city;
    private MyArrayAdapter arrayAdapterCustomer;
    private ListView lvData, lvHistory;
    private AutoCompleteTextView autoeditDistrict, autoeditCity;
    private Boolean check_history = false;
    private ProgressDialog progressDialog;
    private get_data getdata;
    private TextView tv_district;
    private String url="https://doan-72e1b.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        progressDialog.show();
        loadListView();
        getdata=new get_data(MainActivity.this,customerArrayList,arrayAdapterCustomer,progressDialog,"https://doan-72e1b.firebaseio.com/Customers");
        getdata.loadData();
        event();
        //start service
        if(!service_notify.running) {
            Intent myIntent = new Intent(MainActivity.this, service_notify.class);
            // Gọi phương thức startService (Truyền vào đối tượng Intent)
            this.startService(myIntent);}
        //test_datafirebase();
        //test_load_data();
    }

    public void anhXa() {
        btnSearch = (Button) findViewById(R.id.btnSearch);
        lvData = (ListView) findViewById(R.id.lvData);
        customerArrayList = new ArrayList<Customer>();
        tv_district=(TextView)findViewById(R.id.tv_district);
        autoeditCity = (AutoCompleteTextView) findViewById(R.id.autoeditCity);
        autoeditDistrict = (AutoCompleteTextView) findViewById(R.id.autoeditDistrict);
        array_district = new String[]{"Quan 1", "Quan 2", "Quan 3", "Quan 4", "Quan 5", "Quan 6", "Quan 7", "Quan 8", "Quan 9", "Quan 10", "Quan 11", "Quan 12", "Thu Duc", "Binh Thanh", "Go Vap"};
        array_city = new String[]{"Ho Chi Minh", "Ha Noi"};
        lvHistory = (ListView) findViewById(R.id.lvHistory);
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Handling..");
        autoeditDistrict.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.custom_auto,R.id.autoCompleteItem, array_district));
        autoeditCity.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.custom_auto,R.id.autoCompleteItem, array_city));
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
                getdata.search_data(search_district,search_city);
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
                intent.putExtra("button",true);
                startActivity(intent);
                check_history = false;
                progressDialog.cancel();
            }
        });
        lvData.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(get_data.district.containsKey(firstVisibleItem)){
                    tv_district.setText(String.valueOf(get_data.district.get(firstVisibleItem)));
                }
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
                final StorageReference store_image=storage.getReferenceFromUrl("gs://doan-72e1b.appspot.com/hh.jpg");
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
                                if (check_history) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("doan-72e1b").getParent().child(SignInActivity.user+"-history").child(String.valueOf(customerArrayList.get(position).getId()));
                                    myRef.removeValue();
                                    check_history=false;
                                }
                                else {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("doan-72e1b").getParent().child(SignInActivity.user).child(String.valueOf(customerArrayList.get(position).getId()));
                                    myRef.removeValue();
                                    DatabaseReference myRef2 = database.getReference("doan-72e1b").getParent().child(SignInActivity.user + "-history").child(String.valueOf(customerArrayList.get(position).getId()));
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

    public void test_datafirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;

        for (int i = 1; i <= 10; i++) {
            myRef = database.getReference("doan-72e1b").getParent().child("Customers").child(String.valueOf(i));
            Customer customer = new Customer("Tan" + i, "Vo Van Ngan Thu Duc", "Bach Dang", "Binh Thanh", "Ho Chi Minh", (double) (12 + i), 900 + i, null, i);
            myRef.setValue(customer);
        }
        //myRef = database.getReference("doan-72e1b").getParent().child("Customer").child("Ho Chi Minh").child("Quan 9");
        for (int i = 11; i <= 20; i++) {
            myRef = database.getReference("doan-72e1b").getParent().child("Customers").child(String.valueOf(i));
            Customer customer = new Customer("Tan" + i, "Bach Dang Binh Thanh", "Le Van Viet", "Quan 9", "Ho Chi Minh", (double) (12 + i), 900 + i, null, i);
            myRef.setValue(customer);
        }
    }
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
            //loadData();
            String url2=url+"/"+SignInActivity.user;
            getdata=new get_data(MainActivity.this,customerArrayList,arrayAdapterCustomer,progressDialog,url2);
            getdata.loadData();
        }
        if (id == R.id.item_history) {
            check_history = true;
            lvHistory.setVisibility(View.VISIBLE);
            lvData.setVisibility(View.GONE);
            lvHistory.setAdapter(arrayAdapterCustomer);
            String url2=url+"/"+SignInActivity.user+"-history";
            getdata=new get_data(MainActivity.this,customerArrayList,arrayAdapterCustomer,progressDialog,url2);
            getdata.loadData();
            //loadData();
        }
        return super.onOptionsItemSelected(item);
    }
}
