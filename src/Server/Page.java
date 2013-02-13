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

    private Collection<Path> mPaths = new ArrayList<Path>();
    private SCFile mFile;

    /**
     * Default constructor
     */
    Page(SCFile file)
    {
        mFile = file;
    }

    /**
     * Add a path to this page, only new paths
     *
     * @param path
     */
    protected void addPath(Path path)
    {
        mPaths.add(path);
        mFile.saveFileContent(path);
    }

    /**
     * This function restores paths from the Scribble File Type (.scf) at startup
     *
     * @param path
     */
    protected void restorePath(Path path)
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
}