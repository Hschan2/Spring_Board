package kr.co.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.service.BoardService;
import kr.co.service.ReplyService;
import kr.co.vo.BoardVO;
import kr.co.vo.Criteria;
import kr.co.vo.PageMaker;
import kr.co.vo.ReplyVO;
import kr.co.vo.SearchCriteria;

// @Controller => client의 request가 서버에 도착했을 때 어떻게 처리
// @RequestMapping => 사용자 요청을 Controller가 가져와 실행
@Controller
@RequestMapping("/board/*") // board의 모든 것을 가져와라
public class BoardController {
// 출력할 메시지, Logger => 파일로 저장된 로그를 분석해서 가져오기 위해, ex)툴 = log4j
private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	// Inject => 해당 타입의 객체를 찾아 자동으로 할당. BoardService, ReplyService의 객체를 자동으로 할당
	@Inject
	BoardService service;
	
	@Inject
	ReplyService replyService;
	
	// 게시판 글 작성 화면, get은 화면으로 이동
	@RequestMapping(value = "/board/writeView", method = RequestMethod.GET)
	public void writeView() throws Exception{
		logger.info("writeView"); // 글쓰기 화면 출력
	}
	
	// 게시판 글 작성, post는 데이터를 전송
	@RequestMapping(value = "/board/write", method = RequestMethod.POST)
	public String write(BoardVO boardVO, MultipartHttpServletRequest mpRequest) throws Exception{
		logger.info("write"); // 글 작성 출력
		// service에 값을 저장
		service.write(boardVO, mpRequest);
		// 글쓰기 완료하면 목록으로 이동
		return "redirect:/borad/list";
	}
	
