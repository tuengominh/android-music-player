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

    /**
     * PlayerService is the bound service running MediaPlayer
     * and call notification
     */

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

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
    }

    private void createAndConfigMP(Context ctx, int index) {
        try {
            Log.d(TAG, SongUtil.getSongList().get(index).getResPath());
            Log.d(TAG, this.toString());
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

    //response to click events on buttons
    public void play() {
        if (mp == null) return;
        if (!mp.isPlaying()) {
            mp.start();
            Log.d(TAG, "MediaPlayer played");
        }
    }

    public void pause() {
        if (mp == null) return;
        if (mp.isPlaying()) {
            mp.pause();
            Log.d(TAG, "MediaPlayer paused");
        }
    }

    public void stop() {
        if (mp == null) return;
        if (mp.isPlaying()) {
            mp.stop();
            mp.reset();
            Log.d(TAG, "MediaPlayer stopped");
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

    //response to click events on list items (songs)
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

    //binding & unbinding methods
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Service bounded");
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

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
}