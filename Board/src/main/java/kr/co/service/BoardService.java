package kr.co.service;

import java.util.List;

import kr.co.vo.BoardVO;

public interface BoardService {
	
	// 게시글 작성
	public void write(BoardVO boardVO) throws Exception; 
	
	// 게시물 목록 조회
	public List<BoardVO> list() throws Exception;
	
	// 게시물 목록 조회, int인자는 넘겨 받아야 할 id 값
	public BoardVO read(int bno) throws Exception;
	
	// 게시물 수정(VO의 선언 값 모두 적용하기 위해)
	public void update(BoardVO boardVO) throws Exception;
		
	// 게시물 삭제(삭제할 ID 넘기기)
	public void delete(int bno) throws Exception;
}
