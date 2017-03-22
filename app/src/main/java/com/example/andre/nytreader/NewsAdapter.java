package com.example.andre.nytreader;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Andre on 18/03/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final static String NYT_URL = "http://www.nytimes.com/";
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView newsTitle, newsSummary, tvNewsDesk;
        private ImageView ivNewsImage;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            newsTitle = (TextView) itemView.findViewById(R.id.news_title);
            newsSummary = (TextView) itemView.findViewById(R.id.news_summary);
            ivNewsImage = (ImageView) itemView.findViewById(R.id.ivNewsImage);
            tvNewsDesk = (TextView) itemView.findViewById(R.id.news_news_desk);
        }

        public void setData(Doc d) {
            newsTitle.setText(d.headline);
            newsSummary.setText(d.lead_paragraph);
            tvNewsDesk.setText(d.news_desk);
            if (d.image_url != null) {
                Picasso.with(itemView.getContext()).load(NYT_URL + d.image_url).into(ivNewsImage);
            }
        }
    }

    private ArrayList<Doc> docs;
    public NewsAdapter(ArrayList<Doc> docs) {
        this.docs = docs;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       holder.setData(docs.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return docs.size();
    }


}
