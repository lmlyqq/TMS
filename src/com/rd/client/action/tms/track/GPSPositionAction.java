package com.rd.client.action.tms.track;


import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 车辆定位右键
 * @author fanglm 
 *
 */
public class GPSPositionAction implements ClickHandler {

	private ListGrid table;
	

	public GPSPositionAction(ListGrid table) {
		this.table = table;
	}


	@Override
	public void onClick(MenuItemClickEvent event) {
		ListGridRecord record = table.getSelectedRecord();
		if (record == null)
			return;
		
		if(!"已发运".equals(record.getAttribute("STATUS_NAME"))){
			MSGUtil.sayError("操作失败！只能对【已发运】的作业单做车辆定位！");
			return;
		}
		String PLATE_NO=record.getAttribute("PLATE_NO");
		
		
		
	    com.google.gwt.user.client.Window.open("http://61.129.66.212/interface/locationServiceInfo.jsp?vehicleNo="+PLATE_NO+"&userName="+StaticRef.GpsAccount+"&password="+StaticRef.GpsPassWord,"","");
		
	}

}
