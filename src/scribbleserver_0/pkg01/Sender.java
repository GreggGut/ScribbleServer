/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribbleserver_0.pkg01;

import java.net.*;  // for Socket
import java.io.*;   // for IOException and Input/OutputStream

/**
 *
 * @author scribble
 */
public class Sender extends Thread
{

    Sender()
    {
    }
    String server  = "127.0.0.1";       // Server name or IP address
    int servPort  = 21223;
    // Convert input String to bytes using the default character encoding
    String example = "This is an example";
    byte[] byteBuffer=example.getBytes();

    public void run()
    {
        try
        {
            Socket socket = new Socket(server, servPort);

            System.out.println(
                    "Connected to server...sending echo string");

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            out.write(byteBuffer);  // Send the encoded string to the server
            // Receive the same string back from the server
            int totalBytesRcvd = 0;  // Total bytes received so far
            int bytesRcvd;           // Bytes received in last read
            while (totalBytesRcvd < byteBuffer.length)
            {
                if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd,
                        byteBuffer.length - totalBytesRcvd)) == -1)
                {
                    throw new SocketException("Connection close prematurely");
                }
                totalBytesRcvd += bytesRcvd;
            }

            System.out.println(
                    "Received: " + new String(byteBuffer));

            socket.close();  // Close the socket and its streams
        }
        catch (IOException x)
        {
            x.printStackTrace();
        }

    }
    // Create socket that is connected to server on specified port
}