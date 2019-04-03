package WebCrawler;

import WekaAndStem.ScoreGetter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WebCrawler {
    // Fields
    private static final int MAX_FILMS_TO_SEARCH = 1000;
    private List<String> filmsPagesToVisit = new LinkedList<>(); // List of links with unknown films
    private Set<String> filmsPagesVisited = new HashSet<>(); // Set - unique entries, Links with searched films

    public static void main(String args[]){
        WebCrawler crawler = new WebCrawler();
        try {
            crawler.search("https://www.megacritic.ru/novye/filmy");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String nextFilmPage(){
        String nextUrl;
        do {
            nextUrl = this.filmsPagesToVisit.remove(0);
        } while(this.filmsPagesVisited.contains(nextUrl));
        this.filmsPagesVisited.add(nextUrl);
        return nextUrl;
    }

    public void search(String urlForSearch) throws Exception{
        CrawlerLeg leg = new CrawlerLeg();
        Mappers mappers = new Mappers();
        ScoreGetter getter = new ScoreGetter();

        while(this.filmsPagesVisited.size() < MAX_FILMS_TO_SEARCH) {
            if(filmsPagesToVisit.isEmpty()){
                leg.crawl(urlForSearch);
                filmsPagesToVisit = leg.getFilmsPagesToVisit();
                urlForSearch = leg.getNextPageForSearch();
            }

            String currentFilmUrl = nextFilmPage();
            leg.crawl(currentFilmUrl);
            String filmName = leg.getName();
            FilmsWeb film = new FilmsWeb(filmName, leg.getDescription(), leg.getUsersScore(),
                    leg.getCriticsScore());
            leg.getImg(filmName);
            film.setImgLink(filmName);
            film.setGenres(leg.getGenres());

            List<CommentsWeb> comments = leg.getComments();
            for(CommentsWeb comment: comments){
                String result = getter.predict(comment.getText());
                if(result == "good"){
                    comment.setScoreMLT(3);
                } else if(result == "normal") {
                    comment.setScoreMLT(2);
                } else{
                    comment.setScoreMLT(1);
                }
            }

            film.setComments(comments);

            mappers.addFilm(film);
        }
        System.out.println(String.format("**Done** Visited %s web page(s)", this.filmsPagesVisited.size()));
    }
}
