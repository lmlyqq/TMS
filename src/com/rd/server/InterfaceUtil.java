package com.rd.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.rd.client.common.util.ObjUtil;
import com.rd.server.util.LoginContent;

/**
 * 对外接口统一方法
 * @author fanglm
 *
 */
public class InterfaceUtil{
	private  Socket client = null;
	private  DataOutputStream out = null;
	private  DataInputStream in = null;
	
	private  String position_host = "218.206.70.227";
	private  int position_port = 18000;
	private  String position_uname = "yhjc";
	private  String postion_pwd = "yhjc@56";
	private  static PostMethod post = null;
	private static HttpClient httpclient = null;
    private static String url = "http://host:10000/newpartner/partnerServlet";//POST接口
	
//	private String http_url = "http://www.513gs.com/z3/rQueryLocation.jsp?key=jyVRTDQzaNtKch4PCvgf5jxfafl2Nd2xOpuyh&l=";
	
	
//	private URL url;
//    private HttpURLConnection urlconn;

    String inencoding;
    String outencoding;
	    
    public InterfaceUtil(String inencoding, String outencoding) {
        this.inencoding = inencoding;
        this.outencoding = outencoding;
    }
	
	public InterfaceUtil(){
		
	}
	
	public static void main(String[] args) {
		InterfaceUtil iu = new InterfaceUtil();
		iu.mobilePosition("15256091321");
	}
	
