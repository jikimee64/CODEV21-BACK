package com.j2kb.codev21.global.config;

import com.j2kb.codev21.domains.user.domain.SocialLoginType;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class ConverterConfig implements Converter<String, SocialLoginType> {

    @Override
    public SocialLoginType convert(String s) {
        return SocialLoginType.valueOf(s.toUpperCase());
    }
}

