package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Julia on 2018-03-10.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(@NonNull Context context, int resource, @NonNull List<Article> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.single_news, parent, false);
        }
        final Article currArticle = getItem(position);

        TextView titleNameTextView = (TextView) listItemView.findViewById(R.id.article_title);
        titleNameTextView.setText(currArticle.getmTitle());

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.article_section);
        sectionTextView.setText(currArticle.getmSection());

        TextView sectionAuthor = (TextView) listItemView.findViewById(R.id.article_author);
        if(currArticle.getmAuthor() != null){
            sectionAuthor.setText(currArticle.getmAuthor());
        }

        TextView sectionDate = (TextView) listItemView.findViewById(R.id.article_time);

        String date = currArticle.getmDate();
        String[] dates;
        dates = date.split("T");
        date = dates[0];
        sectionDate.setText(date);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(currArticle.getmUrl()));
                getContext().startActivity(i);
            }
        });

        return listItemView;
    }


}
