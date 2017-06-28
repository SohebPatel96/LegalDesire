package com.example.msp.legaldesire;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class Send extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "MainActivity";
    protected Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    private Button button, b;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.panic_center, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            //address_text=(TextView)view.findViewById(R.id.textView1);
            button=(Button)view.findViewById(R.id.button2);
            b=(Button)view.findViewById(R.id.button3);

        buildGoogleApiClient();
            b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_police();
            }
        });
        // Create an instance of GoogleAPIClient
        /*if(isOnline()==true){
            access_contacts();
        }*/
    }

    private void call_police() {
        String url = "tel:100";
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
        startActivity(i);
    }


    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())

                .addConnectionCallbacks(this)

                .addOnConnectionFailedListener(this)

                .addApi(LocationServices.API)

                .build();

    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        action_button();
    }

    private void action_button() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                }, 100);
            }
            return;
        }

        if (isOnline() == true) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLastLocation != null) {
                    String address = getCompleteAddressString(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    sendSmsMessage(address);
                } else {

                    if (isGpsEnabled() == false) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                    } else if (isOnline() == false) {
                        Toast.makeText(getActivity(), "Make sure you are connected to the internet", Toast.LENGTH_LONG).show();
                    } else if (isOnline() == true) {
                        action_button();
                    }
                }
            }
        });
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Current loction address", "Cannot get Address!");
        }
        return strAdd;
    }

    protected void sendSmsMessage(final String address) {
            DatabaseReference mDatabase = null;
            if(FirebaseDatabase.getInstance().getReference().child("User").child("Regular")!=null)
                mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child("Regular");
            else if(FirebaseDatabase.getInstance().getReference().child("User").child("Lawyer")!=null)
                mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child("Lawyer");
            final String email = getArguments().getString("Email");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        if (postSnapshot.child("Email").getValue(String.class).contains(email)) {
                            String contact1 = postSnapshot.child("Emergency Contact 1").getValue(String.class);
                            String contact2 = postSnapshot.child("Emergency Contact 2").getValue(String.class);
                            String contact3 = postSnapshot.child("Emergency Contact 3").getValue(String.class);
                            String contact4 = postSnapshot.child("Emergency Contact 4").getValue(String.class);
                            String contact5 = postSnapshot.child("Emergency Contact 5").getValue(String.class);
                            String contact = contact1 + ", " + contact2 + ", " + contact3 + ", " + contact4 + ", " + contact5;
                            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                            smsIntent.setData(Uri.parse("smsto:"));
                            smsIntent.putExtra("address", contact);
                            smsIntent.setType("vnd.android-dir/mms-sms");
                            smsIntent.putExtra("sms_body", "Panic situation in this area...\nPlease help...\nLocation:" + address);
                            try {
                                startActivity(smsIntent);
                                getActivity().finish();
                                Log.i("Finished sending SMS...", "");
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getActivity(),
                                        "SMS failed, please try again later.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean isGpsEnabled() {
        LocationManager locationManager;
        Context context = getActivity();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return GpsStatus;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), "Sorry", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Sorry", Toast.LENGTH_LONG).show();

    }
}
