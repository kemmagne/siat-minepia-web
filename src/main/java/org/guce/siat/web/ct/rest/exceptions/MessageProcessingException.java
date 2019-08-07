package org.guce.siat.web.ct.rest.exceptions;

/**
 *
 * @author tadzotsa
 */
public class MessageProcessingException extends RuntimeException {

    private static final long serialVersionUID = 3692542164868863674L;

    private final byte[] ebxml;

    public MessageProcessingException(byte[] ebxml, String message) {
        super(message);
        this.ebxml = ebxml;
    }

    public MessageProcessingException(byte[] ebxml, String message, Throwable cause) {
        super(message, cause);
        this.ebxml = ebxml;
    }

    public byte[] getEbxml() {
        return ebxml;
    }

}
