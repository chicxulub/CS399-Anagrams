package chicxulub.nagaram;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent aboutIntent;
    private Intent playIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Button about = (Button) findViewById(R.id.action_about);
        final Button play = (Button) findViewById(R.id.play);

        this.playIntent = new Intent(this, ChooseDifficulty.class);
        this.aboutIntent = new Intent(this, AboutNagaram.class);

        about.setOnClickListener(this);
        play.setOnClickListener(this);

        // rock the image
        final ImageView animationTarget = (ImageView) this.findViewById(R.id.imageView);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);

        new Thread(new Runnable() {
            public void run() {
                animationTarget.startAnimation(animation);
            }
        }).start();
    }

    public void onClick(View v) {
        SplashActivity activity = this;
        switch(v.getId()){
            case R.id.play:
                startActivity(activity.playIntent);
                break;
            case R.id.action_about:
                startActivity(activity.aboutIntent);
                break;

        }
    }


}
