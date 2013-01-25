package com.gengfo.common;

import junit.framework.TestCase;

public class ToStringBuilderTest extends TestCase {

	class ParentDTO {
		public ParentDTO() {
			super();
		}

		public String parentDtoId;

		public ChildDTO[] childDtos;

		public ChildDTO[] getChildDtos() {
			return childDtos;
		}

		public void setChildDtos(ChildDTO[] childDtos) {
			this.childDtos = childDtos;
		}

		public String getParentDtoId() {
			return parentDtoId;
		}

		public void setParentDtoId(String parentDtoId) {
			this.parentDtoId = parentDtoId;
		}

	}

	class ChildDTO {
		public ChildDTO() {
			super();
		}

		public String childDtoId;

		public String getChildDtoId() {
			return childDtoId;
		}

		public void setChildDtoId(String childDtoId) {
			this.childDtoId = childDtoId;
		}

	}

	// ///////////////////////////////
	private ParentDTO populateParentDTO() {
		ParentDTO pDto = new ParentDTO();
		pDto.setParentDtoId("partent dto");
		ChildDTO[] cDtos = new ChildDTO[2];
		cDtos[0] = populateChildDTO1();
		cDtos[1] = populateChildDTO2();
		
		pDto.setChildDtos(cDtos);

		return pDto;
	}

	private ChildDTO populateChildDTO1() {
		ChildDTO cDto = new ChildDTO();
		cDto.setChildDtoId("child dto 1");
		return cDto;
	}

	
	
	private ChildDTO populateChildDTO2() {
		ChildDTO cDto = new ChildDTO();
		cDto.setChildDtoId("child dto 2");
		return cDto;
	}
	
	

	// ///////////////////////////////

	public void testToStringBuilderObject() {
		ParentDTO pDto = populateParentDTO();
		String pDtoString = ToStringBuilder.toString(pDto);
		System.out.println(pDtoString);

	}

}
