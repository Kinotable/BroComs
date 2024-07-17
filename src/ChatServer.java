import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private ServerSocket serverSocket;
    private List<PrintWriter> clients;

    public ChatServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clients = new ArrayList<>();
        System.out.println("Chat server started on port " + port);
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clientHandler.start();
            } catch (IOException e) {
                System.err.println("Error accepting client connection: " + e.getMessage());
            }
        }
    }

    public List<PrintWriter> getClients() {
        return clients;
    }

    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer(8000);
        chatServer.start();
    }
}}