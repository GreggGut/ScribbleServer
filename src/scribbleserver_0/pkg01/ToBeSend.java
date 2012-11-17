/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.net.InetAddress;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class ToBeSend
{

    private String toSend;
    private InetAddress clientAddress;
    private int clientPort;
    private int counter = 0;

    ToBeSend(String toSend, InetAddress clientAddress, int clientPort)
    {
        this.toSend = toSend;
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
    }

    public String getToSend()
    {
        return toSend;
    }

    public InetAddress getClientAddress()
    {
        return clientAddress;
    }

    public int getClientPort()
    {
        return clientPort;
    }

    public int getCounter()
    {
        return counter++;
    }
}
