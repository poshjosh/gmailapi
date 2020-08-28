package com.looseboxes.gmailapi;

import com.google.api.client.auth.oauth2.Credential;
import java.io.IOException;

/**
 * @author chinomso ikwuagwu
 */
public interface CredentialsProvider {

    /**
     * Creates an authorized Credential object.
     * @return An authorized Credential object.
     * @throws IOException If the credentials file cannot be found.
     */
    Credential getCredentials() throws IOException;
}
