package com.rd.client.common.action;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.action.tms.ImgRecord;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
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
 * 基础资料图片预览通用类
 * @author lijun
 *
 */
public class ImageViewAction{
	private ImgRecord[] imgrecord;
	public SGTable table;
	private String type;
	private String fieldName;
	public String fileName;
	public String imageName;
	private Canvas canvas;
	private TileGrid mediaTileGrid;
	private Canvas can;
	private String fileName_;
	
	
	public ImageViewAction(){
		
	}
	
	
	public ImageViewAction(Canvas can,SGTable table,String type,String fieldName){
		this.table = table;
		this.type = type;
		this.fieldName = fieldName;
		this.can = can;
	}
	
	public void getName_(final Canvas ca){
		
		Util.db_async.getImageNames(type + fieldName, new AsyncCallback<ArrayList<String>>() {
			
			@Override
			public void onSuccess(ArrayList<String> result) {
				ArrayList<String> list = result;
				
				if(list != null&&list.size()>0){
					imgrecord = new ImgRecord[list.size()];
			    	for(int i = 0; i<list.toArray().length; i++){
			    		imgrecord[i]= new ImgRecord(list.get(i));
			    	}
			    	show(type + fieldName,ca);
			    	
			    }else {
			    	MSGUtil.sayInfo("无预览图片.");
			    }
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		/*Util.async.readExcel("20170106需求跟进.xls", new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				System.out.println("start");
				if(result != null) {
					for(int i=0; i < result.size(); i++) {
						System.out.println(result.get(i));
					}
				}
				System.out.println("end");
			}
			
		});*/
	}
	public void getName(){
		
		Util.db_async.getImageNames(type + fieldName, new AsyncCallback<ArrayList<String>>() {
			
			@Override
			public void onSuccess(ArrayList<String> result) {
				ArrayList<String> list = result;
				
				if(list != null&&list.size()>0){
					imgrecord = new ImgRecord[list.size()];
			    	for(int i = 0; i<list.toArray().length; i++){
			    		imgrecord[i]= new ImgRecord(list.get(i));
			    	}
			    	show(type + fieldName);
			    	
			    }else {
			    	MSGUtil.sayInfo("无预览图片.");
			    }
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		/*Util.async.readExcel("20170106需求跟进.xls", new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				System.out.println("start");
				if(result != null) {
					for(int i=0; i < result.size(); i++) {
						System.out.println(result.get(i));
					}
				}
				System.out.println("end");
			}
			
		});*/
	}
	private void show(final String p){
	    mediaTileGrid = new TileGrid();
		mediaTileGrid.setTileWidth(160);  
		mediaTileGrid.setTileHeight(165);  
		mediaTileGrid.setHeight(200);  
		mediaTileGrid.setWidth100();  
		mediaTileGrid.setCanDrag(true);  
		mediaTileGrid.setCanAcceptDrop(true);  
		mediaTileGrid.setShowAllRecords(true);
		mediaTileGrid.setShowResizeBar(true);
		mediaTileGrid.setData(imgrecord);

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
		mediaTileGrid.setFields(pictureField,nameField);
		can.addChild(mediaTileGrid);
		mediaTileGrid.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				fileName_ = event.getRecord().getAttribute("name");
				imageName =  p + "/" +fileName_;
			}
		});
		mediaTileGrid.addDoubleClickHandler(new DoubleClickHandler() {
			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				imageName =  p + "/" +fileName_;
				
				canvas = new Canvas();
				canvas.setLeft((Page.getWidth() - 800)/2);
				canvas.setTop((Page.getHeight() - 600)/2);
				Img img = new Img(path.substring(7) + "/" + fileName_, 800, 600);
				img.addDoubleClickHandler(new DoubleClickHandler() {

					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						canvas.destroy();
					}
				});
				canvas.addChild(img);
				canvas.draw();
				canvas.bringToFront();
			}
		});
		
		mediaTileGrid.setCanDrop(true);
		final Menu menu = new Menu();
	    menu.setWidth(140);
		
		MenuItem deleteItem = new MenuItem("删除");
		deleteItem.setKeyTitle("Ctrl+D");
 	    KeyIdentifier deleteKey = new KeyIdentifier();
 	    deleteKey.setCtrlKey(true);
 	    deleteKey.setKeyName("D");
 	    deleteItem.setKeys(deleteKey);
		menu.setItems(deleteItem);
		  
		mediaTileGrid.addShowContextMenuHandler(new ShowContextMenuHandler() {
			
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
										if(result != false){
											deleteAction(p,imageName,mediaTileGrid);
										} else{
											MSGUtil.sayWarning("请选择所要删除的图片！");
										}
									}
									@Override
									public void onFailure(Throwable caught) {
										
									}
								});
						} catch (Exception e) {
						   MSGUtil.sayWarning("文件删除失败！");
						}
					 }
					}
	            });
			}
		});
		
		
		if(!mediaTileGrid.isDrawn()) {
			mediaTileGrid.draw(); 
		}
		else {
			mediaTileGrid.redraw(); 
		}
	}
	private void show(final String p,Canvas cans){
	    mediaTileGrid = new TileGrid();
		mediaTileGrid.setTileWidth(160);  
		mediaTileGrid.setTileHeight(165);  
		mediaTileGrid.setHeight(200);  
		mediaTileGrid.setWidth100();  
		mediaTileGrid.setCanDrag(true);  
		mediaTileGrid.setCanAcceptDrop(true);  
		mediaTileGrid.setShowAllRecords(true);
		mediaTileGrid.setShowResizeBar(true);
		mediaTileGrid.setData(imgrecord);

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
		mediaTileGrid.setFields(pictureField,nameField);
		can.addChild(mediaTileGrid);
		mediaTileGrid.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				fileName_ = event.getRecord().getAttribute("name");
				imageName =  p + "/" +fileName_;
			}
		});
		mediaTileGrid.addDoubleClickHandler(new DoubleClickHandler() {
			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				imageName =  p + "/" +fileName_;
				
				canvas = new Canvas();
				canvas.setLeft((Page.getWidth() - 800)/2);
				canvas.setTop((Page.getHeight() - 600)/2);
				Img img = new Img(path.substring(7) + "/" + fileName_, 800, 600);
				img.addDoubleClickHandler(new DoubleClickHandler() {

					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						canvas.destroy();
					}
				});
				canvas.addChild(img);
				canvas.draw();
				canvas.bringToFront();
			}
		});
		
		mediaTileGrid.setCanDrop(true);
		final Menu menu = new Menu();
	    menu.setWidth(140);
		
		MenuItem deleteItem = new MenuItem("删除");
		deleteItem.setKeyTitle("Ctrl+D");
 	    KeyIdentifier deleteKey = new KeyIdentifier();
 	    deleteKey.setCtrlKey(true);
 	    deleteKey.setKeyName("D");
 	    deleteItem.setKeys(deleteKey);
		menu.setItems(deleteItem);
		  
		mediaTileGrid.addShowContextMenuHandler(new ShowContextMenuHandler() {
			
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
										if(result != false){
											deleteAction(p,imageName,mediaTileGrid);
										} else{
											MSGUtil.sayWarning("请选择所要删除的图片！");
										}
									}
									@Override
									public void onFailure(Throwable caught) {
										
									}
								});
						} catch (Exception e) {
						   MSGUtil.sayWarning("文件删除失败！");
						}
					 }
					}
	            });
			}
		});
		
		
		if(!mediaTileGrid.isDrawn()) {
			mediaTileGrid.draw(); 
			can.draw();
			can.setVisible(true);
		}
		else {
			mediaTileGrid.redraw(); 
		}
	}

   public void deleteAction(String p,String imageName,TileGrid mediaTileGrid){
	   if(can.isCreated()){
		   can.clear();
		   can.removeChild(mediaTileGrid);
	   }
	   new ImageViewAction(can,table,type,fieldName).getName_(can);

   }

}
