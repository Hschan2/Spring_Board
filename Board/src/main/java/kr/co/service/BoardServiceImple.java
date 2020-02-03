package kr.co.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.dao.BoardDAO;
import kr.co.util.FileUtils;
import kr.co.vo.BoardVO;
import kr.co.vo.Criteria;
import kr.co.vo.SearchCriteria;

@Service
public class BoardServiceImple implements BoardService {
	
	@Resource(name="fileUtils")
	private FileUtils fileUtils;
	
	@Inject
	private BoardDAO dao;
	
	// 게시글 작성
	@Override
	public void write(BoardVO boardVO, MultipartHttpServletRequest mpRequest) throws Exception {
		dao.write(boardVO);
		
		List<Map<String,Object>> list = fileUtils.parseInsertFileInfo(boardVO, mpRequest); 
		int size = list.size();
		for(int i=0; i<size; i++){ 
			dao.insertFile(list.get(i)); 
		}
	}

	// 게시글 목록
	@Override
	public List<BoardVO> list(SearchCriteria scri) throws Exception {
		return dao.list(scri); // dao의 list() 함수를 반환한다.
	}
	
	// 게시물 갯수
	@Override
	public int listCount(SearchCriteria scri) throws Exception {
		return dao.listCount(scri); // 값 반환
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
