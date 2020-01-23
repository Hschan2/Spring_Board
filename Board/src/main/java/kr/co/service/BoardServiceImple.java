package kr.co.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.dao.BoardDAO;
import kr.co.vo.BoardVO;

@Service
public class BoardServiceImple implements BoardService {
	@Inject
	private BoardDAO dao;
	
	// 게시글 작성
	@Override
	public void write(BoardVO boardVO) throws Exception {
		dao.write(boardVO);
	}

	// 게시글 목록
	@Override
	public List<BoardVO> list() throws Exception {
		return dao.list(); // dao의 list() 함수를 반환한다.
	}
	
	// 게시물 목록 조회, id값을 받는다
	@Override
	public BoardVO read(int bno) throws Exception {
		return dao.read(bno);
	}
	
	// 게시물 목록 수정, VO의 값 모두 적용
	@Override
	public void update(BoardVO boardVO) throws Exception {
		dao.update(boardVO);
	}

	// 게시물 목록 삭제(ID값 넘기기)
	@Override
	public void delete(int bno) throws Exception {
		dao.delete(bno);
	}
}
