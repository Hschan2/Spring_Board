package kr.co.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.dao.BoardDAO;
import kr.co.util.FileUtils;
import kr.co.vo.BoardVO;
import kr.co.vo.Criteria;
import kr.co.vo.SearchCriteria;

@Service
public class BoardServiceImple implements BoardService {
	
	// 파일첨부 사용
	@Resource(name="fileUtils")
	private FileUtils fileUtils;
	
	// DAO 가져오기
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
	// Transactional => 트랜잭션을 사용하며 격리성을 사용하겠다(충돌 방지)
	@Transactional(isolation = Isolation.READ_COMMITTED)
	@Override
	public BoardVO read(int bno) throws Exception {
		dao.boardHit(bno);
		return dao.read(bno);
	}
	
	// 게시물 목록 수정, VO의 값 모두 적용
	@Override
	public void update(BoardVO boardVO, String[] files, String[] fileNames, MultipartHttpServletRequest mpRequest) throws Exception {
		
		// 게시물 수정 쿼리
		dao.update(boardVO);
		
		// 첨부파일 수정 값을 list레 담는다.
		List<Map<String, Object>> list = fileUtils.parseUpdateFileInfo(boardVO, files, fileNames, mpRequest);
		Map<String, Object> tempMap = null;
		int size = list.size();
		// fileUtils.parseUpdateFileInfo 결과의 크기만큼 반복
		for(int i = 0; i<size; i++) {
			// tempMap에서 IS_NEW값을 가져와 값이 Y면 dao.insertFile 실행
			// 아니면 dao.updateFile 실행
			tempMap = list.get(i);
			if(tempMap.get("IS_NEW").equals("Y")) {
				dao.insertFile(tempMap);
			}else {
				dao.updateFile(tempMap);
			}
		}
	}

	// 게시물 목록 삭제(ID값 넘기기)
	@Override
	public void delete(int bno) throws Exception {
		dao.delete(bno);
	}
	
	// 첨부파일 조회, bno값 넘김(dao에)
	@Override
	public List<Map<String, Object>> selectFileList(int bno) throws Exception {
		// TODO Auto-generated method stub
		return dao.selectFileList(bno);
	}

	// 첨부파일 다운로드
	@Override
	public Map<String, Object> selectFileInfo(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return dao.selectFileInfo(map);
	}
}
