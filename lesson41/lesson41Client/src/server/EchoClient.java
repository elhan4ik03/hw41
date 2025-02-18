package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    private final int port;
    private final String host;

    public EchoClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

  public static EchoClient connectTo(int port) {
        return new EchoClient(port, "localhost");
  }

  public void run() {
      System.out.printf("Для выхода напишите 'bye'%n%n%n");
      try (Socket socket = new Socket(host, port)) {
          Scanner scanner = new Scanner(System.in, "UTF-8");
          OutputStream outputStream = socket.getOutputStream();
          PrintWriter writer = new PrintWriter(outputStream);
          InputStream inputStream = socket.getInputStream();
          Scanner serverScanner = new Scanner(inputStream);

          try(scanner; writer) {
              while (true) {
                  String message = scanner.nextLine();
                  writer.write(message);
                  writer.write(System.lineSeparator());
                  writer.flush();

                  if (serverScanner.hasNextLine()) {
                      String serverResponse = serverScanner.nextLine();
                      System.out.printf("Server: %s%n", serverResponse);
                  }

                  if(message.equalsIgnoreCase("bye")) {
                    return;
                  }
              }
          } catch (NoSuchElementException e) {
              System.out.println("Connection dropped");
          }

      } catch (IOException e) {
          System.out.printf("Cannot connect to %s:%s%n", host, port);
          e.printStackTrace();
      }
  }

}
