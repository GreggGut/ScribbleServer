/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class SCFile
{

    private String name;
    private String location;
    private ArrayList<Page> mPages = new ArrayList<Page>();
    private ArrayList<User> mActiveUsers = new ArrayList<User>();
    private User presentOwner = null;
    private Writer out = null;

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

    //TODO I need to design and implement this
    //Best choice would be to store info into files in real time - As soon as it comes
    synchronized public void saveFileContent(Path path)
    {

        try
        {
            String toSave = getPathInfo(path);
            out.write(toSave);
            out.flush();
        }
        catch (IOException ex)
        {
            Logger.getLogger(SCFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //TOCONF does this really work?
//    @Override
//    protected void finalize()
//    {
//        System.out.println("In finalize");
//        try
//        {
//            out.close();
//        }
//        catch (IOException ex)
//        {
//            Logger.getLogger(SCFile.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    private String getPathInfo(Path path)
    {
        String toSave = String.valueOf(path.getPage());
        toSave += ",";
        int type = path.getType();
        toSave += type;
        toSave += ",";
        if (type == Path.PATH)
        {
            toSave += path.getId();
            toSave += ",";
            toSave += path.getMode();
            toSave += ",";
            toSave += path.getColor();
            toSave += ",";
            toSave += path.getWidth();
            toSave += ",";

            //Get all the parameters of the file
            for (Point point : path.getmPoints())
            {
                toSave += point.x();
                toSave += "-";
                toSave += point.y();
                toSave += "-";
            }
            //toSave = toSave.substring(0, toSave.length() - 2);
        }
        toSave += '\n';

        return toSave;
    }

    private void loadFileContent()
    {
        Reader in = null;
        BufferedReader mBuffer = null;
        try
        {
            String savedName = name.substring(0, name.length() - 4).concat(".scf");
            System.out.println(savedName);
            in = new InputStreamReader(new FileInputStream("documents//" + savedName), "UTF-8");
            mBuffer = new BufferedReader(in);
            String line;
            while ((line = mBuffer.readLine()) != null)
            {
                decodeLine(line);
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(SCFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(SCFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SCFile.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(SCFile.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (NullPointerException x)
            {
            }
        }
    }

    private void decodeLine(String line)
    {
        String[] parts = line.split(",");
        int page = Integer.parseInt(parts[0]);
        short type = Short.parseShort(parts[1]);
        Path newPath;
        if (type == Path.PATH)
        {
            int pathID = Integer.parseInt(parts[2]);
            boolean mode = Boolean.parseBoolean(parts[3]);
            int color = Integer.parseInt(parts[4]);
            int width = Integer.parseInt(parts[5]);


            String[] allPoints = parts[6].split("-");
            newPath = new Path(width, mode, color, width, page);
            for (int i = 0; i < allPoints.length;)
            {
                int x = Integer.parseInt(allPoints[i++]);
                int y = Integer.parseInt(allPoints[i++]);
                Point newPoint = new Point(x, y);
                newPath.AddPoint(newPoint);
            }
        }
        else
        {
            newPath = new Path(type, page);
        }
        getPages().get(page).restorePath(newPath);

    }
}
