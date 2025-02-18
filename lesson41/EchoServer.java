import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run(){
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            try(Socket socket = serverSocket.accept()) {
                handle(socket);
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void handle(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(inputStream);
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        try(Scanner scanner = new Scanner(isr)) {
            while (true) {
                var message = scanner.nextLine().strip();

                System.out.printf("Got: %s%n", message);

                String reversedMessage = new StringBuilder(message).reverse().toString();


                writer.println(reversedMessage);
                writer.flush();

                if (message.toLowerCase().equals("bye")) {

                    System.out.println("Bye bye");

                    return;

                }

            }

        } catch (NoSuchElementException e) {

            System.out.println("Client dropped connection");

        }

            }
        }
