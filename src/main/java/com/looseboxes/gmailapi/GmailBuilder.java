package com.looseboxes.gmailapi;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;
import java.util.Properties;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.gmail.Gmail;
import com.looseboxes.gmailapi.config.GmailConfig;

public class GmailBuilder{

	private String applicationName;
	
	private CredentialsProvider credentialsProvider;
	
	public Gmail build() throws IOException, GeneralSecurityException{
		
		this.initDefaults();
    	
    	final Credential credentials = credentialsProvider.get();
    	
    	// Calling CredentialsProvider#get() initializes various fields/attributes, which  
    	// we now access via their various getters
    	final HttpTransport httpTransport = credentialsProvider.getHttpTransport();
    	final JsonFactory jsonFactory = credentialsProvider.getJsonFactory();
    	
        final Gmail service = new Gmail.Builder(httpTransport, jsonFactory, credentials)
                .setApplicationName(applicationName)
                .build();
        
        return service;
    }
	
	private void initDefaults() {
		
		Objects.requireNonNull(credentialsProvider);
		
		if(this.applicationName == null) {
			this.applicationName = "";
		}
	}

    public GmailBuilder gmailConfig(GmailConfig config) {
    	this.credentialsProviderBuilder(new CredentialsProvider().gmailConfig(config));
    	return this;
    }

	public String getApplicationName() {
		return applicationName;
	}

	public GmailBuilder applicationName(String applicationName) {
		this.applicationName = applicationName;
		return this;
	}

	public CredentialsProvider getCredentialsProviderBuilder() {
		return credentialsProvider;
	}

	public GmailBuilder credentialsProviderBuilder(CredentialsProvider credentialsProviderBuilder) {
		this.credentialsProvider = credentialsProviderBuilder;
		return this;
	}
}
