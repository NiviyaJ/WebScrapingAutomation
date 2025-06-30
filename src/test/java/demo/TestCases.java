package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */
    @Test
    public void testCase01(){
        driver.get("https://www.scrapethissite.com/pages/");
        WebElement hockeyLink = driver.findElement(By.partialLinkText("Hockey Teams"));
        Wrappers.click(driver, hockeyLink);
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/pages/forms/"));
        ArrayList<HashMap<String, String>> listOfData = Wrappers.navigatePageToCollectData(driver, 0.4,4);
        Wrappers.writeToJSONFile(listOfData, "hockey-team-data");
    }

    @Test
    public void testCase02(){
        driver.get("https://www.scrapethissite.com/pages/");
        WebElement oscarFilmLink = driver.findElement(By.partialLinkText("Oscar Winning Films"));
        Wrappers.click(driver, oscarFilmLink);
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/pages/ajax-javascript/"));
        ArrayList<HashMap<String, String>> listOfMovies = Wrappers.getTopMoviesEachYear(driver);
        boolean status = Wrappers.writeToJSONFile(listOfMovies, "oscar-winner-data");
        Assert.assertTrue(status, "File does not exist");
    }
     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }
}