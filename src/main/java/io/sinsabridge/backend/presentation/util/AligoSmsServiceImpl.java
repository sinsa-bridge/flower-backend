package io.sinsabridge.backend.presentation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendRequest;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendResponse;
import io.sinsabridge.backend.sms.service.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;

@Component
public class AligoSmsServiceImpl implements SmsSender {
    private final Logger logger = LoggerFactory.getLogger(AligoSmsServiceImpl.class);

    @Value("${aligo.api.key}")
    private String apiKey;

    @Value("${aligo.api.user_id}")
    private String userId;

    @Value("${aligo.api.sender}")
    private String sender;

    private static final String API_URL = "https://apis.aligo.in/send/";

    @Override
    public SmsSendResponse send(SmsSendRequest request) {
        RestTemplate restTemplate = createRestTemplate();
        HttpHeaders headers = createHeaders();

        MultiValueMap<String, String> map = createRequestBody(request);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        SmsSendResponse responseBody = null;
        try {
            ResponseEntity<SmsSendResponse> responseEntity = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    entity,
                    SmsSendResponse.class
            );
            responseBody = responseEntity.getBody();
            logger.info("result sms : {}", responseBody);
        } catch (Exception e) {
            logger.error("Error occurred while sending SMS", e);
        }
        return responseBody;
    }

    private RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(getHttpMessageConverters());
        return restTemplate;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> createRequestBody(SmsSendRequest request) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("key", apiKey);
        map.add("user_id", userId);
        map.add("sender", sender);
        map.add("receiver", request.getReceiver());
        map.add("msg", request.getMsg());
        return map;
    }

    private List<HttpMessageConverter<?>> getHttpMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new FormHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());

        // MappingJackson2HttpMessageConverter 수정
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(new ObjectMapper());
        List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        converter.setSupportedMediaTypes(supportedMediaTypes);

        converters.add(converter);
        return converters;
    }

}
