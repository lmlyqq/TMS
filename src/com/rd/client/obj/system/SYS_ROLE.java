package com.rd.client.obj.system;

import com.rd.client.common.obj.BaseObj;

/**
 * 系统管理->角色管理
 * @author yuanlei
 *
 */
public class SYS_ROLE extends BaseObj{

	private String ID;
	private String ROLE_ID;
	private String ROLE_NAME;
	private boolean ENABLE_FLAG;
	private boolean MODIFY_FLAG;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getROLE_ID() {
		return ROLE_ID;
	}
	public void setROLE_ID(String rOLEID) {
		ROLE_ID = rOLEID;
	}
	public String getROLE_NAME() {
		return ROLE_NAME;
	}
	public void setROLE_NAME(String rOLENAME) {
		ROLE_NAME = rOLENAME;
	}
	public boolean isENABLE_FLAG() {
		return ENABLE_FLAG;
	}
	public void setENABLE_FLAG(boolean eNABLEFLAG) {
		ENABLE_FLAG = eNABLEFLAG;
	}
	public boolean isMODIFY_FLAG() {
		return MODIFY_FLAG;
	}
	public void setMODIFY_FLAG(boolean mODIFYFLAG) {
		MODIFY_FLAG = mODIFYFLAG;
	}

}
