package custom_listview;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.tannguyen.project2017.R;

import java.util.ArrayList;

import class_Customer.Customer;

/**
 * Created by TanNguyen on 03/05/2017.
 */

public class MyArrayAdapter extends ArrayAdapter<Customer> {
    Activity activity=null;
    ArrayList<Customer> myArray=null;
    int layoutId;

    public MyArrayAdapter(Activity activity, ArrayList<Customer> myArray, int layoutId) {
        super(activity,layoutId,myArray);
        this.activity = activity;
        this.myArray = myArray;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        convertView=inflater.inflate(layoutId,null);
        if(myArray.size()>=0 && position>=0) {
            final TextView tvName=(TextView)convertView.findViewById(R.id.tvName);
            final TextView tvAddressStart=(TextView)convertView.findViewById(R.id.tvAddressStart);
            final TextView tvAddressDestination=(TextView)convertView.findViewById(R.id.tvAddressDestination);
            final TextView tvweight=(TextView)convertView.findViewById(R.id.tvWeight);
            final TextView tvNumPhone=(TextView)convertView.findViewById(R.id.tvNumPhone);
            tvName.setText(myArray.get(position).getName());
            tvAddressStart.setText(myArray.get(position).getAddressStart());
            tvAddressDestination.setText(myArray.get(position).getAddress()+", "+myArray.get(position).getDistrict()+", "+myArray.get(position).getCity());
            tvNumPhone.setText(String.valueOf(myArray.get(position).getPhoneNumber()));
            tvweight.setText(String.valueOf(myArray.get(position).getWeight()));
        }
        return convertView;
    }
}
