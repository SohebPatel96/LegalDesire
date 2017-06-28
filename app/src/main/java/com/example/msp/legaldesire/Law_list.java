package com.example.msp.legaldesire;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.msp.legaldesire.OnLoginSuccessful.myBundle;

public class Law_list extends Fragment {




    //create listview object
    ListView lawlistview;
    ProgressDialog mdialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_law_list, container, false);



        mdialog=new ProgressDialog(getActivity());
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        //create database reference
        DatabaseReference databaseReference_law= FirebaseDatabase.getInstance().getReferenceFromUrl("https://legal-desire-e5f23.firebaseio.com/online_lib/learn_law/law_name");


        //create reference to lawlistview
        lawlistview=(ListView)view.findViewById(R.id.lawlistview);

        //create FirebaseAdapter
        final FirebaseListAdapter<String> firebaseListAdapter_law=new FirebaseListAdapter<String>(
                getActivity(),
                String.class,
                android.R.layout.activity_list_item,
                databaseReference_law
        ) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView_law=(TextView) v.findViewById(android.R.id.text1);
                textView_law.setTextSize(15);
                textView_law.setText(model);
                //DatabaseReference item=getRef(position);
                //String itemkey=item.getKey();
                //Toast.makeText(getApplicationContext(), itemkey, Toast.LENGTH_LONG).show();
                //mdialog.dismiss();


            }
        };
        lawlistview.setAdapter(firebaseListAdapter_law);

        lawlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_law=(String)lawlistview.getItemAtPosition(i);
                // Intent article=new Intent(getActivity(),Articles.class);
                //article.putExtra("law_name",selected_law);
                //startActivity(article);

                myBundle.putString("law_name",selected_law);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Articles articles = new Articles();
                fragmentTransaction.replace(R.id.fragment_container, articles).commit();

            }


        } );
    }


}
