package com.example.dodocagephonograph;

import android.content.Context;
import android.media.MediaPlayer;

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
        // Subscribe(_client);
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
    }

    private void StartRandomPhonograph(int rnd) {
        switch (rnd) {
            case 0:
                _phonographPlayer = MediaPlayer.create(_mainContext, R.raw.record1);
                break;
            case 1:
                _phonographPlayer = MediaPlayer.create(_mainContext, R.raw.record2);
                break;
            case 2:
                _phonographPlayer = MediaPlayer.create(_mainContext, R.raw.record3);
                break;
            default:
                throw new RuntimeException("Random number is not belong (0~2)\n");
        }
        _phonographPlayer.start();
        _state = State.Phonograph;
    }

    private void SetTargetTime(int rnd) {
        switch (rnd) {
            case 0:
                _targetTime = 31;
                break;
            case 1:
                _targetTime = 19;
                break;
            case 2:
                _targetTime = 64;
                break;
            default:
                throw new RuntimeException("Random number is not belong (0~2)\n");
        }
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
        if (_state == State.Phonograph) {
            _phonographPlayer.stop();
        }

        if (_reader != null)
            Inform();

        if (_state == State.Phonograph) {
            Unsubscribe();
            Subscribe(_client);
            _state = State.Exit;
        }
        else if (_state == State.Exit) {
            Unsubscribe();
            _musicPlayer.Subscribe(_client);
            _state = State.Connect;
        }
    }
}
