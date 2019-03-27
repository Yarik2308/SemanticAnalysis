package WebCrawler;

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
                7, 8);
        film.setImgLink("test_img_link");
        List<String> genres = new ArrayList<>();
        genres.add("test_genres_1");
        genres.add("test_genres_2");
        film.setGenres(genres);

        Mappers map = new Mappers();
        map.addFilm(film);

        CommentsWeb comment1 = new CommentsWeb("test_author_1",
                "test_comment_text_1", 7);
        comment1.setFilmId(film.getId());
        comment1.setScoreMLT(3);
        map.addComment(comment1);

        CommentsWeb comment2 = new CommentsWeb( "test_author_2",
                "test_comment_text_2", 2);
        comment2.setFilmId(film.getId());
        comment2.setScoreMLT(1);
        map.addComment(comment2);

        System.out.println("Comment1 ID: " + comment1.getId());
        System.out.println("Comment2 ID: " + comment2.getId());

        FilmsWeb filmWithCommentsAndGenres = map.getFilm(film.getId());
        for(String genre: filmWithCommentsAndGenres.getGenres()){
            System.out.println(genre);
        }
        for(CommentsWeb c: filmWithCommentsAndGenres.getComments()){
            System.out.println("Comment: " + c.getId() + " " + c.getFilmId() + " " + c.getText());
        }
    }

    Mappers(){
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
                    "name varchar(100) NOT NULL, description varchar(4096), viewers_score NUMERIC," +
                    "critics_score NUMERIC, img_link varchar(124));");
            // Create comments table
            statement.execute("CREATE TABLE IF NOT EXISTS comments(comment_id SERIAL NOT NULL PRIMARY KEY," +
                    "film_id SERIAL REFERENCES films(film_id), author varchar(64), text varchar(8192) NOT NULL, " +
                    "score NUMERIC NOT NULL, score_mlt NUMERIC);");

            // Create genres table
            statement.execute("CREATE TABLE IF NOT EXISTS genres(film_id SERIAL REFERENCES films(film_id)," +
                    "genre varchar(32));");
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
                CommentsWeb comment = new CommentsWeb( rs.getString("author"),
                        rs.getString("text"),
                        rs.getInt("score"));
                comment.setFilmId(rs.getInt("film_id"));
                comment.setId(rs.getInt("comment_id"));
                comment.setScoreMLT(rs.getInt("score_mlt"));
                comments.add(comment);
            }

            st.close();
        } catch (SQLException e){
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
        return comments;
    }


    public int addGenre(int film_id, String genre){
        int key = 0;
        try{
            PreparedStatement st = connection.prepareStatement("INSERT INTO genres(film_id, genre)" +
                    "VALUES (?, ?);");
            //set the value
            st.setInt(1, film_id);
            st.setString(2, genre);

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating genre failed, no rows affected.");
            }

            st.close();
        } catch (SQLException e){
            System.out.println("Connection Failed");
            e.printStackTrace();
        }

        return key;
    }

    public List<String> getGenres(int filmId){
        List<String> genres = new ArrayList<>();
        try{
            PreparedStatement st = connection.prepareStatement("SELECT * FROM genres WHERE film_id = ?;");
            st.setInt(1, filmId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                genres.add(rs.getString("genre"));
            }
        } catch (SQLException e){
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
        return genres;
    }


    public int addFilm(FilmsWeb film){
        List<String> genres = film.getGenres();
        if(genres.isEmpty()) {
            System.out.println("No genres in film!");
            return 0;
        }

        List<CommentsWeb> comments = film.getComments();
        if(comments.isEmpty()) {
            System.out.println("No comments in film!");
            return 0;
        }

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
                    System.out.println("Film ID: " + film.getId());
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

        for(String genre: genres){
            addGenre(film.getId(), genre);
        }
        for(CommentsWeb comment: comments){
            comment.setFilmId(film.getId());
            addComment(comment);
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
                        rs.getInt("critics_score"));

                film.setImgLink(rs.getString("img_link"));
                film.setId(rs.getInt("film_id"));
                //return film;
            }

            st.close();
        } catch (SQLException e){
            System.out.println("Connection Failed");
            e.printStackTrace();
        }

        film.setGenres(getGenres(filmId));
        film.setComments(getComments(filmId));
        return film;
    }
}
