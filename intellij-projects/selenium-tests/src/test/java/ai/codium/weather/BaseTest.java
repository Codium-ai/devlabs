package ai.codium.weather;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected void takeScreenshot(String fileName) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            if(!fileName.endsWith(".png")) {
                fileName = fileName+".png";
            }
            FileUtils.copyFile(screenshot, new File(fileName));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    protected void takeScreenshotOnFailure(Throwable e) {
        String exceptionMessage = e.getMessage();
        if (exceptionMessage.length() > 80) {
            exceptionMessage = exceptionMessage.substring(0, 80);
        }
        takeScreenshot("Failure_" + exceptionMessage.replaceAll("[^a-zA-Z0-9]", "_"));
    }


    @BeforeEach
    public void setUp() {
        // Set the path to the chromedriver executable
        System.setProperty("webdriver.chrome.driver", "/Users/davidparry/app/chromedriver-mac-arm64/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        driver = new ChromeDriver(capabilities);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:4000");
    }


    @AfterEach
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}
