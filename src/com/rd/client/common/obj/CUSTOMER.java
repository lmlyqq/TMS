package com.rd.client.common.obj;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 系统配置默认客户
 * @author fanglm
 *
 */
public class CUSTOMER implements IsSerializable {

	private String CUSTOMER_ID; //客户id
	
	private String CUSTOMER_NAME;//客户名称
	
	private String LOAD_AREA_ID;//提货区域ID
	
	private String LOAD_AREA_NAME;//提货区域
	
	private String LOAD_ID;//提货单位id
	
	private String LOAD_NAME;//提货单位
	
	private String LOAD_ADDRESS;//提货单位地址
	
	private String LOAD_CONT_NAME;//提货联系人
	
	private String LOAD_CONT_TEL;//提货联系电话
	
	private String UNLOAD_AREA_ID;//送货区域id
	
	private String UNLOAD_AREA_NAME;//送货区域
	
	private String UNLOAD_ID;//送货单位id
	
	private String UNLOAD_NAME;//送货单位
	
	private String UNLOAD_ADDRESS;//送货单位地址
	
	private String UNLOAD_CONT_NAME;//送货联系人
	
	private String UNLOAD_CONT_TEL;//送货联系电话
	
	private String PAY_TYP;//付款方式
	
	private String SETTLE_TYP;//计费方式
	
	private boolean SLF_DELIVER_FLAG;//客户自提
	
	private boolean ADDR_EDIT_FLAG;//地址点可编辑
	
	private boolean SLF_PICKUP_FLAG;//客户自送
	
	private boolean POD_FLAG;//回单
	
	private String DEFAULT_TRANS_SRVC_ID;//运输服务
	
	private String DEFAULT_ODR_TYP;//订单类型
	
	private String TRANS_UOM;//运输包装
	
	private boolean UNIQ_CONO_FLAG;//客户订单号是否唯一
	
	private String SKU_NAME;//默认货品名称
	
	private String SKU_ID;//默认货品id
	
	private String SKU_SPEC;//默认货品规格
	
	private String PACK_ID; //默认货品包装
	
	private String DFT_SKU_ID; //默认货品
	
	private boolean SKU_EDIT_FLAG;
	
	public boolean isSKU_EDIT_FLAG() {
		return SKU_EDIT_FLAG;
	}

	public void setSKU_EDIT_FLAG(boolean sKUEDITFLAG) {
		SKU_EDIT_FLAG = sKUEDITFLAG;
	}

