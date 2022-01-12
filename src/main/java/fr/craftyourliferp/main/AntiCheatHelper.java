package fr.craftyourliferp.main;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.ProcessInfo;

public class AntiCheatHelper extends Thread {

	public AntiCheatHelper()
	{
		setName("AntiCheat-Helper");
	}
	
	public void startAntiCheat()
	{
		setDaemon(true);
		start();
	}
	
	
	@Override
	public void run()
	{
		while(true)
		{
			
            try {
                check();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
		    
			
		}
	}
	
	private static void check() throws NoSuchFieldException, IllegalAccessException 
	{
		
        List<ProcessInfo> processesList = JProcesses.getProcessList();
	    for (final ProcessInfo processInfo : processesList) {
	        if(processInfo.getName().toLowerCase().contains("cheatengine") || 
	        		processInfo.getName().toLowerCase().contains("autoclick")||
	        			processInfo.getName().toLowerCase().contains("hack"))       
	        {
	        	System.out.println("process name " + processInfo.getName());
	        	
	    		for(int i = 0; i < 50; i++)System.out.print("#");
	    		System.out.println("");
	    		System.out.print("\n");
	    		System.out.println("Les cheats sont interdit sur CYLRP! \n");
	    		System.out.println("");
	    		for(int i = 0; i < 50; i++)System.out.print("#");
	        	
                ClientProxy.forceExit = true;
	        	return;
	        }
	    }
    }

}
