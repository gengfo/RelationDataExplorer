package com.gengfo.common;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * The helper class to generate a string for object.
 * 
 * @author Will Li(ISDC-OCHL/SHA)
 * @since 1.3 Created on 2006-2-23
 */
public class ToStringBuilder {
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	public static final char INNER_CLASS_SEPARATOR_CHAR = '$';

	private static ThreadLocal registry = new ThreadLocal() {
		protected synchronized Object initialValue() {
			return new HashSet();
		}
	};

	private Object curObj;

	private boolean appendStatics;

	private boolean appendTransients;

	public ToStringBuilder(Object obj, boolean addStatics, boolean addTransients) {
		this.curObj = obj;
		this.appendStatics = addStatics;
		this.appendTransients = addTransients;
	}

	static Set getRegistry() {
		return (Set) registry.get();
	}

	static boolean isRegistered(Object value) {
		return getRegistry().contains(value);
	}

	static void register(Object obj) {
		getRegistry().add(obj);
	}

	static void unregister(Object obj) {
		getRegistry().remove(obj);
	}

	protected void registerObject() {
		register(curObj);
	}

	protected void unregisterObject() {
		unregister(curObj);
	}

	public Object getObject() {
		return curObj;
	}

	protected String getNullText() {
		return "null";
	}

	/**
	 * Gets whether or not to append static fields.
	 */
	public boolean isAppendStatics() {
		return this.appendStatics;
	}

	/**
	 * Gets whether or not to append transient fields.
	 */
	public boolean isAppendTransients() {
		return this.appendTransients;
	}

	public String toString() {
		if (null == curObj) {
			return getNullText();
		}

		StringBuffer sb = new StringBuffer();
		
		toString(sb);

		return sb.toString();
	}

	protected boolean accept(Field field) {
		if (field.getName().indexOf(INNER_CLASS_SEPARATOR_CHAR) != -1) {
			// Reject field from inner class.
			return false;
		}

		if (!this.isAppendTransients()
				&& Modifier.isTransient(field.getModifiers())) {
			return false;
		}

		if (!this.isAppendStatics() && Modifier.isStatic(field.getModifiers())) {
			return false;
		}

		return true;
	}

	protected Object getValue(Field field) throws IllegalArgumentException,
			IllegalAccessException {
		return field.get(this.getObject());
	}

	protected void toString(StringBuffer sb) {
		//
		if (null == curObj) {
			sb.append(getNullText());
			return;
		}

		//
		if (isRegistered(curObj)) {
			sb.append(curObj.getClass().getName());

			return;
		}

		try {
			this.registerObject();

			String className = MiscUtil.getClassName(curObj);
			sb.append(className).append(" Starts >>>>>>>>>>");

			Field[] fields = curObj.getClass().getDeclaredFields();
			AccessibleObject.setAccessible(fields, true);

			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];

				if (accept(field)) {
					String name = field.getName();
					Object value = getValue(field);
					sb.append(LINE_SEPARATOR);
					//filed is the value
					if (curObj == value) {
						sb.append("    ").append(name).append(" = ").append(
								"this");
					} else if (isRegistered(value)
							&& !field.getType().isPrimitive()
							&& !field.getType().isArray()) {
						sb.append("    ").append(name).append(" = ").append(
								curObj.toString());
					} else {
						//is array runs here
						sb.append("    ").append(name).append(" = ");
						Format.objectToString(value, sb);
					}
				}
			}

			sb.append(LINE_SEPARATOR).append(className).append(
					" Ends >>>>>>>>>>");
		} catch (IllegalArgumentException e) {
			throw new NestingRuntimeException("error occur",
					ToStringBuilder.class, e);
		} catch (IllegalAccessException e) {
			throw new NestingRuntimeException("Illegal access",
					ToStringBuilder.class, e);
		} finally {
			this.unregisterObject();
		}
	}

	public static String toString(Object obj) {
		return new ToStringBuilder(obj, true, true).toString();
	}

	public static String toString(Object obj, boolean addStatics,
			boolean addTransients) {
		return new ToStringBuilder(obj, addStatics, addTransients).toString();
	}
}
