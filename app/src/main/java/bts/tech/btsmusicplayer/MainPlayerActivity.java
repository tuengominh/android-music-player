package bts.tech.btsmusicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import bts.tech.btsmusicplayer.model.Song;
import bts.tech.btsmusicplayer.service.PlayerService;
import bts.tech.btsmusicplayer.util.SongUtil;
import bts.tech.btsmusicplayer.view.activity.MapActivity;
import bts.tech.btsmusicplayer.view.adapter.SongListAdapter;

public class MainPlayerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    /** This is the main/host activity
     * handling the media player with control buttons
     * inflating the menu, and
     * handling the list view with all songs */

    //fields to get context & build resource path
    public static String PACKAGE_NAME;
    protected static final String TAG = MainPlayerActivity.class.getSimpleName();

    //fields to control lists of songs
    private static List<Integer> playList = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();

    //fields of UI items: list view & buttons
    private Button btnPlay;
    private Button btnPause;
    private Button btnStop;
    private Button btnPrev;
    private Button btnNext;
    private Button btnMap;
    private ListView listView;

    //fields to control the bound service 'PlayerService'
    private PlayerService playerService;
    private boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceInfo) {
            playerService = new PlayerService(serviceInfo);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    //onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);

        //get context to build resource path in other classes
        PACKAGE_NAME = getApplicationContext().getPackageName();

        //setup lists of songs
        for (Song song : SongUtil.getSongList()) {
            songs.add(song);
            playList.add(song.getResId());
        }

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

        this.listView = findViewById(R.id.activity_main_player__song__list__view);
        View listViewHeader = LayoutInflater.from(this).inflate(R.layout.listview_header, null);
        listView.addHeaderView(listViewHeader);
        this.listView.setAdapter(new SongListAdapter(this,R.layout.song_list_adapter, songs));
        this.listView.setOnItemClickListener(this);

        this.btnMap = findViewById(R.id.activity_main_player__btn__map);
        this.btnMap.setOnClickListener(this);
    }

    //bind PlayerService within a Thread object
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

    //control click events on buttons
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_player__btn__play:
                Log.d(TAG,"Played");
                this.playerService.play();
                break;
            case R.id.activity_main_player__btn__pause:
                Log.d(TAG,"Paused");
                this.playerService.pause();
                break;
            case R.id.activity_main_player__btn__stop:
                Log.d(TAG,"Stopped");
                this.playerService.stop();
                break;
            case R.id.activity_main_player__btn__prev:
                Log.d(TAG,"Go back");
                this.playerService.previous();
                break;
            case R.id.activity_main_player__btn__next:
                Log.d(TAG,"Go next");
                this.playerService.next();
                break;
            case R.id.activity_main_player__btn__map:
                Log.d(TAG,"Go to Map");
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
            default:
                Log.w(TAG, "Not clickable");
        }
    }

    //control click events on list items (songs)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "Song no." + position + " clicked");
        if (isBound) {
            this.playerService.selectAndPlaySong(position-1);
        }
    }

    //getters
    public static List<Integer> getPlayList() { return playList; }
    public static List<Song> getSongs() {
        return songs;
    }

    //TODO: Context Menu
    /**
     (long-)click title to see song details
     click view on map to see map
     * @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.media_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
