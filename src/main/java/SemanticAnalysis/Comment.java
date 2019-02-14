package SemanticAnalysis;

public class Comment {
    private Integer score, content_id, element_id, user_id;
    private String text;

    Comment(int score, int content_id, int element_id, int user_id, String text){
        this.score = score;
        this.content_id = content_id;
        this.element_id = element_id;
        this.user_id = user_id;
        this.text = text;
    }

    public Integer getScore(){ return  score; }

    public Integer getContent_id(){ return content_id; }

    public Integer getElement_id(){ return element_id; }

    public Integer getUser_id(){ return user_id; }

    public String getText(){ return text; }

    public void setText(String text) {
        this.text = text;
    }
}
