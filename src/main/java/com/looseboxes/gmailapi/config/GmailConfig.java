package com.looseboxes.gmailapi.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

public class GmailConfig {

    private Api api;
    private OAuth2 oAuth2;

    public GmailConfig() { }

    public InputStream getCredentialsFileInputStream() throws IOException {
        final String path = getApi().getCredentialsFilePath();
        InputStream in = getClass().getResourceAsStream(path);
        if(in == null) {
            in = new FileInputStream(Paths.get(path).toFile());
        }
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + path);
        }
        return in;
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public GmailConfig api(Api api) {
        this.api = api;
        return this;
    }

    public OAuth2 getoAuth2() {
        return oAuth2;
    }

    public void setoAuth2(OAuth2 oAuth2) {
        this.oAuth2 = oAuth2;
    }

    public GmailConfig oAuth2(OAuth2 oAuth2) {
        this.oAuth2 = oAuth2;
        return this;
    }

    @Override
    public String toString() {
        return "GmailConfig{" +
                "api=" + api +
                ", oAuth2=" + oAuth2 +
                '}';
    }
}
