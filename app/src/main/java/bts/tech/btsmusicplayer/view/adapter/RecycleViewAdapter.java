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

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    private final Context context;
    private final List<Song> listItems;

    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgThumb;
        private TextView tvTitle;
        private TextView tvBody;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgThumb = itemView.findViewById(R.id.custom_adapter_main__img__thumbnail);
            this.tvTitle = itemView.findViewById(R.id.custom_adapter_main__tv__title);
            this.tvBody = itemView.findViewById(R.id.custom_adapter_main__tv__body);
        }
    }

    public RecycleViewAdapter(Context context, List<Song> objects) {
        this.context = context;
        this.listItems = objects;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_adapter_main, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvBody.setText(listItems.get(position).getBody());
        holder.tvTitle.setText(listItems.get(position).getTitle());
        holder.imgThumb.setImageResource(context.getResources().getIdentifier(listItems.get(position).getImageId(), "drawable", context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


}