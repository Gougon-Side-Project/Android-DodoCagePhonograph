package com.example.dodocagephonograph;

public interface IObserver {
    void Subscribe(ISubject news);
    void Unsubscribe();
    void Update();
}
