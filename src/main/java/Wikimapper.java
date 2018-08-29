import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintStream;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class Wikimapper {

    public static final String WIKI_PREFIX = "https://en.wikipedia.org/wiki/";

    public static Set<String> getInternalLinks(String pageUrl) {
        try {
            Document page = Jsoup.connect(pageUrl).get();
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
                    .collect(Collectors.toSet());
        } catch (HttpStatusException ex) {
            return new HashSet<>();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        assert args.length == 2 : "Bad args:\n<Wiki page root> <depth>";
        printDegrees(args[0], Integer.parseInt(args[1]), System.out, false);

    }

    public static void printDegrees(String root, int depth, PrintStream output, boolean withSeen) {
        if(depth < 0) {
            throw new IllegalArgumentException("depth must be positive");
        }
        HashSet<String> visitedSet = new HashSet<>();
        long start = System.currentTimeMillis();
        printDegrees(root, visitedSet, 0, depth, output, withSeen);
        long durationMills = System.currentTimeMillis() - start;
        Duration duration = Duration.ofMillis(durationMills);
        output.println("Pages visited : " + visitedSet.size());
        output.println("Duration : " + duration.toString());
        output.println("Has Kevin Bacon? " + (visitedSet.contains("Kevin_Bacon") ? "yes" : "no"));
    }

    private static void printDegrees(String internalUrl, Set<String> visited, int depth, int maxDepth, PrintStream output, boolean withSeen) {
        if(depth <= maxDepth) {
            if(visited.contains(internalUrl)) {
                if(withSeen) {
                    for(int i = 0; i < depth; i++)
                        output.print(" ");
                    output.println(internalUrl + " (seen)");
                } else {
                    return;
                }
            } else {
                visited.add(internalUrl);
                for(int i = 0; i < depth; i++)
                    output.print(" ");
                output.println(internalUrl);
            }
            if(depth != maxDepth) {
                Set<String> internals = getInternalLinks(WIKI_PREFIX + internalUrl);
                internals.forEach(x -> printDegrees(x, visited, depth + 1, maxDepth, output, withSeen));
            }
        }
    }

}
