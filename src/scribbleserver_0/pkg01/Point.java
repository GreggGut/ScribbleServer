/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 *
 * @author scribble
 */
public class Point
{

    private int x;
    private int y;

    Point(int mx, int my)
    {
        x = mx;
        y = my;
    }
    
    public int x()
    {
        return x;
    }
    
    public int y()
    {
        return y;
    }
}
