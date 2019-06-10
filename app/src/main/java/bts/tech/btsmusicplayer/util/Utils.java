package bts.tech.btsmusicplayer.util;

import java.util.ArrayList;
import java.util.List;

import bts.tech.btsmusicplayer.MainPlayerActivity;
import bts.tech.btsmusicplayer.R;
import bts.tech.btsmusicplayer.model.Song;

public class Utils {

    /** Util class used to generate a list of Song objects corresponding to available local 'raw' resources
     * also get flag icons corresponding to songs' countries */

    public static int getFlagResId(String country) {
        int flagResId;
        switch (country) {
            case "Brazil" :
                flagResId = R.mipmap.flag_brazil_foreground;
                break;
            case "India" :
                flagResId = R.mipmap.flag_india_foreground;
                break;
            case "Indonesia" :
                flagResId = R.mipmap.flag_indonesia_foreground;
                break;
            case "South Korea" :
                flagResId = R.mipmap.flag_southkorea_foreground;
                break;
            case "USA" :
                flagResId = R.mipmap.flag_usa_foreground;
                break;
            case "Iceland" :
                flagResId = R.mipmap.flag_iceland_foreground;
                break;
            default :
                flagResId = R.mipmap.ic_launcher_foreground;
                break;
        }
        return flagResId;
    }

    public static List<Song> getSongList() {

        //ArrayList with .mp3 file references
        int[] listResIds = {
                R.raw.bensoundbrazilsamba,
                R.raw.bensoundcountryboy,
                R.raw.bensoundindia,
                R.raw.bensoundlittleplanet,
                R.raw.bensoundpsychedelic,
                R.raw.bensoundrelaxing,
                R.raw.bensoundtheelevatorbossanova
        };

        //ArrayList with song titles
        String[] listTitles = {
                "bensoundbrazilsamba",
                "bensoundcountryboy",
                "bensoundindia",
                "bensoundlittleplanet",
                "bensoundpsychedelic",
                "bensoundrelaxing",
                "bensoundtheelevatorbossanova"
        };

        //ArrayList with countries
        String[] listCountries = {
                "Brazil",
                "USA",
                "India",
                "Iceland",
                "South Korea",
                "Indonesia",
                "Brazil"
        };

        //ArrayList with songs' duration data
        String[] listDuration = {
                "04:00",
                "03:27",
                "04:13",
                "06:36",
                "03:56",
                "04:48",
                "04:14"};

        //ArrayList with song comments
        String[] listComments = {
                "Samba is a Brazilian musical genre and dance style, with its roots in Africa via the West African slave trade and African religious traditions, particularly of Angola",
                "Country music is a genre of American popular music that originated in the Southern United States in the 1920s",
                "The music of India includes multiple varieties of folk music, pop, and Indian classical music. India's classical music tradition, including Hindustani music and Carnatic, has a history spanning millennia and developed over several eras",
                "The music of Iceland includes vibrant folk and pop traditions. Wellknown artists from Iceland include medieval music group Voces Thules, alternative rock band The Sugarcubes, singers Björk and Emiliana Torrini, post-rock band Sigur Rós and indie folk/indie pop band Of Monsters and Men",
                "The Music of South Korea has evolved over the course of the decades since the end of the Korean War, and has its roots in the music of the Korean people, who have inhabited the Korean peninsula for over a millennium. Contemporary South Korean music can be divided into three different main categories: Traditional Korean folk music, popular music, or K-pop, and Western-influenced non-popular music",
                "The music of Indonesia demonstrates its cultural diversity, the local musical creativity, as well as subsequent foreign musical influences that shaped contemporary music scenes of Indonesia. Nearly thousands of Indonesian islands having its own cultural and artistic history and character",
                "Samba is a Brazilian musical genre and dance style, with its roots in Africa via the West African slave trade and African religious traditions, particularly of Angola"
        };

        List<Song> songs = new ArrayList<>();
        for (int i = 0; i < listResIds.length; i++) {
            Song song = new Song(
                    listResIds[i],
                    "android.resource://" + MainPlayerActivity.PACKAGE_NAME + "/raw/" + listResIds[i],
                    "android.resource://" + MainPlayerActivity.PACKAGE_NAME + "/mipmap/" + getFlagResId(listCountries[i]),
                    listTitles[i],
                    listCountries[i],
                    listDuration[i],
                    listComments[i]);
            songs.add(song);
        }
        return songs;
    }
}
