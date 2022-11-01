package com.App;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.Domain.Token;

public class SessionAuth {
    private static List<Token> tokens = new ArrayList<>();
    private static int timeout  = 1; // Timeout in minutes

    public static UUID createSession(String user) { // Create a session
        UUID uniqueUserIdentifier = UUID.randomUUID(); // Create a unique user identifier
        tokens.add(new Token(uniqueUserIdentifier, user)); // Add token to list of tokens
        System.out.println(user + " logged in" + " with token " + uniqueUserIdentifier);
        return uniqueUserIdentifier;
    }
    

    public static boolean validateSession(UUID uniqueUserIdentifier) { // Validate session
        System.out.println("Validating session " + uniqueUserIdentifier + " Amount of tokens" + tokens.size());
        for (Token token : tokens) { // Loop through tokens
            if (token.getUniqueUserIdentifier().equals(uniqueUserIdentifier)) {
                return timeoutHandler(token); // Check if token has timed out
            } 
        }
        return false; // Token not found
    }


    public static boolean timeoutHandler(Token token){ // Check if token has timed out
        if(token.getExpiryDate().isAfter(LocalDateTime.now().minusMinutes(timeout))){ 
            return true; // Token is still valid
        } else {
            tokens.remove(token); // Remove token
            return false; // Token has timed out
        }
    }
    
}
