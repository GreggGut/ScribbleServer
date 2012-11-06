/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.util.Vector;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class GetFileListRequest implements Request
{

    private int requestID;
    Vector<SCFile> mFiles;

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
     * @param mFiles A vector will all the files available of the server
     */
    GetFileListRequest(int requestID, Vector<SCFile> mFiles)
    {
        this.requestID = requestID;
        this.mFiles = mFiles;
    }

    /**
     * Get all the files on the server
     *
     * @return A vector of all the files available on the server
     */
    public Vector<SCFile> getmFiles()
    {
        return mFiles;
    }
}