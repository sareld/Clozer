package com.du.nearby.Clozer;

import android.app.Application;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class ChatApplication extends Application {



    public Socket getSocket(String roomName) {

        Socket mSocket;
        {
            try {
                IO.Options options = new IO.Options();
                options.forceNew=true;
                options.reconnection = false;
                options.query = "room_var="+roomName;
                mSocket = IO.socket(Constants.CHAT_SERVER_URL,options);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return mSocket;
    }
}

