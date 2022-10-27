package com.github.kadehar.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.kadehar.config.ConfigReader;
import com.github.kadehar.config.ProjectConfiguration;
import com.github.kadehar.config.web.WebConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {
    private static final WebConfig webConfig = ConfigReader.Instance.read();

    @BeforeAll
    public static void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        ProjectConfiguration projectConfiguration = new ProjectConfiguration(webConfig);
        projectConfiguration.webConfig();
        projectConfiguration.apiConfig();
    }
}
