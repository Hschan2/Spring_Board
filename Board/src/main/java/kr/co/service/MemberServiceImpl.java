package kr.co.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.dao.MemberDAO;
import kr.co.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {

	@Inject 
	MemberDAO dao;
	
	// 회원가입
	@Override
	public void register(MemberVO vo) throws Exception {
		dao.register(vo);
	}
	
	// 로그인
	@Override
	public MemberVO login(MemberVO vo) throws Exception {
		return dao.login(vo);
	}
	
	//Controller에서 보내는 파라미터들을 memberUpdate(MemberVO vo)로 받고
	@Override
	public void memberUpdate(MemberVO vo) throws Exception {
		//받은 vo를 DAO로 보내줍니다.
		dao.memberUpdate(vo);
	}
	
	// 회원탈퇴, 업데이트와 같은 원리, MemberDAO로 파라미터를 보냄
	@Override
	public void memberDelete(MemberVO vo) throws Exception {
		dao.memberDelete(vo);
	}
	
	// 패스워드 체크, 조회한 값과 보내는 값이 숫자여서 INT, 컨트롤러에서 보내는 값을 받음
	@Override
	public int passChk(MemberVO vo) throws Exception {
		int result = dao.passChk(vo);
		return result;
	}
	
	// 아이디 중복 체크, 컨트롤러에 보낸 값을 받고 그 값을 DAO로 보낸다.
	// 그리고 DAO에서 다시 RETURN한 값을 내보낸다
	@Override
	public int idChk(MemberVO vo) throws Exception {
		int result = dao.idChk(vo);
		return result;
	}
}
