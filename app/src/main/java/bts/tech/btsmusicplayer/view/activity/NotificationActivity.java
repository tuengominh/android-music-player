package bts.tech.btsmusicplayer.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import bts.tech.btsmusicplayer.R;

public class NotificationActivity extends AppCompatActivity {

    /** Manipulates the notification and display comments of playing songs */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }
}