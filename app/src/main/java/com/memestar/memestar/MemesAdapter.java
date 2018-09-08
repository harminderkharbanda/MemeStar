package com.memestar.memestar;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.memestar.memestar.data.data.model.Meme;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MemesAdapter extends RecyclerView.Adapter<MemesAdapter.MemesViewHolder> {

    private MemesClickListener memesClickListener;
    private List<Meme> memesList;

    public MemesAdapter(MemesClickListener listener) {
        memesClickListener = listener;
    }

    public interface MemesClickListener {
        void onMemeClicked(Meme meme);
    }

    @NonNull
    @Override
    public MemesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.meme_card, viewGroup, false);
        return new MemesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemesViewHolder holder, int position) {
        Meme meme = memesList.get(position);
        Picasso.get().load(meme.getImageUrl()).into(holder.memeImage);
    }

    @Override
    public int getItemCount() {
        if (null == memesList) return 0;
        return memesList.size();
    }

    public class MemesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView memeImage;

        MemesViewHolder(@NonNull View itemView) {
            super(itemView);
            memeImage = itemView.findViewById(R.id.meme_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            memesClickListener.onMemeClicked(memesList.get(getAdapterPosition()));
        }
    }

    public void setMemeData(List<Meme> memeData) {
        memesList = memeData;
    }
}
