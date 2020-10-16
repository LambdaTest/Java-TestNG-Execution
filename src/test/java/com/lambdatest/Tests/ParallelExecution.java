package com.lambdatest.Tests;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class ParallelExecution {

    public RemoteWebDriver driver = null;
    String status = "failed";
    String username = System.getenv("LT_USERNAME");
    String accessKey = System.getenv("LT_ACCESS_KEY");

    @BeforeTest
    @org.testng.annotations.Parameters(value={"browser","version","platform"})
    public void setUp(String browser, String version, String platform) throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browser", browser);
        capabilities.setCapability("version", version);
        capabilities.setCapability("platform", platform); // If this cap isn't specified, it will just get the any available one
        capabilities.setCapability("build", "Parallel Test");
        capabilities.setCapability("name", "Parallel Test");

        capabilities.setCapability("network", true); // To enable network logs
        capabilities.setCapability("visual", true); // To enable step by step screenshot
        capabilities.setCapability("video", true); // To enable video recording
        capabilities.setCapability("console", true); // To capture console logs

        try {
            driver= new RemoteWebDriver(new URL("https://"+username+":"+accessKey+"@hub.lambdatest.com/wd/hub"), capabilities);
            System.out.println(driver);
        } catch (MalformedURLException e) {
            System.out.println("Invalid grid URL");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Test(enabled=true)
    public void testSimple() throws Exception {
        try {
        				// Launch the app
        				driver.get("https://lambdatest.github.io/sample-todo-app/");

        				// Click on First Item
        				driver.findElement(By.name("li1")).click();

        				// Click on Second Item
        				driver.findElement(By.name("li2")).click();

        				// Add new item is list
        				driver.findElement(By.id("sampletodotext")).clear();
        				driver.findElement(By.id("sampletodotext")).sendKeys("Yey, Let's add it to list");
        				driver.findElement(By.id("addbutton")).click();

        				// Verify Added item
        				String item = driver.findElement(By.xpath("/html/body/div/div/div/ul/li[6]/span")).getText();
        				AssertJUnit.assertTrue(item.contains("Yey, Let's add it to list"));
        				status = "passed";
        				
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @AfterTest
    public void tearDown() throws Exception {
        if (driver != null) {
            ((JavascriptExecutor) driver).executeScript("lambda-status=" + status);
            driver.quit();
        }
    }
}