package homework19;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FormAuthenticationTest {

  @BeforeAll
  static void setUp() {
    Configuration.browser = "chrome";
    Configuration.browserSize = "1920x1080";
    Configuration.timeout = 10_000; // 10 сек на ожидание элементов
    Configuration.baseUrl = "https://the-internet.herokuapp.com";
  }

  @Test
  void loginLogoutScenario() {
    // 1. Открыть главную страницу
    open("/");

    // 2. Кликнуть по ссылке "Form Authentication"
    $("a[href='/login']").click();

    // 3. Проверить, что заголовок содержит "Login Page"
    $("h2").shouldHave(text("Login Page"));

    // 4-5. Установить Username и Password
    $("#username").setValue("tomsmith");
    $("#password").setValue("SuperSecretPassword!");

    // 6. Нажать кнопку Login
    $("button[type='submit']").click();

    // 7. Проверить сообщение об успешном логине
    $("#flash")
        .shouldBe(visible)
        .shouldHave(text("You logged into a secure area!"));

    // 8. Проверить наличие кнопки Logout
    $("a[href='/logout']").shouldBe(visible).shouldHave(text("Logout"));

    // 9. Нажать Logout
    $("a[href='/logout']").click();

    // 10. Проверить, что вернулись на страницу с заголовком "Login Page"
    $("h2").shouldHave(text("Login Page"));
  }
}
