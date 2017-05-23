package com.rd.server.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;


import com.rd.client.common.util.StaticRef;

/**
 * 文件上传功能
 * @author fanglm
 *
 */
public class Upload {
	
	private String path;
	
	private String file;
	
	private InputStream is;
	
	public Upload(String path,String file,InputStream is){
		this.path = path;
		this.file = file;
		this.is = is;
	}
	
	public String doUploadFile(){
		String succ = StaticRef.SUCCESS_CODE;
		try {
			String fileName = file;
	
			if (fileName.equals(".jpg") || fileName.equals(".gif")) { // 判断后缀名是否为图片
	
				InputStream input = is;
		
				BufferedImage bi = ImageIO.read(input); // 得到BufferedImage对象
		
				if (bi != null) { // 如果图片正确则不为空，不正确时为空
			
			
					if (this.upLoadFile(fileName, path,true)) {
			
						//System.out.println("上传成功！");
						succ = StaticRef.SUCCESS_CODE;
			
					} else {
			
					    //System.out.println("上传失败 ！");
					    succ = StaticRef.FAILURE_CODE;
			
					}
				} else {
		
					//System.out.println("你选择的图片错误或者该图片已经损坏！"); 
					succ = StaticRef.FAILURE_CODE;
		
				}
			}else{
				if (this.upLoadFile(fileName, path,false)) {

					//System.out.println("上传成功！");
					succ = StaticRef.SUCCESS_CODE;

				} else {

					//System.out.println("上传失败！!");
					succ = StaticRef.FAILURE_CODE;

				}
			} 

		} catch (Exception e) {
			e.printStackTrace();
		}
		return succ;

	}
	
	public boolean upLoadFile(String fileName,String path,boolean newName){
		FileOutputStream output = null;
		boolean boo = false;
		try{
			InputStream input = is; // 输入流

			String newFileName =fileName;
			if(newName){
				int random = (int) (Math.random() * 10000); // 随机数
				newFileName = System.currentTimeMillis() + random 
					+ fileName.substring(fileName.indexOf(".")); // 通过得到系统时间加随机数生成新文件名，避免重复
			}
			
			byte[] buff = new byte[4096]; // 缓冲区
			
			File filePath = new File(path);   //文件路径不一定真是存在，不存在就创建新的路径
			if(!filePath.exists()){
				filePath.mkdirs();
			}
			
			File file = new File(path+newFileName);  //查看文件是否存在，不存在创建新文件
			if(!file.exists()){
				file.createNewFile();
			}

			output = new FileOutputStream(path + newFileName); // 输出流

			int bytecount = 1;

			while ((bytecount = input.read(buff, 0, 4096)) != -1){ // 当input.read()方法，不能读取到字节流的时候，返回-1
				output.write(buff, 0, bytecount); // 写入字节到文件
			}
			boo = true;
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				output.flush();
				output.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return boo;
		
	}
}
