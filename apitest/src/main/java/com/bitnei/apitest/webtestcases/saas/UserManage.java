package com.bitnei.apitest.webtestcases.saas;

import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserManage {
	private WebDriver driver; 
	private int delay = 10;
	private String userName = "yuanpeipei@bitnei.cn";
	private String passWord = "123abc";
	private String email = "yaomeng@bitnei.cn";
	WebDriverWait waitVar = null;
	
	@BeforeClass
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		driver = new ChromeDriver();
	    //可以采用隐式设置通用的等待时间
	    driver.manage().timeouts().implicitlyWait(delay, TimeUnit.SECONDS);
	    //也可以采用显式根据条件判断结果设置等待时间
	    waitVar = new WebDriverWait(driver, delay);  
	    driver.manage().window().maximize();
	}
	@AfterClass
	public void afterTest() {
	     //关闭并退出浏览器  
	     driver.quit();  
	}
	
	@Test(description="登录成功,跳转页面用户管理页面成功",priority =0)
	public void LoginToUser() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
		driver.get("http://61.149.8.78:85/passport/login");
	    driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[1]/nz-form-control/div/span/nz-input-group/input")).sendKeys(userName);
	    driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[2]/nz-form-control/div/span/nz-input-group/input")).sendKeys(passWord);
	    Thread.sleep(1000);
	    driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[4]/button")).click();
	    waitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app/changan-layout/div/header/nav/div[1]/a[2]")));
	    Actions action = new Actions(driver);
	    WebElement menuButton = driver.findElement(By.xpath("/html/body/app/changan-layout/div/header/nav/div[2]/div[2]"));
	    action.moveToElement(menuButton).perform();
	    waitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app/changan-layout/div/header/nav/div[2]/div[2]/div/ul/li[5]/a/div[2]")));
	    driver.findElement(By.xpath("/html/body/app/changan-layout/div/header/nav/div[2]/div[2]/div/ul/li[5]/a/div[2]")).click();
	    assertTrue(driver.findElement(By.xpath("/html/body/app-root/app-sass/div/div/app-left-sass-menu/div/div/div[1]/div/a[1]/span")).getText().equals("用户管理"));
	   	   
	}
	
	@Test(description="登录成功,用户管理页面展示高级查询成功",priority =1)
	public void UserOpen() throws InterruptedException {
		 driver.findElement(By.xpath("/html/body/app-root/app-sass/div/div/app-left-sass-menu/div/div/div[2]/app-user-manager/nz-card/div/form/nz-row/nz-col/nz-form-item/div/a")).click();		
		    assertTrue(driver.findElement(By.xpath
		    		("/html/body/app-root/app-sass/div/div/app-left-sass-menu/div/div/div[2]/app-user-manager/nz-card/div/form/nz-row[2]/nz-col[1]/nz-form-item/nz-form-label/label")).getText().equals("用户角色"));		
	}
	
	@Test(description="登录成功,用户管理页面按邮箱查询成功",priority =2)
	public void UserQueryByEmail() throws InterruptedException {
		 driver.findElement(By.id("uEmail")).sendKeys(email);
		 driver.findElement(By.xpath("/html/body/app-root/app-sass/div/div/app-left-sass-menu/div/div/div[2]/app-user-manager/nz-card/div/form/nz-row[1]/nz-col/nz-form-item/div/button[1]")).click();
		 waitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
				 ("/html/body/app-root/app-sass/div/div/app-left-sass-menu/div/div/div[2]/app-user-manager/nz-card/div/st/nz-table/div/nz-spin/div/div[2]/nz-pagination/ul/li[1]")));
		    assertTrue(driver.findElement(By.xpath
		    		("/html/body/app-root/app-sass/div/div/app-left-sass-menu/div/div/div[2]/app-user-manager/nz-card/div/st/nz-table/div/nz-spin/div/div[2]/nz-pagination/ul/li[1]")).getText().equals("共 1 条"));		
	}
	
	@Test(description="登录成功,用户管理页面用户详情查看成功",priority =3)
	public void UserDetail() throws InterruptedException {
		 driver.findElement(By.xpath
				 ("/html/body/app-root/app-sass/div/div/app-left-sass-menu/div/div/div[2]/app-user-manager/nz-card/div/st/nz-table/div/nz-spin/div/div[2]/div/div/div/div/table/tbody/tr[1]/td[8]/span[2]/a[1]/span")).click();
		    assertTrue(driver.getCurrentUrl().equals("http://61.149.8.78:85/manager/platform/user/info;name=yaomeng@bitnei.cn"));		
	}
	
	@Test(description="登录成功,分组管理查看",priority =4)
	public void GourpQuery() throws InterruptedException {
		 driver.findElement(By.xpath
				 ("/html/body/app-root/app-sass/div/div/app-left-sass-menu/div/div/div[1]/div/a[2]/span")).click();
		 assertTrue(driver.findElement(By.xpath
		    		("/html/body/app-root/app-sass/div/div/app-left-sass-menu/div/div/div[2]/group-manage/div/div[1]/h3")).getText().equals("分组管理"));
	}
	
	@Test(description="登录成功,权限管理查看",priority =5)
	public void PermissionQuery() throws InterruptedException {
		 driver.findElement(By.xpath
				 ("/html/body/app-root/app-sass/div/div/app-left-sass-menu/div/div/div[1]/div/a[3]/span")).click();
		 assertTrue(driver.findElement(By.xpath
		    		("/html/body/app-root/app-sass/div/div/app-left-sass-menu/div/div/div[2]/permission/div/div[1]/h3")).getText().equals("权限管理"));
	}

}
