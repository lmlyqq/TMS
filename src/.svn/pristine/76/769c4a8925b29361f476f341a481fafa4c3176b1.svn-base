package com.rd.client.win;

import java.util.HashMap;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGText;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 托运单管理--列表右键 -- 汇总功能
 * @author fanglm
 *
 */
public class CountWin extends Window{

	private int width = 145;
	private int height = 150;
	private String top = "38%";
	private String left = "40%";
	private String title = "汇总";
	private ListGrid table;
	
	public Window window;
	
	public CountWin(ListGrid table){
		this.table = table;
	}
	
	public Window getViewPanel() {
		
		VLayout lay = new VLayout();
		
		SGPanel panel = new SGPanel();
		panel.setNumCols(1);
		
		final SGText qnty = new SGText("SUM_QNTY", "EA数");
		qnty.setTitle(ColorUtil.getBlackTitle(Util.TI18N.QNTY_EACH()));
		qnty.setDisabled(true);
		final SGText gross_w = new SGText("SUM_GROSS_W", "毛重");
		gross_w.setTitle(ColorUtil.getBlackTitle(Util.TI18N.TOT_GROSS_W()));
		gross_w.setDisabled(true);
		final SGText vol = new SGText("SUM_VOL", "体积");
		vol.setTitle(ColorUtil.getBlackTitle(Util.TI18N.TOT_VOL()));
		vol.setDisabled(true);
		
		panel.setItems(qnty,gross_w,vol);
		
		lay.addMember(panel);
		
		if(table != null) {
			if(table.getSelection().length > 1){
				ListGridRecord[] records = table.getSelection();
				double q = 0,g=0,v=0;
				for(int i= 0;i<records.length;i++){
					q = q + Double.parseDouble(ObjUtil.ifNull(records[i].getAttribute("TOT_QNTY_EACH"),"0"));
					g = g + Double.parseDouble(ObjUtil.ifNull(records[i].getAttribute("TOT_GROSS_W"),"0"));
					v = v + Double.parseDouble(ObjUtil.ifNull(records[i].getAttribute("TOT_VOL"),"0"));
				}
				
				qnty.setValue(NumberFormat.getFormat(StaticRef.QNTY_FLOAT).format(q));
				gross_w.setValue(NumberFormat.getFormat(StaticRef.GWT_FLOAT).format(g));
				vol.setValue(NumberFormat.getFormat(StaticRef.VOL_FLOAT).format(v));
			
			}
			else if(table.getSelection().length == 1){
				StringBuffer sql = new StringBuffer();
				sql.append("select SUM(tot_qnty_each) as TOT_QNTY_EACH,SUM(TOT_GROSS_W) AS TOT_GROSS_W,SUM(TOT_VOL) AS TOT_VOL ");
				sql.append(ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), ""));
				
				Util.db_async.execCountSql(sql.toString(), new AsyncCallback<HashMap<String,String>>() {
					
					@Override
					public void onSuccess(HashMap<String, String> result) {
						double q = Double.valueOf(result.get("TOT_QNTY_EACH"));
						double g = Double.valueOf(ObjUtil.ifNull(result.get("TOT_GROSS_W"),"0"));
						double v = Double.valueOf(ObjUtil.ifNull(result.get("TOT_VOL"),"0"));
						qnty.setValue(NumberFormat.getFormat(StaticRef.QNTY_FLOAT).format(q));
						gross_w.setValue(NumberFormat.getFormat(StaticRef.GWT_FLOAT).format(g));
						vol.setValue(NumberFormat.getFormat(StaticRef.VOL_FLOAT).format(v));
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(lay);
		window.setShowCloseButton(true);
		window.show();
		
		window.addMinimizeClickHandler(new MinimizeClickHandler() {
			
			@Override
			public void onMinimizeClick(MinimizeClickEvent event) {
				if(window != null){
					window.minimize();
					window.destroy();
				}
				
			}
		});
		window.addCloseClickHandler(new CloseClickHandler() {

			//for smartgwt3.0
			/*@Override
			public void onCloseClick(CloseClickEvent event) {
				if(window != null){
					window.minimize();
					window.destroy();
				}
			}*/

			@Override
			public void onCloseClick(CloseClientEvent event) {
				if(window != null){
					window.minimize();
					window.destroy();
				}
			}
		});
		
		return window;
		
	}
}
