package io.sinsabridge.backend.presentation.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AligoSmsUtilTest {

    @Autowired
    AligoSmsUtil aligoSmsUtil;

    @Test
    void aligo() {
        aligoSmsUtil.sendSms("01081811891", "wonhwa");
    }
}