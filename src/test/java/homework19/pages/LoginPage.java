package homework19.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

  private final SelenideElement pageTitle    = $("h2");
  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement loginButton   = $("button[type='submit']");
  private final SelenideElement flashMessage  = $("#flash");

  public LoginPage shouldBeOpened() {
    pageTitle.shouldHave(text("Login Page"));
    return this;
  }

  public LoginPage setUsername(String username) {
    usernameInput.setValue(username);
    return this;
  }

  public LoginPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  public SecureAreaPage clickLogin() {
    loginButton.click();
    return new SecureAreaPage();
  }

  public LoginPage shouldShowError(String message) {
    flashMessage.shouldBe(visible).shouldHave(text(message));
    return this;
  }
}