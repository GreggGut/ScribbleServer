/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.ArrayList;

/**
 * The Path class - This is a collection of point that compose a line/curve. It contains an ID and color.
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class Path
{

    private int id;
    private boolean mode; //false for erase, true for write
    private int color;
    //private boolean active;
    private int width;
    private int page;
    private ArrayList<Point> mPoints = new ArrayList<Point>();
    private short type;
    public static short PATH = 0;
    public static short UNDO = 1;
    public static short REDO = 2;

    /**
     * Default constructor
     *
     * @param id The ID of the path
     * @param mode The mode of the path (true for write, false for erase)
     * @param color The color of the path
     * @param active Is the path active (should be show it or not.. is it the most up to date path)
     */
    Path(int id, boolean mode, int color, /*
             * boolean active,
             */ int width, int page)
    {
        this.id = id;
        this.mode = mode;
        this.color = color;
        //this.active = active;
        this.width = width;
        this.page = page;
        this.type = PATH;
    }

    /**
     * Constructor used for undo/redo actions
     *
     * @param type 1 for undo, 2 for redo
     */
    Path(short type, int page)
    {
        this.type = type;
        this.page = page;
    }

    /**
     * Add point to the Path
     *
     * @param mPoint The Point to be added
     */
    void AddPoint(Point mPoint)
    {
        mPoints.add(mPoint);
    }

    /**
     * Get the Path ID
     *
     * @return The ID of the path
     */
    public int getId()
    {
        return id;
    }

    /**
     * Get the mode of the Path
     *
     * @return True if the path mode is write, false if it it erase
     */
    public boolean getMode()
    {
        return mode;
    }

    /**
     * Get the color of the path
     *
     * @return The color of the path
     */
    public int getColor()
    {
        return color;
    }

//    /**
//     * Is the path active (should it be rendered)
//     *
//     * @return True if the path should be rendered, false otherwise
//     */
//    public boolean isActive()
//    {
//        //TODO this might be useless, will try to redo this without this flag
//        return active;
//    }
    /**
     * Get all the points of this Path
     *
     * @return An ArrayList containing all the Points of this Path
     */
    public ArrayList<Point> getmPoints()
    {
        return mPoints;
    }

    public int getPage()
    {
        return page;
    }

    public int getWidth()
    {
        return width;
    }

    public short getType()
    {
        return type;
    }
}