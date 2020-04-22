import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

//verify toast error messages displayed appropriately for wrong inputs
public class ToastMessage extends Base {
    public static void main(String[] args) throws MalformedURLException {
        AndroidDriver<AndroidElement> driver = capabilities("Emulator");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.hideKeyboard();

        driver.findElement(By.xpath("//*[@text='Female']")).click();

        driver.findElementById("com.androidsample.generalstore:id/spinnerCountry").click();

//        driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textMatches(\"India\").instance(0))"));
        driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\"Argentina\"));");

        driver.findElement(By.xpath("//*[@text='Argentina']")).click();

        driver.findElementById("com.androidsample.generalstore:id/btnLetsShop").click();
        String toastMessage = driver.findElement(By.xpath("//android.widget.Toast[1]")).getAttribute("name");
        System.out.println(toastMessage);
        Assert.assertEquals(toastMessage,"Please enter your name");
    }
}
