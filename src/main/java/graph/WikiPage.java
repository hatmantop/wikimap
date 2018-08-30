package graph;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class WikiPage {
    public static final String WIKIPEDIA_ENGLISH_DOC_URL = "https://en.wikipedia.org/wiki/";

    String topicDisplay;
    String url;
    Document doc;
    File diskLoc;

    public WikiPage(String url, boolean fullUrl) {
        if(fullUrl) {
            this.url = url;
        } else {
            this.url = WIKIPEDIA_ENGLISH_DOC_URL + url;
        }

    }

    public Set<WikiPage> getInternalLinks() {
        if(doc == null) {
            try {
                doc = Jsoup.connect(this.url).get();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex.getMessage());
            }
        }
        Document page = doc;
        this.topicDisplay = page.getElementById("firstHeading").html();
        Element content = page.getElementById("content");
        Elements elements = content.getElementsByTag("a");
        return elements.stream()
                .map(x -> x.attr("href"))
                .filter(x -> x.startsWith("/wiki/"))
                .filter(x -> !x.startsWith("/wiki/Category:"))
                .filter(x -> !x.startsWith("/wiki/Wikipedia:"))
                .filter(x -> !x.startsWith("/wiki/Template:"))
                .filter(x -> !x.startsWith("/wiki/File:"))
                .filter(x -> !x.startsWith("/wiki/Portal:"))
                .filter(x -> !x.startsWith("/wiki/Help:"))
                .filter(x -> !x.startsWith("/wiki/Special:"))
                .map(x -> x.split("#")[0])
                .map(x -> x.substring(6))
                .map(x -> new WikiPage(x, false))
                .collect(Collectors.toSet());

    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj instanceof WikiPage) {
            return this.url.equals(((WikiPage) obj).url);
        }
        return false;
    }
}
