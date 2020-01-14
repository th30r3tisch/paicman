package game.controller;

import game.model.Message;
import game.model.Player;
import game.model.map.Town;
import game.model.map.TreeNode;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
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
                            player = message.getPlayer();
                            break;
                        case NOTIFICATION:
                            LOGGER.log(Level.INFO,"Notification msg");
                            break;
                        case INIT:
                            LOGGER.log(Level.INFO,"Server msg");
                            updatePlayer(message.getPlayer());
                            wc.addQuadTree(message.getQuadtree());
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
            socket.close();
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

    private void updatePlayer(Player player){
        ConnectionController.player.setColor(player.getColor());
        for (Town t: player.getOwnedTowns()) {
            ConnectionController.player.setOwnedTown(t);
        }
    }

    public static void mapRequest() throws IOException{
        Message mapRequest = new Message();
        mapRequest.setPlayer(player);
        mapRequest.setType(SERVER);
        mapRequest.setNote(player.getName() + " needs the map.");
        oos.writeObject(mapRequest);
        oos.flush();
    }

    public static void attackRequest(TreeNode atk, TreeNode deff){
        Message mapRequest = new Message();
        mapRequest.setPlayer(player);
        mapRequest.setType(ATTACK);
        mapRequest.setTreeNodes(new ArrayList<>(){{
            add(atk);
            add(deff); }});
        try {
            oos.writeObject(mapRequest);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeAttackRequest(TreeNode atk, TreeNode deff) {
        System.out.println("in remove attack method");
        Message mapRequest = new Message();
        mapRequest.setPlayer(player);
        mapRequest.setType(REMOVE_ATTACK);
        mapRequest.setTreeNodes(new ArrayList<>(){{
            add(atk);
            add(deff); }});
        try {
            oos.writeObject(mapRequest);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeTownOwnerRequest(Player player, Town town){
        Message mapRequest = new Message();
        mapRequest.setPlayer(player);
        mapRequest.setType(CHANGE_OWNER);
        ArrayList<TreeNode> townList = new ArrayList<>();
        townList.add(town);
        mapRequest.setTreeNodes(townList);
        try {
            oos.writeObject(mapRequest);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
