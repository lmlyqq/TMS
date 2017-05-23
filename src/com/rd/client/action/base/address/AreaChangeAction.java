package com.rd.client.action.base.address;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGText;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

/**
 * 省/市/区/街道联动事件
 * @author Administrator
 *
 */
public class AreaChangeAction implements ChangedHandler{

	private SGCombo item;
	
	private SGText fname;
	private SGText itemName;
	private SGCombo item2;
	private ComboBoxItem item3;
	private SGLText item4;
	private SGText item5;
	private SGText item6;
	
	public AreaChangeAction(SGText fatherName, SGCombo childItem, SGText childName) {
		item = childItem;
		fname = fatherName;
		itemName = childName;
	}
	
	public AreaChangeAction(SGText fatherName, SGCombo childItem, SGText childName, SGCombo childItem2) {
		item = childItem;
		fname = fatherName;
		itemName = childName;
		item2 = childItem2;
	}
	
	public AreaChangeAction(SGText fatherName, SGCombo childItem, SGText childName,ComboBoxItem item3,SGLText item4,SGText item5,SGText item6){
		item = childItem;
		fname = fatherName;
		itemName = childName;
		this.item3=item3;
		this.item4=item4;
		this.item5=item5;
		this.item6=item6;
	}
	
	@Override
	public void onChanged(ChangedEvent event) {
		clearValue();
		SGCombo fatherItem = (SGCombo)event.getSource();
		String value = fatherItem.getDisplayValue();
		if(ObjUtil.isNotNull(value) && fname != null){
			fname.setValue(value);
		}
		String province = ObjUtil.ifObjNull(event.getValue(), "").toString();
		event.getForm().clearValue(item.getFieldName());
		item.setValueMap("");
		itemName.setValue("");
		if(ObjUtil.isNotNull(province)) {
			Util.async.getComboValue("BAS_AREA", "AREA_CODE", "AREA_CNAME", " where parent_area_id = '" + province + "'", "", new AsyncCallback<LinkedHashMap<String, String>>() {
				
				public void onFailure(Throwable caught) {	
					;
				}
				public void onSuccess(LinkedHashMap<String, String> result) {
					if(result != null && result.size() > 0) {
						item.clearValue();
						item.setValueMap(result);
						if(item2!=null){
							item2.clearValue();
							item2.setValueMap(result);
						}
					}
				}					
			});
		}
	}
	private void clearValue(){
		if(item3 !=null){
			item3.clearValue();
		}
		if(item4 !=null){
			item4.clearValue();
		}
		if(item5 !=null){
			item5.clearValue();
		}
		if(item6 !=null){
			item6.clearValue();
		}
	}

}
