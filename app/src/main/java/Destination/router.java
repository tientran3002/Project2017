package Destination;

/**
 * Created by TanNguyen on 03/04/2017.
 */

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by TanNguyen on 11/27/2016.
 */

public class router {
    private String distance;
    private String duration;
    private String startAddress;
    private LatLng startLatlng;
    private String endAddress;
    private LatLng endLatlng;
    private List<LatLng> listPoint;

    public router() {
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public LatLng getStartLatlng() {
        return startLatlng;
    }

    public void setStartLatlng(LatLng startLatlng) {
        this.startLatlng = startLatlng;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public LatLng getEndLatlng() {
        return endLatlng;
    }

    public void setEndLatlng(LatLng endLatlng) {
        this.endLatlng = endLatlng;
    }

    public List<LatLng> getListPoint() {
        return listPoint;
    }

    public void setListPoint(List<LatLng> listPoint) {
        this.listPoint = listPoint;
    }
}
