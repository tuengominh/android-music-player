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

public class PlayerService extends Service {

    /** PlayerService is the bound service running MediaPlayer
     * and call notification */

    private static final String TAG = PlayerService.class.getSimpleName();
    private  IBinder iBinder = new Binder();

    //fields to handle MediaPlayer
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private List<Song> songs = MainPlayerActivity.getSongs();
    private List<Integer> playList = MainPlayerActivity.getPlayList();
    private int currentSongIndex = 0;

    public PlayerService() { }

    public PlayerService(IBinder serviceInfo) {
        this.iBinder = serviceInfo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");

        //create media player
        for (int id : playList) {
            mediaPlayer = MediaPlayer.create(this, id);
        }
        this.mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
            }
        });

        callNotification(currentSongIndex);
    }

    //response to click events on buttons
    public void play() {
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.start();
            callNotification(currentSongIndex);
        }
    }

    public void pause() {
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.pause();
        }
    }

    public void stop() {
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
        }
    }

    public void previous() {
        if(this.currentSongIndex <= 0) {
            playSelectedSong(this.playList.size() - 1);
        } else {
            playSelectedSong(this.currentSongIndex - 1);
        }
        this.mediaPlayer.start();
        this.currentSongIndex--;
        callNotification(currentSongIndex);
    }

    public void next() {
        if(this.currentSongIndex >= this.playList.size() - 1) {
            playSelectedSong(0);
        } else {
            playSelectedSong(this.currentSongIndex + 1);
        }
        this.mediaPlayer.start();
        this.currentSongIndex++;
        callNotification(currentSongIndex);
    }

    //response to click events on list items (songs)
    public void playSelectedSong(long position) {
        stop();
        this.mediaPlayer.reset();
        try {
            int index = (int) position;
            //this.mediaPlayer.setDataSource(this,
            //        Uri.parse(this.songs.get(index).getResPath())
            //);
            this.currentSongIndex = index;
            this.mediaPlayer.start();
            //this.mediaPlayer.prepareAsync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //notification when a song is playing
    private void callNotification(int currentSongIndex) {

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