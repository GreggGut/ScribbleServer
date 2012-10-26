/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.net.InetAddress;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class ServerToClient
{

    //Port of the server
    public static final int serverPort = 54555;
    //Transmission protocol
    public static final int LogInSuccessful = 0;
    public static final int logInFailed = 1;
    public static final int logOutSuccessful = 2;
    public static final int logOutFailed = 3;
    public static final int AllowOwnership = 4;
    public static final int DisallowOwnership = 5;
    public static final int NewPath = 6;
    public static final int AddPointToPath = 7;
    public static final int EndCurrentPath = 8;
    public static final int UndoLastAction = 9;
    public static final int RedoLastAction = 10;
    public static final int DeletePath = 11;
    public static final int FileListAvailable = 12;
    public static final int PeriodicAliveCheck = 13;
}
