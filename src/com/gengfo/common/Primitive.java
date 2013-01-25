package com.gengfo.common;

/**
 * The class represents the primitive value;
 * 
 * @author Will Li(ISDC-OCHL/SHA)
 * @since 1.3 Created on 2006-2-16
 */
public class Primitive {

	public static final int BOOLEAN = 0;

	public static final int BYTE = 1;

	public static final int CHAR = 2;

	public static final int DOUBLE = 3;

	public static final int FLOAT = 4;

	public static final int INT = 5;

	public static final int LONG = 6;

	public static final int SHORT = 7;

	private int type;

	Object value;

	public Primitive(boolean b) {
		value = new Boolean(b);
		type = BOOLEAN;
	}

	public Primitive(byte b) {
		value = new Byte(b);
		type = BYTE;
	}

	public Primitive(char ch) {
		value = new Character(ch);
		type = CHAR;
	}

	public Primitive(double d) {
		value = new Double(d);
		type = DOUBLE;
	}

	public Primitive(float f) {
		value = new Float(f);
		type = FLOAT;
	}

	public Primitive(int i) {
		value = new Integer(i);
		type = INT;
	}

	public Primitive(long l) {
		value = new Long(l);
		type = LONG;
	}

	public Primitive(short l) {
		value = new Short(l);
		type = SHORT;
	}

	public Object getValue() {
		return value;
	}
	
	public boolean isBoolean()
	{
	    return this.type == BOOLEAN;
	}

	public boolean isByte()
	{
	    return this.type == BYTE;
	}
	public boolean isChar()
	{
	    return this.type == CHAR;
	}
	public boolean isDouble()
	{
	    return this.type == DOUBLE;
	}
	public boolean isFloat()
	{
	    return this.type == FLOAT;
	}
	public boolean isInt()
	{
	    return this.type == INT;
	}
	public boolean isLong()
	{
	    return this.type == LONG;
	}
	public boolean isShort()
	{
	    return this.type == SHORT;
	}

	
	int getIntType() {
		return type;
	}

	public String getType() {
		switch (type) {
		case BOOLEAN:
			return "boolean";
		case BYTE:
			return "byte";
		case CHAR:
			return "char";
		case DOUBLE:
			return "double";
		case FLOAT:
			return "float";
		case INT:
			return "int";
		case LONG:
			return "long";
		case SHORT:
			return "short";
		default:
			return null; // never reach this line;
		}
	}

	String toStringwithType() {
		switch (type) {
		case BOOLEAN:
			return "boolean:" + value;
		case BYTE:
			return "byte:" + value;
		case CHAR:
			return "char:" + value;
		case DOUBLE:
			return "double:" + value;
		case FLOAT:
			return "float:" + value;
		case INT:
			return "int:" + value;
		case LONG:
			return "long:" + value;
		case SHORT:
			return "short:" + value;
		default:
			return null; // never reach this line;
		}

	}

	public String toString() {
		return value.toString();
	}

	public int hashCode() {
		return value.hashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Primitive))
			return false;

		Primitive p = (Primitive) obj;

		if (type != p.getIntType())
			return false;

		return value.equals(p.getValue());
	}

}
