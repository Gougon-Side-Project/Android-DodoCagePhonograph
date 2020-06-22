package com.example.dodocagephonograph;

import android.os.Handler;

public class TimeHandler extends Handler implements ISubject, IObserver {
    private final String NULL_RUNNABLE_EXCEPTION = "Null runnable in TimeHandler\n";

    private IObserver _reader;
    private ISubject _news;

    private final long INTERVAL = 1000;
    private TimeRunnable _runnable;

    public TimeHandler(int targetTime) {
        _runnable = new TimeRunnable(this, targetTime);
        Subscribe(_runnable);
    }

    public void Tick() {
        if (_runnable == null)
            throw new RuntimeException(NULL_RUNNABLE_EXCEPTION);

        postDelayed(_runnable, INTERVAL);
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
        if (_reader != null)
            Inform();
    }
}
