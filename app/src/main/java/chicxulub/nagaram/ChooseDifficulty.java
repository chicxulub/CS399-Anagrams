package chicxulub.nagaram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ChooseDifficulty extends AppCompatActivity implements View.OnClickListener {

    private Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);

        this.mIntent = new Intent(this, GameActivity.class);

        Button easy = (Button)findViewById(R.id.easy);
        Button interm = (Button)findViewById(R.id.intermediate);
        Button hard = (Button)findViewById(R.id.hard);

        interm.setOnClickListener(this);
        easy.setOnClickListener(this);
        hard.setOnClickListener(this);

    }

    public void onClick(View v) {
        String difficulty = null;
        switch(v.getId()) {
            case R.id.easy:
                difficulty = "easy";
                break;
            case R.id.intermediate:
                difficulty = "intermediate";
                break;
            case R.id.hard:
                difficulty = "hard";
                break;
        }
        this.mIntent.putExtra("level", difficulty);
        startActivity(this.mIntent);
    }
}
