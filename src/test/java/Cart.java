import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.ElementOption;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Cart extends Base {
    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        AndroidDriver<AndroidElement> driver = capabilities("Emulator");
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
        fillForm(driver);
        checkIfTheCartIsEmpty(driver);
        addToCart(driver);
        validateTotalAmount(driver);
        tapCheckBox(driver);
        viewTermsAndConditions(driver);
        printContexts(driver);
    }

    //item to cart and validate if its the same item present in the cart
    private static void addToCart(AndroidDriver<AndroidElement> driver) {
//        driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().resourceId(\"com.androidsample.generalstore:id/rvProductList\")).scrollIntoView(new UiSelector().textMatches(\"Air Jordan 9 Retro\").instance(0))"));
//        driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textMatches(\"Jordan 6 Rings\").instance(0))"));
        driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().resourceId(\"com.androidsample.generalstore:id/rvProductList\")).scrollIntoView(new UiSelector().textMatches(\"Air Jordan 9 Retro\").instance(0))");
        int count = driver.findElements(By.id("com.androidsample.generalstore:id/productName")).size();
        for (int i = 0; i < count; i++) {
            String text = driver.findElements(By.id("com.androidsample.generalstore:id/productName")).get(i).getText();
            if (text.equalsIgnoreCase("Air Jordan 9 Retro")) {
                driver.findElements(By.id("com.androidsample.generalstore:id/productAddCart")).get(i).click();
                break;
            }
        }
        driver.findElement(By.id("com.androidsample.generalstore:id/appbar_btn_cart")).click();
        String productName = driver.findElementByAndroidUIAutomator("resourceId(\"com.androidsample.generalstore:id/productName\")").getText();
        System.out.println("Product Name = " + productName);
        Assert.assertEquals(productName, "Air Jordan 9 Retro");
    }

    private static void validateTotalAmount(AndroidDriver<AndroidElement> driver) throws InterruptedException {
        driver.findElementsByAndroidUIAutomator("text(\"ADD TO CART\")").get(0).click();
        driver.findElementsByAndroidUIAutomator("text(\"ADD TO CART\")").get(0).click();

        /*driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().resourceId(\"com.androidsample.generalstore:id/rvProductList\")).scrollIntoView(new UiSelector().textMatches(\"Air Jordan 9 Retro\").instance(0))");
        int count = driver.findElements(By.id("com.androidsample.generalstore:id/productName")).size();
        for (int i = 0; i < count; i++) {
            String text = driver.findElements(By.id("com.androidsample.generalstore:id/productName")).get(i).getText();
            if (text.equalsIgnoreCase("Air Jordan 9 Retro")) {
                driver.findElements(By.id("com.androidsample.generalstore:id/productAddCart")).get(i).click();
                break;
            }
        }*/
        driver.findElement(By.id("com.androidsample.generalstore:id/appbar_btn_cart")).click();
        Thread.sleep(4000);
        int count = driver.findElementsByAndroidUIAutomator("resourceId(\"com.androidsample.generalstore:id/productPrice\")").size();
        double sum = 0;
        for (int i = 0; i < count; i++) {
            String amount = driver.findElementsById("com.androidsample.generalstore:id/productPrice").get(i).getText();
            double amountValue = getAmountValue(amount, "amount1 = ");
            sum = sum + amountValue;
        }
        System.out.println("sum of products = " + sum);
        String totalAmount = driver.findElementById("com.androidsample.generalstore:id/totalAmountLbl").getText();
        double totalBillAmount = getAmountValue(totalAmount, "Actual = ");
        Assert.assertEquals(sum, totalBillAmount);

    }

    private static void viewTermsAndConditions(AndroidDriver<AndroidElement> driver) {
        driver.findElementsByAndroidUIAutomator("resourceId(\"com.androidsample.generalstore:id/termsButton\")");
        TouchAction t = new TouchAction(driver);
        AndroidElement termsAndCondition = driver.findElementByAndroidUIAutomator("resourceId(\"com.androidsample.generalstore:id/termsButton\")");
        t.longPress(LongPressOptions.longPressOptions()
                .withElement(ElementOption.element(termsAndCondition))
                .withDuration(Duration.ofSeconds(1)))
                .release()
                .perform();
        driver.findElementById("android:id/button1").click();
    }

    //context
    private static void printContexts(AndroidDriver<AndroidElement> driver) throws InterruptedException {
        driver.findElementById("com.androidsample.generalstore:id/btnProceed").click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text=\"Google\"]")));
        Set<String> contextHandles = driver.getContextHandles();
        for (String context : contextHandles) {
            System.out.println(context);
        }
        driver.context("WEBVIEW_com.androidsample.generalstore");
        driver.findElementByName("q").sendKeys("Hello");
        driver.findElementByXPath("//*[@id=\"tsf\"]/div[2]/div[1]/div[1]/button[2]").click();
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
        driver.context("NATIVE_APP");
    }


    private static void tapCheckBox(AndroidDriver<AndroidElement> driver) {
        TouchAction t = new TouchAction(driver);
        t.tap(TapOptions.tapOptions()
                .withElement(ElementOption.element(driver.findElementByAndroidUIAutomator("text(\"Send me e-mails on discounts related to selected products in future\")"))))
                .perform();
    }

    private static double getAmountValue(String amount, String s) {
        amount = amount.substring(1);
        double amountValue = Double.parseDouble(amount);
        System.out.println(s + amountValue);
        return amountValue;
    }

    private static void checkIfTheCartIsEmpty(AndroidDriver<AndroidElement> driver) {
        driver.findElement(By.id("com.androidsample.generalstore:id/appbar_btn_cart")).click();
        String toastMessage = driver.findElement(By.xpath("//android.widget.Toast[1]")).getAttribute("name");
        System.out.println(toastMessage);
        Assert.assertEquals(toastMessage, "Please add some product at first");
    }


}

