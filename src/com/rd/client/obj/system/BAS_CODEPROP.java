package com.rd.client.obj.system;


import java.util.HashSet;
import java.util.Set;

import com.rd.client.common.obj.BaseObj;

/**
 * 数据字典反射类
 * @author yuanlei
 *
 */
public class BAS_CODEPROP extends BaseObj{
	
	protected String PROP_ID;
	protected String PROP_CODE;
	protected String NAME_C;
	protected String NAME_E;
	protected String SHOW_SEQ;
	protected String BIZ_TYPE;
	protected String PARENT_PROP_ID;
	protected String UDF1;
	protected String UDF2;
	
	private Set<BAS_CODES> codeList = new HashSet<BAS_CODES>();
	

	public Set<BAS_CODES> getCodeList() {
		return codeList;
	}
	public void setCodeList(Set<BAS_CODES> codeList) {
		this.codeList = codeList;
	}
	public String getPROP_CODE() {
		return PROP_CODE;
	}
	public void setPROP_CODE(String pROPCODE) {
		PROP_CODE = pROPCODE;
	}
	public String getPROP_ID() {
		return PROP_ID;
	}
	public void setPROP_ID(String pROPID) {
		PROP_ID = pROPID;
	}
	public String getNAME_C() {
		return NAME_C;
	}
	public void setNAME_C(String nAMEC) {
		NAME_C = nAMEC;
	}
	public String getNAME_E() {
		return NAME_E;
	}
	public void setNAME_E(String nAMEE) {
		NAME_E = nAMEE;
	}
	public String getSHOW_SEQ() {
		return SHOW_SEQ;
	}
	public void setSHOW_SEQ(String sHOWSEQ) {
		SHOW_SEQ = sHOWSEQ;
	}
	public boolean getMODIFY_FLAG() {
		return MODIFY_FLAG;
	}
	public void setMODIFY_FLAG(boolean mODIFYFLAG) {
		MODIFY_FLAG = mODIFYFLAG;
	}
	public boolean getENABLE_FLAG() {
		return ENABLE_FLAG;
	}
	public void setENABLE_FLAG(boolean eNABLEFLAG) {
		ENABLE_FLAG = eNABLEFLAG;
	}
	public String getBIZ_TYPE() {
		return BIZ_TYPE;
	}
	public void setBIZ_TYPE(String bIZTYPE) {
		BIZ_TYPE = bIZTYPE;
	}
	public String getPARENT_PROP_ID() {
		return PARENT_PROP_ID;
	}
	public void setPARENT_PROP_ID(String pARENTPROPID) {
		PARENT_PROP_ID = pARENTPROPID;
	}
	public String getUDF1() {
		return UDF1;
	}
	public void setUDF1(String uDF1) {
		UDF1 = uDF1;
	}
	public String getUDF2() {
		return UDF2;
	}
	public void setUDF2(String uDF2) {
		UDF2 = uDF2;
	}
}
