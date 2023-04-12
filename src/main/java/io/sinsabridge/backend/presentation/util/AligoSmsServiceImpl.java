package io.sinsabridge.backend.presentation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendRequest;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendResponse;
import io.sinsabridge.backend.sms.service.SmsSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AligoSmsServiceImpl implements SmsSender {

    @Value("${aligo.api.key}")
    private String apiKey;

    @Value("${aligo.api.user_id}")
    private String userId;

    @Value("${aligo.api.sender}")
    private String sender;

    private static final String API_URL = "https://apis.aligo.in/send/";

    @Override
    public SmsSendResponse send(SmsSendRequest request) {
        RestTemplate restTemplate = new RestTemplate(getHttpMessageConverters());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // StringHttpMessageConverter 등록
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new StringHttpMessageConverter());
        restTemplate.setMessageConverters(converters);

        // MappingJackson2HttpMessageConverter 등록
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        restTemplate.getMessageConverters().add(converter);


        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("key", apiKey);
        map.add("user_id", userId);
        map.add("sender", sender);
        map.add("receiver", request.getReceiver());
        map.add("msg", request.getMsg());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<SmsSendResponse> responseEntity = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                entity,
                SmsSendResponse.class
        );
        return responseEntity.getBody();
    }

    private List<HttpMessageConverter<?>> getHttpMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new FormHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter(new ObjectMapper()));
        return converters;
    }


}
