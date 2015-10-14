package chicxulub.nagaram;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();
    private Intent mHomeIntent;
    private Intent ScoreIntent;
    private String word, solution, difficulty;
    private TextView counter;
    private int count = 0;
    private Handler handler = new Handler();
    private Handler ahandler = new Handler();
    private int RATE = 1000;
    private boolean flag = true;
    private int score;
    private SavedDataHelper saveData;
    private int timeLimit = 5;

    public GameActivity dis = this;
    public ImageView img;
    public TranslateAnimation move;
    public long start;
    public long animStartTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create the helper object to access saved data if not already created
        saveData = saveData.getInstance(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // start frame animation
        ImageView img = (ImageView) findViewById(R.id.walk);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();

        // define the intent for the quit button
        this.mHomeIntent = new Intent(this, SplashActivity.class);
        this.ScoreIntent = new Intent(this, ScoreboardActivity.class);

        // set up our counter
        counter = (TextView) findViewById(R.id.timer);

        this.img = (ImageView) findViewById(R.id.walk);

        tick();
        walk();

        this.start = System.currentTimeMillis();

        // get rid of soft keyboard anywhere you touch
        View view = findViewById(R.id.view);
        setupUI(view);

        // get the view for the chosen word
        TextView text = (TextView) findViewById(R.id.chosenWord);
        // get the difficulty
        difficulty = getIntent().getStringExtra("level");
        // grab a random word and put it in
        generateWords();
        text.setText(word);

        // start time
        //startTime = System.currentTimeMillis();

        // Set the buttons
        Button quit = (Button) findViewById(R.id.quit);
        quit.setOnClickListener(this);
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    private void generateWords() {
        int easyLength = 1820, intermediateLength = 3377, hardLength = 212;
        int wordIndex, pairIndex;
        switch (difficulty) {
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
            word = getWord(pairIndex, wordIndex);
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

    private int getIndex(int max, int mod) {
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
        while (eventType != XmlPullParser.END_DOCUMENT && pairIndex != 0) {
            if (eventType == XmlPullParser.START_TAG)
                if (parser.getName().equals("pair"))
                    pairIndex--;
            eventType = parser.next();
        }
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT && wordIndex != 0) {
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
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && activity != null && activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } else {
            return;
        }

    }

    public void setupUI(View view) {
        final Activity activity = this;
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
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

    boolean checkSolution() {
        EditText userInput = (EditText) findViewById(R.id.editText);
        if (solution.equalsIgnoreCase(userInput.getText().toString())) {
            return true;
        }
        return false;
    }

    public void onClick(View view) {
        TextView text = (TextView) findViewById(R.id.chosenWord);
        TextView feedback = (TextView) findViewById(R.id.feedback);
        switch (view.getId()) {
            case R.id.quit:
                startActivity(this.mHomeIntent);
                break;
            case R.id.submit:
                if (flag) {
                    if (checkSolution()) {
                        score++;
                        feedback.setText("Correct");
                        feedback.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                        view.invalidate();
                    } else {
                        feedback.setText("Failure");
                        feedback.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                        view.invalidate();
                    }

                    // whether you fail or succeed, push the ram back and reset the start times
                    long animStartTime = move.getStartTime();
                    long elapsedTime = System.currentTimeMillis() - this.start;
                    Transformation t = new Transformation();
                    move.getTransformation(animStartTime + elapsedTime, t);
                    float values[] = new float[9];
                    t.getMatrix().getValues(values);
                    float x = values[2];
                    this.img.clearAnimation();

                    this.
                            // push the animation back
                                    pushBack(x);
                    deactivateButtons();
                    this.start = System.currentTimeMillis();
                    ahandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            walk();
                            activateButtons();
                        }
                    }, 2500);

                    // restart start
                    count = 0;
                    generateWords();
                    text.setText(word);
                    ((EditText) findViewById(R.id.editText)).setText("");
                    view.invalidate();
                } else {
                    EditText userInput = (EditText) findViewById(R.id.editText);
                    if ("".equals(userInput.getText().toString()))
                        saveData.truncate();
                    else
                        saveData.insert(userInput.getText().toString(), score);
                    startActivity(this.ScoreIntent);
                }
                break;
        }
    }

    public void deactivateButtons() {
        EditText input = (EditText) findViewById(R.id.editText);
        Button submit = (Button) findViewById(R.id.submit);
        input.setFocusable(false);
        input.setEnabled(false);
        submit.setEnabled(false);
    }

    public void activateButtons() {
        EditText input = (EditText) findViewById(R.id.editText);
        Button submit = (Button) findViewById(R.id.submit);
        input.setFocusable(true);
        input.setFocusableInTouchMode(true);
        input.setEnabled(true);
        submit.setEnabled(true);
    }

    private Runnable counterThread = new Runnable() {
        public void run() {
            tick();
        }
    };

    public void pushBack(float x) {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        TranslateAnimation back = new TranslateAnimation(x, 0, 0, 0);
        back.setDuration(2500);
        back.setFillAfter(true);
        this.img.startAnimation(back);
    }

    public void walk() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int walkWidth = this.img.getBackground().getIntrinsicWidth();

        move = new TranslateAnimation(0, -dm.widthPixels + walkWidth, 0, 0);
        move.setInterpolator(new LinearInterpolator());
        move.setDuration(30000);
        move.setFillAfter(true);
        this.img.startAnimation(move);
    }

    public void tick() {
        TextView feedback = (TextView) findViewById(R.id.feedback);
        EditText input = (EditText) findViewById(R.id.editText);
        Button submit = (Button) findViewById(R.id.submit);

        if (count < 30) {
            Button quit = (Button) findViewById(R.id.quit);
            if (count < timeLimit) {
                count++;
                counter.setText(String.valueOf(count));
                handler.postDelayed(counterThread, RATE);
            } else {
                flag = false;

                hideSoftKeyboard(GameActivity.this);
                input.setText("");
                quit.setVisibility(View.INVISIBLE);
                feedback.setText("Out of Time\nEnter your initials");
            }
        }
    }
}
