import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatApp {
    private String username;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatApp(String username) {
        this.username = username;
    }

    public void startChat() {
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket("localhost", 8080);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Connected to chat server!");

            while (true) {
                System.out.print("[" + username + "] > ");
                String message = scanner.nextLine();

                if (message.equals("/quit")) {
                    break;
                }

                out.println(username + ": " + message);
                System.out.println("Message sent: " + message);

                String response = in.readLine();
                System.out.println("Received message: " + response);
            }

            System.out.println("Goodbye, " + username + "!");
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error connecting to chat server: " + e.getMessage());
        } finally {
            try {
                if (socket!= null) {
                    socket.close();
                }
                if (out!= null) {
                    out.close();
                }
                if (in!= null) {
                    in.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        ChatApp chatApp = new ChatApp(username);
        chatApp.startChat();
    }
}