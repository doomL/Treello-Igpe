package persistence;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class DbUtils {

	public static String cryptPassword(String originalPassword) {
		String generatedSecuredPasswordHash = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
		System.out.println(generatedSecuredPasswordHash);
		return generatedSecuredPasswordHash;
	}

	public static Boolean checkPassword(String originalPassword, String dbPass) {
		System.out.println(originalPassword+" "+dbPass);
		boolean matched = BCrypt.checkpw(originalPassword, dbPass);
		if(matched)
		System.out.println("Password corretta");
		return matched;
	}

}
