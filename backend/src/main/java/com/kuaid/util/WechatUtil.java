package com.kuaid.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class WechatUtil {

    private static final Logger log = LoggerFactory.getLogger(WechatUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.login_url}")
    private String loginUrl;

    public Map<String, String> code2Session(String code) {
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                loginUrl, appid, secret, code);

        Map<String, String> result = new HashMap<>();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(httpGet)) {
                String body = EntityUtils.toString(response.getEntity(), "UTF-8");
                JsonNode json = objectMapper.readTree(body);

                if (json.has("openid")) {
                    result.put("openid", json.get("openid").asText());
                    result.put("session_key", json.get("session_key").asText());
                    if (json.has("unionid")) {
                        result.put("unionid", json.get("unionid").asText());
                    }
                } else {
                    log.error("WeChat login failed: {}", body);
                    result.put("error", json.has("errmsg") ? json.get("errmsg").asText() : "unknown error");
                }
            }
        } catch (IOException e) {
            log.error("WeChat API call failed", e);
            result.put("error", e.getMessage());
        }
        return result;
    }
}
