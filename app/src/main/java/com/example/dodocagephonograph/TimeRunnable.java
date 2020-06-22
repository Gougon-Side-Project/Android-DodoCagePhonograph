package com.example.dodocagephonograph;

public class TimeRunnable implements Runnable, ISubject {
    private IObserver _reader;
    private TimeHandler _handler;
    private int _time;
    private int _targetTime;

    public TimeRunnable(TimeHandler handler, int targetTime) {
        _handler = handler;
        _time = 0;
        _targetTime = targetTime;
    }

    @Override
    public void run() {
        _time++;
        if (_time >= _targetTime)
            Inform();
        else
            _handler.Tick();
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
        if (_reader != null)
            _reader.Update();
    }
}
