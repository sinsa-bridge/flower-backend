package io.sinsabridge.backend.presentation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AligoSmsUtil {

    @Value("${aligo.api.key}")
    private String apiKey;

    @Value("${aligo.api.user_id}")
    private String userId;

    @Value("${aligo.api.sender}")
    private String sender;

    private static final String API_URL = "https://apis.aligo.in/send/";

    public String sendSms(String phoneNumber, String message) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("key", apiKey);
        map.add("user_id", userId);
        map.add("sender", sender);
        map.add("receiver", phoneNumber);
        map.add("msg", message);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            String response = restTemplate.postForObject(API_URL, request, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);

            int resultCode = Integer.parseInt(responseMap.get("result_code").toString());

            if (resultCode == 1) {
                return "success";
            } else {
                return "failure";
            }
        } catch (Exception e) {
            throw new RuntimeException("SMS 발송 오류: " + e.getMessage());
        }
    }
}
