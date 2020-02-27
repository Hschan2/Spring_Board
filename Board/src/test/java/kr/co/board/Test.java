package kr.co.board;

import java.sql.Connection;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class Test {
	// 데이터 베이스에 데이터를 저장하기 위한 객체
	@Inject
    private DataSource ds;
    
	// 데이터베이스와의 연결과 SQL의 실행에 대한 모든 것을 가진 가장 중요한 객체
    @Inject
    private SqlSessionFactory sqlFactory;
 
    // 테스트 실행할 함수 @Test
    @org.junit.Test
    public void test() throws Exception{
        try(Connection conn = ds.getConnection()){
        	// 데이터 베이스가 연결 되었으면
            System.out.println(conn);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
   
  
    @org.junit.Test
    public void factoryTest() {
        System.out.println(sqlFactory);
      
    }
  
    @org.junit.Test
    public void sessionTest() throws Exception{
      
        try(SqlSession session = sqlFactory.openSession()) {
        	// 데이터 베이스 세션이 연결 되었을 때
            System.out.println(session);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
