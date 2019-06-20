package bts.tech.btsmusicplayer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

import bts.tech.btsmusicplayer.MainPlayerActivity;
import bts.tech.btsmusicplayer.model.Song;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    /** PlayerService is the bound service handling MediaPlayer */

    //fields to get context & handle binder
    private static final String TAG = PlayerService.class.getSimpleName();
    private IBinder iBinder = new Binder();

    //fields to handle MediaPlayer
    private MediaPlayer mp;
    private List<Song> songs = MainPlayerActivity.songs;
    public int currentSongIndex = 0;

    //constructors
    public PlayerService() {
    }

    public PlayerService(IBinder serviceInfo) {
        this.iBinder = serviceInfo;
    }

    //onCreate() lifecycle
    @Override
    public void onCreate() {
        super.onCreate();
    }

    //methods implemented by MediaPlayer's interface
    @Override
    public void onPrepared(MediaPlayer mp) {
        play();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.stop();
        mp.release();
    }

    //response to click events on buttons and manipulates MediaPlayer
    public void play() {
        if (mp == null) return;
        if (!mp.isPlaying()) {
            mp.start();
        }
    }

    public void pause() {
        if (mp == null) return;
        if (mp.isPlaying()) {
            mp.pause();
        }
    }

    public void stop() {
        if (mp == null) return;
        if (mp.isPlaying()) {
            mp.stop();
            mp.reset();
        }
    }

    public void previous(Context ctx) {
        this.currentSongIndex--;
        if (this.currentSongIndex < 0) {
            this.currentSongIndex = (songs.size() - 1);
        }
        playByIndex(ctx, this.currentSongIndex);
        Log.d(TAG, "Playing song with index " + currentSongIndex);
    }

    public void next(Context ctx) {
        this.currentSongIndex++;
        if (this.currentSongIndex > (songs.size() - 1)) {
            this.currentSongIndex = 0;
        }
        playByIndex(ctx, this.currentSongIndex);
        Log.d(TAG, "Playing song with index " + currentSongIndex);
    }

    //response to click events on list view items (songs)
    public void playByIndex(Context ctx, int index) {
        try {
            stop();
            createAndConfigMP(ctx, index);
            play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //create and configure media player for each song item
    private void createAndConfigMP(Context ctx, int index) {
        try {
            /*mp = MediaPlayer.create(ctx, songs.get(index).getResId());*/
            mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(ctx, Uri.parse(songs.get(index).getResPath()));
            mp.setOnPreparedListener(this);
            mp.prepareAsync();
            currentSongIndex = index;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //binding & unbinding methods
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}