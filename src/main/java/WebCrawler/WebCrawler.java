package WebCrawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WebCrawler {
    // Fields
    private static final int MAX_PAGES_TO_SEARCH = 10;
    private Set<String> pagesVisited = new HashSet<>(); // Set - unique entries
    private List<String> pagesToVisit = new LinkedList<>();

    public static void main(String args[]){
        //WebCrawler crawler = new WebCrawler();
        //crawler.search("https://www.megacritic.ru/film/kapitan-marvel", "Капитан");
        CrawlerLeg leg = new CrawlerLeg();
        leg.crawl("https://www.megacritic.ru/film/kapitan-marvel");
        String name = leg.getName();
        String description = leg.getDescription();
        List<String> genres = leg.getGenres();
        String imgUrl = leg.getImg(name);
        Double criticsScore = leg.getCriticsScore();
        Double usersScore = leg.getUsersScore();
        List<CommentsWeb> comments = leg.getComments();

    }

    private String nextPage(){
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove(0);
        } while(this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }

    public void search(String url, String searchWord){
        while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
        {
            String currentUrl;
            CrawlerLeg leg = new CrawlerLeg();
            if(this.pagesToVisit.isEmpty())
            {
                currentUrl = url;
                this.pagesVisited.add(url);
            }
            else
            {
                currentUrl = this.nextPage();
            }
            leg.crawl(currentUrl);
            boolean success = leg.searchForWord(searchWord);
            if(success)
            {
                System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
                break;
            }
            this.pagesToVisit.addAll(leg.getLinks());
        }
        System.out.println(String.format("**Done** Visited %s web page(s)", this.pagesVisited.size()));
    }
}
