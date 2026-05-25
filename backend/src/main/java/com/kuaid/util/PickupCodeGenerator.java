package com.kuaid.util;

import com.kuaid.dao.ExpressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PickupCodeGenerator {

    private static final Random RANDOM = new Random();

    @Autowired
    private ExpressMapper expressMapper;

    public String generate(Long stationId) {
        String code;
        int attempts = 0;
        do {
            code = String.format("%06d", RANDOM.nextInt(1000000));
            attempts++;
            if (attempts > 100) {
                throw new RuntimeException("Cannot generate unique pickup code");
            }
        } while (expressMapper.selectByPickupCodeAndStation(code, stationId) != null);
        return code;
    }
}
