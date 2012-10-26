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
public class Page
{

    private int pageNumber;
    private SCFile mParentFile;
    private ArrayList<Path> mPaths = new ArrayList<Path>();

    Page()
    {
    }
}

/*
 * - pageNumber:int
- parentFile:File
- mPaths:Vector<Path>
 */