	public String getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}

	public void setCUSTOMER_ID(String cUSTOMERID) {
		CUSTOMER_ID = cUSTOMERID;
	}
	
	public String getLOAD_CONT_NAME() {
		return LOAD_CONT_NAME;
	}

	public void setLOAD_CONT_NAME(String lOADCONTNAME) {
		LOAD_CONT_NAME = lOADCONTNAME;
	}

	public String getLOAD_CONT_TEL() {
		return LOAD_CONT_TEL;
	}

	public void setLOAD_CONT_TEL(String lOADCONTTEL) {
		LOAD_CONT_TEL = lOADCONTTEL;
	}

	public String getCUSTOMER_NAME() {
		return CUSTOMER_NAME;
	}

	public void setCUSTOMER_NAME(String cUSTOMERNAME) {
		CUSTOMER_NAME = cUSTOMERNAME;
	}

	public String getLOAD_AREA_ID() {
		return LOAD_AREA_ID;
	}

	public void setLOAD_AREA_ID(String lOADAREAID) {
		LOAD_AREA_ID = lOADAREAID;
	}

	public String getLOAD_AREA_NAME() {
		return LOAD_AREA_NAME;
	}

	public void setLOAD_AREA_NAME(String lOADAREANAME) {
		LOAD_AREA_NAME = lOADAREANAME;
	}

	public String getLOAD_ID() {
		return LOAD_ID;
	}

	public void setLOAD_ID(String lOADID) {
		LOAD_ID = lOADID;
	}

	public String getLOAD_NAME() {
		return LOAD_NAME;
	}

	public void setLOAD_NAME(String lOADNAME) {
		LOAD_NAME = lOADNAME;
	}

	public String getLOAD_ADDRESS() {
		return LOAD_ADDRESS;
	}

	public void setLOAD_ADDRESS(String lOADADDRESS) {
		LOAD_ADDRESS = lOADADDRESS;
	}

	public String getUNLOAD_AREA_ID() {
		return UNLOAD_AREA_ID;
	}

	public void setUNLOAD_AREA_ID(String uNLOADAREAID) {
		UNLOAD_AREA_ID = uNLOADAREAID;
	}

	public String getUNLOAD_AREA_NAME() {
		return UNLOAD_AREA_NAME;
	}

	public void setUNLOAD_AREA_NAME(String uNLOADAREANAME) {
		UNLOAD_AREA_NAME = uNLOADAREANAME;
	}

	public String getUNLOAD_ID() {
		return UNLOAD_ID;
	}

	public void setUNLOAD_ID(String uNLOADID) {
		UNLOAD_ID = uNLOADID;
	}

	public String getUNLOAD_NAME() {
		return UNLOAD_NAME;
	}

	public void setUNLOAD_NAME(String uNLOADNAME) {
		UNLOAD_NAME = uNLOADNAME;
	}

	public String getUNLOAD_ADDRESS() {
		return UNLOAD_ADDRESS;
	}

	public void setUNLOAD_ADDRESS(String uNLOADADDRESS) {
		UNLOAD_ADDRESS = uNLOADADDRESS;
	}

	public String getUNLOAD_CONT_NAME() {
		return UNLOAD_CONT_NAME;
	}

	public void setUNLOAD_CONT_NAME(String uNLOADCONTNAME) {
		UNLOAD_CONT_NAME = uNLOADCONTNAME;
	}

	public String getUNLOAD_CONT_TEL() {
		return UNLOAD_CONT_TEL;
	}

	public void setUNLOAD_CONT_TEL(String uNLOADCONTTEL) {
		UNLOAD_CONT_TEL = uNLOADCONTTEL;
	}

	public String getPAY_TYP() {
		return PAY_TYP;
	}

	public void setPAY_TYP(String pAYTYP) {
		PAY_TYP = pAYTYP;
	}

	public String getSETTLE_TYP() {
		return SETTLE_TYP;
	}

	public void setSETTLE_TYP(String sETTLETYP) {
		SETTLE_TYP = sETTLETYP;
	}

	public boolean isSLF_DELIVER_FLAG() {
		return SLF_DELIVER_FLAG;
	}

	public void setSLF_DELIVER_FLAG(boolean sLFDELIVERFLAG) {
		SLF_DELIVER_FLAG = sLFDELIVERFLAG;
	}

	public boolean isADDR_EDIT_FLAG() {
		return ADDR_EDIT_FLAG;
	}

	public void setADDR_EDIT_FLAG(boolean aDDREDITFLAG) {
		ADDR_EDIT_FLAG = aDDREDITFLAG;
	}

	public boolean isSLF_PICKUP_FLAG() {
		return SLF_PICKUP_FLAG;
	}

	public void setSLF_PICKUP_FLAG(boolean sLFPICKUPFLAG) {
		SLF_PICKUP_FLAG = sLFPICKUPFLAG;
	}

	public boolean isPOD_FLAG() {
		return POD_FLAG;
	}

	public void setPOD_FLAG(boolean pODFLAG) {
		POD_FLAG = pODFLAG;
	}

	public String getDEFAULT_TRANS_SRVC_ID() {
		return DEFAULT_TRANS_SRVC_ID;
	}

	public void setDEFAULT_TRANS_SRVC_ID(String dEFAULTTRANSSRVCID) {
		DEFAULT_TRANS_SRVC_ID = dEFAULTTRANSSRVCID;
	}

	public String getDEFAULT_ODR_TYP() {
		return DEFAULT_ODR_TYP;
	}

	public void setDEFAULT_ODR_TYP(String dEFAULTODRTYP) {
		DEFAULT_ODR_TYP = dEFAULTODRTYP;
	}

	public String getTRANS_UOM() {
		return TRANS_UOM;
	}

	public void setTRANS_UOM(String tRANSUOM) {
		TRANS_UOM = tRANSUOM;
	}

	public boolean getUNIQ_CONO_FLAG() {
		return UNIQ_CONO_FLAG;
	}

	public void setUNIQ_CONO_FLAG(boolean uNIQCONOFLAG) {
		UNIQ_CONO_FLAG = uNIQCONOFLAG;
	}

	public String getSKU_NAME() {
		return SKU_NAME;
	}

	public void setSKU_NAME(String sKUNAME) {
		SKU_NAME = sKUNAME;
	}

	public String getSKU_ID() {
		return SKU_ID;
	}

	public void setSKU_ID(String sKUID) {
		SKU_ID = sKUID;
	}

	public String getSKU_SPEC() {
		return SKU_SPEC;
	}

	public void setSKU_SPEC(String sKUSPEC) {
		SKU_SPEC = sKUSPEC;
	}

	public String getPACK_ID() {
		return PACK_ID;
	}

	public void setPACK_ID(String pACKID) {
		PACK_ID = pACKID;
	}

	public String getDFT_SKU_ID() {
		return DFT_SKU_ID;
	}

	public void setDFT_SKU_ID(String dFTSKUID) {
		DFT_SKU_ID = dFTSKUID;
	}

	
}
