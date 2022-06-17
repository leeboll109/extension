package com.dev.board.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.board.dto.UploadMapper;
import com.dev.board.vo.ExtNameVO;
import com.dev.board.vo.UploadVO;

@Service
public class UploadService {

	@Autowired
	private UploadMapper mapper;
	
	private String base_path = "";
		
	public int setFileList(MultipartFile upFile) throws Exception{
		
 		String originalFileName = upFile.getOriginalFilename();
		String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName.length());
		long fileSize = upFile.getSize();
		byte[] data = upFile.getBytes(); 

		// 확장자 소문자로 고정
		ext = ext.toLowerCase();
		
		List<ExtNameVO> list = mapper.getExtList();
		// 사용하려는 확장자는 1로 표시
		for(ExtNameVO item : list) {
			String name = item.getExtName();
			// 이미 존재하는 확장자이기 때문에 실패
			if(ext.equals(name) && "1".equals(item.getExtUse())) {
				return -1;
			}
		}
		
		mkDir();
		FileOutputStream fos = new FileOutputStream(base_path + "\\" + originalFileName);
		fos.write(data);
		fos.close();
		
		UUID uuid = UUID.randomUUID();
		
		UploadVO file = new UploadVO();
		file.setFileOriName(originalFileName);
		file.setFileUrl(base_path);
		file.setFileName(uuid + "." + ext);
		file.setFileSize(Long.toString(fileSize));
		
		int resultMsg = mapper.setFileUpload(file);
		return resultMsg; 
	}
	
	public void mkDir() throws IOException {
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(cal.YEAR);
		int month = cal.get(cal.MONTH) + 1;
		int date = cal.get(cal.DATE);
		
		base_path = "D:\\workspace\\uploadfile\\";
		
		base_path += Integer.toString(year);
		base_path += Integer.toString(month);
		base_path += Integer.toString(date);
		
		System.out.println(base_path);
		File folder = new File(base_path);
		
		if(!folder.exists()) {
			folder.mkdir();
			System.out.println("폴더가 생성되었습니다");
		}else {
			System.out.println("이미 폴더가 존재합니다");
		}
	}
	
	public List<UploadVO> getFileList(){
		return mapper.getFileList();
	}

	public List<ExtNameVO> getExtList(){
		List<ExtNameVO> list = mapper.getExtList();
		List<ExtNameVO> result = new ArrayList<ExtNameVO>();
		
		for(ExtNameVO ext : list) {
			if(ext.getExtDp().equals("1")) {
				result.add(ext);
			}
		}
		
		return result;
	}

	// 고정 확장자 목록
	public List<ExtNameVO> getExtFixList(){
		List<ExtNameVO> list = mapper.getExtList();
		List<ExtNameVO> result = new ArrayList<ExtNameVO>();
		
		// 고정 확장자 사용하는 목록
		for(ExtNameVO ext : list) {
			if("1".equals(ext.getExtUse()) && "0".equals(ext.getExtDp())) {
				result.add(ext);
			}
		}
		
		return result;
	}
	
	// 확장자 등록
	public int setExt(String extName) {
		List<ExtNameVO> list = mapper.getExtList();
		int result = 0;
		extName = extName.toLowerCase();
		
		// 이미 존재하는 확장자는 등록 실패
		for(ExtNameVO ext : list ){
			if(extName.equals(ext.getExtName())) {
				result = -1;
				return result;
			}
		}
		
		if(list.size() > 200) {
			result = -2;
			return result;
		}
		
		result = mapper.setExtName(extName);
		
		return result;
	}
	
	public int deleteFile(String extOriName) {
		
		int result = mapper.deleteFile(extOriName);
		
		return result;
	}
	
	// 물리적으로 제거
	// 리스트로 남기기 보다 제거하고 다시 생성
	public int deleteExt(String extOriName) {
		
		int result = mapper.deleteExt(extOriName);
		
		return result;
	}
	
	// 확장자를 사용하는지 on/off
	public int updateExtUse(ExtNameVO ext) {
		
		int result = mapper.updateExtUse(ext);
		
		return result;
	}
}
