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

    @Override
    public Integer getRequestID()
    {
        return requestID;
    }
    private int requestID;
    Vector<SCFile> mFiles;

    GetFileListRequest(int requestID, Vector<SCFile> mFiles)
    {
        this.requestID = requestID;
        this.mFiles = mFiles;
    }

    public Vector<SCFile> getmFiles()
    {
        return mFiles;
    }
}
