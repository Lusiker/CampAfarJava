package com.campfire.campafar.Configuration;

import com.campfire.campafar.Enum.ControllerStateEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GlobalConfig {
    //用于创建一些全局配置
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer enumSerializer(){
        //用于Jackson ObjectMapper映射请求结果对象
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.serializerByType(ControllerStateEnum.class, new JsonSerializer<ControllerStateEnum>() {
            @Override
            public void serialize(ControllerStateEnum stateEnum, JsonGenerator generator, SerializerProvider provider) throws IOException {
                generator.writeStartObject();
                generator.writeNumberField("state",stateEnum.getStateCode());
                generator.writeStringField("value",stateEnum.getValue());
                generator.writeEndObject();
            }
        });
    }
}
