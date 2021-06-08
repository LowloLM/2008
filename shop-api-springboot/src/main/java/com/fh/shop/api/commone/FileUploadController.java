package com.fh.shop.api.commone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("file")
public class FileUploadController {


	
	/**
	 * 上传图片
	 * 
	 * @return
	 */
	@RequestMapping("uploadFile")
	@ResponseBody
	public Map<String, Object> uploadFile(MultipartFile fileImg,HttpServletRequest request) {
		
		System.out.println(request.getRequestURI());

		Map<String,Object> map = new HashMap<>();
		map.put("username", "admin");
		map.put("password", "123123");
		request.getSession().setAttribute("test", map);
		
		if (fileImg == null || fileImg.getSize() == 0)// 假如文件上传配置不正确,可能造成img对象为空,
		{
			System.out.println("MultipartFile img  对象为空,请检查文件上传配置");
			return null;
		}

		// --------------------优化图片名称,让名字不会重复--------------------------------------

		// 文件名称不能重复
		// 3dc9043e-b2af-4b43-ab53-d95e839cfe96
		String imgNewName = UUID.randomUUID().toString();//// 使用uuid去生成图片名称

		// 截取原始图片名字的后缀名
		// 如:2.jpg
		String imgOldName = fileImg.getOriginalFilename();
		// 截取后缀名
		imgOldName = imgOldName.substring(imgOldName.lastIndexOf("."));

		// 假如你需要用户,只能上传图片.jpg .png .gif if else判断就行

		// 这个名字,就变成了 uuid + 后缀名 3dc9043e-b2af-4b43-ab53-d95e839cfe96.jpg
		imgNewName = imgNewName + imgOldName;

		// --------------------优化图片名称,让名字不会重复--------------------------------------
		// getServletContext获取servlet上下文
		// getRealPath 获取项目在电脑硬盘上面的路径, / 代表根目录
		// D:\huangpeng\apache-tomcat-8.5.47\webapps\day12_upload\
		String path = request.getServletContext().getRealPath("/images/");

		File dir = new File(path);
		if (!dir.exists())// 文件夹是否存在,如果不存在,则需要创建文件夹
		{
			dir.mkdirs();
		}
		// --------------------------动态获取项目路径---------------------
		// 需要使用request对象,去获取项目路径

		// --------------------------动态获取项目路径---------------------

		// 创建图片文件对象
		// D:\huangpeng\apache-tomcat-8.5.47\webapps\day12_upload\images\3dc9043e-b2af-4b43-ab53-d95e839cfe96.jpg
		File file = new File(path + imgNewName);

		// 使用transferTo 方法,保存文件
		try {
			fileImg.transferTo(file);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", true);
		resultMap.put("filePath", "/images/" + imgNewName);
		return resultMap;
	}

	/**
	 * 图片下载
	 * 
	 * @param imgName
	 * @param request
	 * @param response
	 */
	@RequestMapping("download")
	public void download(String imgName, HttpServletRequest request, HttpServletResponse response) {
		if (imgName == null || "".equals(imgName))// 如果文件名称为空,则直接返回空
		{
			System.out.println("文件名称为空,请断点调试,查看");
			return;
		}

		// D:\huangpeng\apache-tomcat-8.5.47\webapps\day12_upload\images\xxxxxx.jpg

		// 获取项目的绝对路径
		String path = request.getServletContext().getRealPath("/") + imgName;

		// 获取图片在电脑硬盘上的,具体路径
		File file = new File(path);

		// 使用java提供的Files对象,把文件显示给jsp页面,让页面下载
		// 第一个参数,图片的具体路径
		// 第二个参数,使用response 的输出流

		// 需要注意的是,使用输出流后,浏览器并不知道,我们是提供的一个文件下载.
		// 所以,我们需要告诉浏览器,我们现在这个请求,是下载文件
		// 使用response方法,告诉浏览器,这里是一个文件,需要进行下载,并不是直接展示
		response.setContentType("application/x-msdownload");

		// 告诉浏览器返回的文件的名称
		response.setHeader("Content-Disposition", "attachment;filename=" + imgName);

		try {
			Files.copy(file.toPath(), response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// 只包括数字的验证字符集
		private static final char[] chars = { '0', '1', '2', '3', '4', '5', '6',
				'7', '8', '9' };
		
		
		// 字符数量
		private static final int SIZE = 3;
		// 干扰线数量
		private static final int LINES = 2;
		// 宽度
		private static final int WIDTH = 80;
		// 高度
		private static final int HEIGHT = 30;
		// 字体大小
		private static final int FONT_SIZE = 30;

	/**
	 * 获取验证码
	 */
	@RequestMapping("getCode")
	public void getCode(HttpServletRequest req,HttpServletResponse resp) {
		StringBuffer sb = new StringBuffer();
		// 1.创建空白图片
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		// 2.获取图片画笔
		Graphics graphic = image.getGraphics();
		// 3.设置画笔颜色
		graphic.setColor(Color.LIGHT_GRAY);
		// 4.绘制矩形背景
		graphic.fillRect(0, 0, WIDTH, HEIGHT);
		// 5.画随机字符
		Random ran = new Random();
		for (int i = 0; i < SIZE; i++) {
			// 取随机字符索引
			int n = ran.nextInt(chars.length);
			// 设置随机颜色
			graphic.setColor(new Color(ran.nextInt(256), new Random().nextInt(256),ran.nextInt(256)));
			// 设置字体大小
			graphic.setFont(new Font(null, Font.BOLD + Font.ITALIC, FONT_SIZE));
			// 画字符
			graphic.drawString(chars[n] + "", i * WIDTH / SIZE, HEIGHT * 2 / 3);
			// 记录字符
			sb.append(chars[n]);
		}
		// 6.画干扰线
		for (int i = 0; i < LINES; i++) {
			// 设置随机颜色
			graphic.setColor(new Color(ran.nextInt(256), new Random().nextInt(256),ran.nextInt(256)));
			// 随机画线
			graphic.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT),
					ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
		}
		
		//存入session
		HttpSession session = req.getSession();
		session.setAttribute("checkCode", sb.toString());
		// 回写图片
		try {
			ImageIO.write(image, "jpg", resp.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 7.返回验证码和图片
	}
}
