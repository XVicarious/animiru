package us.xvicario.animiru;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;

public class VideoLinkExtractor {

    final static String RAPIDVIDEO_HOST = "playercdn.net";

    public static String extractRapidvideo(String url) {
        // todo: tack ?q={360p,480p,720p,1080p} to get a specific video quality
        // qualities are in a div, it might be hard to specifically select them
        String domain = removeWs(URI.create(url).getHost());
        if (!domain.equalsIgnoreCase(RAPIDVIDEO_HOST)) {
            throw new IllegalArgumentException(domain + " is not a proper RapidVideo url!");
        }
        try {
            Document document = Jsoup.connect(url).get();
            Element element = document.select("video").first();
            return element.attr("src");
        } catch (IOException e) {
            // todo: handle this
        }
        return null;
    }

    // todo: make sure it has a www before you fucking use this you dolt
    private static String removeWs(String host) {
        return host.substring(host.indexOf('.') + 1);
    }

}
