package homework19;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import homework19.pages.LoginPage;
import homework19.pages.MainPage;
import homework19.pages.SecureAreaPage;

public class FormAuthenticationTest {

  @BeforeAll
  static void setUp() {
    Configuration.browser = "chrome";
    Configuration.browserSize = "1920x1080";
    Configuration.timeout = 10_000;
    Configuration.baseUrl = "https://the-internet.herokuapp.com";
  }

  @Test
  void loginLogoutScenario() {
    // 1-3. Открыть главную и перейти на страницу логина, проверить заголовок
    LoginPage loginPage = new MainPage()
        .openPage()
        .clickFormAuthentication()
        .shouldBeOpened();

    // 4-6. Ввести креды и залогиниться
    SecureAreaPage secureAreaPage = loginPage
        .setUsername("tomsmith")
        .setPassword("SuperSecretPassword!")
        .clickLogin();

    // 7-8. Проверить сообщение и наличие кнопки Logout
    secureAreaPage
        .shouldShowSuccessMessage("You logged into a secure area!")
        .shouldHaveLogoutButton();

    // 9-10. Разлогиниться и убедиться, что снова на странице логина
    secureAreaPage
        .clickLogout()
        .shouldBeOpened();
  }
}
