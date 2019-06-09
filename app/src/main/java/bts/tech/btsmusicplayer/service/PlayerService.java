package bts.tech.btsmusicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class PlayerService extends Service {

    /** PlayerService is a bind service
     * running MediaPlayer instance in a background thread */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void playSelectedSong(int position) {

    }

    public void play() {
    }

    public void pause() {
    }

    public void stop() {
    }

    public void previous() {
    }

    public void next() {
    }
}