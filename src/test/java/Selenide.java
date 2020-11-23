import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.WebDriverRunner.url;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

@Test
public class Selenide {
    public static void test() throws InterruptedException {
        //Configuration.baseUrl = "http://adm.test.v6.bo.rbc.ru".
        //WebDriver driver = new ChromeDriver();
        open("http://adm.test.v6.bo.rbc.ru/login");
        url(); //функция возвращающая URL
        System.out.println("УРЛ теста: " + url());
        //driver.findElement(By.name("username")).click();
        $(By.name("password")).click();
        Thread.sleep(1000);
        //driver.findElement(By.cssSelector(".btn")).click();
    }
}
