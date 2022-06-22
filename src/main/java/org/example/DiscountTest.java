package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * #-----------------------------
 * #basket         | discount, % / values
 * #-----------------------------
 * #0 - 19         | 0           | 0, 1, 19
 * #20 - 49        | 2           | 20, 49
 * #50 - 99        | 3           | 50, 99
 * #100-499        | 4           | 100, 499
 * #500-999        | 5           | 500, 999
 * #1000-4999      | 6           | 1000, 4999
 * #5000-9999      | 7           | 5000, 9999
 * #10000 and more | 8           | 10000
 */
public class DiscountTest {

    WebDriver driver;
    String email;
    String password;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        register();
        logIn();
    }

    @Test
    public void discountShouldBeZeroUpToTwentyBooks() {
        driver.get("https://www.sharelane.com/cgi-bin/add_to_cart.py?book_id=1");
        driver.findElement(By.linkText("Shopping Cart")).click();
        WebElement quantity = driver.findElement(By.name("q"));
        quantity.clear();
        quantity.sendKeys(String.valueOf(19));
        quantity.sendKeys(Keys.ENTER);
        String actualDiscount = driver.findElement(By.xpath("//table//tr[6]//tr[2]//td[5]//b")).getText();
        assertEquals(Integer.valueOf(actualDiscount), 0,
                "The discount for \"19\" should be \"0\"");
    }

    @Test
    public void discountShouldBeTwoForTwentyBooks() {
        driver.get("https://www.sharelane.com/cgi-bin/add_to_cart.py?book_id=1");
        driver.findElement(By.linkText("Shopping Cart")).click();
        WebElement quantity = driver.findElement(By.name("q"));
        quantity.clear();
        quantity.sendKeys(String.valueOf(20));
        quantity.sendKeys(Keys.ENTER);
        String actualDiscount = driver.findElement(By.xpath("//table//tr[6]//tr[2]//td[5]//b")).getText();
        assertEquals(Integer.valueOf(actualDiscount), 2,
                "The discount for \"20\" should be \"2\"");
    }

    @Test
    public void discountShouldBeThreeForFiftyBooks() {
        driver.get("https://www.sharelane.com/cgi-bin/add_to_cart.py?book_id=1");
        driver.findElement(By.linkText("Shopping Cart")).click();
        WebElement quantity = driver.findElement(By.name("q"));
        quantity.clear();
        quantity.sendKeys(String.valueOf(50));
        quantity.sendKeys(Keys.ENTER);
        String actualDiscount = driver.findElement(By.xpath("//table//tr[6]//tr[2]//td[5]//b")).getText();
        assertEquals(Integer.valueOf(actualDiscount), 3,
                "The discount for \"50\" should be \"3\"");
    }

    public void logIn() {
        register();

        driver.findElement(By.cssSelector("img[src='../images/logo.jpg']")).click();

        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("input[value='Login']")).click();
    }

    private void register() {
        driver.get("https://www.sharelane.com/cgi-bin/register.py?page=1&zip_code=12345");
        driver.findElement(By.name("first_name")).sendKeys("test");
        driver.findElement(By.name("last_name")).sendKeys("test");
        driver.findElement(By.name("email")).sendKeys("test@test.test");
        driver.findElement(By.name("password1")).sendKeys("12345");
        driver.findElement(By.name("password2")).sendKeys("12345");
        driver.findElement(By.cssSelector("input[value='Register']")).click();

        email = driver.findElement(By.xpath("//td[text()='Email']/following::b")).getText();
        password = driver.findElement(By.xpath("//td[text()='Password']/following::td")).getText();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
//        driver.findElement(By.xpath("//a[contains(@href, 'show_book.py?book_id')]")).click();
//        driver.findElement(By.xpath("//img[contains(@src,'product')]")).click();
//        driver.findElement(By.cssSelector("img[src='../images/add_to_cart.gif']")).click();
