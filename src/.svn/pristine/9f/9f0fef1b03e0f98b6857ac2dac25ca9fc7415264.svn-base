package com.rd.client.common.widgets;

import java.util.HashMap;
import java.util.Map;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.grid.events.RowEditorExitEvent;
import com.smartgwt.client.widgets.grid.events.RowEditorExitHandler;
/**
 * 封装的表格类
 * @author yuanlei
 */
public class SGTable extends ListGrid{

	private final static int row_height = 21;
	private final static String style = "myBoxedGridCell";
	public String OP_FLAG = "M";
	public String PKEY = "";
	public String PVALUE = "";
	public boolean isMax = false;  //列表所在布局是否已经最大化
	public static boolean isBind = false;	//是否启用(列表的值编辑后消失处理)
	private HashMap<Integer, HashMap<String, String>> editValuesMap = 
		new HashMap<Integer, HashMap<String, String>>();	//用于保存编辑过的值key为rowNum,value为修改过的值
    //private HLayout rollOverCanvas; 

	public SGTable(DataSource ds, boolean isBindEvent) {
		setCellHeight(row_height);
		setBaseStyle(style);
		setAlternateRecordStyles(true);
		setShowRowNumbers(true);
		setShowFilterEditor(true);
		setFilterOnKeypress(false);
		setDataSource(ds);
		setShowAllRecords(true);
		setAutoFetchData(false);
		setCanReorderRecords(true);
		setWidth100();
		setHeight100();
		setShowHover(true);
		setCanHover(true);
		setCanDragSelectText(true);
        setCanSelectText(true);
		initBindEvent(isBindEvent);
	}
	
	public SGTable(DataSource ds){
		this(ds, isBind);
	}

	/**
	 * yuanlei(2010-6-23)
	 * @param ds             数据源
	 * @param per_width      宽度百分比
	 * @param per_height     高度百分比
	 */
	public SGTable(DataSource ds,String per_width, String per_height, boolean isBindEvent) {
		setCellHeight(row_height);
		setBaseStyle(style);
		setAlternateRecordStyles(true);
		setShowRowNumbers(true);
		setShowFilterEditor(true);
		setFilterOnKeypress(false);
		setDataSource(ds);
		setShowAllRecords(true);
		setAutoFetchData(false);
		setCanReorderRecords(true);
		setCanEdit(true);
		setWidth(per_width);
		setHeight(per_height);
		setShowHover(true);
		setCanHover(true);
		setCanDragSelectText(true);
        setCanSelectText(true);
		initBindEvent(isBindEvent);
	}
	public SGTable(DataSource ds,String per_width, String per_height) {
		this(ds, per_width, per_height, isBind);
	}
	
	/**
	 * yuanlei(2010-6-25)
	 * @param ds              数据源
	 * @param per_width       宽度百分比
	 * @param per_height      高度百分比
	 * @param showFilter      是否显示过滤器
	 * @param showAllRecords  显示所有记录
	 * @param fetchData       是否自动获取数据
	 */
	public SGTable(DataSource ds, String per_width, String per_height, boolean showFilter, boolean showAllRecords, boolean fetchData, boolean isBindEvent) {
		setCellHeight(row_height);
		setBaseStyle(style);
		setAlternateRecordStyles(true);
		setShowRowNumbers(true);
		setShowFilterEditor(showFilter);
		setFilterOnKeypress(false);
		setDataSource(ds);
		setShowAllRecords(showAllRecords);
		setAutoFetchData(fetchData);
		setCanReorderRecords(true);
//		setCanEdit(true);
		setWidth(per_width);
		setHeight(per_height);
//		setShowHover(true);
//		setCanHover(true);
		setCanDragSelectText(true);
        setCanSelectText(true);
		initBindEvent(isBindEvent);
	}
	
	public SGTable(DataSource ds, String per_width, String per_height, boolean showFilter, boolean showAllRecords, boolean fetchData){
		this(ds, per_width, per_height, showFilter, showAllRecords, fetchData, isBind);
	}
	
