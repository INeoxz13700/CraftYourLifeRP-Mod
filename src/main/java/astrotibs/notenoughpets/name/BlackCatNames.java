package astrotibs.notenoughpets.name;

import java.util.Random;

/**
 * Requests a random name intended for a witch's black cat. 
 * @author AstroTibs
 */
public class BlackCatNames
{
	public static final String[] SYL1_S = new String[]{"A", "A", "A", "Ba", "Ba", "Be", "Be", "Bea", "Bi", "Bo", "Bo", "Bu", "Cha", "Ci", "Ci", "Cro", "Croo", "Da", "Da", "De", "Dra", "Dra", "Dra", "Du", "E", "E", "E", "E", "E", "Fe", "Fi", "Ga", "Gla", "Go", "Go", "Gy", "Ha", "He", "Ho", "Ho", "Ho", "I", "I", "I", "Ja", "Je", "Ji", "Ji", "Ju", "Ki", "Kri", "Li", "Lu", "Lu", "Ma", "Ma", "Ma", "Ma", "Me", "Mi", "Mi", "Mi", "Mo", "Moo", "My", "My", "My", "Ni", "Ni", "No", "No", "Ny", "O", "O", "O", "O", "O", "Pha", "Plu", "Po", "Poe", "Pye", "Ra", "Sa", "Sa", "Sa", "Sa", "Se", "Sha", "Sha", "Si", "Sno", "Spa", "Spi", "Sta", "Sto", "Sto", "Sto", "Thu", "Tri", "Twi", "Va", "Vo", "Voo", "Wa", "Wi", "Wi", "Wi", "Wi", "Wi"};
	public static final String[] SYL1_E = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "#", "b", "b", "b", "b", "c", "c", "d", "d", "de#", "de#", "dge#", "g", "ght", "ght#", "k", "ke#", "l", "l", "l", "m", "m", "m", "m#", "mp", "n", "n", "n", "n", "n", "n", "n", "n#", "ne#", "nes#", "nk#", "nt#", "nx#", "nx#", "p", "r", "r", "r", "r", "r", "r", "r", "r", "r", "r", "r", "rk", "rm#", "s", "s", "s", "s", "s", "s", "s", "sh", "sh", "sh#", "st#", "t#", "tch", "tch#", "th", "w", "w#", "x", "x#", "z"};
	
	public static final String[] SYL2_S = new String[]{"a", "ba", "ba", "ba", "be", "ble", "bo", "bo", "bo", "bri", "ca", "ca", "ci", "cli", "co", "coa", "cu", "cu", "cu", "de", "de", "de", "de", "di", "do", "doo", "e", "fi", "gi", "goy", "ha", "he", "i", "i", "ji", "ka", "ki", "kie", "le", "le", "li", "li", "li", "li", "li", "li", "lie", "lo", "ly", "me", "me", "me", "mi", "my", "na", "na", "ne", "ni", "ni", "no", "ny", "phe", "pi", "pie", "ra", "ri", "ri", "ro", "sa", "sha", "si", "si", "si", "sto", "sy", "te", "te", "te", "ti", "ti", "ti", "to", "to", "to", "ty", "ve", "vi", "wa", "za"};
	public static final String[] SYL2_E = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "c", "c#", "c#", "c#", "ck#", "d", "f", "g", "ght#", "ght#", "ght#", "l#", "l#", "le#", "ll#", "lt#", "m#", "m#", "n#", "n#", "n#", "n#", "n#", "ne#", "ne#", "nks#", "nthe#", "pse#", "que#", "r#", "r#", "r#", "r#", "r#", "r#", "r#", "r#", "rd#", "s#", "s#", "s#", "s#", "s#", "s#", "ss#", "t#", "t#", "th#", "w#", "wk#", "x#", "x#", "x#"};
	
	public static final String[] SYL3_S = new String[]{"", "", "a", "cia", "e", "e", "fe", "fe", "i", "ke", "la", "li", "ma", "mo", "na", "na", "ny", "o", "ra", "ra", "ry", "u"};
	public static final String[] SYL3_E = new String[]{"", "", "", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "l#", "n#", "r#", "re#", "re#", "s#", "th#", "tt#", "z#"};
	
	public static final String[] SYL4_S = new String[]{"a", "lee", "no"};
	public static final String[] SYL4_E = new String[]{"#", "n#", "s#"};
	
	
	public static String newRandomName(Random r)
	{
		String name="Black";
		
		while (true)
		{
			// Step 1: Generate a random entry from every half-syllable and slap them together
			name =
					SYL1_S[r.nextInt(SYL1_S.length)] + SYL1_E[r.nextInt(SYL1_E.length)] + // Syllable 1
					SYL2_S[r.nextInt(SYL2_S.length)] + SYL2_E[r.nextInt(SYL2_E.length)] + // Syllable 2
					SYL3_S[r.nextInt(SYL3_S.length)] + SYL3_E[r.nextInt(SYL3_E.length)] + // Syllable 3
					SYL4_S[r.nextInt(SYL4_S.length)] + SYL4_E[r.nextInt(SYL4_E.length)];  // Syllable 4
			
			// Step 2: Trim off everything starting at the first pipe
			name = name.split("#")[0];
			
			// Step 3: Now do name checks and return the name if it passes
			if ( name.length() >= 3)
			{
				// Now, make sure the same characters don't appear in the name three times in a row
				char[] nameRootArray = name.toLowerCase().toCharArray();
				int consecutives = 0;
				for(int ci = 0; ci < nameRootArray.length-2; ci++)
				{
					if (nameRootArray[ci] == nameRootArray[ci+1] && nameRootArray[ci] == nameRootArray[ci+2]) {consecutives++;}
				}
				if (consecutives == 0)
				{
					// Do a content scan
					if ( !contentScan(name) ) {return name;} // Passes all the checks! Accept the name!
					else {continue;} // Something caught the attention of the filter
				}
				else {continue;} // Detected three of the same letter in a row.
			}
			// Now ensure that a two-letter name isn't the same letter twice.
			else if (name.length() == 2)
			{
				if ( name.toLowerCase().charAt(0) != name.toLowerCase().charAt(1) ) {return name;} // Passes all the checks! Accept the name!
			}
			else {continue;} // Root name is too short.
		}
	}
	

	/**
	 * Scans the input string and returns "true" if there is a particular series
	 * of sub-strings within.
	 */
	private static boolean contentScan(String inputString) {

		// Updated in v3.1trades
		String[] filterList = new String[]{
				//"avyngf", // Russian guy - left in because there is a stronghold with that name
				//"erygvu", // Austrian guy - left in because Russian guy was left in
				"erttva", // Black
				"gbttns", // Sticks
				"upgvo", // Lady dog
				"gahp", // Lady place
				"zvhd", // Inventive Nordic lady place
				"xphs", // F
				"gvuf", // Dook
				"laans", // A belt pack that you wear
				"mncf", // Mario Party 8 Oopsie
				"lffhc", // Weakling
				"rxvx", // K
				"rybuffn", // Exit hole
				"fvarc", // Protrusion and exit hole
				"navtni", // Inset exit hole
				"eranro", // Southern companion
				//"rcne" // Snuggle - left in because some names have that string
				"ghyf", // Loves to love
				"rebuj", // Loves to love
				"vngaru", // H toon
				};
		
		for (String s : filterList) {
			if ( ( inputString ).toLowerCase().contains( new StringBuilder( rot13(s) ).reverse().toString() ) ) {return true;}
		}
		
		return false;
	}
	
	
	/**
	 * Rot13 codec
	 * Adapted from: http://introcs.cs.princeton.edu/java/31datatype/Rot13.java.html
	 */
	public static String rot13(String s) {
		
		StringBuilder out = new StringBuilder();
		
        for (int i = 0; i < s.length(); i++) {
        	
            char c = s.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            
            out.append(c);
        }
        return out.toString();
    }
	
}
