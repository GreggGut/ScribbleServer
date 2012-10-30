/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class AddPointsRequest implements Request
{

    @Override
    public Integer getRequestID()
    {
        return requestID;
    }
    private int requestID;
    private int pathID;
    private int numberOfPoints;
    private String points;

    //requestID - pathID - numberOfPoints - Points
    AddPointsRequest(int requestID, int pathID, int numberOfPoints, String points)
    {
        this.requestID = requestID;
        this.pathID = pathID;
        this.numberOfPoints = numberOfPoints;
        this.points = points;
    }

    public int getPathID()
    {
        return pathID;
    }

    public int getNumberOfPoints()
    {
        return numberOfPoints;
    }

    public String getPoints()
    {
        return points;
    }
}
