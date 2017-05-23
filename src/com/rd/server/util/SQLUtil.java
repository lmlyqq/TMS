package com.rd.server.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;

import com.rd.client.common.util.ObjUtil;

public class SQLUtil {
	
	private boolean isIgnoreNull = true;
	
	private String table_alias = "";
	
	private HashMap<String,String> condition = new HashMap<String,String>();
	
	public SQLUtil(boolean bolIgnoreNull) {
		this.isIgnoreNull = bolIgnoreNull;
	}

	public void destroy() throws Exception {
	}
	
	public String addEqualSQL(String strKey, String strValue){
		return addEqualSQL(strKey, strValue, true);
	}
	
	public String addEqualSQL(String strKey, String strValue, boolean isDecode) {
		if(isDecode){
			strValue = decode(strValue);
		}
		StringBuffer sf = new StringBuffer();
		/*if(ObjUtil.isNotNull(strValue)) {
			sf.append(" and ");
			sf.append(strKey);
			sf.append(" = '");
			sf.append(strValue.toString());
			sf.append("'");	
		}
		else {
			if(!isIgnoreNull) {
				if(strValue != null) {
					sf.append(" and ");
					sf.append(strKey);
					sf.append(" = '");
					sf.append(strValue);
					sf.append("'");
				}
				else {
					sf.append(" and ");
					sf.append(strKey);
					sf.append(" is null");
				}
			}
		}*/
		if(ObjUtil.isNotNull(strValue)) {
			sf.append(" and ");
			if(ObjUtil.isNotNull(getTableAlias())) {
				sf.append(getTableAlias());
				sf.append(".");
			}
			sf.append(strKey);
			sf.append(" = :");
			sf.append(strKey);
			setParamValue(strKey, strValue);
		}		
		return sf.toString();
	}
	
