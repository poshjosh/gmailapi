package com.looseboxes.gmailapi;

import com.google.api.services.gmail.Gmail;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author chinomso ikwuagwu
 */
public class GmailFactoryImpl implements GmailFactory{

    private final GmailProvider provider;
    
    public GmailFactoryImpl(String applicationName, GmailConfig properties) {
        this.provider = this.createGmailProvider(applicationName, properties);
    }

    public GmailFactoryImpl(String applicationName, GmailConfig properties, DataStoreProvider dataStoreProvider) {
        this.provider = this.createGmailProvider(applicationName, properties, dataStoreProvider);
    }

    @Override
    public GmailSender getSender() {
        return new GmailSenderImpl(getGmail());
    }
    
    private Gmail _gmail_accessViaGetter;
    @Override
    public Gmail getGmail() {
        if(_gmail_accessViaGetter == null) {
            try{
                _gmail_accessViaGetter = getGmailProvider().get();
            }catch(IOException e) {
                throw new MailException(e);
            }
        }
        return _gmail_accessViaGetter;
    }
    
    @Override
    public GmailProvider getGmailProvider() {
        return provider;
    }

    private GmailProvider createGmailProvider(String applicationName, GmailConfig properties) {
        try{
            return new GmailProviderImpl(applicationName, properties);
        }catch(IOException | GeneralSecurityException e) {
            throw new MailException(e);
        }
    }
    
    private GmailProvider createGmailProvider(String applicationName, 
            GmailConfig properties, DataStoreProvider dataStoreProvider) {
        try{
            return new GmailProviderImpl(applicationName, properties, dataStoreProvider);
        }catch(IOException | GeneralSecurityException e) {
            throw new MailException(e);
        }
    }
}
