package com.looseboxes.gmailapi;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class UrlBrowserImpl implements UrlBrowser{
    
    /**
     * Open a browser at the given URL using {@link Desktop} if available, or alternatively output the
	   * URL to {@link System#out} for command-line applications.
	   *
	   * @param url URL to browse
	   */
    public void browseTo(String url) {
    	
	    Objects.requireNonNull(url);
	    
	    // Ask user to open in their browser using copy-paste
	    System.out.println("Please open the following address in your browser:");
	    System.out.println("  " + url);
	    
	    // Attempt to open it in the browser
	    try {
	        if (Desktop.isDesktopSupported()) {
	            Desktop desktop = Desktop.getDesktop();
	            if (desktop.isSupported(Action.BROWSE)) {
	                System.out.println("Attempting to open that address in the default browser now...");
	                desktop.browse(URI.create(url));
	            } 
 	        }
	    } catch (IOException e) {
	    	System.err.println("Unable to open browser. Reason: " + e);
	    } catch (InternalError e) {
	        // A bug in a JRE can cause Desktop.isDesktopSupported() to throw an
	        // InternalError rather than returning false. The error reads,
	        // "Can't connect to X11 window server using ':0.0' as the value of the
	        // DISPLAY variable." The exact error message may vary slightly.
	    	System.err.println("Unable to open browser. Reason: " + e);
	    }
    }
}
