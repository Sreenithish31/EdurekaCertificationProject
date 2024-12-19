package BlazeAutomation;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.Random;

public class BlazeTestSuite extends UserData {
    WebDriver browser = new ChromeDriver();

    @BeforeTest
    public void initializeBrowser() {
        browser.get("https://www.demoblaze.com/index.html");
        browser.manage().window().maximize();
    }

    @Test(priority = 1, description = "User Registration Test")
    public void testUserRegistration() throws InterruptedException {
        browser.findElement(By.id("signin2")).click();
        Thread.sleep(2000);
        browser.findElement(By.id("sign-username")).sendKeys(usernames[randNames]);
        browser.findElement(By.id("sign-password")).sendKeys("password123");
        browser.findElement(By.cssSelector("button[onclick='register()']")).click();
        Thread.sleep(2000);
        browser.switchTo().alert().accept();
        browser.findElement(By.xpath("//button[text()='Close']")).click();
    }

    @Test(priority = 2, description = "User Login Test")
    public void testUserLogin() throws InterruptedException {
        browser.findElement(By.id("login2")).click();
        Thread.sleep(2000);
        browser.findElement(By.id("loginusername")).sendKeys(usernames[randNames]);
        browser.findElement(By.id("loginpassword")).sendKeys("password123");
        browser.findElement(By.cssSelector("button[onclick='logIn()']")).click();
        Thread.sleep(2000);
    }

    @Test(priority = 3, description = "Validate Categories Contain Items")
    public void validateCategoriesContainItems() throws InterruptedException {
        browser.findElement(By.linkText("Laptops")).click();
        List<WebElement> products = browser.findElements(By.className("col-lg-4"));
        Assert.assertTrue(products.size() > 0, "No products found in the Laptops category.");
    }

    @Test(priority = 4, description = "Add Random Item to Cart")
    public void addRandomItemToCart() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(browser, Duration.ofSeconds(10));
        List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".col-lg-4.col-md-6.mb-4")));

        if (items.size() > 0) {
            int randomItem = new Random().nextInt(items.size());
            items.get(randomItem).click();
            Thread.sleep(2000);
            WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']")));
            addToCartBtn.click();

            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            Assert.assertEquals(alert.getText(), "Product added.", "Unexpected alert message.");
            alert.accept();
            Thread.sleep(2000);
        } else {
            Assert.fail("No items available to add to the cart.");
        }
    }

    @Test(priority = 5, description = "Remove Item from Cart")
    public void removeItemFromCart() throws InterruptedException {
        browser.findElement(By.id("cartur")).click();
        Thread.sleep(2000);
        browser.findElement(By.xpath("(//a[text()='Delete'])[1]")).click();
        Thread.sleep(2000);
    }

    @Test(priority = 6, description = "Complete Purchase Test")
    public void completePurchase() throws InterruptedException {
        browser.findElement(By.id("cartur")).click();
        Thread.sleep(2000);
        browser.findElement(By.xpath("//button[text()='Place Order']")).click();
        Thread.sleep(2000);

        browser.findElement(By.id("name")).sendKeys(usernames[randNames]);
        browser.findElement(By.id("country")).sendKeys("USA");
        browser.findElement(By.id("city")).sendKeys("New York");
        browser.findElement(By.id("card")).sendKeys("4111-1111-1111-1111");
        browser.findElement(By.id("month")).sendKeys("December");
        browser.findElement(By.id("year")).sendKeys("2024");
        browser.findElement(By.xpath("//button[text()='Purchase']")).click();

        String confirmationMessage = browser.findElement(By.xpath("//h2[text()='Thank you for your purchase!']")).getText();
        Assert.assertEquals(confirmationMessage, "Thank you for your purchase!", "Purchase confirmation message mismatch.");
    }

    @AfterTest
    public void closeBrowser() throws InterruptedException {
        Thread.sleep(2000);
        browser.quit();
    }
}
