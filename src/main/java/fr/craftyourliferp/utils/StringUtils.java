package fr.craftyourliferp.utils;

public class StringUtils {
	
	public static String extractString(String str, char startChar, char endChar)
	{
		return extractString(str, startChar, endChar,0);
	}
	
	public static String extractString(String str, char startChar, char endChar, int startAfterIndex)
	{
		String string = "";
		boolean start = false;
		
		for(int i = startAfterIndex; i < str.length(); i++)
		{
			char character = str.charAt(i);
			
			if(character == startChar)
			{
				start = true;
			}
			
			if(start)
			{
				string += character;
				
				
				if(character == endChar)
				{
					return string;
				}
			}
		}
		return "";
	}
	
}
