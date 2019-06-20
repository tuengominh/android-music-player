package bts.tech.btsmusicplayer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import bts.tech.btsmusicplayer.R;

public class NotificationActivity extends AppCompatActivity {

    /** Inflate titles & comments of playing songs */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //get data selectFrom PlayerService
        Intent intent = getIntent();

        final TextView songTitle = findViewById(R.id.activity_notification__tv__song_title);
        final TextView songComment = findViewById(R.id.activity_notification__tv__song__comment);
        final ImageView songIcon = findViewById(R.id.activity_notification__img__song__icon);

        //send data to layout
        songTitle.setText(intent.getStringExtra("title"));
        songComment.setText(intent.getStringExtra("text"));
        songIcon.setImageResource(R.mipmap.ic_launcher_foreground);
    }
}