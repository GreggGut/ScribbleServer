/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class ClientToServer
{

    /**
     * Port of the server
     */
    public static final int SERVERPORT = 21223;// 54555;
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
}
