package com.looseboxes.gmailapi;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.services.gmail.Gmail;
import com.looseboxes.gmailapi.config.GmailConfig;

/**
 * @author chinomso ikwuagwu
 */
public class GmailFactoryImpl implements GmailFactory{

    private final GmailBuilder gmailBuilder;
    
    public GmailFactoryImpl(String applicationName, GmailConfig properties) {
        this.gmailBuilder = this.createGmailBuilder(applicationName, properties);
    }

    public GmailFactoryImpl(String applicationName, GmailConfig properties, DataStoreProvider dataStoreProvider) {
        this.gmailBuilder = this.createGmailBuilder(applicationName, properties, dataStoreProvider);
    }

    @Override
    public GmailSender getSender() {
        return new GmailSenderImpl(getGmail());
    }
    
    private Gmail _gmail_accessViaGetter;
    public Gmail getGmail() {
        if(_gmail_accessViaGetter == null) {
            try{
                _gmail_accessViaGetter = getGmailBuilder().build();
            }catch(IOException | GeneralSecurityException e) {
                throw new MailException(e);
            }
        }
        return _gmail_accessViaGetter;
    }
    
    private GmailBuilder getGmailBuilder() {
        return gmailBuilder;
    }

    private GmailBuilder createGmailBuilder(String applicationName, GmailConfig properties) {
    	return createGmailBuilder(applicationName, properties, null);
    }
    
    private GmailBuilder createGmailBuilder(
    		String applicationName, GmailConfig properties, DataStoreProvider dataStoreProvider) {
    	return new GmailBuilder()
    			.applicationName(applicationName)
    			.credentialsProviderBuilder(
    					new CredentialsProvider()
    							.gmailConfig(properties)
    							.dataStoreProvider(dataStoreProvider));
    }
}
