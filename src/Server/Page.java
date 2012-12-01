package Server;

import java.util.ArrayList;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class Page
{

    //private int pageNumber;
    //private SCFile mParentFile;
    private ArrayList<Path> mPaths = new ArrayList<Path>();

    /**
     * Default constructor
     * TOCONF For now this is only a collection of paths, should there be anything else here?
     */
    Page()
    {
    }

    /**
     * Add a path to this page
     *
     * @param path
     */
    public void addPath(Path path)
    {
        mPaths.add(path);
    }

    /**
     * Get a list of all the paths on this page
     *
     * @return An ArrayList of all the Paths on this page
     */
    public ArrayList<Path> getPaths()
    {
        return mPaths;
    }

    public void removeLastPath()
    {
        //mPaths.remove(mPaths.size() - 1);
    }

    public void deletePath(int pathID)
    {
        for (int i = 0; i < mPaths.size(); i++)
        {
            if (mPaths.get(i).getId() == pathID)
            {
                mPaths.remove(i);
                break;
            }
        }
    }
}