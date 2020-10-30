package com.example.dodocagephonograph;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

public class MainPM implements ISubject, IObserver {
    private Context _mainContext;

    private IObserver _reader;
    private ISubject _news;

    private Client _client;
    private MusicPlayer _musicPlayer;
    private MediaPlayer _phonographPlayer;
    private SocketHandler _socketHandler;
    private TimeHandler _timeHandler;

    private int _targetTime;

    private State _state;

    public MainPM(Context mainContext) {
        _mainContext = mainContext;
        _musicPlayer = new MusicPlayer(_mainContext);
        _socketHandler = new SocketHandler(_mainContext);
        _targetTime = 0;
        _state = State.Ready;
    }

    public State GetState() {
        return _state;
    }

    public void Connect() {
        _client = new Client(_socketHandler);
        _client.start();
        _musicPlayer.Subscribe(_client);
        _state = State.Connect;
    }

    public boolean IsConnected() {
        return _client.IsConnect();
    }

    public void ClickAnswerButton(int rnd) {
        _musicPlayer.Unsubscribe();
        _musicPlayer.Stop();
        StartRandomPhonograph(rnd);
        SetTargetTime(rnd);
        _timeHandler = new TimeHandler(_targetTime);
        Subscribe(_timeHandler);
        _timeHandler.Tick();
        Update();
    }

    private void StartRandomPhonograph(int rnd) {
        _phonographPlayer = MediaPlayer.create(_mainContext, R.raw.mix);
        _phonographPlayer.start();
        Inform();
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
    public void Register(IObserver reader) {
        _reader = reader;
    }

    @Override
    public void Unregister(IObserver reader) {
        _reader = null;
    }

    @Override
    public void Inform() {
        _reader.Update();
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
        if (_state == State.Connect) {
            _state = State.StartPhonograph;
        }
        else if (_state == State.StartPhonograph) {
            _state = State.EndPhonograph;
            _phonographPlayer.stop();
            Unsubscribe();
            Subscribe(_client);
        }
        else if (_state == State.EndPhonograph) {
            _state = State.Exit;
            Unsubscribe();
            _musicPlayer.Subscribe(_client);
            _state = State.Connect;
        }

        if (_reader != null)
            Inform();
    }
}
