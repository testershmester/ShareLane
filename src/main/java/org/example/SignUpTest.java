package org.example;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class SignUpTest {
    public static final String ZIP_CODE_ERROR = "Oops, error on page. ZIP code should have 5 digits";
    public static final String USER_INFO_ERROR = "Oops, error on page. " +
            "Some of your fields have invalid data or email was previously used";
    WebDriver driver;

    @BeforeClass
    public void setPathToWebDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    }

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://www.sharelane.com/cgi-bin/register.py");
    }

    @Test
    public void zipCodeShouldBeValid() {
        WebElement zipCodeField = driver.findElement(By.name("zip_code"));
        zipCodeField.sendKeys("12345");
        driver.findElement(By.cssSelector("input[value='Continue']")).click();
        WebElement registerButton = driver.findElement(By.cssSelector("input[value='Register']"));
        assertTrue(registerButton.isDisplayed(), "User was not redirected to the registration page");
    }

    @Test
    public void userShouldNotBeRegisteredWithEmptyZipCode() {
        driver.findElement(By.cssSelector("input[value='Continue']")).click();
        WebElement errorMessage = driver.findElement(By.className("error_message"));
        assertTrue(errorMessage.isDisplayed(), "Error message is not displayed in case of empty zip code");
        assertEquals(errorMessage.getText(), ZIP_CODE_ERROR, "Error message should be correct");

    }

    @Test
    public void userShouldNotBeRegisteredWithInvalidZipCode() {
        WebElement zipCodeField = driver.findElement(By.name("zip_code"));
        zipCodeField.sendKeys("123456");
        driver.findElement(By.cssSelector("input[value='Continue']")).click();
        WebElement errorMessage = driver.findElement(By.className("error_message"));
        assertTrue(errorMessage.isDisplayed(), "Error message is not displayed in case of zip code more than 5 digits");
    }

    @Test
    public void userShouldBeRegisteredWithValidInfo() {
        driver.get("https://www.sharelane.com/cgi-bin/register.py?page=1&zip_code=12345");
        driver.findElement(By.name("first_name")).sendKeys("test");
        driver.findElement(By.name("last_name")).sendKeys("test");
        driver.findElement(By.name("email")).sendKeys("test@test.test");
        driver.findElement(By.name("password1")).sendKeys("12345");
        driver.findElement(By.name("password2")).sendKeys("12345");
        driver.findElement(By.cssSelector("input[value='Register']")).click();
        assertTrue(driver.findElement(By.className("confirmation_message")).isDisplayed(),
                "The message \"Account is created\" is displayed");
    }

    @Test
    public void userShouldNotBeRegisteredWithoutInfo() {
        driver.get("https://www.sharelane.com/cgi-bin/register.py?page=1&zip_code=12345");
        driver.findElement(By.cssSelector("input[value='Register']")).click();
        WebElement errorMessage = driver.findElement(By.className("error_message"));
        assertTrue(errorMessage.isDisplayed(), "Error message is not displayed in case of registration with empty user info");
        assertEquals(errorMessage.getText(), USER_INFO_ERROR, "Error message should be correct");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.quit();
    }
}
