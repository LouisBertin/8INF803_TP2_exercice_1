package crawler;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Crawler {
    public Crawler() {
        ArrayList allLinks = getAllLinks();
        getLinksData(allLinks);
    }

    private ArrayList getAllLinks() {
        ArrayList links = new ArrayList();
        Document doc = null;

        try {
            doc = Jsoup.connect("http://legacy.aonprd.com/bestiary/monsterIndex.html").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements newsHeadlines = doc.select("#monster-index-wrapper ul li a");
        for (Element headline : newsHeadlines) {
            String url = "http://legacy.aonprd.com/bestiary/" + headline.attr("href");
            links.add(url);
        }

        return links;
    }

    private void getLinksData(ArrayList links) {
        // init json string
        JSONObject jsonString = new JSONObject()
                .put("JSON1", "Hello World!");

        for (int i = 0; i < 3; i++) {
            try {
                String url = (String) links.get(i);
                Document doc = Jsoup.connect(url).get();
                Elements monsterName = doc.select(".monster-header");
                System.out.println(monsterName.text());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
