package crawler;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Crawler {
    // Current file directory
    private final String CURRENT_DIR = System.getProperty("user.dir") + "/src/crawler/";

    /**
     * Constructor
     */
    public Crawler() {
        ArrayList allLinks = getAllLinks();
        JSONArray jsonArray = getLinksData(allLinks);
        jsonToFile(jsonArray);
    }

    /**
     * return all monster links from legacy.aonprd.com
     * @return ArrayList
     */
    private ArrayList getAllLinks() {
        ArrayList links = new ArrayList();
        Document doc = null;

        try {
            doc = Jsoup.connect("http://legacy.aonprd.com/bestiary/monsterIndex.html").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements newsHeadlines = doc.select("#monster-index-wrapper ul li");
        for (Element headline : newsHeadlines) {
            String headlineChar = (String) headline.toString();
            String test = Character.toString(headlineChar.charAt(4));
            if (test.equals("<")) {
                Element tag = Jsoup.parse(headlineChar, "", Parser.xmlParser());
                String url = "http://legacy.aonprd.com/bestiary/" + tag.select("a").attr("href");
                links.add(url);
            }
        }

        return links;
    }

    /**
     * return Json from each monster page
     * @param links ArrayList
     * @return JSONArray
     */
    private JSONArray getLinksData(ArrayList links) {
        // init json string
        JSONArray jsonArray = new JSONArray();

        // TODO : change 10 to links.size() when crawler is over
        for (int i = 0; i < 10; i++) {
            try {
                String url = (String) links.get(i);
                Document doc = Jsoup.connect(url).get();
                Elements monsterNames = doc.select(".stat-block-title > b");

                insertJson(jsonArray, monsterNames);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(jsonArray);
        return jsonArray;
    }

    /**
     * Build Json Array
     * @param jsonArray JSONArray
     * @param monsterNames Elements
     */
    private void insertJson(JSONArray jsonArray, Elements monsterNames) {
        for (Element element: monsterNames) {
            element.select(".stat-block-cr").remove();
            String monsterNameString = element.text();

            JSONObject monster = new JSONObject();
            monster.put("name", monsterNameString);
            monster.put("spells", "test");

            jsonArray.put(monster);
        }
    }

    /**
     * convert JsonArray to Json file
     * @param jsonArray JSONArray
     */
    private void jsonToFile(JSONArray jsonArray) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

        String originalJson = jsonArray.toString();
        JsonNode tree = null;
        try {
            tree = objectMapper .readTree(originalJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String formattedJson = objectMapper.writeValueAsString(tree);

            try (FileWriter file = new FileWriter(CURRENT_DIR + "monsters.json")) {
                file.write(formattedJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
