package com.gengfo.mapping.toplink;

import com.gengfo.mapping.utils.FieldPair;

public class TableRel {

	private String sourceTbName;
	private String sourceAliasName;
	private String middleTbName; // only for many to many case
	private String destTbName;
	private String relType; // OneToOne ; OneToMany ; ManyToMany
	// to add descrition to resolve refer to same table
	private FieldPair[] fieldPairs; //if many to many to get relationship table pair
	
	private org.eclipse.persistence.mappings.DatabaseMapping eclipseLinkDm ;
	
	private oracle.toplink.mappings.DatabaseMapping toplinkDm;
	
	private oracle.toplink.mappings.ManyToManyMapping toplinkMMDm; 

	
	public oracle.toplink.mappings.ManyToManyMapping getToplinkMMDm() {
        return toplinkMMDm;
    }

    public void setToplinkMMDm(oracle.toplink.mappings.ManyToManyMapping toplinkMMDm) {
        this.toplinkMMDm = toplinkMMDm;
    }

    public org.eclipse.persistence.mappings.DatabaseMapping getEclipseLinkDm() {
        return eclipseLinkDm;
    }

    public void setEclipseLinkDm(org.eclipse.persistence.mappings.DatabaseMapping eclipseLinkDm) {
        this.eclipseLinkDm = eclipseLinkDm;
    }

    public oracle.toplink.mappings.DatabaseMapping getToplinkDm() {
        return toplinkDm;
    }

    public void setToplinkDm(oracle.toplink.mappings.DatabaseMapping toplinkDm) {
        this.toplinkDm = toplinkDm;
    }

    public String getMiddleTbName() {
		return middleTbName;
	}

	public void setMiddleTbName(String middleTbName) {
		this.middleTbName = middleTbName;
	}

	public String getDestTbName() {
		return destTbName;
	}

	public void setDestTbName(String destTbName) {
		this.destTbName = destTbName;
	}

	public FieldPair[] getFieldPairs() {
		return fieldPairs;
	}

	public void setFieldPairs(FieldPair[] fieldPairs) {
		this.fieldPairs = fieldPairs;
	}

	public String getSourceTbName() {
		return sourceTbName;
	}

	public void setSourceTbName(String sourceTbName) {
		this.sourceTbName = sourceTbName;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" ");
		sb.append("=== TABLE RELATIONSHIP ===");
		sb.append(" ");

		if (null != this.getSourceTbName()) {
			sb.append(this.getSourceTbName());
		}
		sb.append(" ---> ");

		if (null != this.getDestTbName()) {
			sb.append(this.getDestTbName());
		}
		sb.append(" ");

		if (null != this.getRelType()) {
			sb.append(this.getRelType());
		}
		sb.append(" ");

		if (null != this.getFieldPairs()) {
			for (int i = 0; i < this.getFieldPairs().length; i++) {
				FieldPair fp = this.getFieldPairs()[i];
				if (null != fp.getSourceFd()) {
					sb.append(fp.getSourceFd());
				}

				sb.append(" =");

				if (null != fp.getDestinationFd()) {
					sb.append(fp.getDestinationFd());
				}
				sb.append(" ");
			}
		}

		return sb.toString();
	}

	public String getRelType() {
		return relType;
	}

	public void setRelType(String relType) {
		this.relType = relType;
	}

	public String getSourceAliasName() {
		return sourceAliasName;
	}

	public void setSourceAliasName(String sourceAliasName) {
		this.sourceAliasName = sourceAliasName;
	}

}
