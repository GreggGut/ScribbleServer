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

    //Port of the server
    public static final int serverPort = 54555;
    //Transmission protocol
    public static final int logInSuccessful = 0;
    public static final int logInFailedWrongPassword = 1;
    public static final int logOutSuccessful = 2;
    public static final int logOutFailed = 3;
    public static final int allowOwnership = 4;
    public static final int disallowOwnership = 5;
    public static final int newPath = 6;
    public static final int addPointToPath = 7;
    public static final int endCurrentPath = 8;
    public static final int undoLastAction = 9;
    public static final int redoLastAction = 10;
    public static final int deletePath = 11;
    public static final int fileListAvailable = 12;
    public static final int periodicAliveCheck = 13;
    public static final int noOwnerOfFile = 14;
    public static final int logInFailedUsernameAlreadyLoggedIn = 15;
    public static final int ownershipTakenSuccessfully=16;
    public static final int ownershipTakenFailed=17;
    //public static final int loginFailedWrongPassword=18;
}
