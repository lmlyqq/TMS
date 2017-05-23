package com.rd.client;


import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;

public class OpenPrint extends Window{
	
	private String so_id;

	public OpenPrint(String so_id){
		this.so_id = "rd/" + so_id + ".jpg";
		onModuleLoad();
	}

	private  void onModuleLoad(){
		Window window = new Window();
		window.setAutoSize(true);
    	Img image = new Img(so_id);
    	image.setWidth(1039-210);
    	image.setHeight(500);
    	image.setTop(18);
    	window.setTitle("R&D 打印预览");
    	window.setLeft("10%");
    	window.setTop("10%");
    	window.setWidth(1039-210);  
    	window.setHeight(519);
    	window.addChild(image);
    	window.draw();
	}
}
