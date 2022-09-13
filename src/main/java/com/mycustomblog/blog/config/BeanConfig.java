package com.mycustomblog.blog.config;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public ModelMapper modelMapper(){ return new ModelMapper();}
    @Bean
    public Parser parser(){return Parser.builder().build();}
    @Bean
    public HtmlRenderer htmlRenderer(){return HtmlRenderer.builder().build();}
}
