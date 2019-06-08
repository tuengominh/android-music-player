package bts.tech.btsmusicplayer.view.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bts.tech.btsmusicplayer.R;
import bts.tech.btsmusicplayer.model.Song;
import bts.tech.btsmusicplayer.util.SongUtil;
import bts.tech.btsmusicplayer.view.adapter.SongListAdapter;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        List<Song> myListItems = SongUtil.getListData();

        final RecyclerView recyclerView = findViewById(R.id.activity_second__rv__data);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.HORIZONTAL, false));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.HORIZONTAL));
        recyclerView.setAdapter(new SongListAdapter(this, myListItems));
    }
}