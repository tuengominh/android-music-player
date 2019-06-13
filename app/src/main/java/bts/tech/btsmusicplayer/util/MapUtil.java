package bts.tech.btsmusicplayer.util;

import com.google.android.gms.maps.model.LatLng;

public class MapUtil {

    /** Util class used to get location information of countries
     * by default it will be Barcelona */

    public static LatLng getLatLng(String country) {
        LatLng latLng;
        switch (country) {
            case "Brazil" :
                latLng = new LatLng(-23.533773, -46.625290);
                break;
            case "India" :
                latLng = new LatLng(28.644800, 77.216721);
                break;
            case "Indonesia" :
                latLng = new LatLng(-8.409518, 115.188919);
                break;
            case "South Korea" :
                latLng = new LatLng(37.532600, 127.024612);
                break;
            case "USA" :
                latLng = new LatLng(31.000000, -100.000000);
                break;
            case "Iceland" :
                latLng = new LatLng(64.128288, -21.827774);
                break;
            default :
                latLng = new LatLng(41.390205, 2.154007);
        }
        return latLng;
    }
}
