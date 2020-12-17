package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Element {
    String elementName = null;
    String elementValue = null;
    List<Element> childs;

    public Element() {
        childs = new ArrayList<>();

    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementName() {
        return elementName;
    }

    public void createChilds(String input) {
        ArrayList<String> ch = new ArrayList<>();
        String key = Utils.getKey(input);
        String value = Utils.getValue(input);
        key = key.trim().replace("[", "").replace("]", "").replace("\"", "");
        String[] arr = key.split(",");
        for (String s : arr) {
            ch.add(s);
        }
        recursiveCh(ch, value, this);
    }

    public void recursiveCh(List<String> ch, String value, Element el) {
        if (ch.size() == 1) {
            el.elementValue = value;
            el.elementName = ch.get(0);
            return;
        } else {

            el.elementName = ch.get(0);
            Element child = new Element();
            el.childs.add(child);
            ch.remove(0);
            recursiveCh(ch, value, child);
        }

    }

    public String getJsonValue() {

        String res = "";
        String val = "";

        val = "\"" +  elementValue + "\"";
        res += val;
        return res;
    }

    public String print() {
        if (childs.size() > 0) {
            String res = "{ ";
            for (int i = 0; i < childs.size(); i++) {
                Element e = childs.get(i);
                String lbracket = e.childs.size() > 0 ? "{ " : "";
                String rbracket = e.childs.size() > 0 ? " }" : "";
                String zpt = i < e.childs.size() ? ", " : "";
                res += "\"" + e.getElementName() + "\": " + lbracket + (e.elementValue != null ? ("\"" + e.elementValue + "\"") : "");
                String temp = e.printR();
                temp = temp.trim();
                res += temp;
                res += rbracket + zpt;
            }
            res += " }";
            return res;
        } else {
            String res = getJsonValue();
            return res;
        }

    }

    public String printR() {
        if (childs.size() > 0) {
            String res = "";
            for (int i = 0; i < childs.size(); i++) {
                Element e = childs.get(i);
                String lbracket = e.childs.size() > 0 ? "{ " : "";
                String rbracket = e.childs.size() > 0 ? " }" : "";
                String zpt = i < childs.size() - 1 ? ", " : "";
                res += "\"" + e.elementName + "\": " + lbracket + e.getJsonValue()  + e.printR() + rbracket + zpt;
            }
            return res;
        } else {
            return "";
        }
    }

    public void update(String input) {
        ArrayList<String> ch = new ArrayList<>();
        String key = Utils.getKey(input);
        String value = Utils.getValue(input);
        key = key.trim().replace("[", "").replace("]", "").replace("\"", "");
        String[] arr = key.split(",");
        for (String s : arr) {
            if(s.equals(elementName)) continue;
            ch.add(s);
        }
        recursiveUp(ch, value, this);
    }

    public void recursiveUp(List<String> ch, String value, Element el) {
        if (ch.size() == 0) {
            el.elementValue = value;
            return;
        } else {
            Element child;
            if (findChilds(el.childs, ch.get(0)) != null) {
                child = findChilds(el.childs, ch.get(0));
                ch.remove(0);
                recursiveUp(ch, value, child);
            } else {
                child = new Element();
                child.setElementName(ch.get(0));
                el.childs.add(child);
                ch.remove(0);
                recursiveUp(ch, value, child);
            }
        }
    }

    public String findEl(String key) {
        ArrayList<String> ch = new ArrayList<>();
        key = key.trim().replace("[", "").replace("]", "").replace("\"", "");
        String[] arr = key.split(",");
        for (String s : arr) {
            if(s.equals(elementName)) {
                continue;
            }
            ch.add(s);
        }
        Element r = recursiveFind(ch, this);
        return r == null ? "null" : r.print();
    }

    public Element recursiveFind(List<String> ch, Element el) {
        Element result = null;
        if (ch.size() == 0) {
            return el;
        } else {
            Element child;
            if (findChilds(el.childs, ch.get(0)) != null) {
                child = findChilds(el.childs, ch.get(0));
                ch.remove(0);
                result = recursiveFind(ch, child);
            }
            return result;
        }
    }

    public boolean delEl(String key) {
        ArrayList<String> ch = new ArrayList<>();
        key = key.trim().replace("[", "").replace("]", "").replace("\"", "");
        String[] arr = key.split(",");
        for (String s : arr) {
            if(s.equals(elementName)) {
                continue;
            }
            ch.add(s);
        }
        return recursiveDel(ch, this);
    }

    public boolean recursiveDel(List<String> ch, Element el) {
        boolean result = false;
        if (ch.size() == 1) {
            if (findChilds(el.childs, ch.get(0)) != null) {
                el.childs.remove(findChilds(el.childs, ch.get(0)));
                return true;
            }
            return false;
        } else {
            Element child;
            if (findChilds(el.childs, ch.get(0)) != null) {
                child = findChilds(el.childs, ch.get(0));
                ch.remove(0);
                result = recursiveDel(ch, child);
            }
            return result;
        }
    }

    public Element findChilds(List<Element> childs, String name) {
        for (Element e : childs) {
            if (e.elementName.endsWith(name)) return e;
        }
        return null;
    }

    public void set(String input, Element el, String name) {
        if (input.contains("{")) {
            Map<String, String> map = Utils.getTree(input);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!entry.getKey().contains("-") && entry.getKey().length() > 0) {
                    Element child = new Element();
                    child.elementName = entry.getKey();
                    el.childs.add(child);
                    set(entry.getValue(), child, child.elementName);
                }
            }
        } else {
            el.elementValue = input;
        }
    }
}