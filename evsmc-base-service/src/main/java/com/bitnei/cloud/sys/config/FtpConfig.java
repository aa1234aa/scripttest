package com.bitnei.cloud.sys.config;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FtpConfig {

    private final Environment env;
    private static final String PREFIX = "ftp.";

    public String getPublicHost() {
        return env.getProperty(PREFIX + "publicHost");
    }

    public String getHost() {
        return env.getProperty(PREFIX + "host");
    }

    public Integer getPort() {
        return env.getProperty(PREFIX + "port", Integer.class);
    }

    public String getUsername() {
        return env.getProperty(PREFIX + "username");
    }

    public String getPassword() {
        return env.getProperty(PREFIX + "password");
    }

    public String getPath() {
        return env.getProperty(PREFIX + "path");
    }

    public Integer getMod() {
        return env.getProperty(PREFIX + "mod", Integer.class);
    }

    public String getWebDownlodefilePath() {
        return env.getProperty(PREFIX + "webDownlodefilePath");
    }
}
