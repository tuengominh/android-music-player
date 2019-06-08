package bts.tech.btsmusicplayer.util;

import java.util.ArrayList;
import java.util.List;

import bts.tech.btsmusicplayer.model.Song;

public class Utils {
    public static List<Song> getListData() {
        String[] listItems = {"Android", "iPhone", "WindowsMobile","Blackberry",
                "WebOS", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2", "Ubuntu",
                "Windows7", "Max OS X", "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X",
                "Linux","OS/2", "Android", "iPhone", "WindowsMobile"};
        //'ArrayList' with image references (DO NOT INCLUDE FILE EXTENSIONS!)
        String[] listImages = {"androidicon", "iphoneicon", "windowsmobileicon", "blackberryicon",
                "webosicon", "ubuntuicon", "win7icon", "macosxicon", "linuxicon", "os2icon", "ubuntuicon",
                "win7icon", "macosxicon", "linuxicon", "os2icon", "ubuntuicon", "win7icon", "macosxicon",
                "linuxicon", "os2icon", "androidicon", "iphoneicon", "windowsmobileicon"};

        List<Song> songs = new ArrayList<>();
        for (int i = 0; i < listItems.length; i++ ) {
            Song item = new Song(listImages[i], listItems[i], "A body displaying some information about the operating system");
            songs.add(item);
        }
        return songs;
    }
}
