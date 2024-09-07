package roles;

import database.Database;

/**
 * Admin class is used to store information of an admin user
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class UserBase implements User{

    // instance variables
    private final String name;
    private final String username;
    private final String id;
    private final String password;
    protected Database database;

    // constructor
    /**
     * construct the user base
     * @param id of the user
     * @param name of the user
     * @param username of the user
     * @param password of the user
     */
    public UserBase( String id, String name, String username, String password){
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.database = Database.getInstance();
    }

    /**
     * get the username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * get the name
     */
    public String getName(){
        return this.name;
    }

    /**
     * get the user ID
     */
    public String getId(){
        return this.id;
    }

    /**
     * get the user password
     */
    public String getPassword(){
        return this.password;
    }
}
