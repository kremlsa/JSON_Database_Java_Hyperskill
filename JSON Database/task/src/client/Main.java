package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("Client started!");
        Args arg = new Args();
        JCommander.newBuilder()
                .addObject(arg)
                .build()
                .parse(args);
        System.out.println(arg.command + "*" + arg.key + "*" + arg.value + "*" + arg.filename);
        Gson gson = new Gson();
        String out = "";
        if (arg.filename != null) {
            Reader reader = null;
            try {
                Path fileName = Path.of("C:\\Java_lessons\\JSON Database\\JSON Database\\task\\src\\client\\data\\" + arg.filename);
                out = Files.readString(fileName);
                System.out.println("inside " + out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            Map<String, String> params = new HashMap<>();
            params.put("type", arg.command);
            params.put("key", arg.key);
            params.put("value", arg.value);

            out = gson.toJson(params);
        }
        String address = "127.0.0.1";
        int port = 23456;
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(address), port);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(out);
            output.flush();
            System.out.println("Sent: " + out);
            String in = input.readUTF();
            System.out.println("Received: " + in);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Args {
    @Parameter
    public List<String> messages = new ArrayList<>();
    @Parameter(names={"-t"})
    String command;
    @Parameter(names={"-k"})
    String key;
    @Parameter(names={"-v"})
    String value;
    @Parameter(names={"-in"})
    String filename;
}

