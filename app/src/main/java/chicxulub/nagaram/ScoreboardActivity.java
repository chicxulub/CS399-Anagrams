package chicxulub.nagaram;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

public class ScoreboardActivity extends AppCompatActivity implements View.OnClickListener {
    private SavedDataHelper saveData;
    private Intent homeIntent;
    ScoreAdapter scoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        homeIntent = new Intent(this, SplashActivity.class);

        saveData = saveData.getInstance(this);

        ImageButton goHome = (ImageButton)findViewById(R.id.goHome);
        goHome.setOnClickListener(this);

        List<Score> all = saveData.all(this);
        if (all.size() > 0)
            for (int i = 0; i < all.size(); i++)
                System.out.println(all.toArray()[i]);
        else
            System.out.println("Database is empty");
        scoreAdapter = new ScoreAdapter(ScoreboardActivity.this, all);
        System.out.println(scoreAdapter.getCount());
        ListView listView = (ListView)findViewById(R.id.listview_scoreboard);
        Log.d(getClass().getSimpleName(), String.valueOf(listView));
        listView.setAdapter(scoreAdapter);
        System.out.println("finished");
    }

    @Override
    public void onClick(View v) {
        startActivity(homeIntent);
    }
}
