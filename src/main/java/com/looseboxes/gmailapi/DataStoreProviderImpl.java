package com.looseboxes.gmailapi;

import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author hp
 */
public class DataStoreProviderImpl implements DataStoreProvider{
 
    private final Path tokensDirectory;

    public DataStoreProviderImpl(String tokensDirectory) {
        this(Paths.get(tokensDirectory).toAbsolutePath().normalize());
    }

    public DataStoreProviderImpl(Path tokensDirectory) {
        this.tokensDirectory = Objects.requireNonNull(tokensDirectory);
    }

    @Override
    public DataStoreFactory get() throws IOException{
        return new FileDataStoreFactory(this.getTokensDirectory());
    }

    private File getTokensDirectory() throws IOException{
        if( ! Files.exists(tokensDirectory)) {
            Files.createDirectories(tokensDirectory);
        }
        return tokensDirectory.toFile();
    }
}
