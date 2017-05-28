package eu.hackabit.community;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final EditText name = (EditText) findViewById(R.id.editText);
        name.setText("Eduard Enache");

        final TextView endorsementsNo = (TextView) findViewById((R.id.textView4));
        endorsementsNo.setText("341 endorsements");

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content, new FragmentEventsWall()).commit();
    }
}