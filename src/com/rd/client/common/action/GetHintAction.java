package com.rd.client.common.action;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;

/**
 * 生成助记码
 * @author fanglm
 * @param textItem 转换字符串来源字段
 * @param codeItem 被赋值控件 ... 第一个必须为助记码
 *
 */
public class GetHintAction implements BlurHandler,EditorExitHandler{
	
	private TextItem textItem;
	private TextItem[] codeItem;
	private ListGrid table;

	public GetHintAction(TextItem textItem,TextItem... codeItem){
		this.textItem = textItem;
		this.codeItem = codeItem;
	}
	public GetHintAction(ListGrid table){
		this.table = table;
	}

	@Override
	public void onBlur(BlurEvent event) {
		if(textItem.getValue() == null)
			return;
		String val = textItem.getValue().toString();
//		textItem.setValue(val);
		//生成助记码
		if(ObjUtil.isNotNull(val)){
			Util.async.getHintCode(val, new AsyncCallback<String>() {
			
				@Override
				public void onSuccess(String result) {
					for(int i=0;i<codeItem.length;i++){
						if(i==0)
							codeItem[i].setValue(result);
						else if(codeItem[i].getValue() == null){
							codeItem[i].setValue(result);
						}
					}
				}
			
				@Override
				public void onFailure(Throwable caught) {
				
				}
			});
		}
	}

	@Override
	public void onEditorExit(EditorExitEvent event) {
		if(ObjUtil.isNotNull(event.getNewValue())){
			String content = event.getNewValue().toString();
			final int row = event.getRowNum();
			Util.async.getHintCode(content, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					table.setEditValue(row, "HINT_CODE", result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
		}
	}

}