	public HashMap<String, String> mobilePosition(String mobile) {
		/**
		HashMap<String, String> map = new HashMap<String, String>();
		if(client ==null || !client.isConnected()){
			connect();
		}
		try {
			map = readXML(action(getExactLocalXml(mobile))).get(0);
			while (map.get("result_info").toString().equals("尚未建立连接")) {
//				disConnect();
				connect();
				map = readXML(action(getExactLocalXml(mobile))).get(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;**/
		HashMap<String, String> map = new HashMap<String, String>();
		try{
			String responseText =  requestServer(getExactLocalXml(mobile));
			map = readXML(responseText).get(0);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public ArrayList<HashMap<String, String>> mobilePosition2(String mobile) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();

		try {
			map = mobilePosition(mobile);
			list.add(map);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 连接服务器
	 * 
	 * @param username
	 *            String
	 * @param password
	 *            String
	 * @throws Exception
	 */
	public void connect(){
		try{
			client = new Socket(position_host, position_port);
			client.setSoTimeout(60000);
			out = new DataOutputStream((client.getOutputStream()));
	
			String login = "<?xml version=\"1.0\" encoding=\"GB2312\"?><yhjx_init><request><type>open_session</type><client><id>"
					+ position_uname
					+ "</id><pwd>"
					+ postion_pwd
					+ "</pwd></client></request></yhjx_init>";
			out.write(login.getBytes());
			out.flush();
			in = new DataInputStream(client.getInputStream());
			byte[] reply = new byte[1024];
			int len = in.read(reply);
			if (len != -1) {
				//System.out.println(new String(reply, 0, len, "GBK"));
			} else {
				//System.out.println("线程 没有获取到数据。");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void disConnect(){
		try{
			if(client.isConnected()){
				client.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnect(){
		try{
			out = new DataOutputStream((client.getOutputStream()));
			
			String login = "<?xml version=\"1.0\" encoding=\"GB2312\"?><yhjx_init><request><type>close_session</type><client><id>"
					+ position_uname
					+ "</id><pwd>"
					+ postion_pwd
					+ "</pwd></client></request></yhjx_init>";
			out.write(login.getBytes());
			out.flush();
			in = new DataInputStream(client.getInputStream());
			byte[] reply = new byte[1024];
			int len = in.read(reply);
			if (len != -1) {
				//System.out.println(new String(reply, 0, len, "GBK"));
			} else {
				//System.out.println("线程 没有获取到数据。");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 批量精确定位
	 * @author fanglm
	 * @param param 手机号码拼接字符串
	 * @return
	 */
	public ArrayList<HashMap<String, String>> moreMbPosition(String param){
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		try {
			list = readXML(action(getMutiExactLocalXml(param)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	private String action(String sendXml) throws IOException {
		out.write(sendXml.getBytes("GB2312"));
		out.flush();
		String xml="";
		try{
			in = new DataInputStream(client.getInputStream());
			byte[] reply = new byte[16384];
			int len = in.read(reply);
			if (len != -1) {
				xml  = new String(reply, 0, len, "GB2312");
			} else {
				xml = "线程没有获取到数据。";
			}
		}catch(Exception e){
			e.printStackTrace();
			xml = action(sendXml);
		}
		
		return xml;
		
		

	}


	/**
	 * 精确定位司机XML格式
	 * 
	 * @param mobileNo 车牌号/手机号
	 *            String
	 */
	private  String getExactLocalXml(String mobileNo) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
		sb.append("<yhjx_init>");
		sb.append("<request>");
		sb.append("<username>").append(position_uname).append("</username>");
		sb.append("<password>").append(postion_pwd).append("</password>");
		sb.append("<type>muti_exact_locate</type>");
		sb.append("<info>");
		sb.append("<msid>").append(mobileNo).append("</msid>");
		
		sb.append("<datetime>2010-12-31 12:00:00</datetime> ");
		sb.append("</info>");
		sb.append("</request>");
		sb.append("</yhjx_init>");
		
		return sb.toString();
	}

	// 批量精确定位
	private  String getMutiExactLocalXml(String mobileNo) {
		StringBuffer xml = new StringBuffer();
		String dateTime = getTimeStr();
		xml.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
		xml.append("<yhjx_init>");
		xml.append("<request>");
		xml.append("<type>muti_exact_locate</type>");
		xml.append("<info>");
		xml.append("<msid>" + mobileNo + "</msid>");
		xml.append("<datetime>" + dateTime + "0</datetime>");
		xml.append("<external_note>定时器触发精确定位</external_note>");
		xml.append("</info >");
		xml.append("</request>");
		xml.append("</yhjx_init>");
		return xml.toString();
	}
	
	/**
	 * 发送心跳报文
	 */
	public void heartBeat() throws Exception {
		while (true) {
			String query = "<?xml version=\"1.0\" encoding=\"GB2312\"?><yhjx_init><request><type>heart_beat</type></request></yhjx_init>";
			out.write(query.getBytes());
			out.flush();
			in = new DataInputStream(client.getInputStream());
			byte[] reply = new byte[1024];
			int len = in.read(reply);
			if (len != -1) {
				//System.out.println(new String(reply, 0, len, "GBK"));
			} else {
				//System.out.println("线程 没有获取到数据。");
			}

			Thread.sleep(30000);
		}

	}
	
	
	/**
	 * XML报文解析
	 * @author fanglm
	 * @param param 报文
	 */
	@SuppressWarnings("unchecked")
	private  ArrayList<HashMap<String, String>> readXML(String param) throws Exception{
		//System.out.println(param);
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
			
		try{
			SAXReader saxReader = new SAXReader();
	        Document document = null;
	        document = saxReader.read(new StringReader(param));

	        Element root = document.getRootElement();
	        for (Iterator i = root.elementIterator(); i.hasNext();)
	        {
	            Element ele = (Element) i.next();
	            if (ele.getName().equals("response")) // XML node
	            {
	            	for (Iterator m = ele.elementIterator(); m.hasNext();){
		            	Element result = (Element)m.next();
		            	
		            	if(result.getName().equals("result")){
		            		map = new HashMap<String, String>();
		            		Element result_id = result.element("result_id");//提示信息ID
		            		if(ObjUtil.isNotNull(result_id)){
		            			map.put("result_id", result_id.getStringValue());
		            		}
			            	Element result_info = result.element("result_info");//提示信息
			            	if(ObjUtil.isNotNull(result_info)){
		            			map.put("result_info", result_info.getStringValue());
		            		}
			            	Element datetime = result.element("datetime");//定位时间
			            	if(ObjUtil.isNotNull(datetime)){
		            			map.put("datetime", datetime.getStringValue());
		            		}
			            	Element msid = result.element("msid"); //手机号码
			            	if(ObjUtil.isNotNull(msid)){
		            			map.put("msid", msid.getStringValue());
		            		}
			            	Element area_name = result.element("area_name");//当前位置
			            	if(ObjUtil.isNotNull(area_name)){
		            			map.put("area_name", area_name.getStringValue());
		            		}
			            	Element img_url = result.element("img_url");//当前位置图片
			            	if(ObjUtil.isNotNull(img_url)){
		            			map.put("img_url", img_url.getStringValue());
		            		}
			            	Element external_note = result.element("external_note");//当前位置图片
			            	if(ObjUtil.isNotNull(external_note)){
		            			map.put("external_note", external_note.getStringValue());
		            		}
			            	
			            	list.add(map);
		            	}
	            	}
	            }
	        }
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return list;
	}
	
	@SuppressWarnings("deprecation")
	private static String requestServer(String xml) {
//		if(post == null)
		url = url.replace("host", LoginContent.getInstance().getSys_param().get("POSITION_HOST").getVALUE_STRING());
//		System.out.println(url);
		post = new PostMethod(url);
        String reStr = "";
        try {
            //reStr = new String(xml.getBytes(), "gb2312");
            reStr = URLEncoder.encode(xml, "gb2312");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        post.setRequestBody(reStr);
        post.setRequestContentLength(reStr.length());
        //post.setRequestHeader("Content-type", "text/xml; charset=gb2312");
//        if(httpclient == null)
        	httpclient = new HttpClient();
        httpclient.setTimeout(20000);
        httpclient.setConnectionTimeout(20000);
        int result = 200;
        String resultInfo = "";
        try {
            result = httpclient.executeMethod(post);
            if (result == 200) {
                try {
                	byte[] b = post.getResponseBody();
                    resultInfo = new String(b, "gb2312");
                    resultInfo = URLDecoder.decode(resultInfo,"GB2312");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 
        } catch (HttpException he) {
            he.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }finally{
        	post.releaseConnection();
        }
       
        return resultInfo;
    }
	
	private static String getTimeStr(){
		Date thisDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = sdf.format(thisDate);
		
		return dateTime;
	}
	
	public static boolean isNeedConvert(char para){
		 return ((para&(0x00FF))!=para);
	}
	
}