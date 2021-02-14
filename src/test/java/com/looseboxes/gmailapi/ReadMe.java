package com.looseboxes.gmailapi;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import java.util.Properties;

/**
 * @author chinomso ikwuagwu
 */
public class ReadMe {
    
    public static void main(String... args) {
        
        final String appName = "MyApp";
        
        Properties props = new Properties();
        props.setProperty(GmailAccessProperties.ACCESS_TOKEN_SCOPES, "https://www.googleapis.com/auth/gmail.send");

        props.setProperty(GmailAccessProperties.CREDENTIALS_FILE, "[PATH TO THE JSON FILE YOU DOWNLOADED FROM GOOGLE]");

        props.setProperty(GmailAccessProperties.TOKENS_DIRECTORY, "[PATH TO A DIRECTORY WHERE TOKENS WILL BE SAVED]");
        
        try{

            final String from = "chinomsoikwuagwu@herobids.com";
            final String to = "posh.bc@gmail.com";
            final String subject = "Subject 1";
            final String content = "Message 1";
                   
            final Message message = MessageUtil.createMessage(to, from, subject, content);

            Gmail gmail = new GmailProviderImpl(appName, new GmailConfigImpl(props)).get();
            
            // me is a keyword used to refer to the current user
            //
            gmail.users().messages().send("me", message);
            
        }catch(Exception e) {
        
            e.printStackTrace();
        }
    }
}
