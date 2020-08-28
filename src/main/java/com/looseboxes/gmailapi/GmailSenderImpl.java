package com.looseboxes.gmailapi;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author chinomso ikwuagwu
 */
public class GmailSenderImpl implements GmailSender{
    
    private final Gmail gmail;

    public GmailSenderImpl(Gmail gmail) {
        this.gmail = Objects.requireNonNull(gmail);
    }

    @Override
    public Map send(MimeMessage mimeMessage) throws MailException{
        
        try{
            
            Message message = MessageUtil.fromMimeMessage(mimeMessage);

            Gmail.Users.Messages.Send send = gmail.users().messages().send("me", message);

            return send.execute();
            
        }catch(IOException | MessagingException e) {
        
            throw new MailException(e);
        }
    }
}
