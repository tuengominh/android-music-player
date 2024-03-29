package bts.tech.btsmusicplayer.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import bts.tech.btsmusicplayer.R;
import bts.tech.btsmusicplayer.model.Song;
import bts.tech.btsmusicplayer.util.SongUtil;

public class SongListAdapter extends ArrayAdapter<Song> {

    /** Inflate songs info into a list view in MainPlayerActivity */

    private Context context;
    private int layoutRes;
    private List<Song> songs;

    public SongListAdapter(@NonNull Context context, @LayoutRes int layoutRes, @NonNull List<Song> songs) {
        super(context, layoutRes, songs);
        this.context = context;
        this.layoutRes = layoutRes;
        this.songs = songs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertedView = inflater.inflate(layoutRes,null);

        ImageView img = convertedView.findViewById(R.id.song_list_adapter__flag__icon);
        img.setImageResource(SongUtil.getFlagResId(songs.get(position).getCountry()));

        TextView titleTextView = convertedView.findViewById(R.id.song_list_adapter__tv__title);
        titleTextView.setText(songs.get(position).getTitle());

        TextView bodyTextView = convertedView.findViewById(R.id.song_list_adapter__tv__comment);
        bodyTextView.setText(songs.get(position).getComment());

        return convertedView;
    }
}
