package us.xvicario.animiru;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class NineAnime {

    final static String baseUrl = "http://9anime.is/";
    final static String searchUrl = baseUrl + "search?keyword=";

    public static List<Anime> searchAnime(String keyword) {
        ArrayList<Anime> animeList = new ArrayList<>();
        String completeSearch = searchUrl;
        try {
            completeSearch += URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // todo: log this error
        }
        try {
            Document doc = Jsoup.connect(completeSearch).get();
            Elements shows = doc.select("div.film-list div.item");
            for (Element show : shows) {
                String name = show.select("a.name[data-jtitle]").attr("data-jtitle");
                URL url = new URL(show.select("a.name[href]").get(0).attr("href"));
                animeList.add(new Anime(name, "description", url));
            }
        } catch (IOException e) {
            // todo: deal with this
        }
        return animeList;
    }

}
