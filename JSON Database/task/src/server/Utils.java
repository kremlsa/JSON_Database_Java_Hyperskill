package server;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.*;

public class Utils {
    public static Map<String, String> getTree(String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        addKeys("", root, map, new ArrayList<>());
        return map;
    }

    public static String replaceElement(String input, String key, String value) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((ObjectNode)root).put(key, value);

        return root.toString();
    }

    private static void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> map, List<Integer> suffix) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + "-";
            map.put(currentPath, jsonNode.toString());

            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                addKeys(pathPrefix + entry.getKey(), entry.getValue(), map, suffix);
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            map.put(currentPath, jsonNode.toString());

        } else if (jsonNode.isValueNode()) {
            if (currentPath.contains("-")) {
                for (int i = 0; i < suffix.size(); i++) {
                   // currentPath += "-" + suffix.get(i);
                    currentPath += suffix.get(i);
                }

                suffix = new ArrayList<>();
            }

            ValueNode valueNode = (ValueNode) jsonNode;
            map.put(currentPath, valueNode.asText());
        }
    }

    public static String getType(String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        addKeys("", root, map, new ArrayList<>());
        return map.get("type");
    }

    public static String getKey(String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        addKeys("", root, map, new ArrayList<>());
        return map.get("key");
    }

    public static String getValue(String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        addKeys("", root, map, new ArrayList<>());
        return map.get("value");
    }


}
