package Test;

import Pages.LoginPage; //наш класс
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoginTest {
    WebDriver driver;

    @BeforeTest
    public void setup() {
        driver = new ChromeDriver();
        driver.get("http://adm.test.v6.bo.rbc.ru/login/");
    }

    @Test(priority = 5)
    public void ValidAuthorization() {
        LoginPage login = new LoginPage(driver);
        login.set_username("autotest");
        login.set_password("autotest1315");
        login.click_button();
        Assert.assertTrue(driver.getPageSource().contains("Материалы"));
    }

    @Test(priority = 1)
    public void InvalidLogin() {
        LoginPage login = new LoginPage(driver);
        login.set_username("auto");
        login.set_password("autotest1315");
        login.click_button();
        WebElement errorMessage= driver.findElement(By.cssSelector("li"));
        Assert.assertEquals(errorMessage.getText(),"Пожалуйста, введите корректные имя пользователя и пароль. Оба поля могут быть чувствительны к регистру.");
    }

    @Test(priority = 2)
    public void InvalidPassword() {
        LoginPage login = new LoginPage(driver);
        login.set_username("autotest");
        login.set_password("autotest");
        login.click_button();
        WebElement errorMessage= driver.findElement(By.cssSelector("li"));
        Assert.assertEquals(errorMessage.getText(),"Пожалуйста, введите корректные имя пользователя и пароль. Оба поля могут быть чувствительны к регистру.");
    }

    @AfterTest
    public void QuiteBrowser() {
        driver.quit();
    }
}
