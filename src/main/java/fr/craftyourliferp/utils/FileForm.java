package fr.craftyourliferp.utils;

import java.io.File;

import fr.craftyourliferp.data.UploadDetails;

public class FileForm {
	
	private File file;
	
	private String[] allowedExtension;
	
	private long maxSize;	
	
	public UploadDetails details;
	

	public FileForm(String directory)
	{
		file = new File(directory);
	}
	
	public FileForm allowedExtension(String[] extension)
	{
		allowedExtension = extension;
		return this;
	}
	
	public FileForm fileMaxSize(long size)
	{
		maxSize = size;
		return this;
	}
	
	public UploadDetails canUploaded()
	{
		if(file.length() >= maxSize)
	    {
			details = new UploadDetails(false,"Taille du fichier trop grande");
			return details;
	    }
	
		String filename = file.getName();
		
		for(String extension : allowedExtension)
		{
			if(filename.endsWith("." + extension))
			{
				details = new UploadDetails(true,"");
				return details;
			}
		}
		
		details = new UploadDetails(false,"Mauvaise extension du fichier");
		return details;
	}
	
	public File getFile()
	{
		return file;
	}
	

	

}


