package com.julia.paymentservice.config;

import com.julia.paymentservice.dto.OrderPayment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String boostrapAddress;

    @Bean
    public ConsumerFactory<String, OrderPayment> orderConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-service-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        JsonDeserializer<OrderPayment> deserializer = new JsonDeserializer<>(OrderPayment.class);
        deserializer.addTrustedPackages("com.julia.paymentservice.dto");
        deserializer.setUseTypeHeaders(false);
        ErrorHandlingDeserializer<OrderPayment> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), errorHandlingDeserializer);

    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderPayment> paymentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderPayment> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());
        return factory;
    }
}
