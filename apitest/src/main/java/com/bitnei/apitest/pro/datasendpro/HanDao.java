package com.bitnei.apitest.pro.datasendpro;

import org.springframework.stereotype.Repository;

@Repository(value="hanDao")
public class HanDao {
	public void printBrand(){
        System.out.println("小米笔记本");

    }

}
