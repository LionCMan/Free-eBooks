package com.example.android.freeebooks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * List item Recycler Adapter created by Cian Nolan on 30/05/2017.
 */

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> {

    List<BookInformation> mInfo;
    SearchActivity mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView thubmnail;
        protected TextView title;
        protected TextView subtitle;
        protected TextView author;
        protected RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            thubmnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title_text);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle_text);
            author = (TextView) itemView.findViewById(R.id.authors_text);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
        }
    }

    public ItemRecyclerAdapter(SearchActivity context, List<BookInformation> info) {
        this.mInfo = info;
        this.mContext = context;
    }

    @Override
    public ItemRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.booklist_item,
                parent, false);
        ViewHolder vHolder = new ViewHolder(listItem);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(ItemRecyclerAdapter.ViewHolder holder, int position) {
        final BookInformation currentBook = mInfo.get(position);
        holder.thubmnail.setImageResource(currentBook.getThumbnailId());
        holder.title.setText(currentBook.getTitle());
        holder.subtitle.setText(currentBook.getSubtitle());
        holder.author.setText(currentBook.getAuthor());
        holder.ratingBar.setRating((float) currentBook.getRating());
    }

    @Override
    public int getItemCount() {
        return mInfo.size();
    }
}
