package com.example.dodocagephonograph;

public interface Observable {
    void Register(Observer reader);
    void Unregister(Observer reader);
    void Inform();
}
