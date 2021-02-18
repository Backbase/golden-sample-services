package com.backbase.goldensample.store.config;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

/**
 * Wouldnt it be cool if S-SDK would provide this!
 */
@Getter
@Setter
@Validated
public abstract class AbstractClientConfig {

    @Autowired
    @Qualifier("interServiceRestTemplate")
    protected RestTemplate restTemplate;

    @Value("${backbase.communication.http.default-scheme:http}")
    @Pattern(regexp = "https?")
    private String scheme;

    private String serviceId;

    private Integer servicePort;

    protected AbstractClientConfig(String defaultServiceId) {
        super();
        if (StringUtils.isBlank(defaultServiceId)) {
            throw new IllegalArgumentException("Default service id should not be blank.");
        }
        serviceId = defaultServiceId;
    }


    String basePath() {
        String basePath = String.format("%s://%s", getScheme(), getServiceId());
        if (servicePort == null) {
            return basePath;
        }
        return String.format("%s:%s", basePath, servicePort);
    }

}
