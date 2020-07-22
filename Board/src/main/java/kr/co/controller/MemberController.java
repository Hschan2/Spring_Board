package kr.co.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.lang.reflect.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.service.MemberService;
import kr.co.vo.MemberVO;

@Controller
@RequestMapping("/member/*")
public class MemberController {
private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Inject
	MemberService service;
	
	@Inject
	BCryptPasswordEncoder pwdEncoder;
	
	// 회원가입 get, 회원가입 페이지로 이동
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void getRegister() throws Exception {
		logger.info("get register");
	}
	
	// 회원가입 post, 가입 데이터 값을 전달
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String postRegister(MemberVO vo) throws Exception {
		logger.info("post register");
		int result = service.idChk(vo);
		try {
			if(result == 1) { // 아이디가 중복됐다.
				return "/member/register"; // 그러니 가입 페이지로 가라
			}else if(result == 0) { // 중복된 아이디가 없다.
				String inputPass = vo.getUserPass();
				String pwd = pwdEncoder.encode(inputPass);
				vo.setUserPass(pwd);
				
				service.register(vo); // 아이디가 등록됐다.
			}
			// 요기에서~ 입력된 아이디가 존재한다면 -> 다시 회원가입 페이지로 돌아가기 
			// 존재하지 않는다면 -> register
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return "redirect:/"; // 가입이 되었으니 홈으로 이동한다.
	}
	
	// 로그인
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(MemberVO vo, HttpSession session, RedirectAttributes rttr) throws Exception{
		logger.info("post login");
		
		session.getAttribute("member");
		MemberVO login = service.login(vo);
		boolean pwdMatch = pwdEncoder.matches(vo.getUserPass(), login.getUserPass());
		
		if(login != null && pwdMatch == true) {
			session.setAttribute("member", login);
			return "redirect:/board/list";
		}else {
			session.setAttribute("member", null);
			rttr.addFlashAttribute("msg", false);
			return "redirect:/";
		}
	}
	
	// 로그아웃
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception{
		
		session.invalidate();
		
		return "redirect:/";
	}
	
	// 회원 수정 페이지로 이동
	@RequestMapping(value="/memberUpdateView", method = RequestMethod.GET)
	public String registerUpdateView() throws Exception{
		
		return "member/memberUpdateView";
	}

	// 회원 수정을 위한 데이터 값을 전달
	@RequestMapping(value="/memberUpdate", method = RequestMethod.POST)
	public String registerUpdate(MemberVO vo, HttpSession session) throws Exception{
		
		service.memberUpdate(vo);
		
		session.invalidate();
		
		return "redirect:/";
	}
	
	// 회원 탈퇴 get, 회원탈퇴 화면으로 이동
	@RequestMapping(value="/memberDeleteView", method = RequestMethod.GET)
	public String memberDeleteView() throws Exception{
		return "member/memberDeleteView";
	}
		
	// 회원 탈퇴 post, 회원 탈퇴를 위한 데이터 값을 전달
	@RequestMapping(value="/memberDelete", method = RequestMethod.POST)
	public String memberDelete(MemberVO vo, HttpSession session, RedirectAttributes rttr) throws Exception{
			
	/*
	// 세션에 있는 member를 가져와 member변수에 넣어줍니다.
	MemberVO member = (MemberVO) session.getAttribute("member");
	// 세션에있는 비밀번호
	String sessionPass = member.getUserPass();
	// vo로 들어오는 비밀번호
	String voPass = vo.getUserPass();
	
	// 입력한 비밀번호와 계정 비밀번호가 다르면
	if(!(sessionPass.equals(voPass))) {
		// 비밀번호가 다르다는 메세지 전달
		rttr.addFlashAttribute("msg", false);
		// 탈퇴화면으로 다시 이동
		return "redirect:/member/memberDeleteView";
	}
	*/
		// 비밀번호가 맞으면 회원 탈퇴
		service.memberDelete(vo);
		// 로그아웃 
		session.invalidate();
		// 홈으로 이동
		return "redirect:/";
	}
	
		// 패스워드 체크
		// @ResponseBody는 값을 view로 통해 보여주지 않고 http에 직접 사용
		@ResponseBody
		@RequestMapping(value="/passChk", method = RequestMethod.POST)
		public boolean passChk(MemberVO vo) throws Exception {
			// int result = service.passChk(vo);
			// 일치하면 1 아니면 0이 return
			MemberVO login = service.login(vo);
			boolean pwdChk = pwdEncoder.matches(vo.getUserPass(), login.getUserPass());
			return pwdChk;
		}
		
		// 아이디 중복 체크
		@ResponseBody
		@RequestMapping(value="/idChk", method = RequestMethod.POST)
		public int idChk(MemberVO vo) throws Exception {
			int result = service.idChk(vo);
			// 아이디가 일치(중복)되면 1, 아니면 0 을 출력
			return result;
		}
}
