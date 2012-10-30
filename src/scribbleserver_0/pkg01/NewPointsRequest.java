package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class NewPointsRequest implements Request
{

    @Override
    public int getRequestID()
    {
        return requestID;
    }

    private int requestID;
    private int pathID;
    private String points;

    NewPointsRequest(int requestID, int pathID, String points)
    {
        this.requestID = requestID;
        this.pathID = pathID;
        this.points = points;
    }

    public int getPathID()
    {
        return pathID;
    }

    public String getPoints()
    {
        return points;
    }
}
