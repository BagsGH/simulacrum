package com.bags.simulacrum.Configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Resources for configurations:
 * <p>
 * https://tuhrig.de/using-configurationproperties-to-separate-service-and-configuration
 * https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
 * https://www.baeldung.com/configuration-properties-in-spring-boot
 * https://stackoverflow.com/questions/42839126/configurationproperties-spring-boot-configuration-annotation-processor-not-foun?rq=1
 */

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "simulation")
@Data
public class SimulationConfig {

    private double deltaTime;

}
