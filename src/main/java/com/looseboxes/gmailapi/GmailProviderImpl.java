package com.looseboxes.gmailapi;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

/**
 * @author hp
 */
public class GmailProviderImpl implements GmailProvider {
    
    private final String applicationName;

    /**
     * HTTP_TRANSPORT The network HTTP Transport
     */
    private final HttpTransport httpTransport;
    
    private final JsonFactory jsonFactory;
    
    private final CredentialsProvider credentialsProvider;

    public GmailProviderImpl(String applicationName,  GmailConfig properties)
            throws GeneralSecurityException, IOException{
        this(applicationName, properties, new DataStoreProviderImpl(properties));
    }
    
    public GmailProviderImpl(String applicationName,  GmailConfig properties, 
            DataStoreProvider dataStoreProvider) 
            throws GeneralSecurityException, IOException{
        this(GoogleNetHttpTransport.newTrustedTransport(), 
                JacksonFactory.getDefaultInstance(), 
                applicationName, 
                properties, 
                dataStoreProvider);
    }
    
    protected GmailProviderImpl(
            HttpTransport httpTransport, JsonFactory jsonFactory, 
            String applicationName,  GmailConfig properties,
            DataStoreProvider dataStoreProvider) {
        this(httpTransport, jsonFactory, applicationName, 
                new CredentialsProviderImpl(httpTransport, jsonFactory, properties, dataStoreProvider));
    }
    
    public GmailProviderImpl(
            HttpTransport httpTransport, JsonFactory jsonFactory, 
            String applicationName,  CredentialsProvider credentialsProvider) {
        this.httpTransport = Objects.requireNonNull(httpTransport);
        this.jsonFactory = Objects.requireNonNull(jsonFactory);
        this.applicationName = Objects.requireNonNull(applicationName);
        this.credentialsProvider = Objects.requireNonNull(credentialsProvider);
    }

    @Override
    public Gmail get() throws IOException{
        return get(credentialsProvider);
    }
    
    public Gmail get(CredentialsProvider credentialsProvider) 
            throws IOException{
        Gmail service = new Gmail.Builder(httpTransport, jsonFactory, credentialsProvider.getCredentials())
                .setApplicationName(applicationName)
                .build();
        return service;
    }
}
