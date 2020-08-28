package com.looseboxes.gmailapi;

/**
 * @author chinomso ikwuagwu
 */
public class MailException extends RuntimeException {

    /**
     * Creates a new instance of <code>MailException</code> without detail
     * message.
     */
    public MailException() { }

    /**
     * Constructs an instance of <code>MailException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public MailException(String msg) {
        super(msg);
    }

    public MailException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public MailException(Throwable thrwbl) {
        super(thrwbl);
    }
}
