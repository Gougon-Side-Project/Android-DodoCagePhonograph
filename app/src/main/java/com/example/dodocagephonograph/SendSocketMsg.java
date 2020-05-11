package com.example.dodocagephonograph;

import android.os.AsyncTask;
import android.os.Handler;

import java.io.BufferedWriter;
import java.io.IOException;

class SendSocketMsg extends AsyncTask<String, Void, Void> {

    private BufferedWriter _bw;

    private Handler _handler;

    public SendSocketMsg(BufferedWriter bw, Handler handler) {
        _bw = bw;
        _handler = handler;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            _bw.write(params[0] + "\n");
            _bw.flush();

            _handler.sendEmptyMessage(Client.SEND_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}