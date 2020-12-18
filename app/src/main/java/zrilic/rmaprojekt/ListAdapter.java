package zrilic.rmaprojekt;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Red> {

    private List<Song> songs;
    private LayoutInflater layoutInflater;
    private ItemClickInterface itemClickInterface;
    private Context context;

    public ListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public Red onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.red, parent, false);
        return new Red(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Red holder, int position) {
        Song song = songs.get(position);
        Glide.with(context).load(song.getStrTrackThumb()).into(holder.trackThumb);
        holder.trackId.setText(String.valueOf(song.getId()));
        holder.trackName.setText(song.getStrTrack());
        holder.artistName.setText(song.getStrArtist()+" • "+song.getStrGenre());
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }

    public class Red extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        private TextView trackId;
        private ImageView trackThumb;
        private TextView trackName;
        private TextView artistName;

        public Red(@NonNull View itemView) {
            super(itemView);
            trackId = itemView.findViewById(R.id.trackId);
            trackThumb = itemView.findViewById(R.id.trackThumb);
            trackName = itemView.findViewById(R.id.trackName);
            artistName = itemView.findViewById(R.id.artistName);
            itemView.setOnClickListener(this);
            itemView.setOnTouchListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickInterface == null) {
                return;
            }
            itemClickInterface.onItemClick(songs.get(getAdapterPosition()));
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
            }
            return false;
        }
    }

    public void setItemClickInterface(ItemClickInterface itemClickInterface) {
        this.itemClickInterface = itemClickInterface;
    }

}
