# gmailapi

Send secure email via gmail api. Automate oauth2 token refresh.

### Sample usage

```java
package com.looseboxes.gmailapi;

import com.google.api.services.gmail.Gmail;
import java.util.Properties;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;

/**
 * @author chinomso ikwuagwu
 */
public class ReadMe {
    
    @Test
    public static void main(String... args) {
        
        final String appName = "my app";
        Properties props = new Properties();
        props.setProperty(GmailAccessProperties.ACCESS_TOKEN_SCOPES, "https://www.googleapis.com/auth/gmail.send");
        props.setProperty(GmailAccessProperties.CREDENTIALS_FILE, "[PATH TO THE JSON FILE YOU DOWNLOADED FROM GOOGLE]");
        props.setProperty(GmailAccessProperties.TOKENS_DIRECTORY, "[PATH TO A DIRECTORY WHERE TOKENS WILL BE SAVED]");
        
        try{

            final String from = "looseboxes@gmail.com";
            final String to = "posh.bc@gmail.com";
            final String subject = "Subject 1";
            final String content = "Message 1";
                   
            final MimeMessage mimeMessage = MessageUtil.createEmail(to, from, subject, content);

            Gmail gmail = new GmailProviderImpl(appName, new GmailConfigImpl(props)).get();
            
            // me is a keyword used to refer to the current user
            //
            gmail.users().messages().send("me", MessageUtil.fromMimeMessage(mimeMessage));
            
        }catch(Exception e) {
        
            e.printStackTrace();
        }
    }
}
```
