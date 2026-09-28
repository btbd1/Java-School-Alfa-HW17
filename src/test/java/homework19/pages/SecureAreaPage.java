package homework19.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class SecureAreaPage {

  private final SelenideElement flashMessage = $("#flash");
  private final SelenideElement logoutButton = $("a[href='/logout']");

  public SecureAreaPage shouldShowSuccessMessage(String message) {
    flashMessage.shouldBe(visible).shouldHave(text(message));
    return this;
  }

  public SecureAreaPage shouldHaveLogoutButton() {
    logoutButton.shouldBe(visible).shouldHave(text("Logout"));
    return this;
  }

  public LoginPage clickLogout() {
    logoutButton.click();
    return new LoginPage();
  }
}