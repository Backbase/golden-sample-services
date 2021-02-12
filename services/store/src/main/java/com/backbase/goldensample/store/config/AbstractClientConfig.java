package com.backbase.goldensample.store.config;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

/**
 * Wouldnt it be cool if S-SDK would provide this!
 */
@Getter
@Setter
public abstract class AbstractClientConfig {

    @Autowired
    @Qualifier("interServiceRestTemplate")
    protected RestTemplate restTemplate;

    @Value("${backbase.communication.http.default-scheme:http}")
    @Pattern(regexp = "https?")
    private String scheme;

    private String serviceId;

    private Integer servicePort;


    String basePath() {
        String basePath = String.format("%s://%s", getScheme(), getServiceId());
        if (servicePort == null) {
            return basePath;
        }
        return String.format("%s:%s", basePath, servicePort);
    }

}
