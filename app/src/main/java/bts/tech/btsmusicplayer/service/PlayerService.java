package bts.tech.btsmusicplayer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import bts.tech.btsmusicplayer.util.SongUtil;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    /** PlayerService is the bound service handling MediaPlayer */

    //fields to get context & handle binder
    private static final String TAG = PlayerService.class.getSimpleName();
    private IBinder iBinder = new Binder();

    //fields to handle MediaPlayer
    private MediaPlayer mp;
    public int currentSongIndex = 0;

    //constructors
    public PlayerService() { }
    public PlayerService(IBinder serviceInfo) {
        this.iBinder = serviceInfo;
    }

    //onCreate() lifecycle
    @Override
    public void onCreate() {
        super.onCreate();
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
            this.currentSongIndex = (SongUtil.getSongList().size() - 1);
        }
        playByIndex(ctx, this.currentSongIndex);
        Log.d(TAG, "Playing song with index " + currentSongIndex);
    }

    public void next(Context ctx) {
        this.currentSongIndex++;
        if (this.currentSongIndex > (SongUtil.getSongList().size() - 1)) {
            this.currentSongIndex = 0;
        }
        playByIndex(ctx, this.currentSongIndex);
        Log.d(TAG, "Playing song with index " + currentSongIndex);
    }

    //response to click events on list view items (songs)
    public void playByIndex(Context ctx, int index) {
        try {
            stop();
            mp = new MediaPlayer();
            createAndConfigMP(ctx, index);
            play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //create and configure media player for each song item
    private void createAndConfigMP(Context ctx, int index) {
        try {
            mp = MediaPlayer.create(ctx, SongUtil.getSongList().get(index).getResId());
            Log.d(TAG, mp.toString());
            //mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //mp.setDataSource(this, Uri.parse(SongUtil.getSongList().get(index).getResPath()));
            //mp.prepare();
            mp.setOnPreparedListener(this);
            currentSongIndex = index;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //methods implemented by MediaPlayer's interfaces
    @Override
    public void onPrepared(MediaPlayer mp) {
        play();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.stop();
        mp.release();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
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
}