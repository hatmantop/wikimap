package graph;

import java.io.PrintStream;
import java.time.Duration;
import java.util.HashSet;
import java.util.Scanner;
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

    private void addPage(WikiPage pg) {
        if(pg == null) {
            throw new IllegalArgumentException("null page");
        }
        pages.add(pg);
    }

    private WikiPage getPage(String url) {
        for(WikiPage pg : pages) {
            if(pg.url.equals(url)) {
                return pg;
            }
        }
        return null;
    }

    public void generateFromStart(WikiPage start) {
        this.start = start;
        pages.add(this.start);
        generate(this.start);
    }

    private void generate(WikiPage current) {
        Set<String> linkedTo = current.lnks();
        for(String pagenm : linkedTo) {
            WikiPage page = new WikiPage(pagenm, false);
            if(!pages.contains(page)) {
                pages.add(page);
                addLink(current, page);
                generate(page);
            }
        }
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
            Set<String> linkedTo = current.lnks();
            for(String pageurl : linkedTo) {
                String url = WikiPage.makeurl(pageurl);
                WikiPage pg = getPage(url);
                if(pg == null) {
                    pg = new WikiPage(url, true);
                }
                pages.add(pg);
                addLink(current, pg);
                generate(pg, depthLeft - 1);
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

    public void write(PrintStream out){
        out.println(pages.size());
        for(WikiPage p : pages) {
            p.write(out);
        }
        out.println(links.size());
        for(WikiLink l : links) {
            l.write(out);
        }
    }

    public static WikiGraph read(Scanner in) {
        WikiGraph graph = new WikiGraph();
        int npages = Integer.parseInt(in.nextLine());
        for(int i = 0 ; i < npages; i++) {
            graph.addPage(WikiPage.read(in));
        }
        int nlinks = Integer.parseInt(in.nextLine());
        for(int i = 0; i < nlinks; i++) {
            String from = in.nextLine();
            WikiPage pgFrom = graph.getPage(from);
            String to = in.nextLine();
            WikiPage pgTo = graph.getPage(to);

            graph.addLink(pgFrom, pgTo);
        }
        return graph;
    }
}
