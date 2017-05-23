package com.rd.client.action.tms.dispatch;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.geocode.DirectionResults;
import com.google.gwt.maps.client.geocode.Directions;
import com.google.gwt.maps.client.geocode.DirectionsCallback;
import com.google.gwt.maps.client.geocode.Route;
import com.google.gwt.maps.client.geocode.Waypoint;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 配车确认
 * @author yuanlei
 *
 */
public class LoadDispatchConfirmAction implements ClickHandler {

	private SGTable loadTable;
    private HashMap<String, Object> list_map; 
    private HashMap<String, String> shpm_map; 
    private HashMap<String, String> dist_map; 
	public LoadDispatchConfirmAction(SGTable p_loadTable) {
		this.loadTable = p_loadTable;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		
		ListGridRecord[] records = loadTable.getSelection();
		if(records != null && records.length > 0) {
			list_map = new HashMap<String, Object>(); 
			HashMap<String, String> load_map = new HashMap<String, String>(); //调度单   
			for(int i = 0; i < records.length; i++) {
				if(!records[i].getAttribute("DISPATCH_STAT_NAME").equals(StaticRef.NO_DISPATCH_NAME)) {
					SC.warn("调度单[" + records[i].getAttribute("LOAD_NO") + "]配车状态为[" + records[i].getAttribute("DISPATCH_STAT_NAME") + "],不能执行配车确认!");
					return;
				}
				if(!records[i].getAttribute("STATUS_NAME").equals(StaticRef.TRANS_CREATE_NAME)) {
					SC.warn("调度单[" + records[i].getAttribute("LOAD_NO") + "]非已创建状态,不能执行配车确认!");
					return;
				}
				load_map.put(Integer.toString(i+1), records[i].getAttribute("LOAD_NO"));
			}
			list_map.put("1", load_map);
			list_map.put("2", LoginCache.getLoginUser().getUSER_ID());
			String json = Util.mapToJson(list_map);
			Util.async.execProcedure(json, "SP_LOADNO_DISPATCHCONFIRM(?,?,?)", new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.showOperSuccess();
						//刷新调度单
						ListGridRecord[] records = loadTable.getSelection();
						for(int i = 0; i < records.length; i++) {
							records[i].setAttribute("DISPATCH_STAT_NAME", StaticRef.DISPATCHED_NAME);
							loadTable.updateData(records[i]);
						}
						loadTable.redraw();
						
						final String ret = result;
						String KEY = StaticRef.API_KEY_IN;
						if(Cookies.getCookie("IP_ADDR").indexOf(StaticRef.OUT_IP) < 0) {
							KEY = StaticRef.API_KEY_OUT;
						}
						Maps.loadMapsApi(KEY, "2", false, new Runnable() {
					        public void run() {
								String tmp = ret.substring(2);
								String[] arys = tmp.split(",");
								if(arys != null && arys.length > 0) {
							           
									double lng = 0.00, lat = 0.00;
									Waypoint shpmPoints[] = new Waypoint[2*(arys.length-1)];
									list_map = new HashMap<String, Object>();
									shpm_map = new HashMap<String, String>();
									dist_map = new HashMap<String, String>();
									for(int i = 0; i < arys.length; i++) {
										String[] shpm_lnglat = arys[i].split("@");
										if(i > 0) {
											//计算作业单里程数
											String SHPM_NO = shpm_lnglat[0];
											shpm_map.put(Integer.toString(i), SHPM_NO);
											for(int j = 0; j < 2; j++) {
									    	   lng = Double.parseDouble(shpm_lnglat[2*j + 1]);
											   lat = Double.parseDouble(shpm_lnglat[2*j + 2]);
											   LatLng addr = LatLng.newInstance(lng,lat);
											   shpmPoints[2*(i-1)+j] = new Waypoint(addr);
									        }
									    }								
									}
									Directions.loadFromWaypoints(shpmPoints, new DirectionsCallback() {
										
								        public void onFailure(int statusCode) {
								        	;
								        }
			
								        public void onSuccess(DirectionResults result) {
								            List<Route> routes = result.getRoutes();
								            int x = 1, m = 1;
								            for (Route r : routes) {
								            	//System.out.println(r.getDistance().inLocalizedUnits().replace("&nbsp;公里", "").replace(",", ""));
								            	if(x%2 != 0) {
								            		dist_map.put(Integer.toString(m), r.getDistance().inLocalizedUnits().replace("&nbsp;公里", "").replace(",", ""));
								            		m++;
								            	}
							          	    	x += 1;
								            }
								            //执行存储过程
								            list_map = new HashMap<String, Object>();
								            list_map.put("1", shpm_map);
								            list_map.put("2", dist_map);
											list_map.put("3", LoginCache.getLoginUser().getUSER_ID());
											String json = Util.mapToJson(list_map);
								            Util.async.execProcedure(json, "SP_LOADNO_UPDATEDISTANCE(?,?,?,?)", new AsyncCallback<String>() {

												@Override
												public void onFailure(Throwable caught) {
													;
												}
												@Override
												public void onSuccess(String result) {
													MSGUtil.sayInfo("更新作业单卸货顺序成功!");
													String tmp = result.substring(2);
													String[] arys = tmp.split(",");
													if(arys != null && arys.length > 0) {
												           
														double lng = 0.00, lat = 0.00;
														Waypoint loadPoints[] = null;
														list_map = new HashMap<String, Object>();
														shpm_map = new HashMap<String, String>();
														dist_map = new HashMap<String, String>();
														for(int i = 0; i < arys.length; i++) {
															String[] shpm_lnglat = arys[i].split("@");
															if(i == 0) {
															      	//计算调度单里程数
															   loadPoints = new Waypoint[(shpm_lnglat.length - 1)/2];
															   final String SHPM_NO = shpm_lnglat[0];
															   shpm_map.put(Integer.toString(i+1), SHPM_NO);
														       for(int j = 0; j < (shpm_lnglat.length-1)/2; j++) {
														    	   lng = Double.parseDouble(shpm_lnglat[2*j + 1]);
																   lat = Double.parseDouble(shpm_lnglat[2*j + 2]);
																   LatLng addr = LatLng.newInstance(lng,lat);
																   loadPoints[j] = new Waypoint(addr);
														       }
														       Directions.loadFromWaypoints(loadPoints, new DirectionsCallback() {
														    		
															        public void onFailure(int statusCode) {
															        	;
															        }
										
															        public void onSuccess(DirectionResults result) {
															        	String mile = result.getDistance().inLocalizedUnits().replace("&nbsp;公里", "").replace(",", ""); 
															            StringBuffer sf = new StringBuffer();
															            sf.append("update TRANS_LOAD_HEADER set SETT_MILE = '");
															            sf.append(mile);
															            sf.append("' where LOAD_NO = '");
															            sf.append(SHPM_NO);
															            sf.append("'");
															            Util.async.excuteSQL(sf.toString(), new AsyncCallback<String>() {

																			@Override
																			public void onFailure(Throwable caught) {
																				;
																			}

																			@Override
																			public void onSuccess(String result) {
																	            MSGUtil.sayInfo("更新调度单里程数成功!");
																			}
															            	
															            });
															        }
																});
															}
														}
													}
												}							            								            
								            });
								        }
									});
								}
					        }
						});
						
					}
					else{
						MSGUtil.sayError(result.substring(2));
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		else {
			SC.warn("未选择调度单!");
			return;
		}
	}
}
