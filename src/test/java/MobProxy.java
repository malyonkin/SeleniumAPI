import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Arrays;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;

import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.core.har.HarResponse;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.proxy.auth.AuthType;
import org.apache.http.HttpResponseInterceptor;
import org.junit.Assert;
import org.omg.PortableInterceptor.ClientRequestInterceptor;
import org.omg.PortableInterceptor.RequestInfoOperations;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.management.Query;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.zoom;
import static com.sun.tools.doclint.Entity.not;
import static javax.management.Query.not;
import static org.junit.Assert.assertThat;
import static sun.nio.cs.Surrogate.is;

public class MobProxy {
    public static WebDriver driver;
    public static BrowserMobProxyServer proxy;

    @Test
    public static void main(String[] args){
        
        proxy = new BrowserMobProxyServer();
        proxy.start();

        //proxy.autoAuthorization("","amalenkin","P1oiQmnR", AuthType.BASIC); //работа с Windows окном
        //Работа с разными агентами - андройд и iOS

        //https://habr.com/ru/post/209752/
        /*HttpResponseInterceptor downloader = new FileDownloader()
                .addContentType("application/jpeg");
        proxy.addResponseFilter(downloader);*/

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        try {
            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            seleniumProxy.setHttpProxy(hostIp + ":" + proxy.getPort());
            seleniumProxy.setSslProxy(hostIp + ":" + proxy.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        DesiredCapabilities seleniumCapabilities = new DesiredCapabilities();
        seleniumCapabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        ChromeOptions options = new ChromeOptions();
        options.merge(seleniumCapabilities);
        //options.addArguments(Arrays.asList("allow-running-insecure-content", "ignore-certificate-errors"));

        driver = new ChromeDriver(options);

        //proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);


        try {

            proxy.newHar("BOshechka");

            driver.get("http://adm.test.v6.bo.rbc.ru/login/?next=/request/list/");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Har har = proxy.getHar();

            // список всех обработанных запросов
            for (HarEntry entry : har.getLog().getEntries()) {
                HarRequest request = entry.getRequest();
                HarResponse response = entry.getResponse();

                System.out.println(entry.getRequest().getUrl());
                // время ожидания ответа от сервера в миллисекундах
                System.out.println(entry.getTimings().getWait());
                // время чтения ответа от сервера в миллисекундах
                System.out.println(entry.getTimings().getReceive());

                System.out.println(request.getUrl() + " : " + response.getStatus() + " : " + entry.getTime() + "ms");
                Assert.assertEquals(response.getStatus(),200);
            }

            File harFile = new File("academy.har");
            har.writeTo(harFile);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        driver.quit();
        proxy.stop();
    }
}