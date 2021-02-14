package com.looseboxes.gmailapi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
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
     * Expired access tokens are automatically refreshed using the refresh token, 
     * if applicable
     * 
     * An access token typically has an expiration date of 1 hour, after which 
     * you will get an error if you try to use it. <code>GoogleCredential</code> 
     * takes care of automatically "refreshing" the token, which simply means 
     * getting a new access token. This is done by means of a long-lived refresh 
     * token, which is typically received along with the access token because we 
     * use the <code>access_type=offline</code> parameter during the authorization 
     * code flow.
     * 
     * Creates an authorized Credential object.
     * @return An authorized Credential object.
     * @throws IOException If the credentials file cannot be found.
     */
    @Override
    public Credential getCredentials() throws IOException {
        
        InputStream in = properties.getCredentialsFileInputStream();
        
        GoogleClientSecrets clientSecrets = GoogleClientSecrets
                .load(jsonFactory, new InputStreamReader(in));
        
        // user implies the current user
        final String userId = "user";

        DataStoreFactory dataStoreFactory = this.dataStoreProvider.get();
        
        DataStore dataStore = StoredCredential.getDefaultDataStore(dataStoreFactory);

//        StoredCredential cred = (StoredCredential)dataStore.get(userId);
//        final boolean expired = (System.currentTimeMillis() - cred.getExpirationTimeMilliseconds()) >= 0;
//        System.out.println("Access token expired: " + expired + ", expiry on: " + new Date(cred.getExpirationTimeMilliseconds()));
        
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, properties.getAccessTokenScopes())
                .setDataStoreFactory(dataStoreFactory)
                // offline mode will lead to us receiving a long lived refresh token
                .setAccessType("offline")
                // This listener will help us save the new access token gotten using
                // the refresh token to the data store
                .addRefreshListener(new DataStoreCredentialRefreshListener(userId, dataStore))
                .build();
        
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize(userId);
    }
}
