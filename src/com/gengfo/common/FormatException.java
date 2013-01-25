package com.gengfo.common;

/**
 * The Runtime Exception that happens in string formatting.
 * 
 * @author Will Li(ISDC-OCHL/SHA)
 * @since 1.3 Created on 2006-2-23
 */

public class FormatException extends NestingRuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Default Constructor
     */
    public FormatException() {
        super();
    }

    /**
     * Constructor
     * 
     * @param message The message associated with this exception.
     */
    public FormatException(String message) {
        super(message);
    }

    /**
     * Constructor
     * 
     * @param message The message associated with this exception
     * @param source Who throws the exception
     */
    public FormatException(String message, Object source) {
        super(message, source);
    }

    /**
     * Constructor
     * 
     * @param message The message associated with this exception
     * @param source Who throws the exception
     * @param nestedException The nested exception
     */
    public FormatException(String message, Object source, Throwable nestedException) {
        super(message, source, nestedException);
    }

    /**
     * String representation of this exception.
     * 
     * @return The String representation of this object.
     */
    public String toString() {
        return super.toString();
    }

}
