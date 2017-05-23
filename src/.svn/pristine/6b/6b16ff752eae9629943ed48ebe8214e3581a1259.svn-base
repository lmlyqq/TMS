package com.rd.client.action.tms.dispatch;

import java.util.HashMap;

import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.geocode.DirectionQueryOptions;
import com.google.gwt.maps.client.geocode.DirectionResults;
import com.google.gwt.maps.client.geocode.Directions;
import com.google.gwt.maps.client.geocode.DirectionsCallback;
import com.google.gwt.maps.client.geocode.DirectionsPanel;
import com.google.gwt.maps.client.geocode.Waypoint;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 线路预览
 * @author yuanlei
 *
 */
public class ViewRouteAction implements ClickHandler {

	private SGTable loadTable;
	private Waypoint loadPoints[] = null;
	private LatLng init_addr;
	private Window win;
	private String map_ret;
	public ViewRouteAction(SGTable p_loadTable) {
		this.loadTable = p_loadTable;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		
		ListGridRecord rec = loadTable.getSelectedRecord();
		final String fld = "F_GETLNGLAT(" + rec.getAttribute("LOAD_NO") + ")";
		Util.db_async.getSingleRecord(fld, "dual", " ", null, new AsyncCallback<HashMap<String, String>>() {
			@Override
			public void onSuccess(HashMap<String, String> result) {
				if(result != null && ObjUtil.isNotNull(fld)) {
					final String ret = result.get(fld);
					map_ret = ret;
					String KEY = StaticRef.API_KEY_IN;
					if(Cookies.getCookie("IP_ADDR").indexOf(StaticRef.OUT_IP) < 0) {
						KEY = StaticRef.API_KEY_OUT;
					}
					Maps.loadMapsApi(KEY, "2", false, new Runnable() {
				        public void run() {
				        	buildUI(ret, false);
						}
					});				
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void buildUI(String ret, boolean avoidHighWays) {
		
		String[] arys = ret.split(",");
		if(arys != null && arys.length > 0) {		
	    	String[] shpm_lnglat = arys[0].split("@");
	    	LatLng addr = null;
			double lng = 0.00, lat = 0.00;
			
			//计算调度单里程数
		    loadPoints = new Waypoint[(shpm_lnglat.length - 1)/2];
		    //final String LOAD_NO = shpm_lnglat[0];
	        for(int j = 0; j < (shpm_lnglat.length-1)/2; j++) {
	    	    lng = Double.parseDouble(shpm_lnglat[2*j + 1]);
			    lat = Double.parseDouble(shpm_lnglat[2*j + 2]);
			    addr = LatLng.newInstance(lng,lat);
			    loadPoints[j] = new Waypoint(addr);
			    if(j == 0) {
			    	init_addr = addr;
			    }
	        }
	        
	        if(win == null) {
	        	win = new Window();
	        }
	        else {
	        	Canvas[] cvs = win.getItems();
	        	if(cvs != null && cvs.length > 0) {
	        		win.removeItem(cvs[0]);
	        	}
	        }
	        win.setTitle("线路信息");
	        win.setLeft((Page.getScreenWidth() - Page.getScreenHeight() - 300)/2);
	        win.setTop(0);
	        win.setWidth(Integer.toString(Page.getScreenHeight() + 300));
	        win.setHeight(Page.getScreenHeight() - 10);
	        win.setCanDragReposition(true);
	        win.setCanDragResize(true);
	        VStack vt = new VStack();
	        HStack hp = new HStack();
	        MapWidget map = new MapWidget(init_addr, 7);
		    
		    MapUIOptions options = MapUIOptions.newInstance(Size.newInstance(500, 400));
	        options.setLargeMapControl3d(true);
	        options.setPhysicalMapType(false);
	        options.setSatelliteMapType(false);
	        options.setHybridMapType(false);
	        options.setScaleControl(false);
	        map.setUI(options);
		    map.setSize(Integer.toString(Page.getScreenHeight() - 40) + "px", Integer.toString(Page.getScreenHeight() - 40) + "px");
		    
		    hp.addMember(map);
		    HorizontalPanel panel = new HorizontalPanel();
		    final DirectionsPanel directionsPanel = new DirectionsPanel();
		    directionsPanel.setWidth("300px");
		    directionsPanel.setHeight(Integer.toString(Page.getScreenHeight() - 75) + "px");
		    //directionsPanel.setSize("350px", "360px");
		    panel.add(directionsPanel);
		    hp.addMember(vt);
		    DirectionQueryOptions opts = new DirectionQueryOptions(map,
		            directionsPanel);
		    opts.setAvoidHighways(avoidHighWays);
		    
			Directions.loadFromWaypoints(loadPoints, opts, new DirectionsCallback() {
	    		
		        public void onFailure(int statusCode) {
		        	;
		        }
	
		        public void onSuccess(DirectionResults result) {
		        }
		    });
			SGPanel sf = new SGPanel();
			sf.setHeight(20);
			sf.setBackgroundColor("#ffffff");
			SGCheck check = new SGCheck("AVOID_HIGHTWAYS", "避开高速公路");
			check.setHeight(20);
			check.setColSpan(6);
			check.setEndRow(true);
			sf.setItems(check);
			check.setValue(avoidHighWays);
			check.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					if(event.getValue().toString().equals("true")) {
						buildUI(map_ret, true);
					}
					else {
						buildUI(map_ret, false);
					}
					win.redraw();
				}
				
			});
			vt.addMember(sf);
			vt.addMember(panel);
			win.addItem(hp);
			win.addCloseClickHandler(new CloseClickHandler() {
				//for smartgwt3.0
				//@Override
				/*public void onCloseClick(CloseClickEvent event) {
					if(win != null) {
						win.destroy();
						win = null;
					}
				}*/ 

				@Override
				public void onCloseClick(CloseClientEvent event) {
					if(win != null) {
						win.destroy();
						win = null;
					}
				}
				
			});
			if(win.isDrawn()) {
				win.redraw();
			}
			else {
				win.draw();
			}
		}
	}
}
