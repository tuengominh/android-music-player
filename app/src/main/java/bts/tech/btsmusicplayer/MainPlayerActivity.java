package bts.tech.btsmusicplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bts.tech.btsmusicplayer.data.SongDBHelper;
import bts.tech.btsmusicplayer.model.Song;
import bts.tech.btsmusicplayer.service.PlayerService;
import bts.tech.btsmusicplayer.view.activity.MapActivity;
import bts.tech.btsmusicplayer.view.activity.NotificationActivity;
import bts.tech.btsmusicplayer.view.adapter.SongListAdapter;

public class MainPlayerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    /** This is the main/host activity that inflates control buttons
     * call PlayerService that handles MediaPlayer
     * call notifications while a song is playing
     * and inflating the list view with all songs */

    //fields to get context & build resource path in Util classes
    public static String PACKAGE_NAME;
    protected static final String TAG = MainPlayerActivity.class.getSimpleName();

    //fields to access song database
    private SongDBHelper songDBHelper = new SongDBHelper(this);
    public static List<Song> songs = new ArrayList();

    //fields of UI items: list view & buttons
    private Button btnPlay;
    private Button btnPause;
    private Button btnStop;
    private Button btnPrev;
    private Button btnNext;
    private Button btnMap;
    private ListView listView;
    public SongListAdapter listAdapter;

    //fields to control the bound service 'PlayerService'
    private PlayerService playerService;
    private boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceInfo) {
            playerService = new PlayerService(serviceInfo);

            //when this activity connects to PlayerService for the 1st time, it will automatically play the 1st song
            //when users navigate to this activity from MapActivity, it will play the current playing song in MapActivity
            int index = getIntent().getIntExtra("index", 0);
            playerService.playByIndex(MainPlayerActivity.this, index);
            callNotification(index);

            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    //onCreate() lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);

        //get context to build resource path in other classes
        PACKAGE_NAME = getApplicationContext().getPackageName();

        //create database and retrieve data
        songDBHelper.addSongData();
        this.songs.addAll(songDBHelper.getAll());

        //setup buttons
        this.btnPlay = findViewById(R.id.activity_main_player__btn__play);
        this.btnPlay.setOnClickListener(this);

        this.btnPause = findViewById(R.id.activity_main_player__btn__pause);
        this.btnPause.setOnClickListener(this);

        this.btnStop = findViewById(R.id.activity_main_player__btn__stop);
        this.btnStop.setOnClickListener(this);

        this.btnPrev = findViewById(R.id.activity_main_player__btn__prev);
        this.btnPrev.setOnClickListener(this);

        this.btnNext = findViewById(R.id.activity_main_player__btn__next);
        this.btnNext.setOnClickListener(this);

        this.btnMap = findViewById(R.id.activity_main_player__btn__map);
        this.btnMap.setOnClickListener(this);

        //setup & get converted view from SongListAdapter
        listAdapter = new SongListAdapter(this,R.layout.song_list_adapter, songs);
        this.listView = findViewById(R.id.activity_main_player__song__list__view);
        listView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.listview_header, null));
        this.listView.setAdapter(listAdapter);

        //register for context menu & set on-item click listener for the list view
        registerForContextMenu(listView);
        this.listView.setOnItemClickListener(this);
    }

    //onStart(): bind PlayerService within a Thread object
    @Override
    protected void onStart() {
        super.onStart();
        final Intent serviceIntent = new Intent(this, PlayerService.class);
        Thread thread = new Thread() {
            @Override
            public void run() {
                bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        };
        thread.start();
    }

    //control click events on buttons
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_player__btn__play:
                this.playerService.play();
                break;
            case R.id.activity_main_player__btn__pause:
                this.playerService.pause();
                break;
            case R.id.activity_main_player__btn__stop:
                this.playerService.stop();
                break;
            case R.id.activity_main_player__btn__prev:
                this.playerService.previous(this);
                callNotification(this.playerService.currentSongIndex);
                break;
            case R.id.activity_main_player__btn__next:
                this.playerService.next(this);
                callNotification(this.playerService.currentSongIndex);
                break;
            case R.id.activity_main_player__btn__map:
                playerService.stop();
                startActivity(new Intent(this, MapActivity.class));
                break;
            default:
                Log.w(TAG, "Not clickable");
        }
    }

    //control click events on list view items
    @Override
    public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
        Log.d(TAG, "Song no." + position + " clicked");
        if (isBound) {
            this.playerService.playByIndex(this, position - 1);
            callNotification(position - 1);
        }
    }

    //inflate & handle context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater contMenuInflater = this.getMenuInflater();
        contMenuInflater.inflate(R.menu.floating_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){

        //get info from the long-clicked listview items
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String currentSongTitle = listAdapter.getItem(info.position).getTitle();

        //response to click events on menu items
        switch (item.getItemId()){
            case R.id.floating_menu__item__play:
                Log.d(TAG, "Playing song no." + info.position);
                if (isBound) {
                    this.playerService.playByIndex(this, info.position - 1);
                    callNotification(info.position - 1);
                }
                break;
            case R.id.floating_menu__item__view__map:
                playerService.stop();

                //send data of current playing song when switching to MapActivity
                Intent intent = new Intent(this, MapActivity.class);
                for (int i = 0; i < songs.size(); i++) {
                    if (songs.get(i).getTitle().equals(currentSongTitle)) {
                        intent.putExtra("title", songs.get(i - 1).getTitle());
                    }
                }
                startActivity(intent);
                break;
            default:
                Log.w(TAG, "Not clickable");
        }
        return true;
    }

    //call customized notification when a song is playing
    public void callNotification(int index) {

        //send data to NotificationActivity
        Intent tapIntent = new Intent(this, NotificationActivity.class);
        tapIntent.putExtra("index", index);
        tapIntent.putExtra("title", songs.get(index).getTitle());
        tapIntent.putExtra("text", songs.get(index).getComment());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 23, tapIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_noti_foreground)
                .setContentTitle("Now Playing")
                .setContentText(songs.get(index).getTitle())
                .setContentIntent(pendingIntent);

        ((NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE))
                .notify(new Random().nextInt(4), builder.build());
    }

    //stop PlayerService if isFinishing()
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            unbindService(serviceConnection);
            isBound = false;
            playerService.stopSelf();
        }
    }
}
