package com.looseboxes.gmailapi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * @author hp
 */
public class CredentialsProviderImpl implements CredentialsProvider {
    
    private final GmailConfig properties;
    
    /**
     * HTTP_TRANSPORT The network HTTP Transport
     */
    private final HttpTransport httpTransport;
    
    private final JsonFactory jsonFactory;
    
    private final DataStoreProvider dataStoreProvider;

    public CredentialsProviderImpl(
            HttpTransport httpTransport, JsonFactory jsonFactory,
            GmailConfig gmailProperties, DataStoreProvider dataStoreProvider) {
        this.httpTransport = Objects.requireNonNull(httpTransport);
        this.jsonFactory = Objects.requireNonNull(jsonFactory);
        this.properties = Objects.requireNonNull(gmailProperties);
        this.dataStoreProvider = Objects.requireNonNull(dataStoreProvider);
    }
    
    /**
     * Creates an authorized Credential object.
     * @return An authorized Credential object.
     * @throws IOException If the credentials file cannot be found.
     */
    @Override
    public Credential getCredentials() throws IOException {
        
        InputStream in = properties.getCredentialsFileInputStream();
        
        GoogleClientSecrets clientSecrets = GoogleClientSecrets
                .load(jsonFactory, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, properties.getAccessTokenScopes())
                .setDataStoreFactory(this.dataStoreProvider.get())
                .setAccessType("offline")
                .build();
        
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
