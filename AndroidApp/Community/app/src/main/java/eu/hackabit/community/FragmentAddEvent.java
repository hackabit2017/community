package eu.hackabit.community;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.graphics.Color;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentAddEvent.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAddEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddEvent extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView cameraShot;
    private ToggleButton dangerTB;
    private ToggleButton funTB;
    private ToggleButton lostTB;
    private ToggleButton culturalTB;
    private ToggleButton sportTB;
    boolean isPressed = false;


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

        cameraShot = (ImageView) rootView.findViewById(R.id.image_from_camera);

        cameraShot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getView().getContext(), "You clicked on ImageView", Toast.LENGTH_SHORT).show();
                dispatchTakePictureIntent();

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
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
