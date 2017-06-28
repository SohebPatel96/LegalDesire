package com.example.msp.legaldesire;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by MSP on 6/24/2017.
 */

public class MarkerWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private View view;
    CircleImageView profileImage;
    TextView infoVerify, infoName, infoType;
    private LayoutInflater inflater = null;
    Context context;
    DatabaseReference mDatabase;
    String id, name, profileURL;
    String type1, type2, type3, type4, type5, type6, type7;
    ListView listView;
    Review_Adapter review_adapter;
    RatingBar ratingBar;
    float rating;
    ArrayList<String> review_name = new ArrayList<>();
    ArrayList<Float> review_rating = new ArrayList<>();
    ArrayList<String> review_msg = new ArrayList<>();
    Drawable drawable;


    public MarkerWindowAdapter(Context context) {
        this.context = context;


    }


    @Override
    public View getInfoWindow(final Marker marker) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_marker, null);
        infoName = (TextView) view.findViewById(R.id.text_Name);
        infoType = (TextView) view.findViewById(R.id.text_type);
        profileImage = (CircleImageView) view.findViewById(R.id.profilepic);
        listView = (ListView) view.findViewById(R.id.list_reviews);
        ratingBar = (RatingBar) view.findViewById(R.id.lawyer_rating);
        review_adapter = new Review_Adapter(context, review_name, review_msg, review_rating);
        listView.setAdapter(review_adapter);
        boolean isFirst = false;
        if (type1.equals("Civil")) {
            isFirst = true;
            infoType.setText(type1);
        }
        if (type2.equals("Criminal")) {
            if (isFirst) {
                infoType.setText(infoType.getText() + "," + type2);
            } else {
                isFirst = true;
                infoType.setText(type2);

            }
        }
        if (type3.equals("IPR")) {
            if (isFirst) {
                infoType.setText(infoType.getText() + "," + type3);
            } else {
                isFirst = true;
                infoType.setText(type3);

            }
        }
        if (type4.equals("Taxation")) {
            if (isFirst) {
                infoType.setText(infoType.getText() + "," + type4);
            } else {
                isFirst = true;
                infoType.setText(type4);

            }
        }
        if (type5.equals("Insurance")) {
            if (isFirst) {
                infoType.setText(infoType.getText() + "," + type5);
            } else {
                isFirst = true;
                infoType.setText(type3);

            }
        }
        if (type6.equals("Medical")) {
            if (isFirst) {
                infoType.setText(infoType.getText() + "," + type6);
            } else {
                isFirst = true;
                infoType.setText(type6);

            }
        }
        if (type7.equals("MotorVehicle")) {
            if (isFirst) {
                infoType.setText(infoType.getText() + "," + type7);
            } else {
                //  isFirst = true;
                infoType.setText(type7);

            }
        }

        infoName.setText(name);
        ratingBar.setRating(rating);
        if (drawable != null)
            profileImage.setImageDrawable(drawable);


        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
