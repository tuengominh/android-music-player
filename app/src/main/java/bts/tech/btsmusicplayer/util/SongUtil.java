package bts.tech.btsmusicplayer.util;

import java.util.ArrayList;
import java.util.List;

import bts.tech.btsmusicplayer.R;
import bts.tech.btsmusicplayer.model.Song;

public class SongUtil {
    public static List<Song> getListData() {

        //ArrayList with .mp3 file references
        String[] listIds = {"bensoundbrazilsamba", "bensoundcountryboy", "bensoundindia",
                "bensoundlittleplanet", "bensoundpsychedelic", "bensoundrelaxing",
                "bensoundtheelevatorbossanova"};

        //ArrayList with song titles
        String[] listTitles = {"Brazil Samba", "Country Boy", "India",
                "Little Planet", "Psychedelic", "Relaxing", "The Elevator Bossanova"};

        //ArrayList with countries
        String[] listCountries = {"Brazil", "USA", "India",
                "Iceland", "South Korea", "Indonesia", "Brazil"};

        //ArrayList with songs' duration data
        String[] listDuration = {"04:00", "03:27", "04:13","06:36",
                "03:56", "04:48", "04:14"};

        //ArrayList with song comments
        String[] listComments = {
                "Samba is a Brazilian musical genre and dance style, with its roots in Africa via the West African slave trade and African religious traditions, particularly of Angola",
                "Country music is a genre of American popular music that originated in the Southern United States in the 1920s",
                "The music of India includes multiple varieties of folk music, pop, and Indian classical music. India's classical music tradition, including Hindustani music and Carnatic, has a history spanning millennia and developed over several eras",
                "The music of Iceland includes vibrant folk and pop traditions. Wellknown artists from Iceland include medieval music group Voces Thules, alternative rock band The Sugarcubes, singers Björk and Emiliana Torrini, post-rock band Sigur Rós and indie folk/indie pop band Of Monsters and Men",
                "The Music of South Korea has evolved over the course of the decades since the end of the Korean War, and has its roots in the music of the Korean people, who have inhabited the Korean peninsula for over a millennium. Contemporary South Korean music can be divided into three different main categories: Traditional Korean folk music, popular music, or K-pop, and Western-influenced non-popular music",
                "The music of Indonesia demonstrates its cultural diversity, the local musical creativity, as well as subsequent foreign musical influences that shaped contemporary music scenes of Indonesia. Nearly thousands of Indonesian islands having its own cultural and artistic history and character",
                "The bossa nova is a genre of Brazilian popular music derived from samba and strong influence of jazz"
        };

        List<Song> songs = new ArrayList<>();
        for (int i = 0; i < listIds.length; i++ ) {
            Song song = new Song(listIds[i],
                    R.mipmap.ic_launcher,
                    listTitles[i],
                    listCountries[i],
                    listDuration[i],
                    listComments[i]);
            songs.add(song);
        }
        return songs;
    }
}
