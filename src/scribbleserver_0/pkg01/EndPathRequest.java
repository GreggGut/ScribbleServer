package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class EndPathRequest implements Request
{

    private boolean completed = false;
    private int requestID;
    private int pathID;

    /**
     * Get the request ID - Request are executed in order starting from the smallest (earlier request) to a larger (newer request) request
     * ID
     *
     * @return This request ID
     */
    @Override
    public Integer getRequestID()
    {
        return requestID;
    }

    /**
     * Default constructor
     *
     * @param requestID The ID of the request
     * @param pathID The ID of the path we want to end
     */
    EndPathRequest(int requestID, int pathID)
    {
        this.requestID = requestID;
        this.pathID = pathID;
    }
}
