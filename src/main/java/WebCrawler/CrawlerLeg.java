package WebCrawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scala.Int;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CrawlerLeg {
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    //private List<String> links = new LinkedList<>();
    private Document htmlDocument;


    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a searchForWord after the successful crawl
     *
     * @param url The URL to visit
     *
     * @return whether or not the crawl was successful
     */
    public boolean crawl(String url) {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            if(connection.response().statusCode() == 200) { // 200 is the HTTP OK status code
                System.out.println("\n**Visiting** Received web page at " + url);
            }
            if(!connection.response().contentType().contains("text/html")) {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
            return true;
        }
        catch(IOException ioe) {
            // We were not successful in our HTTP request
            return false;
        }
    }

    public String getNextPageForSearch(){
        String url = new String();
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return url;
        }

        Elements elementsA = htmlDocument.select("a");
        for(Element elementA: elementsA){
            if(elementA.attr("class").equals("jr-pagenav-next jrButton jrSmall")){
                url = elementA.attr("href");
            }
        }
        return url;
    }

    public List<String> getFilmsPagesToVisit(){
        final String MEGACRITIC_URL = "https://www.megacritic.ru";
        List<String> filmsPagesToVisit = new LinkedList<>();
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return filmsPagesToVisit;
        }

        Elements elementsDiv = htmlDocument.select("div");
        for(Element elementDiv: elementsDiv){
            if(elementDiv.attr("class").equals("jrContentTitle")){
                Elements elementsA = elementDiv.select("a");
                for(Element elementA: elementsA){
                    filmsPagesToVisit.add(MEGACRITIC_URL + elementA.attr("href"));
                }
            }
        }
        return filmsPagesToVisit;
    }


    public String getName(){
        String name = new String();
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return name;
        }

        Elements elementsDiv = htmlDocument.select("div");
        for(Element elementDiv: elementsDiv){
            if(elementDiv.attr("class").equals("contentheading")){
                Elements elementsSpan = elementDiv.select("span");
                for(Element elementSpan: elementsSpan){
                    if(elementSpan.attr("itemprop").equals("name")){
                        name = elementSpan.text();
                    }
                }
            }
        }
        return name;
    }

    public String getDescription(){
        String description = new String();
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return description;
        }

        Elements elementsDiv = htmlDocument.select("div");
        for(Element elementDiv: elementsDiv){
            if(elementDiv.attr("itemprop").equals("description")){
                Elements elementsP = elementDiv.select("P");
                description = "";
                for(Element elementP: elementsP){
                    description = description + elementP.text();
                }
            }
        }
        return description;
    }

    public List<String> getGenres(){
        List<String> genres = new ArrayList<>();
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return genres;
        }

        Elements elementsSpan = htmlDocument.select("span");
        for(Element elementSpan: elementsSpan) {
            if (elementSpan.attr("itemprop").equals("genre")) {
                genres.add(elementSpan.text());
            }
        }
        return genres;
    }

    public String getImg(String imgName){
        String imgUrl = new String();
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return imgUrl;
        }

        String imgNetUrl = "no href found";
        Elements elementsDiv = htmlDocument.select("div");
        for(Element elementDiv: elementsDiv){
            if(elementDiv.attr("class").equals("jrListingMainImage jrMediaLeft")){
                Elements elementsImg = elementDiv.select("a");
                for(Element elementImg: elementsImg){
                    imgNetUrl = elementImg.attr("href");
                }
            }
        }

        try {
            //Open a URL Stream
            Connection.Response resultImageResponse = Jsoup.connect(imgNetUrl).ignoreContentType(true).execute();

            // output here
            FileOutputStream out = (new FileOutputStream(new java.io.File("src/main/resources/FilmsImgs/"
                    + imgName)));
            // resultImageResponse.body() is where the image's contents are.
            out.write(resultImageResponse.bodyAsBytes());
            out.close();
        } catch (IOException e){
            System.out.println("Failed to load IMG");
            e.printStackTrace();
        }
        return imgUrl;
    }

    public double getCriticsScore(){
        double score = 0;
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return score;
        }

        Elements elementsDiv = htmlDocument.select("div");
        for(Element elementDiv: elementsDiv) {
            if (elementDiv.attr("id").equals("jr_authorbox2")) {
                score = Double.parseDouble(elementDiv.text());
            }
        }
        return score;
    }

    public double getUsersScore(){
        double score = 0;
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return score;
        }

        Elements elementsDiv = htmlDocument.select("div");
        for(Element elementDiv: elementsDiv) {
            if (elementDiv.attr("id").equals("userbox2")) {
                score = Double.parseDouble(elementDiv.text());
            }
        }
        return score;
    }

    public List<CommentsWeb> getComments(){
        List<CommentsWeb> comments = new ArrayList<>();
        Integer id, filmId, score, scoreMLT;
        String text, author;

        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return comments;
        }

        Elements elementsDiv = htmlDocument.select("div");
        for(Element elementDiv: elementsDiv) {
            if (elementDiv.attr("class").equals("jrReviewListDetail")) {
                // Begin reviews inner
                Elements elementsRevList = elementDiv.select("div");

                for(Element elementRevList: elementsRevList){
                    if(elementRevList.attr("class").equals("jr-layout-inner jrReviewContainer")){
                        // Begin review inner
                        String authorAndScore = "";
                        author = "";
                        score = -1;
                        text = "";
                        Elements elementsRev = elementRevList.select("div");
                        for(Element elementRev: elementsRev){
                            if(elementRev.attr("style").equals("display:inline;")){
                                Elements elementsB = elementRev.select("b");
                                for(Element elementB: elementsB){
                                    if(!elementB.text().equals("%")){
                                        authorAndScore = authorAndScore + elementB.text() + " ";
                                    }
                                }
                                //System.out.println(authorAndScore);
                                try{
                                    int length = authorAndScore.split(" ").length;
                                    for(int i = 1; i<length - 1; i++){
                                        author = author + authorAndScore.split(" ")[i] + " ";
                                    }
                                    score = Integer.parseInt(authorAndScore.split(" ")[length-1]);
                                } catch (NumberFormatException e){
                                    score = - 1;
                                }
                            }
                            if(score!=-1){
                                if(elementRev.attr("class").equals("jrReviewContent")){
                                    text = elementRev.text();
                                    CommentsWeb comment = new CommentsWeb(author, text, score);
                                    comments.add(comment);
                                }
                            }
                        }
                    }
                }
            }
        }

        return comments;
    }

}
