package bts.tech.btsmusicplayer.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import bts.tech.btsmusicplayer.MainPlayerActivity;
import bts.tech.btsmusicplayer.R;
import bts.tech.btsmusicplayer.model.Song;
import bts.tech.btsmusicplayer.view.activity.NotificationActivity;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {

    /** PlayerService is the bound service running MediaPlayer
     * and call notification */

    private static final String TAG = PlayerService.class.getSimpleName();
    private  IBinder iBinder = new Binder();

    //fields to handle MediaPlayer
    private MediaPlayer mediaPlayer;
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
        mediaPlayer = new MediaPlayer();

        for (int id : playList) {
            mediaPlayer = MediaPlayer.create(this, id);
            //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //try {
                //mediaPlayer.setDataSource(this, Uri.parse("android.resource://"
                //        + MainPlayerActivity.PACKAGE_NAME + id));
                //mediaPlayer.setDataSource(getResources().openRawResourceFd(id).getFileDescriptor());
                //mediaPlayer.prepare();
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
            Log.d(TAG, "MediaPlayer for " + id + " created");
        }

        mediaPlayer.setOnPreparedListener(this);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        callNotification();
    }

    //response to click events on buttons
    public void play() {
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        this.mediaPlayer.prepareAsync();
        this.mediaPlayer.start();
        Log.d(TAG, "Playing song with index " + currentSongIndex);
    }

    public void pause() {
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.pause();
            Log.d(TAG, "MediaPlayer paused");
        }
    }

    public void stop() {
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
            Log.d(TAG, "MediaPlayer stopped");
        }
    }

    public void previous() {
        this.currentSongIndex--;
        if(this.currentSongIndex < 0) {
            this.currentSongIndex = this.playList.size() - 1;
        }
        selectSong(this.currentSongIndex);
        this.mediaPlayer.start();
        Log.d(TAG, "Playing song with index " + currentSongIndex);
    }

    public void next() {
        this.currentSongIndex++;
        if(this.currentSongIndex > this.playList.size() - 1) {
            this.currentSongIndex = 0;
        }
        selectSong(this.currentSongIndex);
        this.mediaPlayer.start();
        Log.d(TAG, "Playing song with index " + currentSongIndex);
    }

    //response to click events on list items (songs)
    public void selectSong(int index) {
        this.mediaPlayer.reset();
        try {
            //mediaPlayer.setDataSource(getResources().openRawResourceFd(playList.get(index)).getFileDescriptor());
            //mediaPlayer.setDataSource(this, Uri.parse("android.resource://"
            //        + MainPlayerActivity.PACKAGE_NAME + playList.get(index)));
            this.currentSongIndex = index;
            //this.mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSelectedSong(int index) {
        stop();
        selectSong(index);
        this.mediaPlayer.start();
    }

    //notification when a song is playing
    private void callNotification() {

        //send data to NotificationActivity
        Intent tapIntent = new Intent(this, NotificationActivity.class);
        tapIntent.putExtra("index", this.currentSongIndex);
        tapIntent.putExtra("title", this.songs.get(currentSongIndex).getTitle());
        tapIntent.putExtra("text", this.songs.get(currentSongIndex).getComment());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, tapIntent, PendingIntent.FLAG_CANCEL_CURRENT);

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