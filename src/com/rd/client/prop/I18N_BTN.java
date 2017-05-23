package com.rd.client.prop;

import com.google.gwt.i18n.client.Messages;

public interface I18N_BTN extends Messages{
	String LOGIN(); //登陆
	String CLEAR(); //清除
	
	String NEW(); //新增
	String COPY_NEW();  //复制新增
	String SAVE(); //保存
	String DELETE(); //删除
	String CANCEL(); //取消
	String IMPORT(); //导入
	String EXPORT(); //导出
	String PRINTVIEW(); //打印预览
	String PRINT(); //打印
	String SEARCH();//查询
	String FREEZE();//冻结
	String FREE();//释放
	String CONFIRM();//确定
	String FILTER();  //筛选
	String REFRESH(); //刷新
	String GATHER();  //汇总
	String VIEWSHPM(); //查看相关作业单
	String CREATELOAD();//生成调度单
	String SEARCHSHPM(); //查询作业单
	String SEARCHLOAD(); //查询调度单
	String SENDCONFIRM();//发运确认
	String CANCELSEND();//取消发运
	

	String NEWDETAIL();//新增明细
	String SAVEDETAIL();//保存明细
	String REMOVEDETAIL();//删除明细
	String CANCELDETAIL();//取消明细
	String PAYOUT();//派发
	String CLOSE();
	
	String MOVEDETAIL();//移除明细
	String CANCELSPLIT();//取消拆分
	String SPLIT();//拆分
	String GOODS_INFO(); //货品信息
	String DISPATCH_CONFIRM(); //配车确认
	String CANCEL_DISPATCH();  //取消确认
	String DISPATCH_AUDIT();   //配车审核
	String CANCEL_AUDIT();     //取消审核
	String SPLITBYLDQNTY();    //按发货量拆分
	
	String CONFIRMORDER();//确认回单
	String MANYORDER();//批量回单
	String CANCELORDER();//取消回单
	String PUTIMAGE();//上传影像
	String PUTIN();//上传
	
	String SENDBILLPRINT();
	String LOADBILLPRINT();
	
	String CREATECONDITION();//条件新增按钮
	String SAVECONDITION();//条件保存按钮
	String DELETECONDITION();//条件删除按钮
	String CANCELCONDITION();//条件取消按钮
	String STALOAD();//开始装货
	String FINLOAD();//完成装货
	String ARRIREG();//到库登记
	String VEHLCHECK();//车辆检查
	String LEVAREG();//离库登记
	String SCANNERCONFIRM();//扫描完成
	String PASS();//合格通过
	String NOPASS();//不合格退回
	String CANCELLOAD();
	String EXPORT_DETAIL();
	String COPY();
}
