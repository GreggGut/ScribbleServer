/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SCFile
{

    private String name;
    private String location;
    private ArrayList<Page> mPages = new ArrayList<Page>();
    private ArrayList<User> mActiveUsers = new ArrayList<User>();
    private User presentOwner = null;
    private Writer out = null;
    private static final String splitInfo = ",";
    private static final String splitPoints = "$";
    private static final String ENCODING = "UTF-8";

    /**
     * Default constructor
     *
     * @param name File name
     * @param location File location
     * @param nPages Number of pages the file contains
     */
    SCFile(String name, String location, int nPages)
    {
        this.name = name;
        this.location = location;

        for (int i = 0; i < nPages; i++)
        {
            Page p = new Page(this);
            mPages.add(p);
        }
        loadFileContent();
        openSaveFile();
    }

    private void openSaveFile()
    {
        try
        {
            String savedName = name.substring(0, name.length() - 4).concat(".scf");
            System.out.println(savedName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("documents/" + savedName, true), "UTF-8"));
        }
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(SCFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(SCFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SCFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the file name
     *
     * @return The file name
     */
    public String getName()
    {
        return name;
    }

    //TOCONF this might be a better way of coding. Add path to file, and the file determines where to place it (on which page)
//    public void addPath(int page, Path path)
//    {
//        mPages.get(page).addPath(path);
//    }
    /**
     * Get the file location on the server
     *
     * @return Path to the file on the server
     */
    public String getLocation()
    {
        return location;
    }

    /**
     * Get all the pages on this file
     *
     * @return An ArrayList of all the pages of this document
     */
    public ArrayList<Page> getPages()
    {
        return mPages;
    }

    /**
     * Get all the user who are active on this document
     *
     * @return An ArrayList of all the users that are active on this document
     */
    public ArrayList<User> getmActiveUsers()
    {
        return mActiveUsers;
    }

    /**
     * Get the user who has the ownership of the file (write access)
     *
     * @return The user who has the ownership of the file, null if none
     */
    public User getPresentOwner()
    {
        return presentOwner;
    }

    /**
     * Add an active user - User who is working on this document
     *
     * @param mActiveUsers
     */
    public void addActiveUsers(User user)
    {
        mActiveUsers.add(user);
    }

    /**
     * Set the user who has the ownership of this file (user who will have write access to this file)
     *
     * @param presentOwner
     */
    public void setPresentOwner(User presentOwner)
    {
        this.presentOwner = presentOwner;
    }

    public void removeUser(User removeUser)
    {
        mActiveUsers.remove(removeUser);
    }

    /**
     * This function saves the newly received path into a file
     *
     * @param path The path to be saved (includes undo, redo, and clearAll)
     */
    synchronized public void saveFileContent(Path path)
    {
        try
        {
            String toSave = pathToString(path);
            out.write(toSave);
            //Flushing is used so that the content is written to the file right away
            out.flush();
        }
        catch (IOException ex)
        {
            Logger.getLogger(SCFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This function converts a path , including undo, redo, and clear all, into strings
     *
     * @param path A path object (includes undo, redo, and clearAll)
     * @return String representing a path
     */
    private String pathToString(Path path)
    {
        String toSave = String.valueOf(path.getPage());
        toSave += splitInfo;
        int type = path.getType();
        toSave += type;
        toSave += splitInfo;
        if (type == Path.PATH)
        {
            toSave += path.getId();
            toSave += splitInfo;
            toSave += path.getMode();
            toSave += splitInfo;
            toSave += path.getColor();
            toSave += splitInfo;
            toSave += path.getWidth();
            toSave += splitInfo;

            //Get all the parameters of the file
            for (Point point : path.getmPoints())
            {
                toSave += point.x();
                toSave += splitPoints;
                toSave += point.y();
                toSave += splitPoints;
            }
            //toSave = toSave.substring(0, toSave.length() - 2);
        }
        toSave += '\n';

        return toSave;
    }

    /**
     * This function restores the content of this CSFile from a .scf file
     */
    private void loadFileContent()
    {
        Reader in = null;
        BufferedReader mBuffer = null;
        String savedName = null;
        try
        {
            savedName = name.substring(0, name.length() - 4).concat(".scf");
            System.out.println(savedName);
            in = new InputStreamReader(new FileInputStream("documents//" + savedName), ENCODING);
            mBuffer = new BufferedReader(in);
            String line;
            while ((line = mBuffer.readLine()) != null)
            {
                decodeLine(line);
            }
        }
        catch (FileNotFoundException ex)
        {
            System.out.println(savedName + " file not found. The content will not be restored");
        }
        catch (UnsupportedEncodingException ex)
        {
            System.out.println(ENCODING + " encoding is unsupported and " + savedName + " will not be restored");
        }
        catch (IOException ex)
        {
            System.out.println("An I/O exception occured during file recovery. " + savedName + " will not be resoted");
        }
        finally
        {
            try
            {
                mBuffer.close();
                in.close();
            }
            catch (IOException ex)
            {
                System.out.println("An I/O exception occured while closing " + savedName + " file.");
            }
            catch (NullPointerException x)
            {
                System.out.println("An Null pointer exception occured while closing " + savedName + " buffer.");
            }
        }
    }

    /**
     * This function converts a file line content into a path, undo, redo, or clear all. It is only called at server startup
     *
     * @param line
     */
    private void decodeLine(String line)
    {
        String[] parts = line.split(splitInfo);
        int page = Integer.parseInt(parts[0]);
        short type = Short.parseShort(parts[1]);
        Path newPath;
        //If real path gather all the points
        if (type == Path.PATH)
        {
            int pathID = Integer.parseInt(parts[2]);
            boolean mode = Boolean.parseBoolean(parts[3]);
            int color = Integer.parseInt(parts[4]);
            int width = Integer.parseInt(parts[5]);

            //Splitting the x,y coordinates and recreating the points
            String[] allPoints = parts[6].split(splitPoints);
            newPath = new Path(width, mode, color, width, page);
            for (int i = 0; i < allPoints.length;)
            {
                int x = Integer.parseInt(allPoints[i++]);
                int y = Integer.parseInt(allPoints[i++]);
                Point newPoint = new Point(x, y);
                newPath.AddPoint(newPoint);
            }
        }
        //otherwise just the type (Undo, Redo, ClearAll) and page
        else
        {
            newPath = new Path(type, page);
        }

        getPages().get(page).restorePath(newPath);
    }
}
