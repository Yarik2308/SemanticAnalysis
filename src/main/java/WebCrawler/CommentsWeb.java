package WebCrawler;

public class CommentsWeb {
    private Integer id, filmId, score, scoreMLT;
    private String text;

    CommentsWeb(int film_id, String text, int score){
        this.filmId = film_id;
        this.text = text;
        this.score = score;
    }

    public void setScoreMLT(Integer scoreMLT) {
        this.scoreMLT = scoreMLT;
    }

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

    public Integer getScore() {
        return score;
    }

    public Integer getScoreMLT() {
        return scoreMLT;
    }
}
