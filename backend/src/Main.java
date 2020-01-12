import game.model.Message;
import game.model.MessageType;
import game.model.Player;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final int PORT = 59001;
    private static HashSet<ObjectOutputStream> writers = new HashSet<>();
    private static final HashMap<String, Player> names = new HashMap<>();
    private static ArrayList<Player> players = new ArrayList<>();
    private static Logger LOGGER = Logger.getLogger("InfoLogging");
    private static Game game;

    public static void main(String[] args) throws Exception {
        LOGGER.log(Level.INFO,"Server is live!");
        ExecutorService pool = Executors.newFixedThreadPool(32);
        ServerSocket listener = new ServerSocket(PORT);

        try {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            listener.close();
        }
    }

    /**
     * The client handler task.
     */
    private static class Handler implements Runnable {
        private String name;
        private Socket socket;
        private Player player;
        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;
        private InputStream is;
        private static Logger LOGGER = Logger.getLogger("InfoLogging");

        public Handler(Socket socket) {
            this.socket = socket;
            if (game == null){
                game = new Game();
            }
        }

        public void run() {
            LOGGER.log(Level.INFO,"Trying to connect player");
            try {
                is = socket.getInputStream();
                input = new ObjectInputStream(is);
                os = socket.getOutputStream();
                output = new ObjectOutputStream(os);

                Message connectionSetup = (Message) input.readObject();
                name = connectionSetup.getPlayer().getName();
                checkDuplicatePlayer();
                writers.add(output);
                sendNotification();
                addToList();

                while (socket.isConnected()) {
                    Message clientmsg = (Message) input.readObject();
                    if (clientmsg != null) {
                        LOGGER.log(Level.INFO,clientmsg.getType() + " - " + clientmsg.getPlayer() + ": " + clientmsg.getNote());
                        switch (clientmsg.getType()) {
                            case CONNECTED:
                                addToList();
                                break;
                            case SERVER:
                                sendInitialMap();
                                LOGGER.log(Level.INFO, "Map sent to " + name);
                                break;
                            case ATTACK:
                                LOGGER.log(Level.INFO, "Attack request from " + name);
                                break;
                            case REMOVE_ATTACK:
                                LOGGER.log(Level.INFO, "Cancel attack from " + name);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Exception in run() method for user: " + name, e);
            } finally {
                closeConnections();
            }
        }

        private Message sendInitialMap() throws IOException{
            Message msg = new Message();
            msg.setNote("Initial map received");
            msg.setType(MessageType.SERVER);
            msg.setPlayer(player);
            //msg.setTreeNodes(game.getAreaContent());
            msg.setQuadtree(game.getInitialMap());
            write(msg);
            return msg;
        }

        private Message sendNotification() throws IOException {
            Message msg = new Message();
            msg.setNote(name + " has joined the game.");
            msg.setType(MessageType.NOTIFICATION);
            msg.setPlayer(player);
            write(msg);
            return msg;
        }

        private Message removeFromList() throws IOException {
            Message msg = new Message();
            msg.setNote(name + " has left the game.");
            msg.setType(MessageType.DISCONNECTED);
            write(msg);
            return msg;
        }

        /*
         * For displaying that a user has joined the server
         */
        private Message addToList() throws IOException {
            Message msg = new Message();
            msg.setNote("Welcome, may the best win!");
            msg.setType(MessageType.CONNECTED);
            msg.setPlayer(player);
            write(msg);
            return msg;
        }

        /*
         * Creates and sends a Message type to the listeners.
         */
        private void write(Message msg) throws IOException {
            for (ObjectOutputStream writer : writers) {
                writer.writeObject(msg);
                writer.reset();
            }
        }

        private synchronized void closeConnections() {
            LOGGER.log(Level.INFO,"Start closing connection");
            LOGGER.log(Level.INFO,"HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + players.size());
            if (name != null) {
                names.remove(name);
                LOGGER.log(Level.INFO,"User: " + name + " has been removed!");
            }
            if (player != null){
                players.remove(player);
                LOGGER.log(Level.INFO,"Player object: " + player + " has been removed!");
            }
            if (output != null){
                writers.remove(output);
                LOGGER.log(Level.INFO,"Writer object: " + player + " has been removed!");
            }
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                removeFromList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            LOGGER.log(Level.INFO,"HashMap names: " + names.size() + " writers: " + writers.size() + " usersList size: " + players.size());
            LOGGER.log(Level.INFO,"Closing connection finished");
        }

        private synchronized void checkDuplicatePlayer(){
            LOGGER.log(Level.INFO,"Trying to connect " + name);
            if (!names.containsKey(name)) {
                player = new Player();
                player.setName(name);
                player.setColor(Color.BLUE);
                players.add(player);
                names.put(name, player);

                LOGGER.log(Level.INFO, name + " is connected");
            } else {
                LOGGER.log(Level.SEVERE, name + " is already connected");
            }
        }
    }
}