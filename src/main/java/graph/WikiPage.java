package graph;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class WikiPage {
    public static final String WIKIPEDIA_ENGLISH_DOC_URL = "https://en.wikipedia.org/wiki/";

    String topicDisplay;
    String url;
    Set<String> lnks;

    public WikiPage(String url, boolean fullUrl) {
        if(fullUrl) {
            this.url = url;
        } else {
            this.url = WIKIPEDIA_ENGLISH_DOC_URL + url;
        }
        resolve();
    }

    public static String makeurl(String str) {
        return WIKIPEDIA_ENGLISH_DOC_URL + str;
    }

    public WikiPage(String topicDisplay, String url) {
        this.topicDisplay = topicDisplay;
        this.url = url;
    }

    public Set<String> lnks() {
        return this.lnks;
    }

    private void resolve() {
            try {
                Document page = Jsoup.connect(this.url).get();
                System.err.printf("Debug: got page for %s\n", this.url);
                this.topicDisplay = page.getElementById("firstHeading").html();
                Element content = page.getElementById("content");
                Elements elements = content.getElementsByTag("a");
                this.lnks = elements.stream()
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
                        .collect(Collectors.toSet());
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex.getMessage());
            }
    }

    public void write(PrintStream out){
        out.println(topicDisplay);
        out.println(url);
    }

    public static WikiPage read(Scanner in) {
        String tD = in.nextLine();
        String u = in.nextLine();
        return new WikiPage(tD, u);
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
