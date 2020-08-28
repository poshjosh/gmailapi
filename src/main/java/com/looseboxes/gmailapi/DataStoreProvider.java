package com.looseboxes.gmailapi;

import com.google.api.client.util.store.DataStoreFactory;
import java.io.IOException;

/**
 * @author hp
 */
public interface DataStoreProvider {

    DataStoreFactory get() throws IOException;
}
