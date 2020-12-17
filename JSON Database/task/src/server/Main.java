package server;

public class Main {

    public static void main(String[] args) {
        System.out.println("Server started!");
        Server srv = new Server();
        SimpleDB db = new SimpleDB();
        db.saveDb();
        srv.start(db);
    }
}