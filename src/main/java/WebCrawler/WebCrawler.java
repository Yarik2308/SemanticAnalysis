package WebCrawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WebCrawler {
    // Fields
    private static final int MAX_FILMS_TO_SEARCH = 1000;
    private String nextPageForSearch; // next Page where to find films links
    private List<String> filmsPagesToVisit = new LinkedList<>(); // List of links with unknown films
    private Set<String> filmsPagesVisited = new HashSet<>(); // Set - unique entries, Links with searched films

    public static void main(String args[]){
        //WebCrawler crawler = new WebCrawler();
        //crawler.search("https://www.megacritic.ru/film/kapitan-marvel", "Капитан");
        CrawlerLeg leg = new CrawlerLeg();
//        leg.crawl("https://www.megacritic.ru/film/kapitan-marvel");
//        String name = leg.getName();
//        String description = leg.getDescription();
//        List<String> genres = leg.getGenres();
//        String imgUrl = leg.getImg(name);
//        Double criticsScore = leg.getCriticsScore();
//        Double usersScore = leg.getUsersScore();
//        List<CommentsWeb> comments = leg.getComments();
        leg.crawl("https://www.megacritic.ru/novye/filmy");
        List<String> filmsPages = leg.getFilmsPagesToVisit();
        String nextPage = leg.getNextPageForSearch();
        System.out.println(filmsPages);
        System.out.println(nextPage);

    }

    private String nextFilmPage(){
        String nextUrl;
        do {
            nextUrl = this.filmsPagesToVisit.remove(0);
        } while(this.filmsPagesVisited.contains(nextUrl));
        this.filmsPagesVisited.add(nextUrl);
        return nextUrl;
    }

    public void search(String urlForSearch){
        while(this.filmsPagesVisited.size() < MAX_FILMS_TO_SEARCH)
        {
            CrawlerLeg leg = new CrawlerLeg();
            if(filmsPagesToVisit.isEmpty()){
                leg.crawl(urlForSearch);
            }
            String currentFilmUrl;


            //leg.crawl(currentFilmUrl);


        }
        System.out.println(String.format("**Done** Visited %s web page(s)", this.filmsPagesVisited.size()));
    }
}
