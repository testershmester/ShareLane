package org.example;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


public class SignUpTest {
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
    }

    @Test
    public void userShouldNotBeRegisteredWithInvalidZipCode() {
        WebElement zipCodeField = driver.findElement(By.name("zip_code"));
        zipCodeField.sendKeys("123456");
        driver.findElement(By.cssSelector("input[value='Continue']")).click();
        WebElement errorMessage = driver.findElement(By.className("error_message"));
        assertTrue(errorMessage.isDisplayed(), "Error message is not displayed in case of zip code more than 5 digits");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.quit();
    }
}
