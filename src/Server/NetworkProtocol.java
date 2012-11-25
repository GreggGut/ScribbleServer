/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class NetworkProtocol
{

    /**
     * Port of the server
     */
    //public static final int SERVERPORT = 21223;// 54555;
    /**
     * Network protocol from Client to Server
     */
    public static final int LOGIN = 0;
    public static final int LOGOUT = 1;
    public static final int REQUEST_OWNERSHIP = 2;
    public static final int RELEASE_OWNERSHIP = 3;
    public static final int GET_FILE_LIST = 4;
    public static final int DOWNLOAD_FILE = 5;
    public static final int NEW_PATH = 6;
    public static final int ADD_POINTS = 7;
    public static final int END_PATH = 8;
    public static final int UNDO = 9;
    public static final int REDO = 10;
    public static final int DELETE_PATH = 11;
    /**
     * User to split the info received from the clients
     */
    public static String split = "&";
    /**
     * Used to split the points received from the clients
     */
    public static String splitPoints = "#";


    /**
     * Network protocol from Server to Client
     */
//    public static final int FILE_LIST_AVAILABLE = 101;  We are using GET_FILE_LIST = 4; instead
//    public static final int LOG_IN_SUCCESSFUL = 0;
//    public static final int LOG_IN_FAILED_WRONG_PASSWORD = 1;
//    public static final int LOG_OUT_SUCCESSFUL = 2;
//    public static final int LOG_OUT_FAILED = 3;
//    public static final int ALLOW_OWNERSHIP = 4;
//    public static final int DISALLOW_OWNERSHIP = 5;
//    public static final int NEW_PATH = 6;
//    public static final int ADD_POINTS_TO_PATH = 7;
//    public static final int END_CURRENT_PATH = 8;
//    public static final int UNDO_LAST_ACTION = 9;
//    public static final int REDO_LAST_ACTION = 10;
//    public static final int DELETE_PATH = 11;
//    public static final int FILE_LIST_AVAILABLE = 12;
//    public static final int PERIODIC_ALIVE_CHECK = 13;
//    public static final int OWNERSHIP_IS_AVAILABLE = 14;
//    public static final int LOG_IN_FAIL_USER_ALREADY_LOGGED_IN = 15;

}