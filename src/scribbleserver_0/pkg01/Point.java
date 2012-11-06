/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 * Point class - Used to create Paths (drawings)
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class Point
{

    private int x;
    private int y;

    /**
     * Default constructor
     *
     * @param mx The X coordinate
     * @param my The Y coordinate
     */
    Point(int mx, int my)
    {
        x = mx;
        y = my;
    }

    /**
     * Get the X coordinate of the Point
     *
     * @return The X coordinate to the Point
     */
    public int x()
    {
        return x;
    }

    /**
     * Get the Y coordinate of the Point
     *
     * @return The Y coordinate of the Point
     */
    public int y()
    {
        return y;
    }
}
