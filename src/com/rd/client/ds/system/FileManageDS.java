package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class FileManageDS extends DataSource{
	
	private static FileManageDS instance = null;
	
	public static FileManageDS getInstance(String id){
		if(instance == null){
			instance = new FileManageDS(id);
		}
		return instance;
	}
	
	private FileManageDS(String id){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        
        DataSourceTextField PARENT_ID = new DataSourceTextField("PARENT_ID", "PARENT_ID");
        PARENT_ID.setForeignKey("ID");
        PARENT_ID.setHidden(false);
        
        DataSourceTextField FILE_NAME = new DataSourceTextField("FILE_NAME", "FILE_NAME");
        DataSourceTextField FILE_PATH = new DataSourceTextField("FILE_PATH", "FILE_PATH");
        
        setFields(keyField, FILE_NAME, FILE_PATH, PARENT_ID);
        
        setDataURL("fileManageServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
	}
}
