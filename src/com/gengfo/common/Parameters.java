package com.gengfo.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Parameter information. This includes a parameter type and parameter value.
 * For the parameter information with parameter name, please refer to 
 * {@link com.oocl.frm.jwf.common.util.NamedParameters}
 * for more information.
 * @author Will Li(ISDC-OCHL/SHA)
 * @since 1.3 Created on 2006-2-15
 * @see com.oocl.frm.jwf.common.util.NamedParameters
 */
public class Parameters implements Cloneable {
    protected List paras;

    public Parameters() {
        this.paras = new ArrayList();
    }

    public Parameters(int size) {
        this.paras = new ArrayList(size);
    }

    public Parameters(List list) {
        this.paras = list;
    }

    public Parameters add(boolean b) {
        paras.add(new Primitive(b));

        return this;
    }

    public Parameters add(byte b) {
        paras.add(new Primitive(b));

        return this;
    }

    public Parameters add(char ch) {
        paras.add(new Primitive(ch));

        return this;
    }

    public Parameters add(double d) {
        paras.add(new Primitive(d));

        return this;
    }

    public Parameters add(float f) {
        paras.add(new Primitive(f));

        return this;
    }

    public Parameters add(int i) {
        paras.add(new Primitive(i));

        return this;
    }

    public Parameters add(long l) {
        paras.add(new Primitive(l));

        return this;
    }

    public Parameters add(short sh) {
        paras.add(new Primitive(sh));

        return this;
    }

    public Parameters add(Object obj) {
        paras.add(obj);

        return this;
    }

    public Parameters add(String s) {
        paras.add(s);

        return this;
    }

    public Parameters clear() {
        paras.clear();

        return this;
    }

    public List getParameterList() {
        return paras;
    }

    public Iterator iterator() {
        return paras.iterator();
    }

    public int size() {
        return paras.size();
    }

    public Object get(int i) {
        return paras.get(i);
    }

    public Object[] toArray() {
        int size = this.paras.size();
        Object[] rtValue = new Object[size];
        Iterator ite = this.paras.iterator();

        for (int i = 0; i < size; i++)
            rtValue[i] = ite.next();

        return rtValue;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Parameters)) {
            return false;
        }

        Parameters v = (Parameters) obj;

        if (paras.size() != v.size()) {
            return false;
        }

        for (Iterator ite = paras.iterator(), itev = v.iterator();
                ite.hasNext();) {
            Object para = ite.next();
            Object pv = itev.next();

            if (!((null == para) ? (null == pv) : (para.equals(pv)))) {
                return false;
            }
        }

        return true;
    }

    public int hashCode() {
        int hashCode = 5;

        for (Iterator ite = paras.iterator(); ite.hasNext();) {
            Object o = ite.next();
            hashCode += ((31 * hashCode) + ((null == o) ? 0 : o.hashCode()));
        }

        return hashCode;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        String line_separator = System.getProperty("line.separator");

        for (Iterator ite = paras.iterator(); ite.hasNext();) {
            Object o = ite.next();

            if (o instanceof Primitive) {
                sb.append(((Primitive) o).getType()).append("<<<")
                  .append(((Primitive) o).toString()).append(line_separator);

                continue;
            }

            if (o != null) {
                if (o.getClass().isArray()) {
                    Class tmp = o.getClass();
                    int dimension = 0;

                    while (tmp.isArray()) {
                        tmp = tmp.getComponentType();
                        dimension++;
                    }

                    sb.append(tmp.getName());

                    for (int k = 0; k < dimension; k++)
                        sb.append("[]");
                } else {
                    sb.append(o.getClass().getName());
                }

                sb.append("<<<").append(Format.objectToString(o)).append(line_separator);
            } else {
                sb.append("java.lang.Object<<<null").append(line_separator);
            }
        }

        return sb.toString();
    }
    
    /**
     * Returns a shallow copy of the value list;
     */
    public Object clone(){
        
        if(paras instanceof ArrayList){
            return new Parameters((List)((ArrayList)paras).clone());
        }
        
        ArrayList list = new ArrayList();

        for (Iterator ite = paras.iterator(); ite.hasNext();) {
            Object o = ite.next();
            list.add(o);
        }

        return new Parameters(list);
    }
}
