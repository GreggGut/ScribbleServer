package Server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class Page
{

    //private int pageNumber;
    //private SCFile mParentFile;
    private Collection<Path> mPaths = new ArrayList<Path>();
    private Collection<Path> mSavedPaths = new ArrayList<Path>();

    /**
     * Default constructor
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
    public Collection<Path> getPaths()
    {
        return mPaths;
    }

    public Collection<Path> getPathsToSave()
    {
        Collection<Path> toReturn = new ArrayList<Path>(mPaths);
        toReturn.removeAll(mSavedPaths);
        mSavedPaths.addAll(toReturn);
        return toReturn;
    }

    public void deletePath(int pathID)
    {
        Iterator<Path> pathIterator = mPaths.iterator();
        while (pathIterator.hasNext())
        {
            if (pathIterator.next().getId() == pathID)
            {
                pathIterator.remove();
                break;
            }
        }
    }

    public void clearPage()
    {
        mPaths.clear();
    }

    public Collection<Path> getSavedPaths()
    {
        return mSavedPaths;
    }

    public void addToSavedPaths(Collection<Path> saved)
    {
        mSavedPaths.addAll(saved);
    }
}