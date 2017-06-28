package com.example.msp.legaldesire;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.msp.legaldesire.OnLoginSuccessful.myBundle;

public class Articles extends Fragment {

    //create listview object
    private ListView articlelistview;


    //create firebase storage reference
    FirebaseStorage storage_articles;

    //create Storagereference object
    StorageReference storageReference_articles;

    ProgressDialog mdialog;

    public int check;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_articles, container, false);


        mdialog=new ProgressDialog(getContext());



        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {



        //Intent intent = getIntent();
        //String law = intent.getStringExtra("law_name");
        String law= (String) myBundle.get("law_name");

        //create listview reference
        articlelistview = (ListView) view.findViewById(R.id.articleslist);

        //create firebase storage reference
        storage_articles = FirebaseStorage.getInstance();

        DatabaseReference databaseReference_articles = FirebaseDatabase.getInstance().getReferenceFromUrl("https://legal-desire-e5f23.firebaseio.com/online_lib/learn_law/law_articles/"+law);

        FirebaseListAdapter<String> firebaseListAdapter_articles = new FirebaseListAdapter<String>(
                getActivity(),
                String.class,
                R.layout.articles_items,
                databaseReference_articles
        ) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) v.findViewById(R.id.text);
                //TextView textView1=(TextView)v.findViewById(android.R.id.text2);
                textView.setTextSize(15);
                textView.setText(model);
                //textView1.setText("Audio");
                final ImageButton audio = (ImageButton) v.findViewById(R.id.audio);
                audio.setTag(position);
                final ImageButton pdf = (ImageButton) v.findViewById(R.id.pdf);
                pdf.setTag(position);






                audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (Integer) view.getTag();
                        final String selected_item = (String) articlelistview.getItemAtPosition(position);

                        String r_name=selected_item.substring(0,selected_item.length()-4);
                        String re_name=r_name+".mp3";

                        check = 0;
                        Toast.makeText(getContext(), "Wait while the audio is loading....", Toast.LENGTH_LONG).show();
                        storageReference_articles = storage_articles.getReferenceFromUrl("gs://legal-desire-e5f23.appspot.com/online_lib/learn_law/audios/").child(re_name);

                        storageReference_articles.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                try {
                                   /* String url = uri.toString(); // your URL here
                                    MediaPlayer mediaPlayer = new MediaPlayer();
                                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    mediaPlayer.setDataSource(url);
                                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                                    mediaPlayer.start();*/


                                   myBundle.putString("law_name_article",uri.toString());
                                    myBundle.putString("audio_name_article", selected_item);
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    Play_audio playAudio = new Play_audio();
                                    fragmentTransaction.replace(R.id.fragment_container, playAudio).commit();


                                    //Intent article=new Intent(getActivity(),Play_audio.class);
                                    //article.putExtra("law_name",uri.toString());
                                    //article.putExtra("audio_name",selected_item);
                                    //startActivity(article);



                                } catch (Exception e) {

                                }


                            }
                        });




                    }
                });

                pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        mdialog.setMessage("loading..");
                        mdialog.show();
                        check = 1;
                        int position = (Integer) view.getTag();
                        final String selected_item = (String) articlelistview.getItemAtPosition(position);
                        storageReference_articles = storage_articles.getReferenceFromUrl("gs://legal-desire-e5f23.appspot.com/").child(selected_item);

                        storageReference_articles.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Intent pdfOnpen = new Intent(Intent.ACTION_VIEW);
                                pdfOnpen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                pdfOnpen.setDataAndType(Uri.parse("http://drive.google.com/viewerng/viewer?embedded=true&url=" + uri.toString()), "*/*");
                                mdialog.dismiss();
                                try {
                                    startActivity(pdfOnpen);
                                } catch (ActivityNotFoundException e) {

                                }


                            }


                        });


                    }
                });



            }



        };
        articlelistview.setAdapter(firebaseListAdapter_articles);


    }



}
