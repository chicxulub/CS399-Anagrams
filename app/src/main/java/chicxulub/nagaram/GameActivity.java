package chicxulub.nagaram;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();
    private final Activity dis = this;
    private Intent mHomeIntent;
    private String word, solution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // fileLength = #pairs/file - 1
        int easyLength = 1820, intermediateLength = 3377, hardLength = 212;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        TextView text = (TextView)findViewById(R.id.chosenWord);
        Random rand = new Random();
        int wordIndex = rand.nextInt(2) + 1;
        int pairIndex;
        switch (getIntent().getStringExtra("level")) {
            case "easy":
                try {
                    pairIndex = rand.nextInt(easyLength) + 1;
                    word = getWord("easy", pairIndex, wordIndex);
                    if (wordIndex == 2)
                        solution = getWord("easy", pairIndex, 1);
                    else
                        solution = getWord("easy", pairIndex, 2);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "intermediate":
                try {
                    pairIndex = rand.nextInt(intermediateLength) + 1;
                    word = getWord("intermediate", pairIndex, wordIndex);
                    if (wordIndex == 2)
                        solution = getWord("intermediate", pairIndex, 1);
                    else
                        solution = getWord("intermediate", pairIndex, 2);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "hard":
                try {
                    pairIndex = rand.nextInt(hardLength) + 1;
                    word = getWord("hard", pairIndex, wordIndex);
                    if (wordIndex == 2)
                        solution = getWord("hard", pairIndex, 1);
                    else
                        solution = getWord("hard", pairIndex, 2);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        text.setText(word);
        View view = findViewById(R.id.view);
        setupUI(view);

        this.mHomeIntent = new Intent(this, SplashActivity.class);
        Button quit = (Button)findViewById(R.id.quit);
        quit.setOnClickListener(this);
    }

    public String getWord(String difficulty, int pairIndex, int wordIndex) throws XmlPullParserException, IOException {
        Random rand = new Random();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        InputStream in_s;
        InputStreamReader in_sr;

        switch (difficulty) {
            case "intermediate":
                in_s = getResources().openRawResource(R.raw.intermediate);
                in_sr = new InputStreamReader(in_s);
                parser.setInput(in_sr);
                break;
            case "hard":
                in_s = getResources().openRawResource(R.raw.hard);
                in_sr = new InputStreamReader(in_s);
                parser.setInput(in_sr);
                break;
            case "easy":
            default:
                in_s = getResources().openRawResource(R.raw.easy);
                in_sr = new InputStreamReader(in_s);
                parser.setInput(in_sr);
                break;
        }
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT && pairIndex != 0){
            if (eventType == XmlPullParser.START_TAG)
                if (parser.getName().equals("pair"))
                    pairIndex--;
            eventType = parser.next();
        }
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT && wordIndex !=0){
            if (parser.getEventType() == XmlPullParser.START_TAG)
                if (parser.getName().equals("word"))
                    wordIndex--;
            parser.next();
        }
        if (parser.getEventType() == XmlPullParser.TEXT)
            return parser.getText();
        if (parser.getEventType() == XmlPullParser.END_DOCUMENT)
            return "Reached end of document";
        return "something went wrong";
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
