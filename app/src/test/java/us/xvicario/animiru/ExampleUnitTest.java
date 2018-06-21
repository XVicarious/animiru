package us.xvicario.animiru;

import org.junit.Test;
import org.junit.runners.JUnit4;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_search() {
        ArrayList list = (ArrayList) NineAnime.searchAnime("kill");
        assertFalse(list.isEmpty());
    }

    @Test
    public void test_get_anime() {
        final String hsdxdhero = "https://www6.9anime.is/watch/high-school-dxd-hero.9n3n/3k38vy";
        try {
            Anime anime = new Anime("High School DxD Hero", "description", new URL(hsdxdhero));
            NineAnime.fetchAnime(anime);
            String code = (String) anime.episodeMap.keySet().toArray()[0];
            int paramKey = NineAnime.generateParamKey(code, "33", Integer.parseInt(anime.ts));
            String url = NineAnime.fetchCdnLink(code, "33", paramKey, Integer.parseInt(anime.ts));
            System.out.println(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}