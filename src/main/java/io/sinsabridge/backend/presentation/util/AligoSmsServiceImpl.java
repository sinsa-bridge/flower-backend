package io.sinsabridge.backend.presentation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sinsabridge.backend.sms.domain.entity.SmsHistory;
import io.sinsabridge.backend.sms.domain.repository.SmsHistoryRepository;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendRequest;
import io.sinsabridge.backend.sms.presentation.dto.SmsSendResponse;
import io.sinsabridge.backend.sms.service.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SmsHistoryRepository smsHistoryRepository;

    @Override
    public SmsSendResponse send(SmsSendRequest request) {
        RestTemplate restTemplate = createRestTemplate();
        HttpHeaders headers = createHeaders();

        // 4자리 인증 코드 생성
        String code = generateRandomCode();

        request.setMsg("인증 코드: " + code);

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

            // 문자 메시지 발송 기록을 저장합니다.
            saveSmsHistory(request.getReceiver(), responseBody);
        } catch (Exception e) {
            logger.error("Error occurred while sending SMS", e);
        }
        return responseBody;
    }

    // 기존 메서드들은 그대로 두고, saveSmsHistory() 메서드만 수정합니다.

    /**
     * 문자 메시지 발송 기록을 저장하는 메서드입니다.
     *
     * @param phoneNumber 사용자가 입력한 전화번호
     * @param response    문자 발송 응답 객체
     */
    private void saveSmsHistory(String phoneNumber, SmsSendResponse response) {
        SmsHistory smsHistory = response.toSmsHistory(phoneNumber);
        smsHistoryRepository.save(smsHistory);
    }

    // 기존 메서드들은 그대로 유지합니다.

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

    /**
     * 4자리 인증 코드를 생성하는 메서드입니다.
     *
     * @return 생성된 인증 코드
     */
    private String generateRandomCode() {
        int code = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(code);
    }

}
