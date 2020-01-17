package kr.co.service;

import kr.co.vo.BoardVO;

public interface BoardService {
	public void write(BoardVO boardVO) throws Exception; // 게시글 작성
}
