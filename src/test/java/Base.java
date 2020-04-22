import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Base {
    public static AndroidDriver<AndroidElement> capabilities(String device) throws MalformedURLException {
        File f = new File("src/test/resources");
        File fs = new File(f, "General-Store.apk");

        DesiredCapabilities caps = new DesiredCapabilities();
        if(device.equals("emulator")){
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
        }else if(device.equals("real")){
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Device");
        }
        caps.setCapability(MobileCapabilityType.APP, fs.getAbsolutePath());
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 5);
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        AndroidDriver<AndroidElement> driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        return driver;
    }

    static void fillForm(AndroidDriver<AndroidElement> driver) {
        driver.findElementById("com.androidsample.generalstore:id/nameField").sendKeys("Bhoom");
        driver.hideKeyboard();
        driver.findElement(By.xpath("//*[@text='Female']")).click();
        driver.findElementById("com.androidsample.generalstore:id/spinnerCountry").click();
//        driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textMatches(\"India\").instance(0))"));
        driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\"Argentina\"));");
        driver.findElement(By.xpath("//*[@text='Argentina']")).click();
        driver.findElementById("com.androidsample.generalstore:id/btnLetsShop").click();
    }
}

