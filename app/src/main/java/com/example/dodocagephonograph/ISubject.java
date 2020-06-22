package com.example.dodocagephonograph;

public interface ISubject {
    void Register(IObserver reader);
    void Unregister(IObserver reader);
    void Inform();
}
