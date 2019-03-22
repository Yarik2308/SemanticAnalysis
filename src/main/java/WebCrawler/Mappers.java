package WebCrawler;

import SemanticAnalysis.Comment;
import sun.plugin2.main.client.DisconnectedExecutionContext;
import weka.classifiers.evaluation.output.prediction.Null;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Mappers {
    //  Database credentials
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/semanticanalysis_db";
    private static final String USER = "author";
    private static final String PASS = "author123";
    private static Connection connection = null;

    public static void main(String args[]) throws SQLException {
        FilmsWeb film = new FilmsWeb("test_film_name", "test_film_description",
                7, 8, "test_link");
        Mappers map = new Mappers();
        int filmId = map.addFilm(film);
        if(filmId == film.getId()){
            System.out.println("Film have its id");
        }

        CommentsWeb comment = new CommentsWeb(filmId, "test_author",
                "test_comment_text", 7);
        comment.setScoreMLT(3);
        int commentId = map.addComment(comment);
        if(commentId == comment.getId()){
            System.out.println("Comment have Id");
        }
        System.out.println("Comment ID: " + commentId);

        List<CommentsWeb> comments = new ArrayList<>();
        comments = map.getComments(filmId);
        for(CommentsWeb c: comments){
            System.out.println("Comment: " + c.getId() + " " + c.getFilmId() + " " + c.getText());
        }
    }

    Mappers() throws SQLException{
        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();
            // Create films table
            statement.execute("CREATE TABLE IF NOT EXISTS films(film_id SERIAL NOT NULL PRIMARY KEY," +
                    "name varchar(50) NOT NULL, description varchar(4096), viewers_score NUMERIC," +
                    "critics_score NUMERIC, img_link varchar(124));");
            // // Create comments table
            statement.execute("CREATE TABLE IF NOT EXISTS comments(comment_id SERIAL NOT NULL PRIMARY KEY," +
                    "film_id SERIAL REFERENCES films(film_id), author varchar(32), text varchar(2048) NOT NULL, " +
                    "score NUMERIC NOT NULL, score_mlt NUMERIC);");

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }

    }

    public int addComment(CommentsWeb comment){
        try {
            PreparedStatement st = connection.prepareStatement("INSERT INTO comments(film_id, author, " +
                    "text, score, score_mlt) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, comment.getFilmId());
            st.setString(2, comment.getAuthor());
            st.setString(3, comment.getText());
            st.setInt(4, comment.getScore());
            st.setDouble(5, comment.getScoreMLT());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating comment failed, no rows affected.");
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comment.setId(generatedKeys.getInt(1));
                    System.out.println("Film id: " + comment.getId());
                }
                else {
                    throw new SQLException("Creating comment failed, no ID obtained.");
                }
            }
            st.close();

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return 0;
        }
        return comment.getId();
    }

    public List<CommentsWeb> getComments(int filmId){
        List<CommentsWeb> comments = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(
                    "SELECT * FROM comments WHERE film_id = ?");

            // set the value
            st.setInt(1, filmId);
            ResultSet rs = st.executeQuery();

            // loop through the result set
            while (rs.next()) {
                CommentsWeb comment = new CommentsWeb(rs.getInt("film_id"),
                        rs.getString("author"), rs.getString("text"),
                        rs.getInt("score"));
                comment.setId(rs.getInt("comment_id"));
                comment.setScoreMLT(rs.getInt("score_mlt"));
                comments.add(comment);
            }

        } catch (SQLException e){
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
        return comments;
    }



    public int addFilm(FilmsWeb film){
        try{
            PreparedStatement st = connection.prepareStatement("INSERT INTO films(name, description, " +
                    "viewers_score, critics_score, img_link) VALUES  (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, film.getName());
            st.setString(2, film.getDescription());
            st.setDouble(3, film.getViewersScore());
            st.setDouble(4, film.getCriticsScore());
            st.setString(5, film.getImgLink());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating film failed, no rows affected.");
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    film.setId(generatedKeys.getInt(1));
                    System.out.println("Film id: " + film.getId());
                }
                else {
                    throw new SQLException("Creating film failed, no ID obtained.");
                }
            }

            st.close();
        } catch (SQLException e){
            System.out.println("Connection Failed");
            e.printStackTrace();
            return 0;
        }
        return film.getId();
    }


    public FilmsWeb getFilm(int filmId){
        FilmsWeb film = new FilmsWeb();
        try {
            PreparedStatement st = connection.prepareStatement(
                    "SELECT * FROM films WHERE film_id = ?");

            // set the value
            st.setInt(1, filmId);
            ResultSet rs = st.executeQuery();

            // loop through the result set
            while (rs.next()) {
                film = new FilmsWeb(rs.getString("name"),
                        rs.getString("description"), rs.getInt("viewers_score"),
                        rs.getInt("critics_score"), rs.getString("img_link"));

                film.setId(rs.getInt("film_id"));
                return film;
            }

        } catch (SQLException e){
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
        return film;
    }
}
