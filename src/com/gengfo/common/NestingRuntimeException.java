package com.gengfo.common;

/**
 * NestingException 
 * 
 * It might be the root of all the applicaiton-specific exception.
 * 
 * @author: SEAN QI (DEV-ISDC-OUI/SNT)
 * @since 1.2
 */
public class NestingRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    protected String message;
    protected Throwable nestedException;
    protected Object source;

    /**
     * Constructor 
     */
    public NestingRuntimeException() {
        super();
        this.message = null;
        this.nestedException = null;
        this.source = null;
    }
    /**
     * Constructor
     *
     * @param message The message for the exception
     */
    public NestingRuntimeException(String message) {
        super(message);
        this.message = message;
    }
    /**
     * Constructor
     *
     * @param message The message for the exception.
     * @param source The source where this exception is thrown.
     */
    public NestingRuntimeException(String message, Object source) {
        this(message);
        this.source = source;
    }
    /**
     * Constructor
     *
     * @param message The message for the exception.
     * @param source The source where this exception is thrown.   
     * @param nestedException The nested exception.
     */
    public NestingRuntimeException(
        String message,
        Object source,
        Throwable nestedException) {
        this(message, source);
        this.nestedException = nestedException;
    }
    /**
     * Getter for the message of this exception.
     *
     * @return The message assocaiated with this exception.
     */
    public String getMessage() {
        return this.message;
    }
    /**
     * Getter for the nested exception
     *
     * @return the nested exception or null if there is NONE.
     */
    public Throwable getNestedException() {
        return this.nestedException;
    }
    /**
     * Getter for the source of this exception
     *
     * @return The source of this exception or null if there is NONE.
     */
    public Object getSource() {
        return this.source;
    }
    /**
     * String representation of this exception.
     *
     * @return the String representation of this exception.
     */
    public String toString() {
        String LINE_SEPARATOR = System.getProperty("line.separator");
        String TAB = "    "; // 4 spaces
        StringBuffer sb = new StringBuffer();

        sb.append("(((( ");
        sb.append(this.getClass().getName());
        sb.append(" STARTS ");
        sb.append(LINE_SEPARATOR);
        
        // message
        sb.append(TAB);
        sb.append("[message = ");
        sb.append(this.message);
        sb.append("]");
        // insert one line
        sb.append(LINE_SEPARATOR);
        // source
        sb.append(TAB);
        sb.append("[source = ");
        sb.append(null == this.source ? null : this.source.toString());
        sb.append("]");
        // insert one line
        sb.append(LINE_SEPARATOR);
        // nestedThrowable
        sb.append(TAB);
        sb.append("[nestedException = ");
        sb.append(
            null == this.nestedException ? null : this.nestedException.toString());
        sb.append("]");
        sb.append(LINE_SEPARATOR);

        sb.append("ENDS");
        sb.append(")))) ");
        return sb.toString();
    }
}
