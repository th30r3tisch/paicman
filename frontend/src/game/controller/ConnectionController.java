package game.controller;

import game.model.Message;
import game.model.Player;
import game.model.TreeNode;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static game.model.MessageType.*;

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
    private WorldController wc;
    private Stage stage;

    public ConnectionController(Player player, String serverAddress, WorldController wc, Stage stage) {
        this.serverAddress = serverAddress;
        this.port = 59001;
        ConnectionController.player = player;
        this.wc = wc;
        this.stage = stage;
    }

    public void run() {
        try {
            socket = new Socket(serverAddress, port);
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
                            wc.addNodes(message.getTreeNodes());
                            break;
                        case CONNECTED:
                            LOGGER.log(Level.INFO,"Connected msg");
                            Platform.runLater(()->{ stage.setScene(wc.getScene());});
                            break;
                        case DISCONNECTED:
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

    public static Player getPlayer() {
        return player;
    }

    public static void mapRequest() throws IOException{
        Message mapRequest = new Message();
        mapRequest.setPlayer(player);
        mapRequest.setType(SERVER);
        mapRequest.setNote(player.getName() + " needs the map.");
        oos.writeObject(mapRequest);
        oos.flush();
    }

    public static void attackRequest(ArrayList nodes){
        Message mapRequest = new Message();
        mapRequest.setPlayer(player);
        mapRequest.setType(ATTACK);
        mapRequest.setTreeNodes(nodes);
        try {
            oos.writeObject(mapRequest);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeAttackRequest(ArrayList nodes) {
        Message mapRequest = new Message();
        mapRequest.setPlayer(player);
        mapRequest.setType(REMOVEATTACK);
        mapRequest.setTreeNodes(nodes);
        try {
            oos.writeObject(mapRequest);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
