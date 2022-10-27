package com.github.kadehar.tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.github.javafaker.Faker;
import com.github.kadehar.api.RegisterClient;
import com.github.kadehar.model.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class RegisterTest extends TestBase {

    private static final String AUTH_COOKIE = "NOPCOMMERCE.AUTH";

    private final Faker faker = new Faker();
    private final User user = new User();
    private final RegisterClient client = new RegisterClient();

    @Test
    public void registerNewUserByAPI() {
        step("Generate test data", () ->
                user.setFirstName("Test")
                        .setLastName("Test")
                        .setEmail(faker.internet().emailAddress())
                        .setPassword(faker.numerify("##########"))
                        .setGender("M")
        );

        Map<String, String> cookies = step("Register new user by API", () -> {
            Response registerPage = client.register();
            String token = registerPage.htmlPath()
                    .getString("**.find{it.@name == '__RequestVerificationToken'}.@value");
            Response newUser = client.createNewUser(
                    token,
                    registerPage.cookies(),
                    user
            );

            return newUser.cookies();
        });

        step("Setup user cookie", () -> {
            open("/Themes/DefaultClean/Content/images/logo.png");
            Cookie authCookie = new Cookie(
                    AUTH_COOKIE,
                    cookies.get(AUTH_COOKIE)
            );
            WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            open("/registerresult/1");
        });

        step("Check registration status", () -> {
            step("Registration result message should be visible", () ->
                    $(".result").shouldHave(text("Your registration completed")));
            step("User email should be visible", () ->
                    $(".account").shouldHave(text(user.email())));
        });

        step("Clear cookies", Selenide::clearBrowserCookies);
    }
}
