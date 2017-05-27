package eu.hackabit.community;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final EditText name = (EditText) findViewById(R.id.editText);
        name.setText("Eduard Enache");

        final TextView endorsementsNo = (TextView) findViewById((R.id.textView4));
        endorsementsNo.setText("341 endorsements");

        final ListView listview = (ListView) findViewById(R.id.listView);

        final ArrayList<EventItem> list = new ArrayList<EventItem>();
        for (int i = 0; i < 10; ++i) {
            list.add(new EventItem("title " + i, "description " + i));
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }
}

class EventItem{
    private String title;
    private String description;

    public EventItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String toString() {
        return this.title + " " + this.description;
    }
}

class StableArrayAdapter extends ArrayAdapter<EventItem> {

    HashMap<EventItem, Integer> mIdMap = new HashMap<EventItem, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId,
                              List<EventItem> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        EventItem item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}