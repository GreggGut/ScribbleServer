/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class UndoRequest implements Request
{

    @Override
    public Integer getRequestID()
    {
        return requestID;
    }
    private int requestID;
    private int pathID;
    private int page;

    /**
     * Default constructor
     *
     * @param requestID The ID of the current request
     * @param page The page on which we want to Undo the last action
     * @param pathID The ID of the path we want to Undo
     */
    UndoRequest(int requestID, int page, int pathID)
    {
        this.requestID = requestID;
        this.page = page;
        this.pathID = pathID;
    }
}
