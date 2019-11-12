package sample;

import java.io.IOException;
import java.net.Socket;
//basic client socket connection
public class Connector {
    Socket socket;

    public Connector() {
        init();
    }

    private void init() {
        try {
            socket = new Socket("args", 8000);
            socket.getOutputStream().write(22);
        } catch (IOException e){
            System.out.println(e.toString());
        }

    }
}
