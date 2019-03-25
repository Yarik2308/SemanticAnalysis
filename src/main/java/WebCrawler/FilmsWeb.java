package WebCrawler;

import java.util.ArrayList;
import java.util.List;

public class FilmsWeb {
    Integer id;
    String name, description, imgLink;
    double viewersScore, criticsScore;
    List<CommentsWeb> comments;
    List<String> genres;

    FilmsWeb(){ this.id = 0; }

    FilmsWeb(String name, String description, double viewersScore, double criticsScore, String imgLink){
        this.name = name;
        this.description = description;
        this.viewersScore = viewersScore;
        this.criticsScore = criticsScore;
        this.imgLink = imgLink;
        this.comments = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    public void setId(int Id){ this.id = Id; }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getViewersScore() {
        return viewersScore;
    }

    public double getCriticsScore() {
        return criticsScore;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setComments(List<CommentsWeb> comments) {
        this.comments = comments;
    }

    public void addComment(CommentsWeb comment){
        this.comments.add(comment);
    }

    public List<CommentsWeb> getComments() {
        return comments;
    }

    public void setGenres(List<String> genres) { this.genres = genres; }

    public List<String> getGenres(){ return genres; }
}
