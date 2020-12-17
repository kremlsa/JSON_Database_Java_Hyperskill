package server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server {
    Map<String, Socket> clientSockets = Collections.synchronizedMap(new HashMap<String, Socket>());
    static boolean isWork = true;
    ServerSocket srvsocket;

    static Controller controller;

    public class ClientHandler implements Runnable {
        DataInputStream input;
        DataOutputStream output;
        Socket sock;
        String name;
        int clientNumber;

        public ClientHandler(Socket clientSocket, int number) {
            try {
                clientNumber = number;
                sock = clientSocket;
                input = new DataInputStream(sock.getInputStream());
                output = new DataOutputStream(sock.getOutputStream());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            try {
                boolean isRun = true;
                while (isRun) {
                    String in = input.readUTF();
                    String message = "";
                    System.out.println("Received1: " + in);
                    String type = Utils.getType(in);
                    //Gson gson = new Gson();
                    //Commands com = gson.fromJson(in, Commands.class);

                    if (type.equals("exit")) {
                    //    System.out.println("Received2: " + in);
                        output.writeUTF("{\"response\":\"OK\"}");
                       // System.out.println("Sent: " + "{\"response\":\"OK\"}");
                        output.flush();
                        isWork = false;
                        isRun = false;
                        sock.close();
                        srvsocket.close();
                    } else {
                        //System.out.println(com.type + "*" + com.key + "*" + com.value);
                        String out = controller.command(type, in);
                        System.out.println("Received: " + in);
                        output.writeUTF(out);
                        System.out.println("Sent: " + out);
                        output.close();
                        output.flush();
                    }
                }
            } catch (Exception ex) {
               // ex.printStackTrace();
            }
        }

    }


    public void start(SimpleDB db) {
        controller = new Controller(db);
        new Server().go();
    }

    public boolean checkThreads() {
        Thread t = Thread.currentThread();
        String name = t.getName();
        System.out.println("name = " + name);
        return true;
    }

    public void go() {
        srvsocket = null;
        try {
            try {
                int i = 1;
                srvsocket = new ServerSocket(23456, 0, InetAddress.getByName("127.0.0.1"));
                //srvsocket.setSoTimeout(7000);

                while (isWork) {
                    Socket clientSocket = srvsocket.accept();
                    Thread t = new Thread(new ClientHandler(clientSocket, i));
                    t.start();
               //     System.out.println("Client " + i + " connected!");
                    i++;
                    t.join(1L);
                }
            } catch (Exception ex) {
               // ex.printStackTrace();
            }
        } finally {
            try {
                if (srvsocket != null)
                    srvsocket.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        System.exit(0);
    }

}

class Commands {
    String type;
    String key;
    String value;

    public Commands (String type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }
}