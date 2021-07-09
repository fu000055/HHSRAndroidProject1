package algonquin.cst2335.hhsrandroidproject1.MovieIfo;

import java.util.ArrayList;

/**
 * This class is used to store movie details and movie names into a single object
 */
public class Movie {
    /**
     * Class Variables
     */
    private String MovieName;
    private String ActorName;
    private ArrayList<String>filmsInMovie;
    private int idMovie;
    private int year;
    private int idMo;
    private String movieDis;
    private String genre;

    /**
     * Constructor
     * @param MovieName
     * @param ActorName
     * @param year
     * @param genre
     * @param idMovie
     * @param movieDis
     */
    public Movie(String MovieName,String ActorName,int year,String genre,int idMovie,String movieDis){
        this.MovieName = MovieName;
        this.ActorName = ActorName;
        this.year = year;
        this.genre = genre;
        this.idMovie = idMovie;
        this.movieDis = movieDis;
    }

    /**
     * Constructor with arraylist of movies added
     * @param MovieName
     * @param ActorName
     * @param year
     * @param genre
     * @param idMovie
     * @param movieDis
     * @param fiLS
     */
    public Movie(String MovieName,String ActorName,int year,String genre,int idMovie,String movieDis,ArrayList<String> fiLS){
        this.MovieName = MovieName;
        this.ActorName = ActorName;
        this.year = year;
        this.genre = genre;
        this.idMovie = idMovie;
        this.movieDis = movieDis;
        this.filmsInMovie = fiLS;
    }

    /**
     * Constructor without movie description given
     * @param MovieName
     * @param ActorName
     * @param year
     * @param genre
     * @param idMovie
     */
    public Movie(String MovieName,String ActorName,int year,String genre,int idMovie){
        this.MovieName = MovieName;
        this.ActorName = ActorName;
        this.year = year;
        this.genre = genre;
        this.idMovie = idMovie;
        this.movieDis = "";
    }

    /**
     * Setters and getters for object variables
     * @return parameters
     */
    public String getMovieName(){
        return  MovieName;
    }

    public String getGenre(){
        return genre;
    }

    public  int getYear(){
        return year;
    }

    public int getIdMovie(){
        return idMovie;
    }

    public String getActorName(){
        return ActorName;
    }

    public String getMovieDis(){
        return movieDis;
    }

    public ArrayList<String>getFilmsInMovie(){
        return filmsInMovie;
    }

    public void setFilmsInMovie(ArrayList<String>filmsInMovie){
        this.filmsInMovie = filmsInMovie;
    }
}
