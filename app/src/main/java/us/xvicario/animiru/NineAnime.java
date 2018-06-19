package us.xvicario.animiru;

import android.util.Log;

import com.google.common.primitives.Chars;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class NineAnime implements AnimeSource {

    public static final String SOURCE = "9anime";

    private final static String baseUrl = "https://9anime.is/";
    private final static String searchUrl = baseUrl + "search?keyword=";
    private final static String filterUrl = baseUrl + "filter";

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
            //Log.e("NineAnime", e.getLocalizedMessage());
            System.out.println(e.getLocalizedMessage());
        }
        return animeList;
    }

    public static List<Anime> filterAnime(Genres... genres) {
        ArrayList<Anime> animeList = new ArrayList<>();
        StringBuilder compiledFilterUrl = new StringBuilder(filterUrl + "?");
        for (Genres genre : genres) {
            compiledFilterUrl.append("genre[]=").append(genre.ordinal()).append('&');
        }
        try {

            Document document = Jsoup.connect(URLEncoder.encode(compiledFilterUrl.toString(), "UTF-8")).get();
            Elements shows = document.select("div.film-list div.item");
            for (Element show : shows) {
                String name = show.select("a.name[data-jtitle]").attr("data-jtitle");
                URL url = new URL(show.select("a.name[href]").get(0).attr("href"));
                animeList.add(new Anime(name, "description", url));
            }
        } catch (UnsupportedEncodingException e) {
            // todo: deal with this
        } catch (MalformedURLException e) {
            // todo: deal with this
        } catch (IOException e) {
            // todo: deal with this
        }
        return animeList;
    }

    public static void fetchAnime(Anime anime) {
        try {
            Document document = Jsoup.connect(anime.url.toString()).get();
            anime.ts = document.select("html[data-ts]").first().attr("data-ts");
            Elements episodeLinks = document.select("div.server li a[href]");
            for (Element element : episodeLinks) {
                anime.addEpisode(element.attr("data-id"), new URL(baseUrl + element.attr("href")));
            }
        } catch (IOException e) {
            // todo: deal with this
        }
    }

    public static String fetchCdnLink(String episodeId, String server, int paramKey, int ts) {
        return "https://9anime.is/ajax/episode/info?id=" + episodeId + "&server=" + server + "&_=" + paramKey + "&ts=" + ts;
    }

    public static int generateParamKey(String episodeId, String server, int ts) {
        String dd = "8s5IB3Gt";
        int param = s(dd);
        // id, server, ts
        param += s(a(dd + "id", (String) episodeId));
        param += s(a(dd + "server", (String) server));
        param += s(a(dd + "ts", Integer.toString(ts)));
        return param;
    }

    private static int s(String t) {
        int i = 0;
        List<Character> charList = Chars.asList(t.toCharArray());
        ListIterator<Character> it = charList.listIterator();
        while(it.hasNext()) {
            i += (int) it.next() + it.nextIndex();
        }
        return i;
    }

    private static String a(String t, String e) {
        int tsum = 0;
        for (int c : t.toCharArray()) {
            tsum += c;
        }
        int esum = 0;
        for (int c : e.toCharArray()) {
            esum += c;
        }
        int n = tsum + esum;
        return Integer.toHexString(n);
    }

    public enum Genres {
        ACTION,
        ADVENTURE,
        CARS,
        COMEDY,
        DEMENTIA,
        DEMONS,
        DRAMA,
        ECCHI,
        FANTASY,
        GAME,
        HAREM,
        JOSEI,
        KIDS,
        MAGIC,
        MARTIAL_ARTS,
        MECHA,
        MILITARY,
        MUSIC,
        MYSTERY,
        PARODY,
        POLICE,
        PSYCHOLOGICAL,
        ROMANCE,
        SAMURAI,
        SCHOOL,
        SCIFI,
        SEINEN,
        SHOUJO,
        SHOUJO_AI,
        SHOUNEN,
        SHOUNEN_AI,
        SLICE_OF_LIFE,
        SPACE,
        SPORTS,
        SUPER_POWER,
        SUPERNATURAL,
        THRILLER,
        VAMPIRE,
        YAOI,
        YURI
    }

}
