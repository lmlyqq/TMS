package com.rd.client.obj.system;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.rd.client.common.obj.BaseObj;

/**
 *  系统管理->用户管理
 * @author fanglm 
 *
 */
public class SYS_USER extends BaseObj implements IsSerializable{
	
	private String ID;

	private String USER_ID;
	
	private String USER_NAME;
	
	private String PASSWORD;
	
	private String DEFAULT_ORG_ID;
	
	private String DEFAULT_ORG_PARENTID;
	
	private String DEFAULT_ORG_ID_NAME;
	
	private String USERGRP_ID;
	private String USERGRP_ID_NAME;
	
	private String TEL;
	
	private String MAIL;
	
	private String DESCR;
	
	private String ACTIVE_FLAG;
	
	private String MODIFY_FLAG;
	
	private String CUR_STATUS;
	private String CUR_STATUS_NAME;

	private String SALT;

	private String UDF1;
	
	private String UDF2;
	
	private String ROLE_ID;
	private String ROLE_ID_NAME;
	//FANGLM 2010-12-22 默认仓库
	private String DEFAULT_WHSE_ID;
	private String DEFAULT_WHSE_NAME;
	private String USER_CUSTOMER;
	private String ERROR_LOGIN;
	private String TOKEN;
	private String USER_GROUP;
	private String IP_ADDR;
	private String LOGIN_SYSTEM;
	
	public String getERROR_LOGIN() {
		return ERROR_LOGIN;
	}
	public void setERROR_LOGIN(String eRRORLOGIN) {
		ERROR_LOGIN = eRRORLOGIN;
	}
	public String getUSER_CUSTOMER() {
		return USER_CUSTOMER;
	}
	public void setUSER_CUSTOMER(String uSERCUSTOMER) {
		USER_CUSTOMER = uSERCUSTOMER;
	}
	private String WHSE_NAME;
	public String getWHSE_NAME() {
		return WHSE_NAME;
	}
	public void setWHSE_NAME(String wHSENAME) {
		WHSE_NAME = wHSENAME;
	}
	public String getORG_NAME() {
		return ORG_NAME;
	}
	public void setORG_NAME(String oRGNAME) {
		ORG_NAME = oRGNAME;
	}
	private String ORG_NAME;
	
	public String getROLE_ID() {
		return ROLE_ID;
	}
	public void setROLE_ID(String rOLEID) {
		ROLE_ID = rOLEID;
	}
	public SYS_USER() {
		
	}
	public SYS_USER(String user_id,String user_name,String tel,String descr){
		this.USER_ID = user_id;
		this.USER_NAME = user_name;
		this.TEL = tel;
		this.DESCR = descr;
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	
	public String getSALT() {
		return SALT;
	}

	public void setSALT(String sALT) {
		SALT = sALT;
	}

	public String getCUR_STATUS_NAME() {
		return CUR_STATUS_NAME;
	}

	public void setCUR_STATUS_NAME(String cURSTATUSNAME) {
		CUR_STATUS_NAME = cURSTATUSNAME;
	}

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String uSERID) {
		USER_ID = uSERID;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String uSERNAME) {
		USER_NAME = uSERNAME;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getDEFAULT_ORG_ID() {
		return DEFAULT_ORG_ID;
	}

	public void setDEFAULT_ORG_ID(String dEFAULTORGID) {
		DEFAULT_ORG_ID = dEFAULTORGID;
	}
	
	public String getDEFAULT_ORG_PARENTID() {
		return DEFAULT_ORG_PARENTID;
	}

	public void setDEFAULT_ORG_PARENTID(String dEFAULTORGID) {
		DEFAULT_ORG_PARENTID = dEFAULTORGID;
	}
	
	public String getDEFAULT_ORG_ID_NAME() {
		return DEFAULT_ORG_ID_NAME;
	}
	public void setDEFAULT_ORG_ID_NAME(String dEFAULTORGIDNAME) {
		DEFAULT_ORG_ID_NAME = dEFAULTORGIDNAME;
	}

	public String getUSERGRP_ID() {
		return USERGRP_ID;
	}
	public void setUSERGRP_ID(String uSERGRPID) {
		USERGRP_ID = uSERGRPID;
	}
	public String getUSERGRP_ID_NAME() {
		return USERGRP_ID_NAME;
	}
	public void setUSERGRP_ID_NAME(String uSERGRPIDNAME) {
		USERGRP_ID_NAME = uSERGRPIDNAME;
	}
	public String getROLE_ID_NAME() {
		return ROLE_ID_NAME;
	}
	public void setROLE_ID_NAME(String rOLEIDNAME) {
		ROLE_ID_NAME = rOLEIDNAME;
	}
	public String getTEL() {
		return TEL;
	}

	public void setTEL(String tEL) {
		TEL = tEL;
	}

	public String getMAIL() {
		return MAIL;
	}

	public void setMAIL(String mAIL) {
		MAIL = mAIL;
	}

	public String getDESCR() {
		return DESCR;
	}

	public void setDESCR(String dESCR) {
		DESCR = dESCR;
	}

	public String getACTIVE_FLAG() {
		return ACTIVE_FLAG;
	}

	public void setACTIVE_FLAG(String aCTIVEFLAG) {
		ACTIVE_FLAG = aCTIVEFLAG;
	}

	public String getMODIFY_FLAG() {
		return MODIFY_FLAG;
	}

	public void setMODIFY_FLAG(String mODIFYFLAG) {
		MODIFY_FLAG = mODIFYFLAG;
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
	
	public String getCUR_STATUS() {
		return CUR_STATUS;
	}

	public void setCUR_STATUS(String cURSTATUS) {
		CUR_STATUS = cURSTATUS;
	}
	public String getDEFAULT_WHSE_ID() {
		return DEFAULT_WHSE_ID;
	}
	public void setDEFAULT_WHSE_ID(String dEFAULTWHSEID) {
		DEFAULT_WHSE_ID = dEFAULTWHSEID;
	}
	public String getDEFAULT_WHSE_NAME() {
		return DEFAULT_WHSE_NAME;
	}
	public void setDEFAULT_WHSE_NAME(String dEFAULTWHSENAME) {
		DEFAULT_WHSE_NAME = dEFAULTWHSENAME;
	}
	public String getTOKEN() {
		return TOKEN;
	}
	public void setTOKEN(String tOKEN) {
		TOKEN = tOKEN;
	}
	public String getUSER_GROUP() {
		return USER_GROUP;
	}
	public void setUSER_GROUP(String uSERGROUP) {
		USER_GROUP = uSERGROUP;
	}
	public String getIP_ADDR() {
		return IP_ADDR;
	}
	public void setIP_ADDR(String iPADDR) {
		IP_ADDR = iPADDR;
	}
	public String getLOGIN_SYSTEM() {
		return LOGIN_SYSTEM;
	}
	public void setLOGIN_SYSTEM(String lOGINSYSTEM) {
		LOGIN_SYSTEM = lOGINSYSTEM;
	}
	
}