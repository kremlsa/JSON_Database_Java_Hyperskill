package server;

public class Controller {
    SimpleDB db;

    Controller(SimpleDB db) {
        this.db = db;
    }

    public String command(String command, String input) {
        String result = "ERROR";
        switch (command) {
            case "set":
                String key = Utils.getKey(input);
                if (key.contains("[")) {
                    key = key.trim().replace("[", "").replace("]", "").replace("\"", "");
                    String[] arr = key.split(",");
                    String res = "";
                    if (db.getRecord(arr[0]) != null) {
                        Element el = db.getRecord(arr[0]);
                        el.update(input);
                    } else {
                        Element el = new Element();
                        el.createChilds(input);
                        db.setRecord(arr[0], el);
                    }
                } else {
                    String value = Utils.getValue(input);
                    Element el = new Element();
                    el.setElementName(key);
                    el.set(value, el, key);
                    db.setRecord(key, el);
                }
                result = "{\"response\":\"OK\"}";
                break;
            case "get":
                String res = null;
                String[] arr = null;
                key = Utils.getKey(input);
                if (key.contains("[")) {
                    key = key.trim().replace("[", "").replace("]", "").replace("\"", "");
                    arr = key.split(",");
                    if (arr.length > 1) {
                        System.out.println(Utils.getKey(input));
                        res = db.getRecord(arr[0]).findEl(Utils.getKey(input));
                    } else {
                        res = db.getRecord(arr[0]).print();
                    }

                } else if (db.getRecord(key) != null) {
                    res = db.getRecord(key).print();
                }
                if (res == null) {
                    result = "{\"response\":\"ERROR\",\"reason\":\"No such key\"}";
                } else {
                    if (res.startsWith("{")) result = "{\"response\":\"OK\",\"value\":" + res + "}";
                    else
                        result = "{\"response\":\"OK\",\"value\":" + res + "}";
                }
                break;
            case "delete":
                key = Utils.getKey(input);
                if (key.contains("[")) {
                    key = key.trim().replace("[", "").replace("]", "").replace("\"", "");
                    arr = key.split(",");
                    if (arr.length > 1) {
                        if (db.getRecord(arr[0]).delEl(Utils.getKey(input))) {
                            result = "{\"response\":\"OK\"}";
                        } else {
                            result = "{\"response\":\"ERROR\",\"reason\":\"No such key\"}";
                        }

                    } else {
                        if (db.deleteRecord(key)) {
                            result = "{\"response\":\"OK\"}";
                        } else {
                            result = "{\"response\":\"ERROR\",\"reason\":\"No such key\"}";
                        }
                    }
                } else {
                    if (db.deleteRecord(key)) {
                        result = "{\"response\":\"OK\"}";
                    } else {
                        result = "{\"response\":\"ERROR\",\"reason\":\"No such key\"}";
                    }
                }
                break;
        }
        return result;
    }
}
