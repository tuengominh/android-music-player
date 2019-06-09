package bts.tech.btsmusicplayer;

import android.os.Bundle;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    //implements NavigationView.OnNavigationItemSelectedListener

    /** This is the main/host activity
     * handling the media player with control buttons
     * inflating the menu, and
     * handling the list view with all songs */

    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();
    }

    @Override
    public void onClick(View v) {

    }
}
