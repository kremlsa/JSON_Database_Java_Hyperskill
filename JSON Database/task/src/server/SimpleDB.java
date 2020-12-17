package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleDB {
    Map<String, Element> db = new LinkedHashMap<>();

    public void setRecord(String key, Element element) {

        db.put(key, element);
        saveDb();
    }

    public Element getRecord(String key) {
        return db.getOrDefault(key, null);
    }

    public boolean deleteRecord(String key) {
        if (db.containsKey(key)) {
            db.remove(key);
            saveDb();
            return true;
        } else {
            return false;
        }
    }

    public void saveDb() {
        File file = new File("C:\\Java_lessons\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json");
        //file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson2 = new GsonBuilder().create();
            gson2.toJson(db, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDb() {

    }

}

