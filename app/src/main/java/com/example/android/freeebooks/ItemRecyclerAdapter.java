package com.example.android.freeebooks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * List item Recycler Adapter created by Cian Nolan on 30/05/2017.
 */

class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> {

    private List<BookInformation> mInfo;
    private SearchActivity mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView title;
        TextView author;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title_text);
            author = (TextView) itemView.findViewById(R.id.authors_text);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
        }
    }

    ItemRecyclerAdapter(SearchActivity context, List<BookInformation> info) {
        this.mInfo = info;
        this.mContext = context;
    }

    @Override
    public ItemRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.booklist_item,
                parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ItemRecyclerAdapter.ViewHolder holder, int position) {
        final BookInformation currentBook = mInfo.get(position);
        Picasso.with(mContext)
                .load(currentBook.getThumbnailLink())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.thumbnail);
        holder.title.setText(currentBook.getTitle());
        holder.author.setText(currentBook.getAuthor());
        holder.ratingBar.setRating((float) currentBook.getRating());
    }

    @Override
    public int getItemCount() {
        return mInfo.size();
    }
}
