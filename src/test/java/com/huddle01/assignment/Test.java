package com.huddle01.assignment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Test {

	
	public static ChromeOptions getChromeOptions() {
		ChromeOptions options = new ChromeOptions();
	    options.addArguments("use-fake-device-for-media-stream");
	    options.addArguments("use-fake-ui-for-media-stream");
	    
	    return options;
	    
	}
	
	public static DesiredCapabilities getCapabilities() {
		
		Map<String, Object> prefs = new HashMap<String, Object>();
	    Map<String, Object> profile = new HashMap<String, Object>();
	    Map<String, Object> contentSettings = new HashMap<String, Object>();
	    ChromeOptions options = getChromeOptions();
	    // SET CHROME OPTIONS
	    // 0 - Default, 1 - Allow, 2 - Block
	    contentSettings.put("notifications", 2);
	    profile.put("managed_default_content_settings", contentSettings);
	    prefs.put("profile", profile);
	    options.setExperimentalOption("prefs", prefs);
	    
	    DesiredCapabilities caps = new DesiredCapabilities();
	    
	    caps.setCapability(ChromeOptions.CAPABILITY, options);
	    
	    return caps;
		
	}
	
	public static WebDriver getChromeWebDriver(DesiredCapabilities capabilities) {
		
		@SuppressWarnings("deprecation")
		WebDriver driver=new ChromeDriver(capabilities); 
		
		return driver;
	}
	
	public static void loginUserOne(WebDriver driver) {
		
	}
	public static void main(String[] args) throws InterruptedException {

		String chromedriverPath = System.getProperty("user.dir")+"/src/test/resources/drivers/chromedriver";

		System.setProperty("webdriver.chrome.driver", chromedriverPath);  
		
		@SuppressWarnings("deprecation")
		WebDriver driver=getChromeWebDriver(getCapabilities());
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();  
		
		driver.navigate().to("https://beta.huddle01.com/");  
		
		WebDriverWait wait = new WebDriverWait(driver,30);
		
		WebElement webElement = driver.findElement(By.xpath("//button[text()='Start Meeting']"));
		webElement.click();
		
		
		String meetingUrl = driver.getCurrentUrl();
		System.out.println("meetingUrl:"+meetingUrl);
		
		WebDriver driver02=getChromeWebDriver(getCapabilities());
		driver02.get(meetingUrl);
		
		WebElement inputBox = driver.findElement(By.xpath("//input[@placeholder='Please Enter Your Name']"));
		//inputBox.wait(5000);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='Please Enter Your Name']")));
		inputBox.clear();
		inputBox.sendKeys("User1");
		
		WebElement enterMeetingBtn = driver.findElement(By.xpath("//button[text()='Enter Meeting']"));
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Enter Meeting']")));
		enterMeetingBtn.click();
		
		
		WebElement inputBox2 = driver02.findElement(By.xpath("//input[@placeholder='Please Enter Your Name']"));
		//inputBox.wait(5000);
		new WebDriverWait(driver02,30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='Please Enter Your Name']")));
		inputBox2.clear();
		inputBox2.sendKeys("User2");
		
		new WebDriverWait(driver02,30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Enter Meeting']"))).click();
		
		
		WebElement imgLock = driver.findElement(By.xpath("//img[@alt='notifications' and contains(@src,'LockActive')]"));
		imgLock.click();
        WebElement admitAllBtn = driver.findElement(By.xpath("//button[text()='Admit All']"));
        new WebDriverWait(driver,30).until(ExpectedConditions.visibilityOf(admitAllBtn)).click();
        
		
		
		//driver.quit();
		 
	}

}
