import crawler.Crawler;
import crawler.Helper;
import org.json.JSONArray;

public class Main {

    /**
     * bestiary links
     */
    public static String[] monstersLinks = {
            "http://legacy.aonprd.com/bestiary/monsterIndex.html",
            "http://legacy.aonprd.com/bestiary2/additionalMonsterIndex.html",
            "http://legacy.aonprd.com/bestiary3/monsterIndex.html",
            "http://legacy.aonprd.com/bestiary4/monsterIndex.html",
            "http://legacy.aonprd.com/bestiary5/index.html"
    };

    public static void main(String[] args) {
        // crawl bestiary 1
        Crawler link1 = new Crawler(monstersLinks[0]);
        // crawl bestiary 2
        Crawler link2 = new Crawler(monstersLinks[1]);
        // crawl bestiary 3
        Crawler link3 = new Crawler(monstersLinks[2]);
        // crawl bestiary 4
        Crawler link4 = new Crawler(monstersLinks[3]);
        // crawl bestiary 5
        Crawler link5 = new Crawler(monstersLinks[4]);

        // merge all arrays
        JSONArray array = Helper.concatArray(
                link1.getJsonArray(),
                link2.getJsonArray(),
                link3.getJsonArray(),
                link4.getJsonArray(),
                link5.getJsonArray()
        );

        // save it to file
        Helper.jsonToFile(array);
    }

}
