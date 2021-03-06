import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.FileInputStream;

/**
 * Runs queries against a back-end database
 */
public class Query {
    private static Properties configProps = new Properties();

    private static String imdbUrl;
    private static String customerUrl;

    private static String postgreSQLDriver;
    private static String postgreSQLUser;
    private static String postgreSQLPassword;

    // DB Connection
    private Connection _imdb;
    private Connection _customer_db;

    // Canned queries

    private String _search_sql = "SELECT * FROM movie WHERE lower(name) like lower(?) ORDER BY id";
    private PreparedStatement _search_statement;

    private String _director_mid_sql = "SELECT y.* "
                     + "FROM movie_directors x, directors y "
                     + "WHERE x.mid = ? and x.did = y.id";
    private PreparedStatement _director_mid_statement;


    private String _actor_pid_sql = "SELECT y.* "
                     + "FROM casts x, actor y "
                     + "WHERE x.mid = ? and x.pid = y.id";
    private PreparedStatement _actor_pid_statement;

    /* uncomment, and edit, after your create your own customer database */
    
    private String _customer_login_sql = "SELECT * FROM customer WHERE login = ? and password = ?";
    private PreparedStatement _customer_login_statement;

    private String _begin_transaction_read_write_sql = "BEGIN TRANSACTION READ WRITE";
    private PreparedStatement _begin_transaction_read_write_statement;

    private String _commit_transaction_sql = "COMMIT TRANSACTION";
    private PreparedStatement _commit_transaction_statement;

    private String _rollback_transaction_sql = "ROLLBACK TRANSACTION";
    private PreparedStatement _rollback_transaction_statement;
     
    private String _movie_rental_sql = "SELECT customer_id FROM ActiveRental WHERE movie_id = ?";
    private PreparedStatement _movie_rental_statement;


    //(a) movies, (b) movies join directors, (c) movies join actors

    private String _a_movie_sql = "SELECT * FROM Movie WHERE lower(name) like lower(?) ORDER BY id";
    private PreparedStatement _a_movie_statement;
    private String _b_movie_directors_sql = "SELECT m.id, d.fname, d.lname FROM movie as m join movie_directors as md on m.id = md.mid join directors as d on md.did = d.id WHERE lower(m.name) like lower(?) ORDER BY m.id";
    private PreparedStatement _b_movie_directors_statement;
    private String _c_movie_actor_sql = "SELECT c.mid, a.fname, a.lname FROM movie as m join casts as c on m.id = c.mid join actor as a on c.pid = a.id WHERE lower(m.name) like lower(?) ORDER BY c.mid";
    private PreparedStatement _c_movie_actor_statement;

    public Query() {
    }

    /**********************************************************/
    /* Connections to postgres databases */

    public void openConnection() throws Exception {
        configProps.load(new FileInputStream("dbconn.config"));
        
        
        imdbUrl        = configProps.getProperty("imdbUrl");
        customerUrl    = configProps.getProperty("customerUrl");
        postgreSQLDriver   = configProps.getProperty("postgreSQLDriver");
        postgreSQLUser     = configProps.getProperty("postgreSQLUser");
        postgreSQLPassword = configProps.getProperty("postgreSQLPassword");


        /* load jdbc drivers */
        Class.forName(postgreSQLDriver).newInstance();

        /* open connections to TWO databases: imdb and the customer database */
        _imdb = DriverManager.getConnection(imdbUrl, // database
                postgreSQLUser, // user
                postgreSQLPassword); // password

        _customer_db = DriverManager.getConnection(customerUrl, // database
                postgreSQLUser, // user
                postgreSQLPassword); // password
    }

    public void closeConnection() throws Exception {
        _imdb.close();
        _customer_db.close();
    }

    /**********************************************************/
    /* prepare all the SQL statements in this method.
      "preparing" a statement is almost like compiling it.  Note
       that the parameters (with ?) are still not filled in */

    public void prepareStatements() throws Exception {

        _search_statement = _imdb.prepareStatement(_search_sql);
        _director_mid_statement = _imdb.prepareStatement(_director_mid_sql);
        _actor_pid_statement = _imdb.prepareStatement(_actor_pid_sql);
        _a_movie_statement = _imdb.prepareStatement(_a_movie_sql);
        _b_movie_directors_statement = _imdb.prepareStatement(_b_movie_directors_sql);
        _c_movie_actor_statement = _imdb.prepareStatement(_c_movie_actor_sql);


        /* uncomment after you create your customers database */
        
        _customer_login_statement = _customer_db.prepareStatement(_customer_login_sql);
        _begin_transaction_read_write_statement = _customer_db.prepareStatement(_begin_transaction_read_write_sql);
        _commit_transaction_statement = _customer_db.prepareStatement(_commit_transaction_sql);
        _rollback_transaction_statement = _customer_db.prepareStatement(_rollback_transaction_sql);

        _movie_rental_statement = _customer_db.prepareStatement(_movie_rental_sql);


        

        /* add here more prepare statements for all the other queries you need */
        /* . . . . . . */
    }


    /**********************************************************/
    /* suggested helper functions  */

    public int helper_compute_remaining_rentals(int cid) throws Exception {
        /* how many movies can she/he still rent ? */
        /* you have to compute and return the difference between the customer's plan
           and the count of oustanding rentals */
        return (99);
    }

    public String helper_compute_customer_name(int cid) throws Exception {
        /* you find  the first + last name of the current customer */
        return ("JoeFirstName" + " " + "JoeLastName");

    }

    public boolean helper_check_plan(int plan_id) throws Exception {
        /* is plan_id a valid plan id ?  you have to figure out */
        return true;
    }

