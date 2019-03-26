package WebCrawler;

import SemanticAnalysis.Comment;

public class CommentsWeb {
    private Integer id, filmId, score, scoreMLT;
    private String text, author;

    CommentsWeb(String author, String text, int score){
        this.author = author;
        this.text = text;
        this.score = score;
    }

    public void setScoreMLT(Integer scoreMLT) {
        this.scoreMLT = scoreMLT;
    }

    public String getAuthor(){ return author; }

    public String getText() {
        return text;
    }

    public void setId(Integer id) { this.id = id; }

    public Integer getId() {
        return id;
    }

    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public Integer getScore() {
        return score;
    }

    public Integer getScoreMLT() {
        return scoreMLT;
    }
}
