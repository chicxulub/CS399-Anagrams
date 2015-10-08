package chicxulub.nagaram;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class FakeWinActivity extends AppCompatActivity implements View.OnClickListener {
    private final Activity dis = this;
    private Intent mMainIntent;
    private Intent mScoreIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_win);
        setupUI(findViewById(R.id.root));

        this.mMainIntent = new Intent(this, SplashActivity.class);
        this.mScoreIntent = new Intent(this, ScoreboardActivity.class);

        Button main = (Button)findViewById(R.id.main);
        Button scores = (Button)findViewById(R.id.scores);

        main.setOnClickListener(this);
        scores.setOnClickListener(this);
    }

    // http://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(dis);
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.main:
                startActivity(this.mMainIntent);
                break;
            case R.id.scores:
                startActivity(this.mScoreIntent);
                break;
        }
    }

}
