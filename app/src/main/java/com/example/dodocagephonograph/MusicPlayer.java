package com.example.dodocagephonograph;

import android.app.Service;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

public class MusicPlayer implements IObserver {
    private MediaPlayer _mediaPlayer = null;

    Vibrator _vibrator = null;

    private ISubject _news = null;

    private Context _context;

    public MusicPlayer(Context context) {
        _context = context;
    }

    private void Vibrating() {
        long[] wave = {0, 400, 800, 1200};
        _vibrator = (Vibrator) _context.getSystemService(Service.VIBRATOR_SERVICE);
        _vibrator.vibrate(VibrationEffect.createWaveform( wave, 0));
    }

    public void Play() {
        _mediaPlayer = null;
        _mediaPlayer = MediaPlayer.create(_context, R.raw.ringbell);
        _mediaPlayer.setLooping(true);
        _mediaPlayer.start();
        Vibrating();
    }

    public void Stop() {
        _mediaPlayer.stop();
        _vibrator.cancel();
    }

    public void Release() {
        _mediaPlayer.release();
    }

    @Override
    public void Subscribe(ISubject news) {
        _news = news;
        _news.Register(this);
    }

    @Override
    public void Unsubscribe() {
        _news.Unregister(this);
    }

    @Override
    public void Update() {
        Play();
    }
}