	public String addLikeSQL(String strKey, String strValue) {
		strValue = decode(strValue);
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(strValue)) {
			sf.append(" and (");
			sf.append(strKey);
			sf.append(" like '%");
			sf.append(strValue.toString());
			sf.append("' or ");
			sf.append(strKey);
			sf.append(" like '");
			sf.append(strValue.toString());
			sf.append("%')");
		}
		return sf.toString();
	}
	
	
	public String addALikeSQL(String strKey, String strValue) {
		strValue = decode(strValue);
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(strValue)) {
			/*sf.append(" and ");
			sf.append(strKey);
			sf.append(" like '%");
			sf.append(strValue);
			sf.append("%'");*/
			sf.append(" and ");
			if(ObjUtil.isNotNull(getTableAlias())) {
				sf.append(getTableAlias());
				sf.append(".");
			}
			sf.append(strKey);
			sf.append(" like :");
			if(strKey.indexOf("||") > -1){
				strKey = strKey.replaceAll("\\|\\|", "_");
			}
			sf.append(strKey);
			setParamValue(strKey, "%"+strValue+"%");
		}
		return sf.toString();
	}
	
	public String addTimeSQL(String strKey, String strValue, String strSign) {
		
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(strValue)) {
			sf.append(" and ");
			sf.append(strKey);
			sf.append(strSign);
			sf.append(" to_date('");
			sf.append(strValue.toString());
			sf.append("','yyyy/mm/dd hh24:mi')");
			/*sf.append(" and ");
			if(ObjUtil.isNotNull(getTableAlias())) {
				sf.append(getTableAlias());
				sf.append(".");
			}
			sf.append(strKey);
			sf.append(strSign);
			sf.append(" :");
			sf.append(strValue);
			setParamValue(strValue, strValue);*/
		}
		return sf.toString();
	}
	
	public String addMathSQL(String strKey, String strValue, String strSign) {
		strValue = decode(strValue);
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(strValue)) {
			/*sf.append(" and "); 
			sf.append(strKey);
			
			sf.append(strSign);
			sf.append("'");
			sf.append(strValue);
			sf.append("' ");*/
			
			sf.append(" and ");
			if(ObjUtil.isNotNull(getTableAlias())) {
				sf.append(getTableAlias());
				sf.append(".");
			}
			sf.append(strKey);
			sf.append(strSign);
			sf.append(" :");
			sf.append(strValue);
			setParamValue(strValue, strValue);
		}
		return sf.toString();
	}
	
	public String addNumberSQL(String strKey, String strValue, String strSign) {
		
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(strValue)) {
			sf.append(" and to_number("); 
			sf.append(strKey);
			sf.append(") ");
			sf.append(strSign);
			sf.append(" ");
			sf.append(strValue);
		}
		return sf.toString();
	}
	
	public String addAddrRole(String role_name,String user_id){
		role_name = decode(role_name);
		StringBuffer sf = new StringBuffer();
		if("经销商".equals(role_name)){
			sf.append(" and unload_id in (select addr_id from sys_user_addr where user_id='");
			sf.append(user_id);
			sf.append("')");
		}else if( "仓库主管".equals(role_name)){
			sf.append(" and load_id in (select a.id from sys_user_whse t,bas_address a where t.whse_id = a.whse_id and user_id='");
			sf.append(user_id);
			sf.append("')");
		}
		
		return sf.toString();
	}
	
	/**
	 * 查询条件增加客户权限
	 * @author fanglm
	 * @param key
	 * @return
	 */
	public String addCustomerRole(String key,String userCust){
		if(ObjUtil.isNotNull(userCust)){
			StringBuffer sf = new StringBuffer();
			sf.append(" and ");
			sf.append(key);
			sf.append(" in (");
			sf.append(userCust);
			sf.append(")");
			return sf.toString();
		}else{
			return "";
		}
		
		
	}
	public String addCustomerRole(String userCust){
		return addCustomerRole("CUSTOMER_ID",userCust);
	}
	
	/**
	 * 包含下级机构
	 * @author fanglm
	 * @param exec_org_id
	 * @param c_org_flag
	 * @return
	 */
	public String addOrgRole(String user_id,String exec_org_id,String c_org_flag,String c_rdc_flag){
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(exec_org_id)) {
			if(ObjUtil.isNotNull(c_org_flag) && c_org_flag.equals("true")) { //执行机构
            	sf.append(" and exists ");
            	sf.append("    (SELECT 'x' ");
            	sf.append("     From bas_org ");
				sf.append("     Where t.EXEC_ORG_ID = bas_org.id AND (bas_org.ORG_INDEX Like '%,"); //
				sf.append(exec_org_id);
				sf.append("%'");
				if(ObjUtil.isNotNull(c_rdc_flag) && c_rdc_flag.equals("true")){
					sf.append(" or parent_org_id ='A03'");
				}
				sf.append("))");
			}
			else {
				sf.append(" and (EXEC_ORG_ID = '");
				sf.append(exec_org_id);
				sf.append("'");
				if(ObjUtil.isNotNull(c_rdc_flag) && c_rdc_flag.equals("true")){
					sf.append(" or exec_org_id in(select id from bas_org where parent_org_id ='A03')");
				}
				sf.append(")");
			}
		}
		return sf.toString();
	}
	
	/**
	 * 包含下级机构
	 * @author fanglm
	 * @param exec_org_id
	 * @param c_org_flag
	 * @return
	 */
	public String addOrgRole(String user_id,String exec_org_id,String c_org_flag,String c_rdc_flag, String flag){
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(exec_org_id)) {
			String org_name = "SIGN_ORG_ID";
			if(ObjUtil.isNotNull(flag) && flag.equals("true")) {
				org_name = "EXEC_ORG_ID";
			}
			if(ObjUtil.isNotNull(c_org_flag) && c_org_flag.equals("true")) { //执行机构
            	sf.append(" and exists ");
            	sf.append("    (SELECT 'x' ");
            	sf.append("     From bas_org ");
				sf.append("     Where t.");
				sf.append(org_name);
				sf.append(" = bas_org.id AND (bas_org.ORG_INDEX Like '%,"); //
				sf.append(exec_org_id);
				sf.append("%'");
				if(ObjUtil.isNotNull(c_rdc_flag) && c_rdc_flag.equals("true")){
					sf.append(" or parent_org_id ='A03'");
				}
				sf.append("))");
			}
			else {
				sf.append(" and (");
				sf.append(org_name);
				sf.append(" = '");
				sf.append(exec_org_id);
				sf.append("'");
				if(ObjUtil.isNotNull(c_rdc_flag) && c_rdc_flag.equals("true")){
					sf.append(" or ");
					sf.append(org_name);
					sf.append(" in(select id from bas_org where parent_org_id ='A03')");
				}
				sf.append(")");
			}
		}
		return sf.toString();
	}

	
	public String addFlagSQL(String strKey, String strValue) {
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(strValue) && (strValue.equals("true") || strValue.equals("Y"))) {
			/*sf.append(" and ");
			sf.append(strKey);
			sf.append(" = 'Y'");*/
			sf.append(" and ");
			if(ObjUtil.isNotNull(getTableAlias())) {
				sf.append(getTableAlias());
				sf.append(".");
			}
			sf.append(strKey);
			sf.append(" = :");
			sf.append(strKey);
			setParamValue(strKey, "Y");
		}
		else {
			/*if(!isIgnoreNull) {
				if(strValue != null) {
					sf.append(" and ");
					sf.append(strKey);
					sf.append(" = 'N'");
				}
				else {
					sf.append(" and ");
					sf.append(strKey);
					sf.append(" is null");
				}
			}*/
			if(ObjUtil.isNotNull(getTableAlias())) {
				sf.append(" and ");
				sf.append(getTableAlias());
				sf.append(".");
				sf.append(strKey);
				sf.append(" <> :");
				sf.append(strKey);
				setParamValue(strKey, "Y");
			}
		}
		return sf.toString();
	}
	
	/**
	 * Y OR N 标记字段的SQL
	 * @author Administrator
	 * @param strKey
	 * @param strValue
	 * @param ignoreNull 是否忽略N的值
	 * @return
	 */
	public String addFlagSQL(String strKey, String strValue, boolean ignoreNull) {
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(strValue) && (strValue.equals("true") || strValue.equals("Y"))) {
			/*sf.append(" and ");
			sf.append(strKey);
			sf.append(" = 'Y'");*/
			sf.append(" and ");
			if(ObjUtil.isNotNull(getTableAlias())) {
				sf.append(getTableAlias());
				sf.append(".");
			}
			sf.append(strKey);
			sf.append(" = :");
			sf.append(strKey);
			setParamValue(strKey, "Y");
		}
		else {
			/*if(!isIgnoreNull) {
				if(strValue != null) {
					sf.append(" and ");
					sf.append(strKey);
					sf.append(" = 'N'");
				}
				else {
					sf.append(" and ");
					sf.append(strKey);
					sf.append(" is null");
				}
			}*/
			if(!ignoreNull) {
				if(ObjUtil.isNotNull(getTableAlias())) {
					sf.append(" and ");
					sf.append(getTableAlias());
					sf.append(".");
					sf.append(strKey);
					sf.append(" <> :");
					sf.append(strKey);
					setParamValue(strKey, "Y");
				}
			}
		}
		return sf.toString();
	}
	
	public String addFlagORSQL(String strKey, String strValue){
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(strValue) && strValue.equals("true")) {
			sf.append(" or ");
			sf.append(strKey);
			sf.append(" = 'Y'");
		}
		else {
			if(!isIgnoreNull) {
				if(strValue != null) {
					sf.append(" and ");
					sf.append(strKey);
					sf.append(" = 'N'");
				}
				else {
					sf.append(" and ");
					sf.append(strKey);
					sf.append(" is null");
				}
			}
		}
		return sf.toString();
	}
	
	/**
	 * 获取表或视图下所有字段名的SQL
	 * @author yuanlei 
	 */
	@SuppressWarnings("unchecked")
	public String getColName(String view_name, String alias) {
		
		StringBuffer sf = new StringBuffer();
		sf.append("select F_ROWTOCOL('");
		sf.append(view_name);
		sf.append("','");
		if(ObjUtil.isNotNull(alias)) {
			sf.append(getTableAlias());
		}
		sf.append("') AS COLS from dual");
		
		Session session = LoginContent.getInstance().getSession();
		Query query = session.createSQLQuery(sf.toString()).addScalar("COLS", Hibernate.STRING)
		.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
		.setCacheable(true).setCacheRegion(view_name);
		List object1 = query.list();
        String cols = "";
        if(object1 != null && object1.size() > 0) {
        	HashMap<String, String> map = (HashMap<String, String>)object1.get(0);
        	cols = ObjUtil.ifNull(map.get("COLS").toString(),"");
        }
        return cols;
	}
	
	/**
	 * 获取表或视图下所有字段名的SQL
	 * @author yuanlei 
	 */
	public String getColName(String view_name) {
		
		return getColName(view_name,"");
	}
	
	/**
	 * 解析【自定义查询】页签的查询条件并生成SQL
	 * @author yuanlei
	 * @param operator 操作符好
	 * @param criteria 查询条件集合
	 * @return
	 */
	public String addCriteriaSQL(String operator, String[] criteria) {
		StringBuffer sf = new StringBuffer();
		if(criteria != null) {
			for(int i = 0; i < criteria.length; i++) {
				sf.append(getSplitSQL(operator, criteria[i].substring(1,criteria[i].length() - 1)));
			}
		}
		return sf.toString();
	}
	
	public String getSplitSQL(String operator, String criteria) {
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(criteria)) {
			String[] params = criteria.replace("\"", "").split(",");
			if(params != null && params.length > 0) {
				if(params[0].indexOf("fieldName") >= 0) {
					String[] field = params[0].split(":");
					String[] oper  = params[1].split(":");
					String[] value = params[2].split(":");
					if(oper[1].compareTo("iContains") == 0) {
						sf.append(" ");
						sf.append(operator);
						sf.append(" ");
						sf.append(field[1]);
						sf.append(" like '%");
						sf.append(value[1]);
						sf.append("%'");
					}
					else if(oper[1].compareTo("equals") == 0) {
						sf.append(" ");
						sf.append(operator);
						sf.append(" ");
						sf.append(field[1]);
						sf.append(" = '");
						sf.append(value[1]);
						sf.append("'");
					}
					else if(oper[1].compareTo("greaterThan") == 0) {
						sf.append(" ");
						sf.append(operator);
						sf.append(" ");
						sf.append(field[1]);
						sf.append(" > '");
						sf.append(value[1]);
						sf.append("'");
					}
					//yuanlei 2012-09-11 
					//自定义查询增加‘>’、‘>=’和‘<=’三种解析方式
					else if(oper[1].compareTo("lessThan") == 0) {
						sf.append(" ");
						sf.append(operator);
						sf.append(" ");
						sf.append(field[1]);
						sf.append(" > '");
						sf.append(value[1]);
						sf.append("'");
					}
					else if(oper[1].compareTo("greaterOrEqual") == 0) {
						sf.append(" ");
						sf.append(operator);
						sf.append(" ");
						sf.append(field[1]);
						sf.append(" >= '");
						sf.append(value[1]);
						sf.append("'");
					}
					else if(oper[1].compareTo("lessOrEqual") == 0) {
						sf.append(" ");
						sf.append(operator);
						sf.append(" ");
						sf.append(field[1]);
						sf.append(" <= '");
						sf.append(value[1]);
						sf.append("'");
					}
					//yuanlei
				}
				else {
					int oper_start = criteria.indexOf("operator");                //操作标记开始位
					int comm_start = criteria.indexOf(",", oper_start);           //操作符后面的逗号位置
					int crit_start = criteria.indexOf("criteria", oper_start);    //子查询语句开始位
					String oper = criteria.substring(oper_start + 11, comm_start - 1);
					String[] crit= criteria.substring(crit_start + 11, criteria.length() - 2).split("},"); 
					int oper_pos = addCriteriaSQL(oper, crit).indexOf(oper);
					sf.append(" ");
					sf.append(operator);
					sf.append("(");
					sf.append(addCriteriaSQL(oper, crit).substring(oper_pos + oper.length()));
					sf.append(")");
				}
			}
		}
		return sf.toString();
	}
	
	public Query getQuery2(String sql,String keys,HashMap<String, Type> map){
		if(keys != null){
			keys = keys.replaceAll("to_char\\(\\w*,'yyyy/mm/dd hh24:mi:ss'\\) as ", "");
		}
		Session curSession = LoginContent.getInstance().getSession();
		SQLQuery query = (SQLQuery)curSession.createSQLQuery(sql);
		String[] names = keys.split(",");
		for(int i=0;i<names.length;i++){
			if(map != null && ObjUtil.isNotNull(map.get(names[i]))){
		        if(names[i].indexOf("distinct") < 0) {
				    query.addScalar(names[i], map.get(names[i]));
				}
		        else {
		        	String name = names[i].replace("distinct", "").trim();
		        	query.addScalar(name, map.get(names[i]));
		        }
			}else{
				if(names[i].indexOf("distinct") < 0) {
					query.addScalar(names[i],Hibernate.STRING);
				}
				else {
					String name = names[i].replace("distinct", "").trim();
		        	query.addScalar(name, Hibernate.STRING);
				}
			}
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		//LoginContent.getInstance().closeSession();
		//curSession = null;
		return query;
	}
	
	@SuppressWarnings("unchecked")
	public SQLQuery getQuery(String sql,String keys,HashMap<String, Type> map){
		if(keys != null){
			keys = keys.replaceAll("to_char\\(\\w*,'yyyy/mm/dd hh24:mi:ss'\\) as ", "");
		}
		Session curSession = LoginContent.getInstance().getSession();
		SQLQuery query = (SQLQuery)curSession.createSQLQuery(sql);
		
		//将动态SQL对应的参数值关联过来
		Iterator it = getParamValue().entrySet().iterator();
		for(int i=0;it.hasNext();i++){
			Map.Entry entry=(Map.Entry)it.next();
			String key = (String)entry.getKey();
			/*if(key.indexOf("_TIME") > 0) {
				//时间类型
				String date = value.toString();
				if(date.length() == 16) {
					date += ":00";
				}
				query.setTimestamp(key, java.sql.Timestamp.valueOf(date));
			}
			else {*/
				query.setParameter(key, entry.getValue());
			//}
		}
		if(keys != null) {
			String[] names = keys.split(",");
			for(int i=0;i<names.length;i++){
				if(map != null && ObjUtil.isNotNull(map.get(names[i]))){
			        if(names[i].indexOf("distinct") < 0) {
					    query.addScalar(names[i], map.get(names[i]));
					}
			        else {
			        	String name = names[i].replace("distinct", "").trim();
			        	query.addScalar(name, map.get(names[i]));
			        }
				}else{
					if(names[i].indexOf("distinct") < 0) {
						query.addScalar(names[i],Hibernate.STRING);
					}
					else {
						String name = names[i].replace("distinct", "").trim();
			        	query.addScalar(name, Hibernate.STRING);
					}
				}
			}
		}
		//query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		//LoginContent.getInstance().closeSession();
		//curSession = null;
		return query;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getRecordByQuery(String sql){
		ArrayList<String> retList = new ArrayList<String>();
		String sql2 = "select count(1) as CC from (" + sql + ")";
		Session curSession = LoginContent.getInstance().getSession();
		SQLQuery query = (SQLQuery)curSession.createSQLQuery(sql2);
		
		//将动态SQL对应的参数值关联过来
		Iterator it = getParamValue().entrySet().iterator();
		for(int i=0;it.hasNext();i++){
			Map.Entry entry=(Map.Entry)it.next();
			String key = (String)entry.getKey();
			/*if(key.indexOf("_TIME") > 0) {
				//时间类型
				String date = value.toString();
				if(date.length() == 16) {
					date += ":00";
				}
				query.setTimestamp(key, java.sql.Timestamp.valueOf(date));
			}
			else {*/
				query.setParameter(key, entry.getValue());
				sql = sql.replaceAll(":" + key, "'" + entry.getValue() + "'");
			//}
		}

		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, BigDecimal>> object = query.list();
		int count = object.get(0).get("CC").intValue();
		
		retList.add(Integer.toString(count));
    	int pos = sql.toUpperCase().indexOf(" FROM");
    	if(pos > 0) {
    		retList.add(sql.substring(pos));
    		retList.add(sql.substring(7,pos-1));
    	}
    	else {
    		retList.add("");
    		retList.add("");
    	}

        return retList;
	}
	
	/**
	 * 适用于左LIKE，使用索引，提高查询效率
	 * @author yuanlei
	 * @param strKey
	 * @param strValue
	 * @return
	 */
	public String addLeftLikeSQL(String strKey, String strValue) {
		strValue = decode(strValue);
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(strValue)) {
			sf.append(" and reverse(");
			sf.append(strKey);
			sf.append(") like reverse('%");
			sf.append(strValue.toString());
			sf.append("')");
		}
		return sf.toString();
	}
	
	/**
	 * 适用于右LIKE，使用索引，提高查询效率
	 * @author yuanlei
	 * @param strKey
	 * @param strValue
	 * @return
	 */
	public String addRightLikeSQL(String strKey, String strValue) {
		strValue = decode(strValue);
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(strValue)) {
			sf.append(" and ");
			sf.append(strKey);
			sf.append(" like '");
			sf.append(strValue.toString());
			sf.append("%'");
		}
		return sf.toString();
	}
	
	public String addForeignLikeSQL(String strKey, String strValue,String tableName) {
		strValue = decode(strValue);
		StringBuffer sf = new StringBuffer();
		if(ObjUtil.isNotNull(strValue)) {
			/*sf.append(" and ");
			sf.append(strKey);
			sf.append(" like '%");
			sf.append(strValue);
			sf.append("%'");*/
			sf.append(" and ");
			if(ObjUtil.isNotNull(tableName)) {
				sf.append(tableName);
				sf.append(".");
			}
			sf.append(strKey);
			sf.append(" like :");
			sf.append(strKey);
			setParamValue(strKey, "%"+strValue+"%");
		}
		return sf.toString();
	}
	
	public void setTableAlias(String alias) {
		this.table_alias = alias;
	}
	
	public String getTableAlias() {
		return this.table_alias;
	}
	
	public void setParamValue(String key,String value) {
		condition.put(key, value);
	}
	
	public HashMap<String,String> getParamValue() {
		return condition;
	}
	
	public String decode(String value){
		if(ObjUtil.isNotNull(value)){
			try {
				if(isNeedDecode(value)){
					return new String(URLDecoder.decode(value, "UTF-8")
							.getBytes("iso-8859-1"));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	private boolean isNeedDecode(String value) throws UnsupportedEncodingException{
		boolean needDecode = false;
		byte[] b;
		b = value.getBytes("iso-8859-1");
		for (int i = 0; i < b.length; i++) {
			byte b1 = b[i];
			if(b1 == 63){
				break;
			}else if(b1 > 0){
				continue;
			}else if(b1 < 0){
				needDecode = true;
				break;
			}
		}
		return needDecode;
	}
	
	public String addSQL(String fields,String colName,String replaceName) {
		if(fields.indexOf(colName) >= 0) {
			fields = fields.replaceAll(colName, replaceName + " AS " + colName);
		}
		else {
			fields = fields + "," + replaceName + " AS " + colName;
		}
		return fields;
	}
	
}