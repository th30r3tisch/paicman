package game.controller;

import game.model.Message;
import game.model.Player;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static game.model.MessageType.CONNECTED;

// is responsible for handling all the connections
public class ConnectionController implements Runnable{
    private String serverAddress;
    private Socket socket;
    public int port;
    private static ObjectOutputStream oos;
    private InputStream inputStream;
    private ObjectInputStream ois;
    private OutputStream outputStream;
    private static Logger LOGGER = Logger.getLogger("InfoLogging");
    private static Player player;
    private boolean isConnected;

    public ConnectionController(Player player, String serverAddress) {
        this.serverAddress = serverAddress;
        this.port = 59001;
        ConnectionController.player = player;
        isConnected = false;
    }

    public void run() {
        try {
            socket = new Socket(serverAddress, port);
            //LoginController.getInstance().showScene();
            outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            inputStream = socket.getInputStream();
            ois = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not Connect", e);
        }
        LOGGER.log(Level.INFO, "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());

        try {
            login();
            LOGGER.log(Level.INFO,"Sockets in and out ready!");
            while (socket.isConnected()) {
                Message message = (Message) ois.readObject();

                if (message != null) {
                    LOGGER.log(Level.INFO, " Message received: " + message.getNote() + " MessageType: " + message.getType() + " Name: " + message.getPlayer());
                    switch (message.getType()) {
                        case PLAYER:
                            LOGGER.log(Level.INFO,"Player msg");
                            break;
                        case NOTIFICATION:
                            LOGGER.log(Level.INFO,"Notification msg");
                            break;
                        case SERVER:
                            LOGGER.log(Level.INFO,"Server msg");
                            break;
                        case CONNECTED:
                            isConnected = true;
                            LOGGER.log(Level.INFO,"Connected msg");
                            break;
                        case DISCONNECTED:
                            isConnected = false;
                            LOGGER.log(Level.INFO,"Disconnected msg");
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void login() throws IOException {
        Message loginmsg = new Message();
        loginmsg.setPlayer(player);
        loginmsg.setType(CONNECTED);
        loginmsg.setNote("has connected");
        oos.writeObject(loginmsg);
    }

    public boolean isConnected() {
        return isConnected;
    }
}
