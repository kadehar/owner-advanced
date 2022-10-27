package com.github.kadehar.config.web;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:${env}.properties",
        "file:~/${env}.properties",
        "file:./${env}.properties"
})
public interface WebConfig extends Config {
    @Key("browser")
    Browser browser();
    @Key("browserVersion")
    String browserVersion();
    @Key("browserSize")
    String browserSize();
    @Key("baseUrl")
    String baseUrl();
    @Key("isRemote")
    boolean isRemote();
    @Key("remoteUrl")
    String remoteUrl();
}
