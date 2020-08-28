package com.looseboxes.gmailapi;

import com.google.api.services.gmail.Gmail;
import java.io.IOException;

/**
 * @author chinomso ikwuagwu
 */
public interface GmailFactory {
    
    GmailSender getSender();
    
    default Gmail getGmail() {
        try{
            return getGmailProvider().get();
        }catch(IOException e) {
            throw new MailException(e);
        }
    }
    
    GmailProvider getGmailProvider();
}
