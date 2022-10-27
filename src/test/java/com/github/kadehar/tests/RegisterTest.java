package com.github.kadehar.tests;

import com.codeborne.selenide.WebDriverRunner;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;

public class RegisterTest extends TestBase {

    private final Faker faker = new Faker();

    @Test
    public void registerNewUserByAPI() {
        Response registerPage = given()
                .log().all()
                .when()
                .get("/register")
                .then()
                .log().all()
                .extract()
                .response();

        String token = registerPage.htmlPath()
                .getString("**.find{it.@name == '__RequestVerificationToken'}.@value");
        String email = faker.internet().emailAddress();
        String password = faker.numerify("##########");
        Response newUser = given()
                .log().uri()
                .log().cookies()
                .log().headers()
                .cookies(registerPage.cookies())
                .contentType(ContentType.URLENC)
                .formParam(
                        "__RequestVerificationToken",
                        token
                )
                .formParam(
                        "Gender",
                        "M"
                )
                .formParam(
                        "FirstName",
                        "Test"
                )
                .formParam(
                        "LastName",
                        "Test"
                )
                .formParam(
                        "Email",
                        email
                )
                .formParam(
                        "Password",
                        password
                )
                .formParam(
                        "ConfirmPassword",
                        password
                )
                .formParam(
                        "register-button",
                        "Register"
                )
                .when()
                .post("/register")
                .then()
                .log().headers()
                .log().cookies()
                .log().body()
                .extract()
                .response();
        Map<String, String> cookies = newUser.cookies();

        open("/Themes/DefaultClean/Content/images/logo.png");
        Cookie authCookie = new Cookie("NOPCOMMERCE.AUTH", cookies.get("NOPCOMMERCE.AUTH"));
        WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
        open("/registerresult/1");

        $(".result").shouldHave(text("Your registration completed"));
        $(".account").shouldHave(text(email));

        clearBrowserCookies();
    }
}
