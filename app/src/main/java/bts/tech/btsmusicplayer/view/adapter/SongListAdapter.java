package bts.tech.btsmusicplayer.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import bts.tech.btsmusicplayer.R;
import bts.tech.btsmusicplayer.model.Song;

public class SongListAdapter extends ArrayAdapter<Song> {

    /** Inflate the song list into a list view */

    private Context context;
    private int layoutResource;
    private List<Song> songs;

    public SongListAdapter(@NonNull Context context, int resource, @NonNull List<Song> songs) {
        super(context, resource, songs);
        this.context = context;
        this.layoutResource = resource;
        this.songs = songs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layoutResource, null);
        ImageView img = convertView.findViewById(R.id.song_list_adapter_main__icon);
        img.setImageResource(context.getResources().getIdentifier(String.valueOf(R.mipmap.ic_launcher_round), "drawable", context.getPackageName()));
        TextView title = convertView.findViewById(R.id.song_list_adapter_main__tv__title);
        title.setText(songs.get(position).getTitle());
        TextView body = convertView.findViewById(R.id.song_list_adapter_main__tv__duration);
        body.setText(songs.get(position).getDuration());

        return convertView;
    }

}