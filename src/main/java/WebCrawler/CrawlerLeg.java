package WebCrawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CrawlerLeg {
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<>();
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
            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage) {
                this.links.add(link.absUrl("href"));
            }
            return true;
        }
        catch(IOException ioe) {
            // We were not successful in our HTTP request
            return false;
        }
    }


    /**
     * Performs a search on the body of on the HTML document that is retrieved. This method should
     * only be called after a successful crawl.
     *
     * @param searchWord The word or string to look for
     *
     * @return whether or not the word was found
     */
    public boolean searchForWord(String searchWord) {
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }
        System.out.println("Searching for the word " + searchWord + "...");
        String bodyText = this.htmlDocument.body().text();
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
    }

    public String getName(){
        String name = new String();
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return name;
        }
        System.out.println("Searching for the name of film...");

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
        System.out.println("Searching for the description of film...");

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
        System.out.println(description);
        return description;
    }

    public List<String> getGenres(){
        List<String> genres = new ArrayList<>();
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return genres;
        }
        System.out.println("Searching for the genres of film...");

        Elements elementsSpan = htmlDocument.select("span");
        for(Element elementSpan: elementsSpan) {
            if (elementSpan.attr("itemprop").equals("genre")) {
                genres.add(elementSpan.text());
            }
        }
        System.out.println(genres);
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
        System.out.println(imgNetUrl);
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

    public List<String> getLinks() {
        return this.links;
    }

}
