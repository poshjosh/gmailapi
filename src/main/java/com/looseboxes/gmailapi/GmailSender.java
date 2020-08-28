package com.looseboxes.gmailapi;

import java.util.Map;
import javax.mail.internet.MimeMessage;

/**
 * @author chinomso ikwuagwu
 */
public interface GmailSender {
    
    Map send(MimeMessage mimeMessage) throws MailException;
}
