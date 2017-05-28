package eu.hackabit.community;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String ANONYMOUS = "anonymous";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;
    private Button eventSubmit;
    private EditText eventTitle;
    private EditText eventDescription;
    private TextView newLocation;
    private TextView foundNearbyEvents;
    private ImageView imageView;
    private double latitude;
    private double longitude;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest = createLocationRequest();
    private Location mCurrentLocation;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events_locations");
    GeoFire geoFire = new GeoFire(ref);
    GeoQuery geoQuery;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference("events");
    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this);


    private void updateUI() {
        String loc = String.valueOf(mCurrentLocation.getLatitude()) + ' ' + String.valueOf(mCurrentLocation.getLongitude());
        newLocation.setText(loc);
    }

    private void updateUINearby(String title, String desription) {
        String loc = "title: " + title + " description: "+ desription;
        foundNearbyEvents.setText(loc);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (LocationListener) this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if(geoQuery != null){
            geoQuery.removeAllListeners();
        }
        geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 0.005);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        System.out.println(snapshot.child("title").getValue());
                        System.out.println(snapshot.child("description").getValue());

                        mBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
                                .setContentTitle(String.valueOf(snapshot.child("title").getValue()))
                                .setContentText(String.valueOf(snapshot.child("description").getValue()));
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify((int)(Math.random()), mBuilder.build());
                        updateUINearby(String.valueOf(snapshot.child("title").getValue()), String.valueOf(snapshot.child("description").getValue()));
                    }
                    @Override
                    public void onCancelled(DatabaseError error){

                    }
                });
            }
            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });
        updateUI();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResultd) {
        System.out.println("On connection failed");
        return;
    }

    @Override
    public void onConnected(Bundle connectionHint){
        System.out.println("Connected Ghita");
        System.out.println("///////////////////////////// mafifest"+android.Manifest.permission.ACCESS_FINE_LOCATION + ' ' + PackageManager.PERMISSION_GRANTED);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
                System.out.println("location on connect --->" + latitude+ ' ' + longitude);
            }
            startLocationUpdates();
        }
        System.out.println("exiting");
    }

    @Override
    public void onConnectionSuspended(int connectionHint){
        System.out.println("On connection suspended");
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsername = ANONYMOUS;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

//        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
//        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
//        mLinearLayoutManager = new LinearLayoutManager(this);
//        mLinearLayoutManager.setStackFromEnd(true);
        System.out.println("In create");


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();

        eventSubmit = (Button) findViewById(R.id.submitEvent);
        eventTitle = (EditText) findViewById(R.id.eventTitle);
        eventDescription = (EditText) findViewById(R.id.eventDescription);
        newLocation = (TextView) findViewById(R.id.newLocation);
        foundNearbyEvents = (TextView) findViewById(R.id.foundNearbyEvents);
        imageView = (ImageView) findViewById(R.id.imageView);



        eventSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the data from an ImageView as bytes
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference("imagines");
                UploadTask uploadTask = storageRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        System.out.println("-------failure------");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        System.out.println("Success");
                        System.out.print(downloadUrl);
                        String title = eventTitle.getText().toString();
                        String description = eventDescription.getText().toString();

                        Map<String, String> userMap = new HashMap<String, String>();
                        userMap.put("title", title);
                        userMap.put("description", description);
                        userMap.put("photoUrl", downloadUrl.toString());
                        userMap.put("date", String.valueOf(new Date()));
                        userMap.put("user", mUsername);
                        DatabaseReference newRef = myRef.push();
                        System.out.print("----------test-------------");
                        newRef.setValue(userMap);
                        String eventID = newRef.getKey();
                        System.out.println(eventID);

                        System.out.print("location ----------> " + latitude+ ' ' + longitude);
                        geoFire.setLocation(eventID, new GeoLocation(latitude, longitude));
                    }
                });
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("events");


//        Map<String, String> userMap = new HashMap<String, String>();
//        userMap.put("Ghita", "hello");


//        DatabaseReference newRef = myRef.push();
//        System.out.println(newRef.getKey());
//        newRef.setValue(userMap);
//        String eventID = newRef.getKey();
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events_locations");
//        GeoFire geoFire = new GeoFire(ref);
//        geoFire.setLocation(eventID, new GeoLocation(37.7853889, -122.4056973));
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.sign_out_menu:
//                mFirebaseAuth.signOut();
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
//                mUsername = ANONYMOUS;
//                startActivity(new Intent(this, SignInActivity.class));
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS     ;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        System.out.println("In start");
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        System.out.println("In stop");
        super.onStop();
        mGoogleApiClient.disconnect();
    }
}

