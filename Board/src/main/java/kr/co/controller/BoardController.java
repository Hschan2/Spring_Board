package kr.co.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	@RequestMapping(value = "/board/writeView", method = RequestMethod.GET)
	public void writeView() throws Exception{
		logger.info("writeView"); // 글쓰기 화면 출력
		
	}
	
	// 게시판 글 작성
	@RequestMapping(value = "/board/write", method = RequestMethod.POST)
	public String write(BoardVO boardVO) throws Exception{
		logger.info("write"); // 글 작성 출력
		
		service.write(boardVO);
		
		return "redirect:/borad/list";
	}
	
	// 게시판 목록 조회
		@RequestMapping(value = "/list", method = RequestMethod.GET)
		public String list(Model model) throws Exception{
			logger.info("list"); // list의 정보를 log 한다.
			
			// service.list()의 데이터를 list의 이름으로 담을 것
			model.addAttribute("list",service.list());
			
			return "board/list";
			
		}
}
