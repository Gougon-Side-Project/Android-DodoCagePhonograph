package com.example.dodocagephonograph;

import android.content.Context;
import android.media.MediaPlayer;

public class MainPM implements Observable, Observer {
    private Context _mainContext;

    private Observer _reader;
    private Observable _news;

    private Client _client;
    private MusicPlayer _musicPlayer;
    private MediaPlayer _phonographPlayer;
    private SocketHandler _socketHandler;
    private TimeHandler _timeHandler;

    private int _targetTime;

    public MainPM(Context mainContext) {
        _mainContext = mainContext;
        _musicPlayer = new MusicPlayer(_mainContext);
        _socketHandler = new SocketHandler(_mainContext);
        _targetTime = 0;
    }

    public void Connect() {
        _client = new Client(_socketHandler);
        _client.start();
        _musicPlayer.Subscribe(_client);
    }

    public boolean IsConnected() {
        return _client.IsConnect();
    }

    public void ClickAnswerButton(int rnd) {
        _musicPlayer.Stop();
        StartRandomPhonograph(rnd);
        SetTargetTime(rnd);
        _timeHandler = new TimeHandler(_targetTime);
        Subscribe(_timeHandler);
        _timeHandler.Tick();
    }

    private void StartRandomPhonograph(int rnd) {
        _phonographPlayer = MediaPlayer.create(_mainContext, R.raw.mix);
        _phonographPlayer.start();
    }

    private void SetTargetTime(int rnd) {
        _targetTime = 89;
    }

    public void Send(String sendMsg) {
        _client.Send(sendMsg);
    }

    public void Destroy() {
        if (_client.IsConnect())
            _client.Close();
        _musicPlayer.Release();
        _phonographPlayer.release();
    }

    @Override
    public void Register(Observer reader) {
        _reader = reader;
    }

    @Override
    public void Unregister(Observer reader) {
        _reader = null;
    }

    @Override
    public void Inform() {
        _reader.Update();
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
        _phonographPlayer.stop();
        if (_reader != null)
            Inform();
    }
}
