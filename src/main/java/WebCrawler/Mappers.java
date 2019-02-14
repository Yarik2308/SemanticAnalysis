package WebCrawler;

import sun.plugin2.main.client.DisconnectedExecutionContext;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.List;

public class Mappers {
    //  Database credentials
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/semanticanalysis_db";
    private static final String USER = "author";
    private static final String PASS = "author123";
    private static Connection connection = null;

    public static void main(String args[]) throws SQLException {
        FilmsWeb film = new FilmsWeb("test_name", "test_description", 7, 8,
                "test_link");
        Mappers map = new Mappers();
        map.addFilm(film);
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
                    "name varchar(50) NOT NULL, description varchar(2048), viewers_score NUMERIC," +
                    "critics_score NUMERIC, img_link varchar(124));");
            // // Create comments table
            statement.execute("CREATE TABLE IF NOT EXISTS comments(comment_id SERIAL NOT NULL PRIMARY KEY," +
                    "film_id SERIAL REFERENCES films(film_id), text varchar(2048) NOT NULL, score NUMERIC NOT NULL, " +
                    "score_mlt varchar(10));");

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

    public boolean addComent(CommentsWeb comment){
        try {
            PreparedStatement st = connection.prepareStatement("INSERT INTO comments(film_id, text, score, " +
                    "score_mlt) VALUES (?, ?, ?, ?)");

            st.setInt(1, comment.getFilmId());
            st.setString(2, comment.getText());
            st.setInt(3, comment.getScore());
            st.setDouble(4, comment.getScoreMLT());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

//    public List<CommentsWeb> getComments(int filmId){
//        Statement st = connection.createStatement();
//        st.execute("");
//    }



    public boolean addFilm(FilmsWeb film){
        try{
            PreparedStatement st = connection.prepareStatement("INSERT INTO films(name, description, " +
                    "viewers_score, critics_score, img_link) VALUES  (?, ?, ?, ?, ?)");
            st.setString(1, film.getName());
            st.setString(2, film.getDescription());
            st.setDouble(3, film.getViewersScore());
            st.setDouble(4, film.getCriticsScore());
            st.setString(5, film.getImgLink());
            st.execute();
            st.close();
        } catch (SQLException e){
            System.out.println("Connection Failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }


//    public FilmsWeb getFilm(int filmId){
//        Statement st = connection.createStatement();
//        st.execute("");
//    }
}
