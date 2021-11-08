package com.flab.realestateinvest.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.realestateinvest.common.ResponseBody;
import com.flab.realestateinvest.dto.FailResponseDto;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;



@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle
            (HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 사용자 요청 경로
        String requestUrl = request.getRequestURL().toString();
        // 사용자 세션 정보
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        String userId = (String) session.getAttribute(sessionId);

        // 회원 가입 경로
        if(requestUrl.contains("/users") && request.getMethod().contains(HttpMethod.POST.name())){
            return toSignUpPath(userId, response);
        }else if(requestUrl.contains("/login")){ // 로그인 경로
            return toLoginPath(userId, request, response);
        }else{ // 이외의 경로
            return toOtherPath(userId, response);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
    }
    @Override
    public void afterCompletion
            (HttpServletRequest request, HttpServletResponse response, Object
                    handler, Exception exception)   {
    }

    private boolean toSignUpPath(String userId, HttpServletResponse response) throws IOException {
        // 로그인 된 상태에서 회원가입을 시도하는 경우 알림 메시지 전송
        if(userId != null){
            sendToClient(new FailResponseDto("로그아웃을 먼저 해주세요."), response);
            return false;
        }
        return true;
    }

    private boolean toLoginPath(String userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 로그인 한 사람이 동일한 브라우저로 다시 로그인을 하는 경우
        if(userId != null){
            Map<String,String> body = jsonRequestToMap(request);
            String email = body.get("email");
            // 같은 아이디로 로그인 하는 경우 알림 메시지 전송
            if(userId.equals(email)){
                sendToClient(new FailResponseDto("이미 로그인한 상태입니다."), response);
                return false;
            }else{ // 다른 아이디로 로그인 하는 경우 기존 세션 제거
                request.getSession().removeAttribute(request.getSession().getId());
            }
        }
        return true;
    }

    private boolean toOtherPath(String userId, HttpServletResponse response) throws IOException {
        // 나머지 경로는 세션이 없다면 요청 불가
        if(userId == null){
            sendToClient(new FailResponseDto("권한이 없습니다."), response);
            return false;
        }
        return true;
    }

    // 사용자 요청에 대한 응답
    private void sendToClient(Object obj, HttpServletResponse response) throws IOException {
        PrintWriter printWriter;
        if(obj == null){
            obj = new Object();
        }

        ObjectMapper mapper = new ObjectMapper();
        String rslt = mapper.writeValueAsString(obj);

        response.setCharacterEncoding("UTF-8");
        printWriter = response.getWriter();
        printWriter.print(rslt);
        printWriter.flush();
        printWriter.close();
    }

    // 사용자 json 요청을 Map 형식으로 변경
    private Map jsonRequestToMap(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String msgBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        return new ObjectMapper().readValue(msgBody, Map.class);
    }
}