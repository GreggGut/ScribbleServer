/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class NewPathRequest implements Request
{

    @Override
    public int getRequestID()
    {
        return requestID;
    }
    private int requestID;
    private int pathID;
    private boolean mode;
    private int color;
    private boolean active;
    private int page;

    NewPathRequest(int requestID, int pathID, boolean mode, int color, boolean active, int page)
    {
        this.requestID = requestID;
        this.pathID = pathID;
        this.mode = mode;
        this.color = color;
        this.active = active;
        this.page = page;
    }

    public int getPathID()
    {
        return pathID;
    }

    public boolean isMode()
    {
        return mode;
    }

    public int getColor()
    {
        return color;
    }

    public boolean isActive()
    {
        return active;
    }

    public int getPage()
    {
        return page;
    }
}
