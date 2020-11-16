package com.bitnei.apitest.webtestcases.saas;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SaasLogin {
	
	private WebDriver driver; 
	private int delay = 10;
	private String userName = "yaomeng@bitnei.cn";
	private String passWord = "123456a";
	private String wrongPassWord = "123ab";
	WebDriverWait waitVar = null;
	
	@BeforeMethod
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		driver = new ChromeDriver();
	    //可以采用隐式设置通用的等待时间
	    driver.manage().timeouts().implicitlyWait(delay, TimeUnit.SECONDS);
	    //也可以采用显式根据条件判断结果设置等待时间
	    waitVar = new WebDriverWait(driver, delay);  
	    driver.manage().window().maximize();
	}
	@AfterMethod
	public void afterTest() {
	     //关闭并退出浏览器  
	     driver.quit();  
	}
	
	@Test(description="点击登录按钮跳转正确")
	public void Login(){		
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);         
		driver.get("http://61.149.8.78:85/home"); 
		driver.findElement(By.xpath("/html/body/app-root/app-sass/div/header/nav/div[2]/div/a[4]")).click();      
		String get_title = driver.getCurrentUrl();
		assertTrue(get_title.equals("http://61.149.8.78:85/passport/login"));          
	}
	
	@Test(description="登录失败,请输入用户名")
	public void LoginNoName() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
		driver.get("http://61.149.8.78:85/passport/login");
	    Thread.sleep(1000);
	    driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[4]/button")).click();
	    //waitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app/changan-layout/div/header/nav/div[1]/a[2]")));
	    assertTrue(driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-alert/div/span")).getText().equals("请输入用户名"));
	}
	
	@Test(description="登录失败,请输入密码")
	public void LoginNoPassWord() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
		driver.get("http://61.149.8.78:85/passport/login");
	    driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[1]/nz-form-control/div/span/nz-input-group/input")).sendKeys(userName);
	    Thread.sleep(1000);
	    driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[4]/button")).click();
	    //waitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app/changan-layout/div/header/nav/div[1]/a[2]")));
	    assertTrue(driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-alert/div/span")).getText().equals("请输入密码"));
	}
	
	
	@Test(description="登录失败,用户名密码错误")
	public void LoginFaild() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
		driver.get("http://61.149.8.78:85/passport/login");
	    driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[1]/nz-form-control/div/span/nz-input-group/input")).sendKeys(userName);
	    driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[2]/nz-form-control/div/span/nz-input-group/input")).sendKeys(wrongPassWord);
	    Thread.sleep(1000);
	    driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[4]/button")).click();
	    //waitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app/changan-layout/div/header/nav/div[1]/a[2]")));
	    assertTrue(driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-alert/div/span")).getText().equals("用户名或密码错误！"));
	}
	
	@Test(description="登录成功")
	public void LoginSucsess() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
		driver.get("http://61.149.8.78:85/passport/login");
//	    waitVar.until(ExpectedConditions.elementToBeClickable(By.xpath
//				("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[4]/button")));
	    driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[1]/nz-form-control/div/span/nz-input-group/input")).sendKeys(userName);
	    driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[2]/nz-form-control/div/span/nz-input-group/input")).sendKeys(passWord);
	    Thread.sleep(1000);
	    driver.findElement(By.xpath("/html/body/app-root/layout-passport/div/div/passport-login/div/form/nz-form-item[4]/button")).click();
	    waitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app/changan-layout/div/header/nav/div[1]/a[2]")));
	    System.out.println(driver.getTitle());
	    assertTrue(driver.getTitle().equals("智能网联汽车大数据云平台"));
	}

}
