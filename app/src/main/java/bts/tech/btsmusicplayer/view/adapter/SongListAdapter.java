package bts.tech.btsmusicplayer.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bts.tech.btsmusicplayer.R;
import bts.tech.btsmusicplayer.model.Song;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.MediaPlayerHolder> {

    private final Context context;
    private final List<Song> songList;

    class MediaPlayerHolder extends RecyclerView.ViewHolder{

        private ImageView songIcon;
        private TextView tvTitle;
        private TextView tvDuration;

        public MediaPlayerHolder(@NonNull View itemView) {
            super(itemView);
            this.songIcon = itemView.findViewById(R.id.song_list_adapter_main__icon);
            this.tvTitle = itemView.findViewById(R.id.song_list_adapter_main__tv__title);
            this.tvDuration = itemView.findViewById(R.id.song_list_adapter_main__tv__duration);
        }
    }

    public SongListAdapter(Context context, List<Song> objects) {
        this.context = context;
        this.songList = objects;

    }

    @NonNull
    @Override
    public MediaPlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_list_adapter_main, parent, false);
        return new MediaPlayerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaPlayerHolder holder, int position) {
        holder.tvDuration.setText(songList.get(position).getDuration());
        holder.tvTitle.setText(songList.get(position).getTitle());
        holder.songIcon.setImageResource(context.getResources().getIdentifier(songList.get(position).getSongId(), "drawable", context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
}