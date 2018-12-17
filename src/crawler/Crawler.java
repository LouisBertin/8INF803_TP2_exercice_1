package crawler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Crawler {
    // Current file directory
    private final String CURRENT_DIR = System.getProperty("user.dir") + "/src/crawler/";
    // jsonArray
    private JSONArray jsonArray;

    /**
     * Constructor
     */
    public Crawler(String url) {
        ArrayList allLinks = getAllLinks(url);
        jsonArray = getLinksData(allLinks);
    }

    /**
     * return all monster links from legacy.aonprd.com
     * @return ArrayList
     */
    private ArrayList getAllLinks(String url) {
        ArrayList links = new ArrayList();
        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements newsHeadlines = doc.select("#monster-index-wrapper ul li");

        for (Element headline : newsHeadlines) {
            String headlineChar = (String) headline.toString();
            String test = Character.toString(headlineChar.charAt(4));
            if (test.equals("<")) {
                Element tag = Jsoup.parse(headlineChar, "", Parser.xmlParser());
                String urlFetched = Helper.removeUrlLastPart(url) + "/" + tag.select("a").attr("href");
                links.add(urlFetched);
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
        for (int i = 0; i < links.size(); i++) {
            try {
                String url = (String) links.get(i);
                Document doc = Jsoup.connect(url).get();

                // fetch monster name
                Elements monsterNames = doc.select(".stat-block-title > b");
                if (monsterNames.isEmpty()) {
                    monsterNames = doc.select(".stat-block-title");
                }

                insertJson(jsonArray, monsterNames, doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;
    }

    /**
     * Build Json Array
     * @param jsonArray    JSONArray
     * @param monsterNames Elements
     */
    private void insertJson(JSONArray jsonArray, Elements monsterNames, Document doc) {
        for (Element element : monsterNames) {
            // fetch monster name
            element.select(".stat-block-cr").remove();
            String monsterNameString = element.text();
            // fetch monster spells
            Elements elements = doc.select(".stat-block-title, .stat-block-breaker:contains(Offense) ~ .stat-block-2 a");
            List spells = getMonsterSpells(elements, monsterNameString);

            JSONObject monster = new JSONObject();
            monster.put("name", monsterNameString);
            monster.put("spells", ((Elements) spells).eachText());

            jsonArray.put(monster);
        }
    }

    /**
     * return monster spells
     * @param elements Elements
     * @param monsterName MonsterName
     * @return Elements
     */
    private Elements getMonsterSpells(Elements elements, String monsterName) {
        Elements spells = new Elements();
        int rightH1 = 0;

        // clean array
        for (Element elem : elements) {
            elem.select(".stat-block-cr").remove();
        }

        // return spells for current monster
        search : {
            for (Element element : elements) {
                if (!element.classNames().isEmpty()) {
                    if (rightH1 == 1) {
                        break search;
                    }
                    if (element.text().equals(monsterName)) {
                        rightH1++;
                    }
                }
                if (element.tag().toString().equals("a") && rightH1 == 1) {
                    spells.add(element);
                }
            }
        }

        return spells;
    }

    /**
     *
     * @return JSONArray
     */
    public JSONArray getJsonArray() {
        return jsonArray;
    }

}
