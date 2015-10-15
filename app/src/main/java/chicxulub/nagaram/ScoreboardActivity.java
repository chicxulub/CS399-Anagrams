package chicxulub.nagaram;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

public class ScoreboardActivity extends AppCompatActivity {
    private SavedDataHelper saveData;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        saveData = saveData.getInstance(this);


        List<String> all = saveData.all(this);
        if (all.size() > 0)
            for (int i = 0; i < all.size(); i++)
                System.out.println(all.toArray()[i]);
        else
            System.out.println("Database is empty");
        arrayAdapter = new ArrayAdapter<String>(ScoreboardActivity.this, R.layout.activity_scoreboard,
                R.id.list_item_scoreboard_textview, all);
        System.out.println(arrayAdapter.getCount());
        ListView listView = (ListView)findViewById(R.id.listview_scoreboard);
        Log.d(getClass().getSimpleName(), String.valueOf(listView));
        listView.setAdapter(arrayAdapter);

    }
/*    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        List<String> all = saveData.all(this);
        arrayAdapter = new ArrayAdapter<String>(ScoreboardActivity.this, R.layout.list_item_scoreboard,
                R.id.list_item_scoreboard_textview, all);

        View rootView = inflater.inflate(R.layout.activity_scoreboard, container, false);

        listView.setAdapter(arrayAdapter);
    }
*/

}
