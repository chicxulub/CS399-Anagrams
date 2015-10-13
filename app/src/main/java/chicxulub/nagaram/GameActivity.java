package chicxulub.nagaram;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
    private String word, solution, difficulty;
    private long startTime, endTime, timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        TextView text = (TextView)findViewById(R.id.chosenWord);
        difficulty = getIntent().getStringExtra("level");
        generateWords();
        text.setText(word);
        View view = findViewById(R.id.view);
        setupUI(view);
        startTime = System.currentTimeMillis();

        this.mHomeIntent = new Intent(this, SplashActivity.class);
        Button quit = (Button)findViewById(R.id.quit);
        quit.setOnClickListener(this);
        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    private void generateWords(){
        int easyLength = 1820, intermediateLength = 3377, hardLength = 212;
        int wordIndex, pairIndex;
        switch (difficulty){
            case "intermediate":
                pairIndex = getIndex(intermediateLength, 1);
                break;
            case "hard":
                pairIndex = getIndex(hardLength, 1);
                break;
            case "easy":
            default:
                pairIndex = getIndex(easyLength, 1);
        }
        wordIndex = getIndex(2, 1);

        try {
            word = getWord (pairIndex, wordIndex);
            if (wordIndex == 2)
                solution = getWord(pairIndex, 1);
            else
                solution = getWord(pairIndex, 2);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getIndex(int max, int mod){
        // fileLength = #pairs/file - 1
        int index;
        Random rand = new Random();
        index = rand.nextInt(max) + mod;
        return index;
    }

    private String getWord(int pairIndex, int wordIndex) throws XmlPullParserException, IOException {

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        InputStream in_s;
        InputStreamReader in_sr;
        switch (difficulty) {
            case "intermediate":
                in_s = getResources().openRawResource(R.raw.intermediate);
                break;
            case "hard":
                in_s = getResources().openRawResource(R.raw.hard);
                break;
            case "easy":
            default:
                in_s = getResources().openRawResource(R.raw.easy);
                break;
        }
        in_sr = new InputStreamReader(in_s);
        parser.setInput(in_sr);
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

    boolean checkSolution(){
        EditText userInput = (EditText)findViewById(R.id.editText);
        if (solution.equalsIgnoreCase(userInput.getText().toString())) {
            return true;
        }
        return false;
    }

    public void onClick(View view) {
        TextView text = (TextView)findViewById(R.id.chosenWord);
        switch(view.getId()){
            case R.id.quit:
                startActivity(this.mHomeIntent);
                break;
            case R.id.submit:
                endTime = System.currentTimeMillis();
                timeLeft = 30-(endTime-startTime)/1000;
                if (timeLeft > 0) {
                    if (checkSolution()) {
                        text.setText("Congratulations!");
                        view.invalidate();
                    } else {
                        text.setText("You fail!");
                        view.invalidate();
                    }
                    generateWords();
                    text.setText(word);
                    ((EditText)findViewById(R.id.editText)).setText("");
                    view.invalidate();
                }
                else {
                    text.setText("Out of time");
                    view.invalidate();
                }
                break;
        }
    }
}
