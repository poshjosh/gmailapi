package com.looseboxes.gmailapi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author hp
 */
public class GmailConfigImpl implements GmailConfig{
    
    private final Properties properties;

    public GmailConfigImpl(Properties properties) {
        this.properties = Objects.requireNonNull(properties);
    }
    
    @Override
    public InputStream getCredentialsFileInputStream() 
            throws FileNotFoundException{
        final String path = properties.getProperty(GmailAccessProperties.CREDENTIALS_FILE);
        InputStream in = this.getClass().getResourceAsStream(path);
        if(in == null) {
            in = new FileInputStream(Paths.get(path).toFile());
        }
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + path);
        }
        return in;
    }
    
    @Override
    public Path getTokensDirectory() {
        return Paths.get(properties.getProperty(GmailAccessProperties.TOKENS_DIRECTORY));
    }
    
    @Override
    public List<String> getAccessTokenScopes() {
        String scopesProp = properties.getProperty(GmailAccessProperties.ACCESS_TOKEN_SCOPES);
        return Arrays.asList(scopesProp.split(","))
                .stream()
                .filter((p) -> p != null && ! p.isEmpty())
                .collect(Collectors.toList());
    }
}
