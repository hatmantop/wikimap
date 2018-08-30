package graph;

import java.io.PrintStream;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class WikiGraph {

    Set<WikiLink> links;
    Set<WikiPage> pages;
    WikiPage start;

    public WikiGraph() {
        links = new HashSet<>();
        pages = new HashSet<>();
    }

    private void addLink(WikiPage from, WikiPage to) {
        links.add(new WikiLink(from, to));
    }

    public void generateFromStart(WikiPage start, int depth) {
        if(depth < 0) {
            throw new IllegalArgumentException("depth must be positive");
        }
        this.start = start;
        pages.add(this.start);
        generate(this.start, depth);
    }

    private void generate(WikiPage current, int depthLeft) {
        if(depthLeft > 0) {
            Set<WikiPage> linkedTo = current.getInternalLinks();
            for(WikiPage page : linkedTo) {
                if(!pages.contains(page)) {
                    pages.add(page);
                    addLink(current, page);
                    generate(page, depthLeft - 1);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Num pages : ");
        builder.append(pages.size());
        return builder.toString();
    }
}
