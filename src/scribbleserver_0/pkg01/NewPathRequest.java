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

    private int requestID;
    private int pathID;
    private boolean mode;
    private int color;
    private boolean active;
    private int page;
    private int width;

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
     * @param pathID The new path ID
     * @param mode The mode of the path (true for write, false for erase)
     * @param color The color of the path
     * @param active True if the renderer should draw this path, otherwise false
     * @param page The page to which this request corresponds
     * //TOCONF Do we need a page number here???
     */
    NewPathRequest(int requestID, int pathID, boolean mode, int color, boolean active, int page, int width)
    {
        this.requestID = requestID;
        this.pathID = pathID;
        this.mode = mode;
        this.color = color;
        this.active = active;
        this.page = page;
        this.width = width;
    }

    /**
     * Get the path ID
     *
     * @return The path ID
     */
    public int getPathID()
    {
        return pathID;
    }

    /**
     * Is this path written or erased
     *
     * @return True if the path is written, false if it is erased
     */
    public boolean isMode()
    {
        return mode;
    }

    /**
     * This function will return the color of the path
     *
     * @return The color of the path
     */
    public int getColor()
    {
        return color;
    }

    /**
     * Is this path active, should it be rendered or not
     * //TOCONF Do we need this??
     *
     * @return True is the path should be rendered, false otherwise
     */
    public boolean isActive()
    {
        return active;
    }

    /**
     * Get the page on which this request corresponds to
     *
     * @return The page on which this request occurs
     */
    public int getPage()
    {
        return page;
    }

    /**
     * Get the width on the path
     * @return 
     */
    public int getWidth()
    {
        return width;
    }
    
}
