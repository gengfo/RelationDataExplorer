OR4EclipselinkTest
OR4ToplinkTest


EclipseLinkOutputDataToExcelTest

1. DataHolder to contain the basic data
	db connection
	aliasName:tableName mapping
	tableName:aliasName mapping
	ailasName:descriptor mapping
	tableName:pkName mapping
	
	
2. key steps for getting value
	first input aliasName, key fieldName, keyValue
	put it to unhandle list
	
	visit first item in unhandle list
	move the picked item to handled list
	get aliasName, key fieldName, keyValue
	
	get descritor according to aliasName
	
	
	to get the full set of all (aliasName, key fieldName, keyValue) to show table content
		

	show table content tableName, fieldName, fieldValue
	

