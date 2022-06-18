package com.dev.board.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.board.service.UploadService;
import com.dev.board.vo.ExtNameVO;

@RestController
public class UploadController {

	@Autowired
	private UploadService service;
	
	@PostMapping("/upload")
	public int upload(@RequestParam(value="uploadFile", required=false) MultipartFile file) throws Exception {
		
		int result = service.setS3File(file);
		
		return result;
	}
	
	@PostMapping("/extList")
	public List<ExtNameVO> getExtList() {
		
		return service.getExtList();
	}
	
	@PostMapping("/extFixList")
	public List<ExtNameVO> getExtFixList() {
		
		return service.getExtFixList();
	}
	
	@PostMapping("/extName")
	public int extName(@RequestParam("extName") String extName) {
		
		int result = service.setExt(extName);
		
		return result;
	}
	
	@PostMapping("/extDelete")
	public int extDelete(@RequestParam("extOriName") String extOriName) {
		
		int result = service.deleteExt(extOriName);
		
		return result;
	}
	
	@PostMapping("/fileDelete")
	public int fileDelete(@RequestParam("fileName") String fileName) {
		
		int result = service.deleteFile(fileName);
		
		return result;
	}
	
	@PostMapping("/updateExtUse")
	public int updateExtUse(@RequestParam("extName") String extName, @RequestParam("extUse") String extUse) {
		ExtNameVO ext = new ExtNameVO();
		extName = extName.toLowerCase();
		ext.setExtName(extName);
		ext.setExtUse(extUse);
		int result = service.updateExtUse(ext);
		
		return result;
	}
}
