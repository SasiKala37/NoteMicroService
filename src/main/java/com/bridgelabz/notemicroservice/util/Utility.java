package com.bridgelabz.notemicroservice.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bridgelabz.notemicroservice.exceptions.NoteCreationException;

public class Utility {

	private Utility() {

	}

	public static void validateTitleAndDesc(String title, String description) throws NoteCreationException {
		if ((title == null || title.trim().isEmpty()) && description == null || description.trim().isEmpty()) {
			throw new NoteCreationException("Title and Description fields cannot be empty");
		}
	}
	
	public static boolean validateUrl(String url) {
		Pattern pattern = Pattern.compile("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$");
	    Matcher matcher=pattern.matcher(url);
	    return matcher.matches();
	}
	
	public static String getDomainName(String url) {

		String pattern = "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,15}";
		Pattern patrn = Pattern.compile(pattern);

		String domainName = "";
		Matcher matcher = patrn.matcher(url);

		if (matcher.find()) {
			domainName = matcher.group(0).toLowerCase().trim();
		}
		return domainName;

		
	}

}
