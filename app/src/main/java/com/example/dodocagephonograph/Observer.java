package com.example.dodocagephonograph;

public interface Observer {
    void Subscribe(Observable news);
    void Unsubscribe();
    void Update();
}