	public SGTable(DataSource ds, String per_width,boolean showFilter, boolean showAllRecords, boolean fetchData, boolean isBindEvent) {
		setCellHeight(row_height);
		setBaseStyle(style);
		setAlternateRecordStyles(true);
		setShowRowNumbers(true);
		setShowFilterEditor(showFilter);
		setFilterOnKeypress(false);
		setDataSource(ds);
		setShowAllRecords(showAllRecords);
		setAutoFetchData(fetchData);
		setCanReorderRecords(true);
		setCanEdit(true);
		setWidth(per_width);
		setShowHover(true);
		setCanHover(true);
		setCanDragSelectText(true);
        setCanSelectText(true);
		initBindEvent(isBindEvent);
	}
	
	public SGTable(DataSource ds, String per_width,boolean showFilter, boolean showAllRecords, boolean fetchData){
		this(ds, per_width, showFilter, showAllRecords, fetchData, isBind);
	}
	
	protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {
		// TODO Auto-generated method stub
		return super.getBaseStyle(record, rowNum, colNum); 
	}
	
	/**
	 * yuanlei(2010-6-25)
	 * @param ds              数据源
	 * @param per_width       宽度百分比
	 * @param per_height      高度百分比
	 * @param showFilter      是否显示过滤器
	 * @param showAllRecords  显示所有记录
	 * @param fetchData       是否自动获取数据
	 */
	public SGTable(boolean isBindEvent) {
		setCellHeight(row_height);
		setShowFilterEditor(false);
		setShowAllRecords(true);
		setAutoFetchData(false);
		setCanDragSelectText(true);
        setCanSelectText(true);
		initBindEvent(isBindEvent);
	}
	
	public SGTable(){
		this(isBind);
	}
	
	public void initBindEvent(){
		initBindEvent(true);
	}
	
	private void initBindEvent(boolean isBindEvent){
		if(!isBindEvent) return;
		final SGTable thisObj = this;
    	this.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
//				Record record = event.getRecord();
//				if(thisObj == null || record == null)return;
//				int colNum = event.getColNum();
//				int rowNum = event.getRowNum();
//				String thisName = thisObj.getFieldName(colNum);
//				Object eValue = thisObj.getEditedCell(rowNum, colNum);
//				String oValue = record.getAttribute(thisName);
//				if(!(eValue == null || eValue.toString().length() == 0)){
//					record.setAttribute(thisName, eValue.toString());
//					thisObj.setEditValue(rowNum, colNum, eValue.toString());
//					if(!record.getAttributeAsBoolean("sessionFlag")){
//						thisObj.createEditSession(rowNum, colNum, record);
//						record.setAttribute("sessionFlag", true);
//					}
//				}
//				else if(!(oValue == null || oValue.toString().length() == 0)){
//					record.setAttribute(thisName, oValue);
//					thisObj.setEditValue(rowNum, colNum, oValue);
//				}
				
			}
		});
    	
    	this.addRowEditorExitHandler(new RowEditorExitHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onRowEditorExit(RowEditorExitEvent event) {
				int rowNum = event.getRowNum();
				Record record = event.getRecord();
				HashMap<String, String> evMap = (HashMap<String, String>)thisObj.getEditValues(event.getRowNum());
				if(!(evMap == null || evMap.isEmpty())){
					if(editValuesMap.containsKey(rowNum)){
						editValuesMap.get(rowNum).putAll(evMap);
					}else{
						editValuesMap.put(rowNum, evMap);
					}
					if(record != null){
						for (Map.Entry<String, String> entry : evMap.entrySet()) {
							record.setAttribute(entry.getKey(), entry.getValue());
						}
						if(!record.getAttributeAsBoolean("sessionFlag")){
							thisObj.createEditSession(rowNum, 0, record);
							record.setAttribute("sessionFlag", true);
						}
					}
				}
			}
		});
    	
    }
	
	public HashMap<Integer, HashMap<String, String>> getEditValuesMap(){
		return this.editValuesMap;
	}
	
	public native void createEditSession(int rowNum, int colNum, Record record) /*-{
		var self = this.@com.smartgwt.client.widgets.BaseWidget::getOrCreateJsObj()();
		self.createEditSession(rowNum, colNum);
	}-*/;
}
