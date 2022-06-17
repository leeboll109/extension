package com.dev.board.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dev.board.service.UploadService;

@Controller
public class MainBoardController {

	@Autowired
	private UploadService service;
	
	@GetMapping("/main")
	public String mainBoard(Model model) {
		
		model.addAttribute("extSize", service.getExtList().size());
		model.addAttribute("extList", service.getExtList());
		model.addAttribute("fileList", service.getFileList());
		
		return "main";
	}
}
