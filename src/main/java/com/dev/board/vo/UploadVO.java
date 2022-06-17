package com.dev.board.vo;

import lombok.Data;

@Data
public class UploadVO {

	private Long fileId;
	
	private String fileName;
	private String fileOriName;
	private String fileUrl;
	private String fileSize;
	private String fileTime;
}
