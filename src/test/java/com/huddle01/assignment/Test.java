package com.huddle01.assignment;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Test {

    static {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");
    }

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

        contentSettings.put("notifications", 2);
        profile.put("managed_default_content_settings", contentSettings);
        prefs.put("profile", profile);
        options.setExperimentalOption("prefs", prefs);

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(ChromeOptions.CAPABILITY, options);
        return caps;
    }

    public static void main(String[] args) {
        String url = "https://beta.huddle01.com/", meetingUrl;
        WebDriver driver_1 = null, driver_2 = null;

        try {
            // Init Browser 1
            driver_1 = getChromeWebDriver(getCapabilities());
            driver_1.navigate().to(url);
            clickOnStartMeeting(driver_1);
            System.out.println("meetingUrl:" + (meetingUrl = driver_1.getCurrentUrl())); // Get meeting link
            System.out.println("Browser 1 initialized..");

            // Login User 1
            loginUser(driver_1, "User1");

            // Init Browser 2
            driver_2 = getChromeWebDriver(getCapabilities());
            System.out.println("Browser 2 initialized..");
            driver_2.navigate().to(meetingUrl);

            // Login User 2
            loginUser(driver_2, "User2");

            // Accept User2 invitation
            admitAllUsers(driver_1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver_1.quit();
            driver_2.quit();
        }
    }

    public static void clickOnStartMeeting(WebDriver driver) {
        driver.findElement(By.xpath("//button[text()='Start Meeting']")).click();
        System.out.println("Clicked on Start Meeting.");
    }

    public static void loginUser(WebDriver driver, String userName) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement inputBox = driver.findElement(By.xpath("//input[@placeholder='Please Enter Your Name']"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='Please Enter Your Name']")));
        inputBox.clear();
        inputBox.sendKeys("");
        inputBox.sendKeys(userName);

        WebElement enterMeetingBtn = driver.findElement(By.xpath("//button[text()='Enter Meeting']"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Enter Meeting']")));
        enterMeetingBtn.click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return;
    }
    
    public static void admitAllUsers(WebDriver driver) {
        WebElement imgLock = driver.findElement(By.xpath("//img[@alt='notifications' and contains(@src,'LockActive')]"));
        imgLock.click();
        WebElement admitAllBtn = driver.findElement(By.xpath("//button[text()='Admit All']"));
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(admitAllBtn)).click();
        return;
    }

    public static WebDriver getChromeWebDriver(DesiredCapabilities capabilities) {
        @SuppressWarnings("deprecation")
        WebDriver driver = new ChromeDriver(capabilities);
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(60000, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
        return driver;
    }
}