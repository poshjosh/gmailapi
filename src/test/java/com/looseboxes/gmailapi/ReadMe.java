package com.looseboxes.gmailapi;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.looseboxes.gmailapi.config.Api;
import com.looseboxes.gmailapi.config.GmailConfig;
import com.looseboxes.gmailapi.config.OAuth2;

/**
 * @author chinomso ikwuagwu
 */
public class ReadMe {

    public static void main(String... args) {
        
        final String appName = "MyApp";

        Path dir = Paths.get(System.getProperty("user.home"), ".webstore", "config", "gmail");
        GmailConfig gmailConfig = new GmailConfig()
                .api(new Api().credentialsFilePath(dir.resolve("client.json").toString()).tokensDirectoryPath(dir.toString()))
                .oAuth2(new OAuth2().accessTokenScopes(Arrays.asList("https://www.googleapis.com/auth/gmail.send")));

        try{

            final String from = "noreply@herobids.com";
            final String to = "posh.bc@gmail.com";
            final String subject = "Subject 1";
            final String content = "Message 1";
                   
            final Message message = MessageUtil.createMessage(to, from, subject, content);
            
            final Gmail gmail = new GmailBuilder()
            		.applicationName(appName)
            		.gmailConfig(gmailConfig)
            		.build();
           
            // `me` is a keyword used to refer to the current user.
            // Beware that method #send() does not send the message. To send the message you have to call 
            // method #execute on the object returned by method #send() 
            gmail.users().messages().send("me", message).execute();
            
        }catch(Exception e) {
        
            e.printStackTrace();
        }
    }
}