	// 게시판 목록 조회
	// SearchCriteria => 검색 타입을 속성으로 가지고 있다.
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, @ModelAttribute("scri") SearchCriteria scri) throws Exception{
		logger.info("list");
		// 리스트에 값을 전달
		model.addAttribute("list", service.list(scri));
		
		// 목록 페이지 숫자
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(scri);
		pageMaker.setTotalCount(service.listCount(scri));
		
		model.addAttribute("pageMaker", pageMaker);
		
		return "board/list";
		
	}
		
		// 게시판 조회
		@RequestMapping(value = "/readView", method = RequestMethod.GET)
		public String read(BoardVO boardVO, @ModelAttribute("scri") SearchCriteria scri, Model model) throws Exception{
			logger.info("read");
			
			model.addAttribute("read", service.read(boardVO.getBno()));
			model.addAttribute("scri", scri);
			
			// 댓글 목록, 댓글은 여러개의 개수를 가지고 있기 때문에 배열로
			List<ReplyVO> replyList = replyService.readReply(boardVO.getBno());
			model.addAttribute("replyList", replyList);
			
			List<Map<String, Object>> fileList = service.selectFileList(boardVO.getBno());
			model.addAttribute("file", fileList);
			return "board/readView";
		}
		
		// 게시판 수정뷰
		@RequestMapping(value = "/updateView", method = RequestMethod.GET)
		public String updateView(BoardVO boardVO, @ModelAttribute("scri") SearchCriteria scri, Model model)
				throws Exception {
			logger.info("updateView");

			model.addAttribute("update", service.read(boardVO.getBno()));
			model.addAttribute("scri", scri);

			// 페이지에 파일목록을 보이게 해준다. 파일은 여러개이기 때문에
			List<Map<String, Object>> fileList = service.selectFileList(boardVO.getBno());
			model.addAttribute("file", fileList);
			return "board/updateView";
		}

		// 게시판 수정
		// RedirectAttributes => 데이터를 전달할 때 get방식은 url에 노출되는 단점을 노출되지 않게 전달
		@RequestMapping(value = "/update", method = RequestMethod.POST)
		public String update(BoardVO boardVO, 
							 @ModelAttribute("scri") SearchCriteria scri, 
							 RedirectAttributes rttr,
							 // @RequestParam의 value들은 JSP 파일의 같은 이름을 가진 값을 string으로 담겠다
							 @RequestParam(value="fileNoDel[]") String[] files,
							 @RequestParam(value="fileNameDel[]") String[] fileNames,
							 MultipartHttpServletRequest mpRequest) throws Exception {
			logger.info("update");
			service.update(boardVO, files, fileNames, mpRequest);

			rttr.addAttribute("page", scri.getPage());
			rttr.addAttribute("perPageNum", scri.getPerPageNum());
			rttr.addAttribute("searchType", scri.getSearchType());
			rttr.addAttribute("keyword", scri.getKeyword());

			return "redirect:/board/list";
		}

		// 게시판 삭제
		@RequestMapping(value = "/delete", method = RequestMethod.POST)
		public String delete(BoardVO boardVO, @ModelAttribute("scri") SearchCriteria scri, RedirectAttributes rttr) throws Exception{
			logger.info("delete");
			
			service.delete(boardVO.getBno());
			
			rttr.addAttribute("page", scri.getPage());
			rttr.addAttribute("perPageNum", scri.getPerPageNum());
			rttr.addAttribute("searchType", scri.getSearchType());
			rttr.addAttribute("keyword", scri.getKeyword());
			
			return "redirect:/board/list";
		}
		
		// 댓글 작성
		// ReplyVO = 댓글 작성위한 데이터, SearchCriteria = page, keyword 등의 값 받기, RedirectAttributes = redirect 했을 때 값 데리고 이동
		@RequestMapping(value="/replyWrite", method = RequestMethod.POST)
		public String replyWrite(ReplyVO vo, SearchCriteria scri, RedirectAttributes rttr) throws Exception {
			logger.info("reply Write");
			
			replyService.writeReply(vo);
			
			rttr.addAttribute("bno", vo.getBno());
			rttr.addAttribute("page", scri.getPage());
			rttr.addAttribute("perPageNum", scri.getPerPageNum());
			rttr.addAttribute("searchType", scri.getSearchType());
			rttr.addAttribute("keyword", scri.getKeyword());
			
			return "redirect:/board/readView";
		}
		
		//댓글 수정 GET, 수정 페이지에 접근
		@RequestMapping(value="/replyUpdateView", method = RequestMethod.GET)
		public String replyUpdateView(ReplyVO vo, SearchCriteria scri, Model model) throws Exception {
			logger.info("reply Write");
			
			model.addAttribute("replyUpdate", replyService.selectReply(vo.getRno()));
			model.addAttribute("scri", scri);
			
			return "board/replyUpdateView";
		}
		
		//댓글 수정 POST, 수정한 값을 전송
		@RequestMapping(value="/replyUpdate", method = RequestMethod.POST)
		public String replyUpdate(ReplyVO vo, SearchCriteria scri, RedirectAttributes rttr) throws Exception {
			logger.info("reply Write");
			
			replyService.updateReply(vo);
			
			rttr.addAttribute("bno", vo.getBno());
			rttr.addAttribute("page", scri.getPage());
			rttr.addAttribute("perPageNum", scri.getPerPageNum());
			rttr.addAttribute("searchType", scri.getSearchType());
			rttr.addAttribute("keyword", scri.getKeyword());
			
			return "redirect:/board/readView";
		}
		
		//댓글 삭제 GET, 삭제 페이지로 전송
		@RequestMapping(value="/replyDeleteView", method = RequestMethod.GET)
		public String replyDeleteView(ReplyVO vo, SearchCriteria scri, Model model) throws Exception {
			logger.info("reply Write");
			
			model.addAttribute("replyDelete", replyService.selectReply(vo.getRno()));
			model.addAttribute("scri", scri);
			

			return "board/replyDeleteView";
		}
		
		//댓글 삭제, 삭제 값을 전달
		@RequestMapping(value="/replyDelete", method = RequestMethod.POST)
		public String replyDelete(ReplyVO vo, SearchCriteria scri, RedirectAttributes rttr) throws Exception {
			logger.info("reply Write");
			
			replyService.deleteReply(vo);
			
			rttr.addAttribute("bno", vo.getBno());
			rttr.addAttribute("page", scri.getPage());
			rttr.addAttribute("perPageNum", scri.getPerPageNum());
			rttr.addAttribute("searchType", scri.getSearchType());
			rttr.addAttribute("keyword", scri.getKeyword());
			
			return "redirect:/board/readView";
		}
		
		@RequestMapping(value="/fileDown")
		public void fileDown(@RequestParam Map<String, Object> map, HttpServletResponse response) throws Exception{
			Map<String, Object> resultMap = service.selectFileInfo(map);
			String storedFileName = (String) resultMap.get("STORED_FILE_NAME");
			String originalFileName = (String) resultMap.get("ORG_FILE_NAME");
			
			// 파일을 저장했던 위치에서 첨부파일을 읽어 byte[]형식으로 변환한다.
			byte fileByte[] = org.apache.commons.io.FileUtils.readFileToByteArray(new File("D:\\mp\\file\\"+storedFileName));
			
			response.setContentType("application/octet-stream");
			response.setContentLength(fileByte.length);
			response.setHeader("Content-Disposition",  "attachment; fileName=\""+URLEncoder.encode(originalFileName, "UTF-8")+"\";");
			response.getOutputStream().write(fileByte);
			response.getOutputStream().flush();
			response.getOutputStream().close(); // finish는 데이터를 비우고 끝, close는 닫기
		}
}
