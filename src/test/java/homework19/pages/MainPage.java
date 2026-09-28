package homework19.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MainPage {

  private final SelenideElement formAuthenticationLink = $("a[href='/login']");

  public MainPage openPage() {
    return open("/", MainPage.class);
  }

  public LoginPage clickFormAuthentication() {
    formAuthenticationLink.click();
    return new LoginPage();
  }
}