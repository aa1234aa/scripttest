package com.bitnei.apitest.testcases.datasend;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.testng.annotations.Test;

import com.bitnei.apitest.pro.datasendpro.HanDao;
@Test
@Service
public class HanTest {
	@Resource(name="hanDao")
	private HanDao hanDao;
	
	public void HabDao() {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-mybatis.xml");
		HanDao hanDao = (HanDao) context.getBean("hanDao");

		System.out.println("hanDao------------"+hanDao);
		hanDao.printBrand();
	};
	

	
	
	


}
