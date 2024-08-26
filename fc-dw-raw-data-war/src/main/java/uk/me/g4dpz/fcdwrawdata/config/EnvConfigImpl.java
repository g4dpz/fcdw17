package uk.me.g4dpz.fcdwrawdata.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EnvConfigImpl implements EnvConfig {

    private static final Logger LOG = LoggerFactory.getLogger(EnvConfigImpl.class);

    @Override
    public String satpredictURL() {
        return getEnv("SATPREDICT_URL");
    }

    private String getEnv(String environmentVariableName) {
        final String value = System.getenv(environmentVariableName);
        if (StringUtils.isBlank(value)) {
            LOG.error("Configuration error. Not set: " + environmentVariableName);
            throw new RuntimeException("Configuration error. Not set: " + environmentVariableName);
        }
        return value;
    }
}
