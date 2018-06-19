package us.xvicario.animiru;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Anime {

    public String title;
    public String description;
    public URL url;
    public Map<String, ArrayList<URL>> episodeMap;
    public String ts;

    public Anime(String title, String description, URL url) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.episodeMap = new HashMap<>();
    }

    public void addEpisode(String episodeCode, URL url) {
        if (!episodeMap.containsKey(episodeCode)) {
            ArrayList<URL> episodeUrls = new ArrayList<>();
            episodeUrls.add(url);
            episodeMap.put(episodeCode, episodeUrls);
        } else {
            episodeMap.get(episodeCode).add(url);
        }
    }

}
