package com.dev.board.dto;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dev.board.vo.ExtNameVO;
import com.dev.board.vo.UploadVO;


@Mapper
public interface UploadMapper {

	public List<UploadVO> getFileList();
	public List<ExtNameVO> getExtList();
	public int setExtName(String param);
	public int setFileUpload(UploadVO param);
	public int deleteFile(String param);
	public int deleteExt(String param);
	public int updateExtUse(ExtNameVO param);
}
