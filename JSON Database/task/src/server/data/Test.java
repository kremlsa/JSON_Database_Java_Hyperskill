package server.data;

import server.Element;
import server.Utils;

import javax.swing.text.Utilities;

public class Test {

    public static void main(String[] args) {
        System.out.println("Server started!");
        Element el = new Element();
      /*  el.createChilds("{\n" +
                "   \"type\":\"set\",\n" +
                "   \"key\":\"person\",\n" +
                "   \"value\":{\n" +
                "      \"name\":\"Elon Musk\",\n" +
                "      \"car\":{\n" +
                "         \"model\":\"Tesla Roadster\",\n" +
                "         \"year\":\"2018\"\n" +
                "      },\n" +
                "      \"rocket\":{\n" +
                "         \"name\":\"Falcon 9\",\n" +
                "         \"launches\":\"87\"\n" +
                "      }\n" +
                "   }\n" +
                "}");*/
        el.setElementName("person");

        el.set("{\n" +
                "      \"name\":\"Elon Musk\",\n" +
                "      \"car\":{\n" +
                "         \"model\":\"Tesla Roadster\",\n" +
                "         \"year\":\"2018\"\n" +
                "      },\n" +
                "      \"rocket\":{\n" +
                "         \"name\":\"Falcon 9\",\n" +
                "         \"launches\":\"87\"\n" +
                "      }\n" +
                "}", el, "person");


        System.out.println(el.print());
        System.out.println();

        el.update("{\"type\":\"set\",\"key\":[\"person\",\"rocket\",\"launches\"],\"value\":\"88\"}");
        boolean r = el.delEl("[\"person\",\"name\"]");

        System.out.println(r);

        System.out.println(el.print());

        System.out.println(el.getElementName());




    }
}
