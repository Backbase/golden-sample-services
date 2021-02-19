package com.backbase.goldensample.store.config;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.backbase.buildingblocks.context.ContextScoped;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

/**
 * Abstract client config provides provides the required properties for a client config and supports m10y.
 *
 * <p>Required a subclass that is annotated with <code>@ConfigurationProperties(YOUR_CLIENT_CONFIG_SUFFIX)</code>.
 */
@Getter
@Setter
@Validated
@ContextScoped
abstract class AbstractClientConfig {

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
        if (isBlank(defaultServiceId)) {
            throw new IllegalArgumentException("Default service id should not be blank.");
        }
        serviceId = defaultServiceId;
    }

    String basePath() {
        String basePath = String.format("%s://%s", getScheme(), getServiceId());
        if (getServicePort() == null) {
            return basePath;
        }
        return String.format("%s:%s", basePath, getServicePort());
    }

}
