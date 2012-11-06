/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class ServerToClient
{

    /**
     * Transmission protocol from the server to the client
     * TOCONF Those might be redundant for some actions (when request automatically forwarded to other users)
     */
    public static final int LOG_IN_SUCCESSFUL = 0;
    public static final int LOG_IN_FAILED_WRONG_PASSWORD = 1;
    public static final int LOG_OUT_SUCCESSFUL = 2;
    public static final int LOG_OUT_FAILED = 3;
    public static final int ALLOW_OWNERSHIP = 4;
    public static final int DISALLOW_OWNERSHIP = 5;
    public static final int NEW_PATH = 6;
    public static final int ADD_POINTS_TO_PATH = 7;
    public static final int END_CURRENT_PATH = 8;
    public static final int UNDO_LAST_ACTION = 9;
    public static final int REDO_LAST_ACTION = 10;
    public static final int DELETE_PATH = 11;
    public static final int FILE_LIAST_AVAILABLE = 12;
    public static final int PERIODIC_ALIVE_CHECK = 13;
    public static final int OWNERSHIP_IS_AVAILABLE = 14;
    public static final int LOG_IN_FAIL_USER_ALREADY_LOGGED_IN = 15;
}
