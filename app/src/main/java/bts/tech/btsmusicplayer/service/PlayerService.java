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

import java.util.List;
import java.util.Random;

import bts.tech.btsmusicplayer.MainPlayerActivity;
import bts.tech.btsmusicplayer.R;
import bts.tech.btsmusicplayer.model.Song;
import bts.tech.btsmusicplayer.view.activity.NotificationActivity;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    /** PlayerService is the bound service running MediaPlayer
     * and call notification */

    private static final String TAG = PlayerService.class.getSimpleName();
    private  IBinder iBinder = new Binder();

    //fields to handle MediaPlayer
    private List<Song> songs = MainPlayerActivity.getSongs();
    private List<Integer> playList = MainPlayerActivity.getPlayList();
    private MediaPlayer mp = new MediaPlayer();
    private int currentSongIndex;

    //constructors
    public PlayerService() { }
    public PlayerService(IBinder serviceInfo) {
        this.iBinder = serviceInfo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");

        //create media player
        //TODO: use Uri.parse() & prepareAsync()
        mp = MediaPlayer.create(this, playList.get(0));
        mp.setOnPreparedListener(this);
        mp.setOnCompletionListener(this);
        currentSongIndex = 0;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        callNotification();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.stop();
        mp.release();
    }

    //response to click events on buttons
    public void play() {
        if (mp == null) {
            mp = MediaPlayer.create(this, playList.get(0));
            currentSongIndex = 0;
        }
        playByMediaPlayer(mp);
    }

    public void pause() {
        if (mp != null && mp.isPlaying()) {
            mp.pause();
            Log.d(TAG, "MediaPlayer paused");
        }
    }

    public void stop() {
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            Log.d(TAG, "MediaPlayer stopped");
        }
    }

    public void previous() {
        this.currentSongIndex--;
        if(this.currentSongIndex < 0) {
            this.currentSongIndex = this.playList.size() - 1;
        }
        playSongByIndex(this.currentSongIndex);
        Log.d(TAG, "Playing song with index " + currentSongIndex);
        Log.d(TAG, songs.get(currentSongIndex).getTitle());
    }

    public void next() {
        this.currentSongIndex++;
        if(this.currentSongIndex > this.playList.size() - 1) {
            this.currentSongIndex = 0;
        }
        playSongByIndex(this.currentSongIndex);
        Log.d(TAG, "Playing song with index " + currentSongIndex);
        Log.d(TAG, songs.get(currentSongIndex).getTitle());
    }

    //response to click events on list items (songs)
    public void playSongByIndex(int index) {
        if (mp == null) {
            mp = MediaPlayer.create(this, playList.get(index));
            currentSongIndex = index;
        }
        playByMediaPlayer(mp);
    }

    private void playByMediaPlayer(MediaPlayer mp) {
        try {
            mp.setOnPreparedListener(this);
            mp.setOnCompletionListener(this);
            Log.d(TAG, "Playing song with index " + currentSongIndex);
            Log.d(TAG, songs.get(currentSongIndex).getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //notification when a song is playing
    private void callNotification() {

        //send data to NotificationActivity
        Intent tapIntent = new Intent(this, NotificationActivity.class);
        tapIntent.putExtra("index", this.currentSongIndex);
        tapIntent.putExtra("title", this.songs.get(currentSongIndex).getTitle());
        tapIntent.putExtra("text", this.songs.get(currentSongIndex).getComment());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 23, tapIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_noti_foreground)
                .setContentTitle("Now Playing")
                .setContentText(this.songs.get(currentSongIndex).getTitle())
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
}