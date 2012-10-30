/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.util.ArrayList;

/**
 *
 * @author scribble
 */
public class Path
{

    private int id;
    private boolean mode; //false for erase, true for write
    private int color;
    private boolean active;
    private ArrayList<Point> mPoints = new ArrayList<Point>();

    Path(int id, boolean mode, int color, boolean active)
    {
    }
    
}