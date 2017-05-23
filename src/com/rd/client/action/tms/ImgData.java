package com.rd.client.action.tms;

import com.smartgwt.client.widgets.tile.TileGrid;

public class ImgData {
	private static ImgRecord[] records;
	public TileGrid tile;
	
	public ImgData(){
		
	}
	
	 public static ImgRecord[] getRecords() {  
		if (records == null) {  
		  records = getNewRecords();  
		}  
		 return records;  
		}  
	 public static ImgRecord[] getNewRecords() {  
		return new ImgRecord[]{  
		   new ImgRecord("1291961955879.jpg","1291961955879.jpg"),
//		   new ImgRecord("1291963757322.jpg","1291963757322.jpg")
		};  
	}  
}
