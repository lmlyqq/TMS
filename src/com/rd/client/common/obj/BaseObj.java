package com.rd.client.common.obj;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 数据模型的继承类，避免重复生成ID,ADDWHO,ADDTIME,EDITWHO,EDITIME,ENABLE_FLAG,MODIFY_FLAG,MMC等
 * @author yuanlei
 *
 */
public class BaseObj implements IsSerializable{

	/**
	 * 添加人
	 */
	protected String ADDWHO;
	/**
	 * 添加时间
	 */
	protected Date ADDTIME;
	/**
	 * 修改人
	 */
	protected String EDITWHO;
	/**
	 * 修改时间
	 */
	protected Date EDITTIME;
	/**
	 * GUID
	 */
	protected String ID;
	/**
	 * 助记码
	 */
	protected String HINT_CODE;
	/**
	 * 可编辑标记
	 */
	protected boolean MODIFY_FLAG;
	/**
	 * 可用标记
	 */
	protected boolean ENABLE_FLAG;  
	public boolean isMODIFY_FLAG() {
		return MODIFY_FLAG;
	}
	public void setMODIFY_FLAG(boolean mODIFYFLAG) {
		MODIFY_FLAG = mODIFYFLAG;
	}
	public boolean isENABLE_FLAG() {
		return ENABLE_FLAG;
	}
	public void setENABLE_FLAG(boolean eNABLEFLAG) {
		ENABLE_FLAG = eNABLEFLAG;
	}
	public String getHINT_CODE() {
		return HINT_CODE;
	}
	public void setHINT_CODE(String hINT_CODE) {
		HINT_CODE = hINT_CODE;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getADDWHO() {
		return ADDWHO;
	}
	public void setADDWHO(String aDDWHO) {
		ADDWHO = aDDWHO;
	}
	public Date getADDTIME() {
		return ADDTIME;
	}
	public void setADDTIME(Date aDDTIME) {
		ADDTIME = aDDTIME;
	}
	public String getEDITWHO() {
		return EDITWHO;
	}
	public void setEDITWHO(String eDITWHO) {
		EDITWHO = eDITWHO;
	}
	public Date getEDITTIME() {
		return EDITTIME;
	}
	public void setEDITTIME(Date eDITTIME) {
		EDITTIME = eDITTIME;
	}
}
