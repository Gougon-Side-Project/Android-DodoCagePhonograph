package com.example.dodocagephonograph;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import android.app.admin.DevicePolicyManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Observer {
    private final String NOT_CONNECTED_MSG = "You are not connected";
    private final String RESPONSE_ANSWER = "&Answer";

    private ImageView _textImageView;
    private ImageView _buttonImageView;

    private MainPM _pm;
    private Observable _news;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _textImageView = findViewById(R.id.waitAnswerText);
        _textImageView.setImageResource(R.drawable.wait_answer_text_animation_list);
        _buttonImageView = findViewById(R.id.buttonAnimation);
        _buttonImageView.setImageResource(R.drawable.btn001);
        AnimationDrawable textAnimationDrawable = (AnimationDrawable) _textImageView.getDrawable();
        textAnimationDrawable.start();

        _pm = new MainPM(this);
        Subscribe(_pm);
        _pm.Connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _pm.Destroy();
    }

    private void Send(String sendMsg) {
        if (!_pm.IsConnected()) {
            Toast.makeText(this, NOT_CONNECTED_MSG, Toast.LENGTH_SHORT).show();
            return;
        }

        _pm.Send(sendMsg);
    }

    public void ClickAnswerButton(View view) {
        int rnd = new Random().nextInt(3);
        _pm.ClickAnswerButton(rnd);
        ChangeBackground(rnd);
    }

    private void ChangeBackground(int rnd) {
        DeleteUI(findViewById(R.id.buttonClickArea));
        _buttonImageView.setImageResource(R.drawable.button_animation_list);
        AnimationDrawable buttonAnimationDrawable = (AnimationDrawable) _buttonImageView.getDrawable();
        buttonAnimationDrawable.start();
        ImageView waitText = findViewById(R.id.waitAnswerText);
        waitText.setImageResource(R.drawable.text000);
        GifImageView background = findViewById(R.id.waitAnswerBackground);
        GifDrawable gifDrawable;
        try {
            switch(rnd) {
                case 0:
                    gifDrawable = new GifDrawable(getResources(), R.drawable.answer1);
                    break;
                case 1:
                    gifDrawable = new GifDrawable(getResources(), R.drawable.answer2);
                    break;
                case 2:
                    gifDrawable = new GifDrawable(getResources(), R.drawable.answer3);
                    break;
                default:
                    throw new RuntimeException("Random number is not belong (0~2)\n");
            }
            background.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void DeleteUI(View view) {
        ConstraintLayout layout = findViewById(R.id.constraintLayout);
        layout.removeView(view);
    }

    @Override
    public void Subscribe(Observable news) {
        _news = news;
        _news.Register(this);
    }

    @Override
    public void Unsubscribe() {
        _news.Unregister(this);
    }

    @Override
    public void Update() {
        Drawable drawable = getDrawable(R.drawable.black);
        _textImageView.setImageDrawable(drawable);
        // DeleteUI(findViewById(R.id.waitAnswerText));
        GifImageView background = findViewById(R.id.waitAnswerBackground);
        background.setImageDrawable(drawable);
        Send(RESPONSE_ANSWER);
    }
}
