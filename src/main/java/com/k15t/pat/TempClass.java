package com.k15t.pat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TempClass {

	public static void main(String[] args) {
		//String data ="Schliemannstrae 27 10437 Berlin";
		//String data = "Name der Strase 25a 88489 Teststadt";
		String data = "Werbellin Strasse 69; Neukoeln; 12053 Berlin";
		//String regexp = "^((?:\\p{L}| |\\d|\\.|-)+?) (\\d+(?: ?- ?\\d+)? *[a-zA-Z]?) (\\d{5}) ((?:\\p{L}| |-)+)(?: *\\(([^\\)]+)\\))?$";
		String regexp ="^([^0-9]+) ([0-9]+.*?) ([0-9]{5}) (.*)$";
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(data);
		boolean matchFound = matcher.find();

		if (matchFound) {
			// Get all groups for this match
			for (int i = 0; i <= matcher.groupCount() && i<=5; i++) {
				String groupStr = matcher.group(i);
				System.out.println(groupStr);
			}
		}
		System.out.println("nothing found");
	}

}
