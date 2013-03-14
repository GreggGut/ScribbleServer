/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class Console extends Thread
{

    private ScribbleClients mClients;
    private static final int USERS = 1;
    private static final int FILES = 2;
    private static final int NEW_USER = 3;

    Console(ScribbleClients mClients)
    {
        this.mClients = mClients;
    }

    @Override
    public void run()
    {
        while (true)
        {
            Scanner reader = new Scanner(System.in);
            printCommands();
            System.out.print("Enter Command: ");
            analyse(reader.nextLine());

        }
    }

    private void analyse(String choice)
    {
        try
        {
            int c = Integer.parseInt(choice);
            //System.out.println("choice: " + c);
            switch (c)
            {
                case USERS:
                    mClients.printUsers();

                    break;

                case FILES:
                    for (SCFile file : mClients.getFiles())
                    {
                        System.out.println(file.getName());
                    }
                    break;
                case NEW_USER:
                    createNewUser();
                    break;
                default:
                    System.out.println("Unknow command");
                   // printCommands();
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Unknow command");
            printCommands();
        }
    }

    private void printCommands()
    {
        System.out.println("1 - List Users\n2 - List Available Files\n3 - Create new User");
    }

    private void createNewUser()
    {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter new Username: ");
        String username = reader.nextLine();
        if (!userExists(username))
        {
            do
            {
                System.out.print("Enter new password: ");
                String pass1 = reader.nextLine();
                System.out.print("ReEnter new password: ");
                String pass2 = reader.nextLine();

                if (pass1.equals(pass2))
                {
                    doCreate(username, pass1);
                    System.out.println("User " + username + " has been created successfully");
                    break;
                }
                else
                {
                    System.out.println("Passwords do not match");
                }
            }
            while (true);
        }
        else
        {
            System.out.println("User " + username + " already exists...");
        }
    }

    private void doCreate(String user, String pass)
    {
        Writer out = null;
        try
        {
            String savedName = "resources//credential";
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(savedName, true), "UTF-8"));
            String newUser = user + "," + pass;
            out.write(newUser);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean userExists(String username)
    {
        username = username.toLowerCase();
        BufferedReader buffer = null;
        boolean exists = false;
        try
        {
            buffer = new BufferedReader(new FileReader("resources//credential"));
            String lineFromFile;
            while ((lineFromFile = buffer.readLine()) != null)
            {
                String cr[] = lineFromFile.split(",");
                if (cr.length == 2 && cr[0].equals(username))
                {
                    exists = true;
                    break;
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception x)
        {
        }
        finally
        {
            try
            {
                buffer.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return exists;
    }
}
