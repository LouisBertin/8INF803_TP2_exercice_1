import crawler.Crawler;
import crawler.Helper;
import org.json.JSONArray;

public class Main {

    public static String[] monstersLinks = {
            "http://legacy.aonprd.com/bestiary/monsterIndex.html",
            "http://legacy.aonprd.com/bestiary2/additionalMonsterIndex.html"
    };

    public static void main(String[] args) {
        // crawl first link
        Crawler link1 = new Crawler(monstersLinks[0]);
        // crawl second link
        Crawler link2 = new Crawler(monstersLinks[1]);

        // merge all arrays
        JSONArray array = Helper.concatArray(link1.getJsonArray(), link2.getJsonArray());
        // save it to file
        Helper.jsonToFile(array);
    }

}
