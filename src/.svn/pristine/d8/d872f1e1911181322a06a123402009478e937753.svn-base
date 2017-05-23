package com.rd.client.win;


import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VLayout;
/**
 * 客户Buss二级窗口
 * @author Administrator
 * 
 */
public class WeatherWin extends Window{
	
	private int width = 700;
	private int height = 300;
	private String top = "28%";
	private String left = "33%";
	private String title = "天气预报";
	public Window window;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */
	public WeatherWin(){
		
	}
	

	public Window getViewPanel() {
			  
		VLayout lay = new VLayout();
		lay.setWidth100();  
		lay.setHeight100();  
		
//        final SGPanel form=new SGPanel();
//        form.setIsGroup(true);  
//        form.setGroupTitle("天气预报");
//        form.setMargin(3);
//        form.setPadding(20);
//        form.setHeight("70%");
//        form.setWidth("99%");
//        form.setLeft("10%");
//        form.setAlign(Alignment.CENTER);
//        form.setTitleSuffix(":");
//        
//        final ComboBoxItem CITY_NAME=new ComboBoxItem("CITY_NAME", "城市名称");
//        CITY_NAME.setStartRow(true);
//        CITY_NAME.setValue("宁波");
//        TextItem AREA_ID=new TextItem("AREA_ID", Util.TI18N.END_AREA());
//        AREA_ID.setVisible(false);
//        Util.initArea(CITY_NAME, AREA_ID);
//        
//        Img img = new Img("http://api.map.baidu.com/images/weather/day/qing.png", 50, 50);
//        Canvas canvas = new Canvas();
//        canvas.addChild(img);
//		canvas.draw();
//		canvas.bringToFront();
//        
//        final StaticTextItem WEATHER_AREA=new StaticTextItem("WEATHER_AREA","");//当前查询城市
//        WEATHER_AREA.setStartRow(true);
//        WEATHER_AREA.setWidth(FormUtil.Width);
//
//        final StaticTextItem WEATHER_TODAY=new StaticTextItem("WEATHER_TODAY","");//今天
//        WEATHER_TODAY.setStartRow(true);
//        WEATHER_TODAY.setWidth(FormUtil.longWidth);
//                
//        final StaticTextItem WEATHER_TOMORROW=new StaticTextItem("WEATHER_TOMORROW","");//明天
//        WEATHER_TOMORROW.setStartRow(true);
//        WEATHER_TOMORROW.setWidth(FormUtil.longWidth);
//        
//        final StaticTextItem WEATHER_TDATOMORROW=new StaticTextItem("WEATHER_TDATOMORROW","");//后台
//        WEATHER_TDATOMORROW.setStartRow(true);
//        WEATHER_TDATOMORROW.setWidth(FormUtil.longWidth);
//        
//        final StaticTextItem WEATHER_TDFNOW=new StaticTextItem("WEATHER_TDFNOW","");//大后天
//        WEATHER_TDFNOW.setStartRow(true);
//        WEATHER_TDFNOW.setWidth(FormUtil.longWidth);
//        
//        
//        SGButtonItem searchButton1=new SGButtonItem(StaticRef.FETCH_BTN);
//        searchButton1.setStartRow(false);
//        searchButton1.addClickHandler(
//  		    new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
//  			
//  			@Override
//  			public void onClick(
//  					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
//  			
//  				if(CITY_NAME.getValue()!=null&&!CITY_NAME.getValue().equals("")){
//  				
//  					String cityName=CITY_NAME.getValue().toString();
//  				
//	  				Util.async.getWeather(cityName, new AsyncCallback<ArrayList<Map<String,String>>>() {
//						
//						@Override
//						public void onSuccess(ArrayList<Map<String, String>> result) {
//							
//							if(result!=null&&result.size()>0){
//								WEATHER_AREA.setValue(result.get(0).get("city")+"  PM2.5:"+result.get(0).get("pm25"));
//								for(int i=1;i<result.size();i++){
//									
//									if(i==1){
//										WEATHER_TODAY.setValue(result.get(i).get("data"));	
//									}
//									else if(i==2){
//										WEATHER_TOMORROW.setValue(result.get(i).get("data"));	
//									}
//									else if(i==3){
//										WEATHER_TDATOMORROW.setValue(result.get(i).get("data"));	
//									}	
//									else if(i==4){
//										WEATHER_TDFNOW.setValue(result.get(i).get("data"));	
//									}
//								}
//							}else{							
//								SC.say("请输入正确城市！");
//							}							
//						}
//						
//						@Override
//						public void onFailure(Throwable caught) {
//							SC.say("查询天气出错！");
//						}
//					});
//  				
//  				}
//  			}
//  		});
//        form.setItems(CITY_NAME,searchButton1,WEATHER_AREA,WEATHER_TODAY,WEATHER_TOMORROW,WEATHER_TDATOMORROW,WEATHER_TDFNOW);       
//        
        final HTMLPane htmlPane = new HTMLPane();  
        htmlPane.setShowEdges(true);  
        htmlPane.setContents("<iframe name='weather_inc' src='http://i.tianqi.com/index.php?c=code&id=13' width='650' height='221' frameborder='0' marginwidth='0' marginheight='0' scrolling='no'></iframe>");  
        htmlPane.setContentsType(ContentsType.PAGE);  
        
        //lay.addMember(form);
        lay.addMember(htmlPane);
        
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(lay);
		window.setShowCloseButton(true);
		window.show();
	
		return window;
	}

}
