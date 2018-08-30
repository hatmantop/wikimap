package graph;

public class WikiLink {
    WikiPage from;
    WikiPage to;

    public WikiLink(WikiPage from, WikiPage to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(obj instanceof WikiLink) {
            return this.from.equals(((WikiLink) obj).from) && this.to.equals(((WikiLink) obj).to);
        }
        return false;
    }
}
