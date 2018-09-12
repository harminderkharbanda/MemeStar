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
        void onWAClicked(Meme meme);
        void onShareClicked(Meme meme);
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
        private ImageView waImage;
        private ImageView shareImage;

        MemesViewHolder(@NonNull View itemView) {
            super(itemView);
            memeImage = itemView.findViewById(R.id.meme_iv);
            waImage = itemView.findViewById(R.id.wa_icon_iv);
            shareImage = itemView.findViewById(R.id.share_icon_iv);
            memeImage.setOnClickListener(this);
            waImage.setOnClickListener(this);
            shareImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            switch (viewId)
            {
                case R.id.meme_iv:
                    memesClickListener.onMemeClicked(memesList.get(getAdapterPosition()));
                    break;
                case R.id.wa_icon_iv:
                    memesClickListener.onWAClicked(memesList.get(getAdapterPosition()));
                    break;
                case R.id.share_icon_iv:
                    memesClickListener.onShareClicked(memesList.get(getAdapterPosition()));
            }
        }
    }

    public void setMemeData(List<Meme> memeData) {
        memesList = memeData;
    }
}
