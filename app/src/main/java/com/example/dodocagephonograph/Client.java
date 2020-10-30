package com.example.dodocagephonograph;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client extends Thread implements Observable {

    public static final int SEND_CODE = 0;
    public static final int RCV_CODE = 1;
    public static final int CONNECT_CODE = 2;

    private final String ASK_NAME = "%NAME";
    private final String RESPONSE_NAME = "&NAME";

    private final String IP = "172.20.10.12";
    private final Integer PORT = 6321;

    private Socket _socket;
    private BufferedWriter _bw;
    private BufferedReader _br;

    private Handler _handler;

    private boolean _isConnect;

    private Observer _reader;

    public Client(Handler handler) {
        _handler = handler;
        _isConnect = false;
    }

    @Override
    public void run() {
        try {
            _socket = new Socket(IP, PORT);
            Message message = _handler.obtainMessage(CONNECT_CODE, PORT);
            _handler.sendMessage(message);
            _isConnect = true;

            _bw = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
            _br = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            String rcvMsg;
            while(true) {
                rcvMsg = _br.readLine();
                if (rcvMsg != null) {
                    if (rcvMsg.contains(ASK_NAME)) {
                        _bw.write(RESPONSE_NAME + "|dncoffee" + "\n");
                        _bw.flush();
                    }
                    else if (rcvMsg.contains("%Ring")) {
                        Inform();
                    }
                    else {
                        message = _handler.obtainMessage(RCV_CODE, rcvMsg);
                        _handler.sendMessage(message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Close() {
        try {
            _isConnect = false;
            _bw.close();
            _socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean IsConnect() {
        return _isConnect;
    }

    public void Send(String sendMsg) {
        new SendSocketMsg(_bw, _handler).execute(sendMsg);
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
        if (_reader != null)
            _reader.Update();
    }
}
