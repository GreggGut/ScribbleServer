/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.Scanner;

/**
 *
 * @author Grzegorz Gut <Gregg.Gut@gmail.com>
 */
public class Console extends Thread
{

    private ScribbleClients mClients;
    private static final int USERS = 1;
    private static final int FILES = 2;

    Console(ScribbleClients mClients)
    {
        this.mClients = mClients;
    }

    @Override
    public void run()
    {
        printCommands();
        while (true)
        {
            Scanner reader = new Scanner(System.in);
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
                default:
                    System.out.println("Unknow command");
                    printCommands();
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
        System.out.println("1 - List Users\n2 - List Available Files");
    }
}
