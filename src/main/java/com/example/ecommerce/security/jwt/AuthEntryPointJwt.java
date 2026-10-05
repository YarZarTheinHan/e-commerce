package com.example.ecommerce.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, 
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // 1. Response Type ကို JSON Format အဖြစ် သတ်မှတ်ခြင်း
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        // 2. HTTP Status Code ကို 401 (Unauthorized) ဟု သတ်မှတ်ခြင်း
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 3. Frontend သို့ ပြန်ပို့ပေးမည့် Error JSON Structure ပြင်ဆင်ခြင်း
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage()); // ဥပမာ - "Full authentication is required to access this resource"
        body.put("path", request.getServletPath()); // မည်သည့် API Path ကို ခေါ်ရန် ကြိုးစားခဲ့သလဲ

        // 4. Java Map ကို JSON String အဖြစ် ပြောင်းပြီး Response ထဲ ထည့်ပို့ပေးခြင်း
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}