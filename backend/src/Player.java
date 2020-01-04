import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


class Player implements Runnable {
    Socket socket;
    Scanner input;
    PrintWriter output;

    public Player(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {socket.close();} catch (IOException e) {}
        }
    }

    private void setup() throws IOException {
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
    }
}
