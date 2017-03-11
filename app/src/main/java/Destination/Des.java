package Destination;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import class_Customer.Customer;

/**
 * Created by TanNguyen on 03/04/2017.
 */

public class Des extends AsyncTask<Customer,Void, List<router>> {
    private static final String urlmap = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String keymap = "AIzaSyDfTkWB102fvp3xxIiIdY92Hsl-6QKREHE";
    private GoogleMap mymap;
    private Activity activity;
    private TextView tvDuration,tvDistance;

    public Des(GoogleMap mymap, Activity activity,TextView tvDistance,TextView tvDuration) {
        this.mymap = mymap;
        this.activity = activity;
        this.tvDistance=tvDistance;
        this.tvDuration=tvDuration;
    }

    @Override
    protected List<router> doInBackground(Customer... params) {
        try {
            String url="";
            String urlLocationStart = URLEncoder.encode(params[0].getAddressStart(), "utf-8");
            String urlLocationEnd = URLEncoder.encode((params[0].getAddress()+" "+params[0].getDistrict()+" "+params[0].getCity()), "utf-8");
            url = urlmap + "origin=" + urlLocationStart + "&destination=" + urlLocationEnd + "&mode=" + "driving" + "&key=" + keymap;
            URL link=new URL(url);
            InputStream inputStream = link.openConnection().getInputStream();
            StringBuffer buffer=new StringBuffer();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line="";
            while ((line=bufferedReader.readLine())!=null) {
                buffer.append(line+"\n");
            }
            return handle_json(String.valueOf(buffer));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    //handle json
    private List<router> handle_json(String buffer) throws JSONException {
        if (buffer == null) {
            return null;
        }
        List<router> routerList = new ArrayList<router>();
        //create json from string buffer
        JSONObject jsonData = new JSONObject(buffer);
        //take a node routes
        JSONArray listjsonRout = jsonData.getJSONArray("routes");
        //take a node in node routes
        for (int i = 0; i < listjsonRout.length(); i++) {
            router router = new router();
            JSONObject jsonrout = listjsonRout.getJSONObject(i);
            //take a pokyline
            JSONObject jsonOverView = jsonrout.getJSONObject("overview_polyline");
            //take a point
            JSONArray jsonLegs = jsonrout.getJSONArray("legs");
            //take a first leg
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            //take a distance
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            router.setDistance(jsonDistance.getString("text"));
            //take a duration
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
            router.setDuration(jsonDuration.getString("text"));
            //take a address start
            router.setStartAddress(jsonLeg.getString("start_address"));
            //take a address end
            router.setEndAddress(jsonLeg.getString("end_address"));
            //take a latlng start address
            JSONObject jsonLaglatstart = jsonLeg.getJSONObject("start_location");
            router.setStartLatlng(new LatLng(Double.parseDouble(jsonLaglatstart.getString("lat")), Double.parseDouble(jsonLaglatstart.getString("lng"))));
            //take a latlng end address
            JSONObject jsonLagLatEnd = jsonLeg.getJSONObject("end_location");
            router.setEndLatlng(new LatLng(Double.parseDouble(jsonLagLatEnd.getString("lat")), Double.parseDouble(jsonLagLatEnd.getString("lng"))));
            //take a polylin point
            router.setListPoint(decodePolyLine(jsonOverView.getString("points")));
            routerList.add(router);
        }
        return routerList;
//            Message message=MapsActivity.handler.obtainMessage();
//            message.obj=routerList;
//            message.arg1=1;
//            MapsActivity.handler.sendMessage(message);
    }
    @Override
    protected void onPostExecute(List<router> routerList) {
        super.onPostExecute(routerList);
        //create list maker to show location on path
        List<Marker> makerStart = new ArrayList<Marker>();
        List<Marker> makerDestination = new ArrayList<Marker>();
        //create polylin to draw on map
        List<Polyline> poly = new ArrayList<Polyline>();
        for (int i = 0; i < routerList.size(); i++) {
            //forward start Address
            show_location(routerList.get(i).getStartLatlng());
            //mymap.animateCamera(CameraUpdateFactory.newLatLngZoom(routerList.get(i).getStartLatlng(), 10));
            //show distance and duration
            tvDistance.setText(routerList.get(i).getDistance());
            tvDuration.setText(routerList.get(i).getDuration());
            makerStart.add(create_marker(routerList.get(i).getStartLatlng()));
            //create end marker
            makerDestination.add(create_marker(routerList.get(i).getEndLatlng()));
            //create polyline
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.geodesic(true);
            polylineOptions.color(Color.RED);
            polylineOptions.width(10);
            for (int j = 0; j < routerList.get(i).getListPoint().size(); j++) {
                polylineOptions.add(routerList.get(i).getListPoint().get(j));
            }
            poly.add(mymap.addPolyline(polylineOptions));
        }
    }
    private List<LatLng> decodePolyLine(String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
    public void show_location(LatLng latLng) {
        if (latLng == null) {
            Toast.makeText(activity, "don't look location", Toast.LENGTH_LONG).show();
            return;
        }
        //set attibute for location
        CameraPosition position = new CameraPosition.Builder()
                .target(latLng)
                //dinh huong cho camera
                .bearing(0)
                //set goc nhin
                .tilt(40)
                //zoom
                .zoom(15)
                .build();

        //forward to location on map
        mymap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        //set maker for location
        // create_marker(latLng);
    }
    public Marker create_marker(LatLng latLng) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);
        marker.title("My Location");
        Marker marker1 = mymap.addMarker(marker);
        //set custom adapter
        //mymap.setInfoWindowAdapter(new Mymaker(activity));
        // marker1.showInfoWindow();
        return marker1;
    }
}
