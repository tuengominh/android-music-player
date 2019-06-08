package bts.tech.btsmusicplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import bts.tech.btsmusicplayer.model.Song;
import bts.tech.btsmusicplayer.util.Utils;
import bts.tech.btsmusicplayer.view.ui.activity.SecondActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
        //implements NavigationView.OnNavigationItemSelectedListener

    private MediaPlayer player;
    private NotificationCompat.Builder builder;
    private NotificationManager notiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);*/

        this.player = MediaPlayer.create(this, R.raw.bensoundindia);
        this.player.start();

        List<Song> myListItems = Utils.getListData();

        final ListView listView = findViewById(R.id.activity_main__list_view__data);
        //listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems));
        View header = LayoutInflater.from(this).inflate(R.layout.listview_header, null);
        listView.addHeaderView(header);
        listView.setAdapter(new MyListAdapter(this, R.layout.custom_adapter_main, myListItems));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Position " + position + " clicked", Toast.LENGTH_SHORT).show();
            }
        });

        final ConstraintLayout constraintLayout = findViewById(R.id.drawer_layout);
        constraintLayout.setOnClickListener(this);


        this.builder = new NotificationCompat.Builder(this, "channelId");
        this.notiManager = (NotificationManager) this.getSystemService(Service.NOTIFICATION_SERVICE);

        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Song name")
                .setContentText("Song description")
                .setAutoCancel(true)
                .setContentIntent(getPendingIntentWithRequestCode(23));
    }

    private PendingIntent getPendingIntentWithRequestCode(int requestCode) {
        Intent notificationIntent = new Intent(this, SecondActivity.class);
        return PendingIntent.getActivity(this, requestCode, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawer_layout:
                Log.i("MainActivity", "background clicked");
                this.notiManager.notify(1, this.builder.build());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stop the payer in case the application is going to finish. Bear in mind that a
        //screen orientation change also calls onDestroy(); however, the track should not
        //stop then. You could, for example, use an “if” clause to check the return value of the
        //isFinishing().
    }

    /*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
}
