package com.rd.client.common.action;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;

/**
 * 检查字段值是否存在
 * @author fanglm
 *
 */
public class CheckUnique implements BlurHandler{
	
	private String tableName;
	
	private String columeName;
	
	private TextItem textItem;
	
	public CheckUnique(String tableName,String columeName,TextItem textItem){
		this.tableName = tableName;
		this.columeName = columeName;
		this.textItem = textItem;
	}

	@Override
	public void onBlur(BlurEvent event) {
		final TextItem item = textItem;
		if(textItem.getValue() != null){
			String value = textItem.getValue().toString();
			if(ObjUtil.isNotNull(value)){
				Util.async.countChild(tableName, columeName, value, new AsyncCallback<Integer>() {
					
					@Override
					public void onSuccess(Integer result) {
						if(result > 0){
							item.setHint("<nobr>"+ColorUtil.getRedTitle("中文描述已存在，请检查数据."));
						}
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
			}
		}else{
			item.setHint("<nobr>"+ColorUtil.getRedTitle("中文描述未填写."));
		}
	}

}
