package com.looseboxes.gmailapi;

import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;

import java.io.IOException;
import java.io.Serializable;

public class StoredCredentailState implements Serializable {

    private boolean accessTokenPresent;
    private boolean refreshTokenPresent;
    private long expirationTimeMilliseconds;

    public StoredCredentailState() { }

    public static StoredCredentailState loadFrom(String tokensDir) {
        StoredCredentailState storedCredentailState = new StoredCredentailState();
        try {
            final String userId = "user"; // refers to current user
            DataStoreFactory dataStoreFactory = new DataStoreProviderImpl(tokensDir).get();
            DataStore<StoredCredential> dataStore = StoredCredential.getDefaultDataStore(dataStoreFactory);
            StoredCredential cred = dataStore.get(userId);
            storedCredentailState.setAccessTokenPresent(cred.getAccessToken() != null);
            storedCredentailState.setRefreshTokenPresent(cred.getRefreshToken() != null);
            storedCredentailState.setExpirationTimeMilliseconds(cred.getExpirationTimeMilliseconds());
        }catch(IOException e) {
            e.printStackTrace();
        }
        return storedCredentailState;
    }

    public long getTimeTillExpiryMinutes() {
        return expirationTimeMilliseconds < 1 ? -1 : (expirationTimeMilliseconds - System.currentTimeMillis()) / 60_000;
    }

    public boolean isAccessTokenPresent() {
        return accessTokenPresent;
    }

    public void setAccessTokenPresent(boolean accessTokenPresent) {
        this.accessTokenPresent = accessTokenPresent;
    }

    public boolean isRefreshTokenPresent() {
        return refreshTokenPresent;
    }

    public void setRefreshTokenPresent(boolean refreshTokenPresent) {
        this.refreshTokenPresent = refreshTokenPresent;
    }

    public long getExpirationTimeMilliseconds() {
        return expirationTimeMilliseconds;
    }

    public void setExpirationTimeMilliseconds(long expirationTimeMilliseconds) {
        this.expirationTimeMilliseconds = expirationTimeMilliseconds;
    }

    @Override
    public String toString() {
        return "StoredCredentailState{" +
                "timeTillExpiry=" + getTimeTillExpiryMinutes() + " minutes" +
                ", accessTokenPresent=" + accessTokenPresent +
                ", refreshTokenPresent=" + refreshTokenPresent +
                ", expirationTimeMillis=" + expirationTimeMilliseconds +
                '}';
    }
}
