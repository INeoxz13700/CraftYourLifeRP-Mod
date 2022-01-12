package fr.craftyourliferp.radio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.common.base.Preconditions;


public class RadioFileReader {

	private BufferedReader reader;
		
	private String fileData;
	
	private HttpURLConnection con;
	
	public RadioFileReader(String fileUrl) throws IOException
	{
		Preconditions.checkArgument(fileUrl.endsWith(".radio"));
        URL url = new URL(fileUrl);
        con = (HttpURLConnection)url.openConnection();

        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
	}
	
	public RadioFileReader(File file) throws IOException
	{
		Preconditions.checkArgument(file.getName().endsWith(".radio"));
		reader = new BufferedReader(new FileReader(file));
	}
	
	public RadioFileReader read() throws IOException
	{
		String read = "";
		String content = "";
		while ((read = reader.readLine()) != null) 
		{
			content += read + "\n";
		}
		reader.close();
		this.fileData = content;
		con.disconnect();
		return this;
	}
	
	public String[] getContent()
	{
		return fileData.split("\n###############\n");
	}
	
	
	
}
