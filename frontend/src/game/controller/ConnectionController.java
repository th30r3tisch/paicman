package game.controller;

import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// is responsible for handling all the connections
public class ConnectionController {
    String serverAddress;
    Scanner in;
    PrintWriter out;
    Stage stage;

    public ConnectionController(Stage primaryStage, String serverAddress) {
        this.stage = primaryStage;
        this.serverAddress = serverAddress;
    }

    public void run() throws IOException {
        try {
            var socket = new Socket(serverAddress, 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (in.hasNextLine()) {
                var line = in.nextLine();
                if (line.startsWith("SUBMITNAME")) {
                    stage.show();
                }
            }
        } finally {
            System.out.println("test");
        }
    }

    public void login(String nickname){
        out.println(nickname);
    }
}
