package com.pinyougou.shop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import domain.Result;
import util.FastDFSClient;

/**
 * 文件上传Controller
 * @author Trik
 *
 */
@RestController
public class UploadController {

	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;//文件服务器地址
	
	@RequestMapping("/upload")
	public Result upload(MultipartFile file) {
		System.out.println("进入了uploadController");
		String originalFilename = file.getOriginalFilename();	
		String extName = originalFilename.substring(originalFilename.indexOf(".")+1);
		try {
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
			String path = fastDFSClient.uploadFile(file.getBytes(),extName);
			String url = FILE_SERVER_URL + path;
			return new Result(true,url);
		}catch(Exception e) {
			e.printStackTrace();
			return new Result(false,"上传失败");
		}
	}
}
