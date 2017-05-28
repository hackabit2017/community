package eu.hackabit.community;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.graphics.Color;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentAddEvent.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAddEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddEvent extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView cameraShot;
    private Bitmap cameraBitmap;
    private ToggleButton dangerTB;
    private ToggleButton funTB;
    private ToggleButton lostTB;
    private ToggleButton culturalTB;
    private ToggleButton sportTB;
    boolean isPressed = false;
    private Context thisContext;


    public static final String ANONYMOUS = "anonymous";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String tagValue;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentAddEvent() {
        // Required empty public constructor
    }

    /**a
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAddEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddEvent newInstance(String param1, String param2) {
        FragmentAddEvent fragment = new FragmentAddEvent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_new_event, container, false);

        mUsername = ANONYMOUS;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        cameraShot = (ImageView) rootView.findViewById(R.id.image_from_camera);

        cameraShot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getView().getContext(), "You clicked on ImageView", Toast.LENGTH_SHORT).show();
                dispatchTakePictureIntent();

            }
        });

        newLocation = (TextView) rootView.findViewById(R.id.eventLocation);
        eventTitle = (EditText) rootView.findViewById(R.id.editEventTitle);
        eventDescription = (EditText) rootView.findViewById(R.id.editEventDescr);

        eventSubmit = (Button) rootView.findViewById(R.id.eventSubmit);

        eventSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the data from an ImageView as bytes
                Bitmap bitmap = cameraBitmap;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference("image_"+ System.currentTimeMillis());
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

                        if(tagValue == null)
                            tagValue = "no tags defined";

                        Map<String, String> userMap = new HashMap<String, String>();
                        userMap.put("title", title);
                        userMap.put("description", description);
                        userMap.put("photoUrl", downloadUrl.toString());
                        userMap.put("date", String.valueOf(new Date()));
                        userMap.put("user", "Cristi Baca");
                        userMap.put("tag", tagValue);
                        userMap.put("rating", String.valueOf((new Random()).nextInt(20)));
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

        dangerTB = (ToggleButton) rootView.findViewById(R.id.dangerTB);
        funTB = (ToggleButton) rootView.findViewById(R.id.funTB);
        lostTB = (ToggleButton) rootView.findViewById(R.id.lostTB);
        culturalTB = (ToggleButton) rootView.findViewById(R.id.culturalTB);
        sportTB = (ToggleButton) rootView.findViewById(R.id.sportTB);

        dangerTB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isPressed == false) {
                    isPressed = true;
                    tagValue = "danger";
                    funTB.setEnabled(false);
                    lostTB.setEnabled(false);
                    culturalTB.setEnabled(false);
                    sportTB.setEnabled(false);
                }
                else
                    if(dangerTB.isEnabled()) {
                        isPressed = false;
                        funTB.setEnabled(true);
                        lostTB.setEnabled(true);
                        culturalTB.setEnabled(true);
                        sportTB.setEnabled(true);
                    }
                customizeButton(dangerTB, "#FF0000");

            }
        });

        funTB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(isPressed == false) {
                    isPressed = true;
                    tagValue = "fun";
                    dangerTB.setEnabled(false);
                    lostTB.setEnabled(false);
                    culturalTB.setEnabled(false);
                    sportTB.setEnabled(false);
                }
                else
                if(funTB.isEnabled()) {
                    isPressed = false;
                    dangerTB.setEnabled(true);
                    lostTB.setEnabled(true);
                    culturalTB.setEnabled(true);
                    sportTB.setEnabled(true);
                }

                customizeButton(funTB,"#58D68D");

            }
        });

        lostTB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(isPressed == false) {
                    isPressed = true;
                    tagValue = "lost";
                    dangerTB.setEnabled(false);
                    funTB.setEnabled(false);
                    culturalTB.setEnabled(false);
                    sportTB.setEnabled(false);
                }
                else
                if(lostTB.isEnabled()) {
                    isPressed = false;
                    dangerTB.setEnabled(true);
                    funTB.setEnabled(true);
                    culturalTB.setEnabled(true);
                    sportTB.setEnabled(true);
                }

                customizeButton(lostTB,"#FF00BF");

            }
        });

        culturalTB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(isPressed == false) {
                    isPressed = true;
                    tagValue = "cultural";
                    dangerTB.setEnabled(false);
                    funTB.setEnabled(false);
                    lostTB.setEnabled(false);
                    sportTB.setEnabled(false);
                }
                else
                if(culturalTB.isEnabled()) {
                    isPressed = false;
                    dangerTB.setEnabled(true);
                    funTB.setEnabled(true);
                    lostTB.setEnabled(true);
                    sportTB.setEnabled(true);
                }

                customizeButton(culturalTB,"#5499C7");

            }
        });

        sportTB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(isPressed == false) {
                    isPressed = true;
                    tagValue = "sport";
                    dangerTB.setEnabled(false);
                    funTB.setEnabled(false);
                    lostTB.setEnabled(false);
                    culturalTB.setEnabled(false);
                }
                else
                if(sportTB.isEnabled()) {
                    isPressed = false;
                    dangerTB.setEnabled(true);
                    funTB.setEnabled(true);
                    lostTB.setEnabled(true);
                    culturalTB.setEnabled(true);
                }

                customizeButton(sportTB,"#FF8000");

            }
        });

        return rootView;

    }

    public void customizeButton(ToggleButton tb, String color){

        if(!tb.isChecked())
            tb.setTextColor(Color.parseColor(color));
        else
            tb.setTextColor(Color.parseColor("#FFFFFF"));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Matrix matrix = new Matrix();

            matrix.postRotate(90);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap,imageBitmap.getWidth(),imageBitmap.getHeight(),true);

            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
            cameraShot.clearColorFilter();
            cameraShot.setImageBitmap(imageBitmap);

            cameraBitmap = imageBitmap;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        System.out.println("In stop");
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
        mGoogleApiClient = null;
    }

    private void updateUI() {
        String loc = String.valueOf(mCurrentLocation.getLatitude()) + ' ' + String.valueOf(mCurrentLocation.getLongitude());
        newLocation.setText(loc);
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

                        String title = String.valueOf(snapshot.child("title").getValue());
                        String description = String.valueOf(snapshot.child("description").getValue());

                        ((EventWall)getActivity()).sendNotification(title, description);
//                        updateUINearby(String.valueOf(snapshot.child("title").getValue()), String.valueOf(snapshot.child("description").getValue()));
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

    private void updateUINearby(String title, String desription) {
        String loc = "title: " + title + " description: "+ desription;
        foundNearbyEvents.setText(loc);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        thisContext = context;

        if (mGoogleApiClient == null) {

            System.out.println("In create");

            mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                    .addConnectionCallbacks(this)
                    .enableAutoManage(this.getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        }

        System.out.println("In start");
        mGoogleApiClient.connect();

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
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (LocationListener) this);
    }

    @Override
    public void onConnectionSuspended(int connectionHint){
        System.out.println("On connection suspended");
        return;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
