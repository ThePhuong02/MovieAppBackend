package movieapp.webmovie.repository.custom.implement;

import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.repository.MovieRepository;
import movieapp.webmovie.repository.custom.MovieRepositoryCustom;
import movieapp.webmovie.utils.NumberUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class MovieRepositoryImpl implements  MovieRepositoryCustom {
    static final String DB_URL = "jdbc:mysql://localhost:3306/db_movieapp";
    static final String USER = "root";
    static final String PASSWORD ="Dai121298";

    public void querySqlNomal(Map<String, Object> param, StringBuilder where){
        for(Map.Entry<String, Object> item : param.entrySet()){
            String key = item.getKey();
            String value = item.getValue().toString();
            if(NumberUtil.isNumber(value) == true){
                where.append(" AND m." + key.toLowerCase() + " = " + value);
            }else{
                where.append(" AND m." + key.toLowerCase() + " LIKE '%" + value + "%'");
            }
        }
    }

    public List<Movie> findAll(Map<String, Object> param){
        List<Movie> movies = new ArrayList<>();
        StringBuilder sql = new StringBuilder(" SELECT m.* FROM movies m");
        StringBuilder where = new StringBuilder(" WHERE 1=1");
        querySqlNomal(param, where);
        sql.append(where).append(" GROUP BY m.movieid");

        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql.toString())){

            while(rs.next()){
                Movie movie = new Movie();
                movie.setPoster(rs.getString("poster"));
                movie.setDescription(rs.getString("description"));
                movie.setTitle(rs.getString("title"));
                movie.setMovieID(rs.getLong("movieid"));
                movies.add(movie);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return movies;
    }
}
