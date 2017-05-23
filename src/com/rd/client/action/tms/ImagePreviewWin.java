package com.rd.client.action.tms;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.viewer.DetailViewerField;


/**
 * 上传图片预览通用窗口
 * @author lijun
 * @editor fanglm 2010-12-29 15:38
 *
 */
public class ImagePreviewWin extends Window {
	private int width = 600;
	private int height = 227;
	private Window window;
	private ImgRecord[] imgrecord;
	public SGTable table;
	private String type;
	private String fieldName;
	public String fileName;
	public String imageName;
	private Canvas canvas;
	private String stat = "false";
	
	public ImagePreviewWin(){
		
	}
	public ImagePreviewWin(SGTable table){
		this.table = table;
	}
	public ImagePreviewWin(SGTable table,String type,String fieldName){
		this.table = table;
		this.type = type;
		this.fieldName = fieldName;
	}
	
	public void getViewPanel(){
		final String path = type + table.getSelectedRecord().getAttribute(fieldName);
		
		Util.db_async.getImageNames(path, new AsyncCallback<ArrayList<String>>() {
			
			@Override
			public void onSuccess(ArrayList<String> result) {
				ArrayList<String> list = result;
				
				if(list != null){
					imgrecord = new ImgRecord[list.size()];
			    	for(int i = 0; i<list.toArray().length; i++){
			    		imgrecord[i]= new ImgRecord(list.get(i));
			    	}
			    	show(path);
			    	
			    }else {
			    	MSGUtil.sayInfo("无预览图片.");
			    }
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
	}
	private void show(final String p){
		final TileGrid tileGrid = new TileGrid();  
		tileGrid.setTileWidth(194);  
		tileGrid.setTileHeight(165);  
		tileGrid.setHeight(200);  
		tileGrid.setWidth100();  
		tileGrid.setCanDrag(true);  
		tileGrid.setCanAcceptDrop(true);  
		tileGrid.setShowAllRecords(true);  
		tileGrid.setData(imgrecord);

		
		String http = p;
		if(http.indexOf("#") > 0){
			http = http.replace("#", "%23");
		}
		final String path = http;
		String imgpath = path.substring(path.indexOf("test"));
		
		DetailViewerField pictureField = new DetailViewerField("picture");  
		DetailViewerField nameField = new DetailViewerField("name"); 
		pictureField.setType("image");  
		pictureField.setImageURLPrefix(imgpath+"/");
		
		pictureField.setImageWidth(186);  
		pictureField.setImageHeight(120);
		tileGrid.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				if(stat.equals("true")){
					return;
				}
				fileName = event.getRecord().getAttribute("name");
				imageName =  p + "/" +fileName;
				
				canvas = new Canvas();
				canvas.setLeft((Page.getWidth() - 800)/2);
				canvas.setTop((Page.getHeight() - 600)/2);
				stat = "true";
				Img img = new Img(path.substring(7) + "/" + event.getRecord().getAttribute("name"), 800, 600);
				img.addDoubleClickHandler(new DoubleClickHandler() {

					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						canvas.destroy();
						stat = "false";
					}
				});
				
				canvas.addChild(img);
				canvas.draw();
				canvas.bringToFront();
			}
		});
		
		tileGrid.setCanDrop(true);
		final Menu menu = new Menu();
	    menu.setWidth(140);
		
		MenuItem deleteItem = new MenuItem("删除");
		deleteItem.setKeyTitle("Ctrl+D");
 	    KeyIdentifier deleteKey = new KeyIdentifier();
 	    deleteKey.setCtrlKey(true);
 	    deleteKey.setKeyName("D");
 	    deleteItem.setKeys(deleteKey);
		menu.setItems(deleteItem);
		
		tileGrid.setFields(pictureField,nameField);  
		tileGrid.addShowContextMenuHandler(new ShowContextMenuHandler() {
			
			@Override
			public void onShowContextMenu(ShowContextMenuEvent event) {
				menu.showContextMenu();
                event.cancel();
			}
		});
		
		deleteItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				
				SC.confirm("确定要删除文件吗？", new BooleanCallback() {
					public void execute(Boolean value) { 
					 if(value!=null && value){
						 try {
							 Util.db_async.isDelete(imageName, new AsyncCallback<Boolean>() {
									
									@Override
									public void onSuccess(Boolean result) {
										window.destroy();
										getViewPanel();
										tileGrid.redraw();
									}
									
									@Override
									public void onFailure(Throwable caught) {
										
									}
								});
						} catch (Exception e) {
						   SC.warn("文件删除失败！");
						}
							
					 }
					}
	            });
			}
		});
        tileGrid.draw(); 
        
       
        window = new Window();

		window.setTitle("影像浏览");
		window.setLeft("20%");
		window.setTop("25%");
		window.setWidth(width);  
		window.setHeight(height); 
		window.setAlign(Alignment.CENTER);
		window.setCanDragReposition(true);  
		window.setCanDragResize(true);
		window.addItem(tileGrid);
		window.setCanDragResize(false);
		
		window.show();
	}

}
