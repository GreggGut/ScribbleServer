/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

/**
 * This is just an interface for all the Requests that a user can place. This is done so that we can have a list of all the requests and so
 * that they can be ordered using their request ID
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public interface Request
{

    public Integer getRequestID();
}
