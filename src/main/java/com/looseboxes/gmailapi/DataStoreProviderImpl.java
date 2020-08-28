package com.looseboxes.gmailapi;

import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author hp
 */
public class DataStoreProviderImpl implements DataStoreProvider{
 
    private final GmailConfig config;

    public DataStoreProviderImpl(GmailConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    public DataStoreFactory get() throws IOException{
        return new FileDataStoreFactory(this.getTokensDirectory());
    }

    
    private File getTokensDirectory() throws IOException{
        Path path = config.getTokensDirectory();
        if( ! Files.exists(path)) {
            Files.createDirectories(path);
        }
        return path.toFile();
    }
}
