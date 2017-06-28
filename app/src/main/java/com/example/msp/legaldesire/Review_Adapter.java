package com.example.msp.legaldesire;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MSP on 6/26/2017.
 */

public class Review_Adapter extends ArrayAdapter<String> {
    public static String TAG = "chatlistadapter";
    Context context;
    LayoutInflater inflater;

    ArrayList<String> name;
    ArrayList<String> review;
    ArrayList<Float> rating;

    public Review_Adapter(Context context, ArrayList<String> name,
                          ArrayList<String> review,
                          ArrayList<Float> rating) {
        super(context, R.layout.review_adapter, name);
        this.context = context;
        this.name = name;
        this.review = review;
        this.rating = rating;
    }

    public class ViewHolder {
        TextView mReview, mName;
        RatingBar mRatingBar;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.review_adapter, null);
        }

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.mName = (TextView) convertView.findViewById(R.id.txt_name);
        viewHolder.mReview = (TextView) convertView.findViewById(R.id.txt_review);
        viewHolder.mRatingBar = (RatingBar) convertView.findViewById(R.id.rating);
        viewHolder.mName.setText(name.get(position));
        viewHolder.mReview.setText(review.get(position));
        viewHolder.mRatingBar.setRating(rating.get(position));


        return convertView;
    }
}
