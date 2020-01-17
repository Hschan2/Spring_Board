package kr.co.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.service.BoardService;
import kr.co.vo.BoardVO;

@Controller
@RequestMapping("/board/*") // board의 모든 것을 가져와라
public class BoardController {
	// 출력할 메시지
private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Inject
	BoardService service;
	
	// 게시판 글 작성 화면
	@RequestMapping(value = "/borad/writeView", method = RequestMethod.GET)
	public void writeView() throws Exception{
		logger.info("writeView"); // 글쓰기 화면 출력
		
	}
	
	// 게시판 글 작성
	@RequestMapping(value = "/borad/write", method = RequestMethod.POST)
	public String write(BoardVO boardVO) throws Exception{
		logger.info("write"); // 글 작성 출력
		
		service.write(boardVO);
		
		return "redirect:/";
	}
}