    public boolean helper_check_movie(int mid) throws Exception {
        /* is mid a valid movie id ? you have to figure out  */
        return true;
    }

    private int helper_who_has_this_movie(int mid) throws Exception {
        /* find the customer id (cid) of whoever currently rents the movie mid; return -1 if none */
        return (77);
    }

    /**********************************************************/
    /* login transaction: invoked only once, when the app is started  */
    public int transaction_login(String name, String password) throws Exception {
        /* authenticates the user, and returns the user id, or -1 if authentication fails */

        /* Uncomment after you create your own customers database */
        
        int cid;

        _customer_login_statement.clearParameters();
        _customer_login_statement.setString(1,name);
        _customer_login_statement.setString(2,password);
        ResultSet cid_set = _customer_login_statement.executeQuery();
        if (cid_set.next()) cid = cid_set.getInt(1);
        else cid = -1;
        return(cid);
         
        //return (55);
    }

    public void transaction_personal_data(int cid) throws Exception {
        /* println the customer's personal data: name, and plan number */
    }


    /**********************************************************/
    /* main functions in this project: */

    public void transaction_search(int cid, String movie_title)
            throws Exception {
        /* searches for movies with matching titles: SELECT * FROM movie WHERE name LIKE movie_title */
        /* prints the movies, directors, actors, and the availability status:
           AVAILABLE, or UNAVAILABLE, or YOU CURRENTLY RENT IT */

        /* set the first (and single) '?' parameter */
        _search_statement.clearParameters();
        _search_statement.setString(1, '%' + movie_title + '%');

        ResultSet movie_set = _search_statement.executeQuery();
        while (movie_set.next()) {
            int mid = movie_set.getInt(1);
            System.out.println("ID: " + mid + " NAME: "
                    + movie_set.getString(2) + " YEAR: "
                    + movie_set.getString(3));
            /* do a dependent join with directors */
            _director_mid_statement.clearParameters();
            _director_mid_statement.setInt(1, mid);
            ResultSet director_set = _director_mid_statement.executeQuery();
            while (director_set.next()) {
                System.out.println("\t\tDirector: " + director_set.getString(3)
                        + " " + director_set.getString(2));
            }
            director_set.close();

            /* now you need to retrieve the actors, in the same manner */
            _actor_pid_statement.clearParameters();
            _actor_pid_statement.setInt(1, mid);
            ResultSet actor_set = _actor_pid_statement.executeQuery();
            while (actor_set.next()) {
                System.out.println("\t\tActor: " + actor_set.getString(3)
                        + " " + actor_set.getString(2));
            }
            actor_set.close();

            /* then you have to find the status: of "AVAILABLE" "YOU HAVE IT", "UNAVAILABLE" */
            _movie_rental_statement.clearParameters();
            _movie_rental_statement.setInt(1, mid);
            ResultSet rental_set = _movie_rental_statement.executeQuery();
            int rented_customer_id = 0;
            while (rental_set.next()){
                rented_customer_id = Integer.parseInt(rental_set.getString(1));
            }
            rental_set.close();

            System.out.print("\t\tStatus: ");
            if (rented_customer_id > 0){
                if (rented_customer_id == cid){
                    System.out.println("YOU HAVE IT");
                }else{
                    System.out.println("UNAVAILABLE");
                }
            }else{
                System.out.println("AVAILABLE");
            }

        }
        System.out.println();
    }

    public void transaction_choose_plan(int cid, int pid) throws Exception {
        /* updates the customer's plan to pid: UPDATE customers SET plid = pid */
        /* remember to enforce consistency ! */
    }

    public void transaction_list_plans() throws Exception {
        /* println all available plans: SELECT * FROM plan */
    }
    
    public void transaction_list_user_rentals(int cid) throws Exception {
        /* println all movies rented by the current user*/
    }

    public void transaction_rent(int cid, int mid) throws Exception {
        /* rend the movie mid to the customer cid */
        /* remember to enforce consistency ! */
    }

    public void transaction_return(int cid, int mid) throws Exception {
        /* return the movie mid by the customer cid */
    }

    public void transaction_fast_search(int cid, String movie_title)
            throws Exception {
        /* like transaction_search, but uses joins instead of independent joins
           Needs to run three SQL queries: (a) movies, (b) movies join directors, (c) movies join actors
           Answers are sorted by mid.
           Then merge-joins the three answer sets */

        _a_movie_statement.clearParameters();
        _a_movie_statement.setString(1, '%' + movie_title + '%');
        ResultSet _a_set = _a_movie_statement.executeQuery();
        _b_movie_directors_statement.clearParameters();
        _b_movie_directors_statement.setString(1, '%' + movie_title + '%');
        ResultSet _b_set = _b_movie_directors_statement.executeQuery();
        _c_movie_actor_statement.clearParameters();
        _c_movie_actor_statement.setString(1, '%' + movie_title + '%');
        ResultSet _c_set = _c_movie_actor_statement.executeQuery();
        _b_set.next();
        _c_set.next();


        while (_a_set.next()) {
            int mid = _a_set.getInt(1);
            System.out.println("ID: " + mid + " NAME: "
                    + _a_set.getString(2) + " YEAR: "
                    + _a_set.getString(3));


            while (_b_set.getInt(1) == mid){
                    System.out.println("\t\tDirector: " + _b_set.getString(3)
                        + " " + _b_set.getString(2));
                    if (!_b_set.next()){
                        break;
                    }           
            }

            while (_c_set.getInt(1) == mid){

                    System.out.println("\t\tActor: " + _c_set.getString(3)
                        + " " + _c_set.getString(2));
                    if (!_c_set.next()){
                        break;
                    }           
            }            

        }
    }

}
