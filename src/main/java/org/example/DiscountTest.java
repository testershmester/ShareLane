package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.time.Duration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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
    public void setPathToWebDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    }

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.sharelane.com/cgi-bin/register.py");
    }

    @DataProvider
    public Object[][] discounts() {
        Object[][] objects = new Object[11][2];
        objects[0][0] = 1;
        objects[0][1] = 0;
        objects[1][0] = 19;
        objects[1][1] = 0;
        objects[2][0] = 20;
        objects[2][1] = 2;
        objects[3][0] = 49;
        objects[3][1] = 2;
        objects[4][0] = 50;
        objects[4][1] = 3;
        objects[5][0] = 99;
        objects[5][1] = 3;
        objects[6][0] = 100;
        objects[6][1] = 4;
        objects[7][0] = 499;
        objects[7][1] = 4;
        objects[8][0] = 500;
        objects[8][1] = 5;
        objects[9][0] = 999;
        objects[9][1] = 5;
        objects[10][0] = 5000;
        objects[10][1] = 6;
        return objects;
    }

    @Test(dataProvider = "discounts")
    public void discountShouldBeZeroUpToTwentyBooks(int books, int discount) {
        logIn();
        driver.get("https://www.sharelane.com/cgi-bin/add_to_cart.py?book_id=1");
        driver.findElement(By.linkText("Shopping Cart")).click();
        WebElement quantity = driver.findElement(By.name("q"));
        quantity.clear();
        quantity.sendKeys(String.valueOf(books));
        quantity.sendKeys(Keys.ENTER);
        String actualDiscount = driver.findElement(By.xpath("//table//tr[6]//tr[2]//td[5]//b")).getText();
        assertEquals(Integer.valueOf(actualDiscount), discount,
                "The discount for \"" + books + "\" should be \"" + discount + "\"");
    }

    public void logIn() {
        driver.get("https://www.sharelane.com/cgi-bin/register.py?page=1&zip_code=12345");
        driver.findElement(By.name("first_name")).sendKeys("test");
        driver.findElement(By.name("last_name")).sendKeys("test");
        driver.findElement(By.name("email")).sendKeys("test@test.test");
        driver.findElement(By.name("password1")).sendKeys("12345");
        driver.findElement(By.name("password2")).sendKeys("12345");
        driver.findElement(By.cssSelector("input[value='Register']")).click();

        email = driver.findElement(By.xpath("//td[text()='Email']/following::b")).getText();
        password = driver.findElement(By.xpath("//td[text()='Password']/following::td")).getText();

        driver.findElement(By.cssSelector("img[src='../images/logo.jpg']")).click();

        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("input[value='Login']")).click();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.quit();
    }
}
//        driver.findElement(By.xpath("//a[contains(@href, 'show_book.py?book_id')]")).click();
//        driver.findElement(By.xpath("//img[contains(@src,'product')]")).click();
//        driver.findElement(By.cssSelector("img[src='../images/add_to_cart.gif']")).click();
