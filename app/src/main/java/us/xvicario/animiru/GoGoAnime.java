package us.xvicario.animiru;

import com.google.common.collect.ImmutableList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoGoAnime implements AnimeSource {

    public static final String SOURCE = "GoGoAnime";
    public static final ImmutableList<String> SERVERS =
            new ImmutableList.Builder<String>().addAll(Arrays.asList(
                    "Vidstreaming", "Vidcdn", "Streamango", "Estream", "Oload", "OpenUpload",
                    "Thevideo", "Mp4Upload", "YourUpload", "Bestream")).build();

    private static final String baseUrl = "https://www.gogoanime.in/";
    private static final String searchUrl = baseUrl + "search.html?keyword=";

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
            Elements shows = doc.selectFirst(".items").children();
            for (Element show : shows) {
                String name = show.selectFirst(".name").child(0).attr("title");
                URL url = new URL(baseUrl + show.selectFirst(".name").child(0).attr("href"));
                animeList.add(new Anime(name, "description", url));
            }
        } catch (IOException e) {
            //Log.e("NineAnime", e.getLocalizedMessage());
            System.out.println(e.getMessage());
        }
        return animeList;
    }

    /*public static List<Anime> filterAnime(Genres... genres) {
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
    }*/

    public static void fetchAnime(Anime anime) {
        final String apiUrl = "https://www.gogoanime.in/load-list-episode?ep_start=1"; //&ep_end=13&id=1267&default_ep=0
        try {
            Document document = Jsoup.connect(anime.url.toString()).get();
            String lastEpisode = (document.selectFirst(".active").attr("ep_end"));
            String animeId = document.selectFirst(".movie_id").attr("value");
            String defaultEpisode= document.selectFirst(".default_ep").attr("value");

            String episodeList = apiUrl + "&ep_end=" + lastEpisode + "&id=" + animeId + "&default_ep=" + defaultEpisode;
            Document episodeLinks = Jsoup.connect(episodeList).get();


            for (Element element : episodeLinks.select("[href]")) {     // todo: this will add the episodes from latest to newest. Is that OK?
                URL link = new URL(baseUrl + element.attr("href"));
                String episodeCode = (element.child(0).select(".name").text());
                anime.addEpisode(episodeCode, link);
            }
        } catch (IOException e) {
            // todo: deal with this
        }
    }

    public static String fetchCdnLink(String episodeId, String server) {
        try {
            Document document = Jsoup.connect(episodeId).get();
            //System.out.println(document.selectFirst(".anime").parent());
            Elements links = document.selectFirst(".anime").parent().children();
            for (Element i : links) {
                String cdnLink = i.child(0).attr("data-video");
                if (i.child(0).text().contains(server)) {
                    return cdnLink;
                }
            }
        } catch (IOException e) {
            // todo: deal with this
        }
        return "No link found";
    }

}
