package com.example.dodocagephonograph;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class SocketHandler extends Handler {
    private final String CONNECT_SUCCESS_MSG = " port connect successful!";
    private final String SEND_SUCCESSFUL = "Message send successful";

    private Context _mainContext;

    public SocketHandler(Context mainContext) {
        _mainContext = mainContext;
    }

    // for debug
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Client.SEND_CODE:
                Toast.makeText(_mainContext, SEND_SUCCESSFUL, Toast.LENGTH_SHORT).show();
                break;
            case Client.RCV_CODE:
                Toast.makeText(_mainContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                break;
            case Client.CONNECT_CODE:
                Toast.makeText(_mainContext, msg.obj + CONNECT_SUCCESS_MSG, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        super.handleMessage(msg);
    }
}
