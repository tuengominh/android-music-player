package bts.tech.btsmusicplayer.util;

import com.google.android.gms.maps.model.LatLng;

public class MapUtil {

    /** Util class used to get location information based on song titles */

    public static LatLng getLatLngByTitle(String title) {
        LatLng latLng;
        switch (title) {
            case "bensoundbrazilsamba" :
                latLng = new LatLng(-23.533773, -46.625290); //Brazil - Sao Paulo
                break;
            case "bensoundindia" :
                latLng = new LatLng(28.644800, 77.216721); //India - New Delhi
                break;
            case "bensoundrelaxing" :
                latLng = new LatLng(-8.409518, 115.188919); //Indonesia - Bali
                break;
            case "bensoundpsychedelic" :
                latLng = new LatLng(37.532600, 127.024612); //South Korea - Seoul
                break;
            case "bensoundcountryboy" :
                latLng = new LatLng(31.000000, -100.000000); //USA - Texas
                break;
            case "bensoundlittleplanet" :
                latLng = new LatLng(64.128288, -21.827774); //Iceland - Reykjavik
                break;
            case "bensoundtheelevatorbossanova" :
                latLng = new LatLng(-2.163106, -55.126648); //Brazil - Amazon River
                break;
            default :
                latLng = new LatLng(41.390205, 2.154007); //Barcelona
        }
        return latLng;
    }
}
