package kr.co.dao;

import kr.co.vo.BoardVO;

public interface BoardDAO {
	public void write(BoardVO boardVO) throws Exception; // 게시글 작성
}
