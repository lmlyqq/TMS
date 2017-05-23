package com.rd.client.common.widgets;

import com.rd.client.common.util.StaticRef;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.tree.TreeGrid;

/**
 * 封装的TreeTable
 * @author yuanlei
 *
 */
public class TreeTable extends TreeGrid{

	public String OP_FLAG = "M";
	public String PKEY = "";
	public String PVALUE = "";
	
	public TreeTable(DataSource ds, String per_width, String per_height) {
        setLoadDataOnDemand(false);
        setWidth(per_width);
        setHeight(per_height);
        setShowHeader(false);
        setCanAcceptDroppedRecords(true);
        setDataSource(ds);
        setShowResizeBar(true);
        setFolderIcon(StaticRef.ICON_LEAF);
        setShowOpenIcons(false);
        setShowDropIcons(false);
        setClosedIconSuffix("");
        setNodeIcon(StaticRef.ICON_NODE);
	}
}
