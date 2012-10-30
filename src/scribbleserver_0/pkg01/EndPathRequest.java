package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class EndPathRequest implements Request
{

    @Override
    public int getRequestID()
    {
        return requestID;
    }

    private boolean completed = false;
    private int requestID;
    private int pathID;

    public EndPathRequest(int requestID, int pathID)
    {
        this.requestID = requestID;
        this.pathID = pathID;
    }
}
