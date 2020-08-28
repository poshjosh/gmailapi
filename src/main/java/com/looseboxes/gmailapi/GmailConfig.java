package com.looseboxes.gmailapi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * @author hp
 */
public interface GmailConfig {
    
    public InputStream getCredentialsFileInputStream() throws IOException;
    
    public Path getTokensDirectory() throws IOException;
    
    public List<String> getAccessTokenScopes();
}
