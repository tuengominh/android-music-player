package bts.tech.btsmusicplayer.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Random;

import bts.tech.btsmusicplayer.R;
import bts.tech.btsmusicplayer.util.SongUtil;
import bts.tech.btsmusicplayer.view.activity.NotificationActivity;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    /**
     * PlayerService is the bound service running MediaPlayer
     * and call notification
     */

    private static final String TAG = PlayerService.class.getSimpleName();
    private IBinder iBinder = new Binder();

    //fields to handle MediaPlayer
    private MediaPlayer mp;
    private int currentSongIndex = 0;

    //constructors
    public PlayerService() {
    }

    public PlayerService(IBinder serviceInfo) {
        this.iBinder = serviceInfo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
        mp = new MediaPlayer();
        createAndConfigMP(0);
    }

    private void createAndConfigMP(int index) {
        try {
            Log.d(TAG, SongUtil.getSongList().get(index).getResPath());
            Log.d(TAG, this.toString());
            mp = MediaPlayer.create(this, SongUtil.getSongList().get(index).getResId());
            Log.d(TAG, mp.toString());
            //mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //mp.setDataSource(this, Uri.parse(SongUtil.getSongList().get(index).getResPath()));
            //mp.prepare();
            mp.setOnPreparedListener(this);
            currentSongIndex = index;
            callNotification(index);
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

    public void previous() {
        this.currentSongIndex--;
        if (this.currentSongIndex < 0) {
            this.currentSongIndex = (SongUtil.getSongList().size() - 1);
        }
        playByIndex(this.currentSongIndex);
        Log.d(TAG, "Playing song with index " + currentSongIndex);
    }

    public void next() {
        this.currentSongIndex++;
        if (this.currentSongIndex > (SongUtil.getSongList().size() - 1)) {
            this.currentSongIndex = 0;
        }
        playByIndex(this.currentSongIndex);
        Log.d(TAG, "Playing song with index " + currentSongIndex);
    }

    //response to click events on list items (songs)
    public void playByIndex(int index) {
        try {
            stop();
            mp = new MediaPlayer();
            createAndConfigMP(index);
            play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //notification when a song is playing
    private void callNotification(int index) {

        //send data to NotificationActivity
        Intent tapIntent = new Intent(this, NotificationActivity.class);
        tapIntent.putExtra("index", index);
        tapIntent.putExtra("title", SongUtil.getSongList().get(index).getTitle());
        tapIntent.putExtra("text", SongUtil.getSongList().get(index).getComment());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 23, tapIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_noti_foreground)
                .setContentTitle("Now Playing")
                .setContentText(SongUtil.getSongList().get(index).getTitle())
                .setContentIntent(pendingIntent);

        ((NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE))
                .notify(new Random().nextInt(4), builder.build());
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