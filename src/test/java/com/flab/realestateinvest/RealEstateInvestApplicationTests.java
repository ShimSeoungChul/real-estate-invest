package com.flab.realestateinvest;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;

@SpringBootTest
class RealEstateInvestApplicationTests {
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Test
	public void connectionTest(){
		try(Connection con = sqlSessionFactory.openSession().getConnection()){
			System.out.println("커넥션 성공");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
