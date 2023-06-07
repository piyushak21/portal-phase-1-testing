package com.starkenn.Starkenn;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.starkenn.Starkenn.pageObjects.BasePage;
import com.starkenn.Starkenn.pageObjects.DashboardPage;
import com.starkenn.Starkenn.pageObjects.DevicesPage;
import com.starkenn.Starkenn.pageObjects.LoginPage;
import com.starkenn.Starkenn.pageObjects.VehicePage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Devices {
	WebDriver driver;
	WebDriverWait wait;
	LoginPage loginPage;
	DashboardPage dashboardPage;
	BasePage basePage;
	DevicesPage devicePage ;
	
	@BeforeClass
	public void intializeDriver(){
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("http://sktn-react.s3-website.ap-south-1.amazonaws.com/");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		loginPage = new LoginPage(driver);
		loginPage.enterEmail("john@gmail.com");
		loginPage.enterPassword("123456");
		loginPage.clickOnSubmit();
		wait= new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlContains("/customer-dashboard"));
		dashboardPage = new DashboardPage(driver);
		basePage = new BasePage(driver);
		devicePage = new DevicesPage(driver);
	}
	
	@BeforeMethod
	public void openUrl(){
	driver.get("http://sktn-react.s3-website.ap-south-1.amazonaws.com/customer-dashboard");
	dashboardPage.clickOnDevices();
	wait.until(ExpectedConditions.urlContains("/customer-devices"));
	}
	
	@Test
	public void verifyTotaRecordsCount () 
	{
		basePage.selectRecordsPerPage("30");
		int noOfRecorsInTable = basePage.getNumberOfRecordsInTable();
		int totalRecordsCount = basePage.getTotalRecords();
		//Assert.assertEquals(noOfRecorsInTable, totalRecordsCount);
	}
	
	@Test
	public void verifySearchFunctionaityByDeviceId () 
	{
		String deviceId = "FC0";
		basePage.selectRecordsPerPage("30");
		devicePage.searchDeviceId(deviceId);
		int noOfRecordsMatchingWithDeviceId = devicePage.getNoRecordsMatchingWithDeviceId(deviceId);
		int totalNoOfRecordsInTable = basePage.getNumberOfRecordsInTable();
		Assert.assertEquals(noOfRecordsMatchingWithDeviceId, totalNoOfRecordsInTable);
	}
	
	@Test
	public void checkStatusOfDeviceId () 
	{
		String adminDeviceStatus = "Active";
		String deviceId = "EC3101A";
		basePage.selectRecordsPerPage("30");
		devicePage.searchDeviceId(deviceId);
		String deviceStatus = devicePage.getStatusOfDevice(deviceId);
		Assert.assertEquals(deviceStatus, adminDeviceStatus);
	}
	
	@AfterClass
	public void closeBrowser() {
		driver.quit();
	}

}
