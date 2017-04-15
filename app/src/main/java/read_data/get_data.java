package read_data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.tannguyen.project2017.MainActivity;
import com.example.tannguyen.project2017.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import class_Customer.Customer;
import custom_listview.MyArrayAdapter;

/**
 * Created by TanNguyen on 04/09/2017.
 */

public class get_data {
    private Context context;
    private ArrayList<Customer> customerArrayList;
    private  MyArrayAdapter arrayAdapterCustomer;
    private ProgressDialog progressDialog;
    private String url;
    public static HashMap district;
    private String dis;

    public get_data(Context context, ArrayList<Customer> customerArrayList, MyArrayAdapter arrayAdapterCustomer, ProgressDialog progressDialog, String url) {
        this.context = context;
        this.customerArrayList = customerArrayList;
        this.arrayAdapterCustomer = arrayAdapterCustomer;
        this.progressDialog = progressDialog;
        this.url=url;
        this.dis="";
        this.district=new HashMap();
    }

    //load du lieu tu firebase
    public void loadData() {
        Firebase.setAndroidContext(context);
        Firebase myFireBase = new Firebase(url);
        myFireBase.orderByChild("district").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //a[0] =  dataSnapshot.getValue();
                customerArrayList.clear();
                arrayAdapterCustomer.notifyDataSetChanged();
                int position=0;
                for (DataSnapshot dataS : dataSnapshot.getChildren()) {
                    Customer customer = dataS.getValue(Customer.class);
                    customerArrayList.add(customer);
                    arrayAdapterCustomer.notifyDataSetChanged();
                    if(!customer.getDistrict().equals(dis)) {
                        dis=customer.getDistrict();
                        district.put(position,customer.getDistrict()+", "+customer.getCity());
                    }
                    position++;
                }
                progressDialog.cancel();
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
    public void search_data(final String dis_search, final String city_search) {
        dis="";
        Firebase.setAndroidContext(context);
        Firebase myFireBase = new Firebase(url);
        myFireBase.orderByChild("district").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //a[0] =  dataSnapshot.getValue();
                customerArrayList.clear();
                arrayAdapterCustomer.notifyDataSetChanged();
                for (DataSnapshot dataS : dataSnapshot.getChildren()) {
                    Customer customer = dataS.getValue(Customer.class);
                    if(customer.getDistrict().equals(dis_search) && customer.getCity().equals(city_search)) {
                        dis=customer.getDistrict();
                        customerArrayList.add(customer);
                        arrayAdapterCustomer.notifyDataSetChanged();
                        district.put(0,customer.getDistrict()+", "+customer.getCity());
                    }
                    else if(!dis.equals("")) {
                        break;
                    }
                }
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
