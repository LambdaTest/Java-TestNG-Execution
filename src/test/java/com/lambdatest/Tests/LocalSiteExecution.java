package com.lambdatest.Tests;

import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import com.lambdatest.tunnel.Tunnel;

public class LocalSiteExecution {
    Tunnel t;

    WebDriver driver = null;
    public static String status = "passed";

    String username = System.getenv("LT_USERNAME");
    String accessKey = System.getenv("LT_ACCESS_KEY");


    @Test
    public void test() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("build", "Maven Tunnel Sample");
        capabilities.setCapability("name", "Sample Maven Test");
        capabilities.setCapability("platform", "Windows 10");
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("version","latest");
        capabilities.setCapability("tunnel",true);
        capabilities.setCapability("network",true);
        capabilities.setCapability("console",true);
        capabilities.setCapability("visual",true);

        t = new Tunnel();
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("user", username);
        options.put("key", accessKey);

        t.start(options);
        driver = new RemoteWebDriver(new URL("http://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub"), capabilities);
        System.out.println("Started session");

        // Launch the app
        driver.get("http://localhost");
        Thread.sleep(5000);
        String title = driver.getTitle();
        System.out.println(title);
        ((JavascriptExecutor) driver).executeScript("lambda-status=" + status);
        driver.quit();
        t.stop();
    }
}