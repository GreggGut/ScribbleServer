package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class AddPointsRequest implements Request
{

    private int requestID;
    private int pathID;
    private int numberOfPoints;
    private String points;

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
     * @param pathID The ID of the path to which we want to add points
     * @param numberOfPoints The Number of Points included in this request
     * @param points A string that corresponds to points
     */
    AddPointsRequest(int requestID, int pathID, int numberOfPoints, String points)
    {
        this.requestID = requestID;
        this.pathID = pathID;
        this.numberOfPoints = numberOfPoints;
        this.points = points;
    }

    /**
     * Get the Path ID to which we want to add some points
     *
     * @return The path ID to which we will add points
     */
    public int getPathID()
    {
        return pathID;
    }

    /**
     * Get the number of points this request contains
     *
     * @return The number of points in this request
     * TOCONF Do we need the number of points?????
     */
    public int getNumberOfPoints()
    {
        return numberOfPoints;
    }

    /**
     * Get the string that corresponds to all the points of this request
     *
     * @return A string that can be parsed into Points
     */
    public String getPoints()
    {
        return points;
    }
}
