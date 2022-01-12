package fr.craftyourliferp.data;

import java.util.ArrayList;

public class ServerData {

	 public String description;

	 public String favicon;
	
	 public double latency;
	
	 public Players players;
	 
	 public VersionInfo version;
	 
	 public class Players
	 {
	 	public int max;
	 	
	 	public int online;
	 	
	 	public ArrayList<PlayerInfo> sample;
	 	
	 	
	 }
	 
	 public class PlayerInfo
	 {
	 	public String id;
	 	
	 	public String name;
	 }

	 public class VersionInfo
	 {
	 	public String name;
	 	public int protocol;
	 }
		
}




