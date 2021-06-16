package com.looseboxes.gmailapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Objects;
import java.util.Properties;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.looseboxes.gmailapi.config.GmailConfig;
import com.looseboxes.gmailapi.config.OAuth2;

public class CredentialsProvider{

	private GmailConfig gmailConfig;
    
    /**
     * HTTP_TRANSPORT The network HTTP Transport
     */
    private HttpTransport httpTransport;
    
    private JsonFactory jsonFactory;
    
    private DataStoreProvider dataStoreProvider;
    
    private UrlBrowser urlBrowser;
    
    private VerificationCodeReceiver verificationCodeReceiver;

	public Credential get() throws IOException, GeneralSecurityException{
    	
    	this.initDefaults();
    	
    	return this.getCredential();
    }
    
    private void initDefaults() throws IOException, GeneralSecurityException{
    	
    	Objects.requireNonNull(this.getGmailConfig());
    	
    	if(httpTransport == null) {
    		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    	}
    	
    	if(jsonFactory == null) {
    		jsonFactory = JacksonFactory.getDefaultInstance();
    	}

    	if(dataStoreProvider == null) {
    		dataStoreProvider = new DataStoreProviderImpl(getGmailConfig().getApi().getTokensDirectoryPath());
    	}
    	
    	if(urlBrowser == null) {
    		urlBrowser = new UrlBrowserImpl();
    	}
    	
    	if(verificationCodeReceiver == null) {
    		verificationCodeReceiver = new LocalServerReceiver.Builder().setPort(8888).build();
    	}
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
    private Credential getCredential() throws IOException {
        
        InputStream in = gmailConfig.getCredentialsFileInputStream();

        // This will close the InputStreamReader
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
        
        // user implies the current user
        final String userId = "user";

        DataStoreFactory dataStoreFactory = this.dataStoreProvider.get();
        
        DataStore<StoredCredential> dataStore = StoredCredential.getDefaultDataStore(dataStoreFactory);

//        StoredCredential cred = dataStore.get(userId);
//        final boolean expired = (System.currentTimeMillis() - cred.getExpirationTimeMilliseconds()) >= 0;
//        System.out.println("Access token expired: " + expired + ", expiry on: " + new Date(cred.getExpirationTimeMilliseconds()));

		OAuth2 oauth2 = gmailConfig.getoAuth2();
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, oauth2.getAccessTokenScopes())
				.setCredentialDataStore(dataStore)
                .setAccessType(oauth2.getAccessType())
				.setApprovalPrompt(oauth2.getApprovalPrompt())
                // This listener will help us save the new access token gotten using
                // the refresh token to the data store
                .addRefreshListener(new DataStoreCredentialRefreshListener(userId, dataStore))
                .build();

        return new AuthorizationCodeInstalledAppWithFlexibleBrowser(flow, verificationCodeReceiver, urlBrowser).authorize(userId);
    }

    public GmailConfig getGmailConfig() {
		return gmailConfig;
	}

	public CredentialsProvider gmailConfig(GmailConfig gmailConfig) {
		this.gmailConfig = gmailConfig;
		return this;
	}

	public HttpTransport getHttpTransport() {
		return httpTransport;
	}

	public CredentialsProvider httpTransport(HttpTransport httpTransport) {
		this.httpTransport = httpTransport;
		return this;
	}

	public JsonFactory getJsonFactory() {
		return jsonFactory;
	}

	public CredentialsProvider jsonFactory(JsonFactory jsonFactory) {
		this.jsonFactory = jsonFactory;
		return this;
	}

	public DataStoreProvider getDataStoreProvider() {
		return dataStoreProvider;
	}

	public CredentialsProvider dataStoreProvider(DataStoreProvider dataStoreProvider) {
		this.dataStoreProvider = dataStoreProvider;
		return this;
	}

	public UrlBrowser getUrlBrowser() {
		return urlBrowser;
	}

	public CredentialsProvider urlBrowser(UrlBrowser urlBrowser) {
		this.urlBrowser = urlBrowser;
		return this;
	}

	public VerificationCodeReceiver getVerificationCodeReceiver() {
		return verificationCodeReceiver;
	}

	public CredentialsProvider verificationCodeReceiver(VerificationCodeReceiver verificationCodeReceiver) {
		this.verificationCodeReceiver = verificationCodeReceiver;
		return this;
	}
}
