package chicxulub.nagaram;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileReader;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();
    private final Activity dis = this;
    private Intent mHomeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        TextView text = (TextView)findViewById(R.id.chosenWord);
        switch (getIntent().getStringExtra("difficulty")) {
            case "easy":
                try {
                    text.setText(getWord("easy"));
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                break;
            case "intermediate":
                try {
                    text.setText(getWord("intermediate"));
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                break;
            case "hard":
                try {
                    text.setText(getWord("hard"));
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                break;
        }
        View view = findViewById(R.id.view);
        setupUI(view);

        this.mHomeIntent = new Intent(this, SplashActivity.class);
        Button quit = (Button)findViewById(R.id.quit);
        quit.setOnClickListener(this);
    }

    public static String getWord(String difficulty) throws XmlPullParserException{
        int fileLength;
        Random rand = new Random();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        switch (difficulty) {
            case "intermediate":
                fileLength = 3378;
                parser.setInput();
                break;
            case "hard":
                fileLength = 213;
                break;
            case "easy":
            default:
                fileLength = 1821;
                break;

        }
        rand.nextInt(fileLength);
        return "shit";
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
        switch(view.getId()){
            case R.id.quit:
                startActivity(this.mHomeIntent);
                break;
            case R.id.submit:

        }
    }

}
