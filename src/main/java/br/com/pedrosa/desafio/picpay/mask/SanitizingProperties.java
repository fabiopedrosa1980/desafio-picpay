package br.com.pedrosa.desafio.picpay.mask;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@ConfigurationProperties("sanitizing")
@Configuration
public class SanitizingProperties {
    private List<Pattern> keyPatternsToMask = Stream.of(
            ".+key\\b.*",
            ".+password\\b.*",
            ".+secret\\b.*",
            ".+token\\b.*"
    ).map(Pattern::compile).toList();

    public List<Pattern> getKeyPatternsToMask() {
        return keyPatternsToMask;
    }

    public void setKeyPatternsToMask(List<Pattern> keyPatternsToMask) {
        this.keyPatternsToMask = keyPatternsToMask;
    }
}
