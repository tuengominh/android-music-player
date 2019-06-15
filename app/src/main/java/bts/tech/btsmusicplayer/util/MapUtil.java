package bts.tech.btsmusicplayer.util;

import com.google.android.gms.maps.model.LatLng;

public class MapUtil {

    /** Util class used to get location information based on song titles
     * by default it will be Barcelona */

    public static LatLng getLatLng(String title) {
        LatLng latLng;
        switch (title) {
            case "bensoundbrazilsamba" :
                latLng = new LatLng(-23.533773, -46.625290);
                break;
            case "bensoundindia" :
                latLng = new LatLng(28.644800, 77.216721);
                break;
            case "bensoundrelaxing" :
                latLng = new LatLng(-8.409518, 115.188919);
                break;
            case "bensoundpsychedelic" :
                latLng = new LatLng(37.532600, 127.024612);
                break;
            case "bensoundcountryboy" :
                latLng = new LatLng(31.000000, -100.000000);
                break;
            case "bensoundlittleplanet" :
                latLng = new LatLng(64.128288, -21.827774);
                break;
            case "bensoundtheelevatorbossanova" :
                latLng = new LatLng(-2.163106, -55.126648);
                break;
            default :
                latLng = new LatLng(41.390205, 2.154007);
        }
        return latLng;
    }
}
