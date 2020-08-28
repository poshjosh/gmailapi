package com.looseboxes.gmailapi;

import com.google.api.services.gmail.Gmail;
import java.io.IOException;

/**
 * @author chinomso ikwuagwu
 */
public interface GmailProvider {
    Gmail get() throws IOException;
}
