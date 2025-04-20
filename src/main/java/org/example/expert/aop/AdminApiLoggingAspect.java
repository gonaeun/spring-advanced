package org.example.expert.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.expert.domain.common.dto.AuthUser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminApiLoggingAspect {   // 관리자만 접근할 수 있는 요청/응답에 대한 데이터 접근 로그 기록

    private final ObjectMapper objectMapper;

    // @Around : AOP 로직 수행하는 어노테이션
    // deleteComment(), changeUserRole() 메서드에만 적용
    @Around("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || " +
            "execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public Object logAdminApi(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis(); // 요청 시작 시간 측정

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();  // 호출된 메서드 정보
        String apiPath = method.getDeclaringClass().getSimpleName() + "." + method.getName(); // 메서드 경로 (클래스.메서드)

        Object[] args = joinPoint.getArgs();  // getArgs() : AOP에서 현재 실행중인 메서드의 모든 파라미터들을 배열로 반환
        Map<String, Object> requestData = new HashMap<>(); // 그 파라미터들을 JSON으로 로그에 남기기 위해서 Map에 담을 준비

        for (Object arg : args) {
            if (arg instanceof AuthUser authUser) {
                requestData.put("userId", authUser.getId()); // AuthUser은 인증된 사용자 정보이므로, userId만 따로 추출
            } else {
                requestData.put(arg.getClass().getSimpleName(), arg); // 나머지 requestDto는 객체 전체를 기록
            }
        }

        // request body : 파라미터 Map을 JSON 문자열로 반환하여 로그 출력
        String requestJson = objectMapper.writeValueAsString(requestData);
        log.info("[ADMIN API REQUEST] path={}, request={}", apiPath, requestJson);

        // 실제 메서드 (deleteComment, changeUserRole) 실행
        Object response = joinPoint.proceed();

        // response body : 실행결과를 JSON 문자열로 직렬화
        String responseJson = objectMapper.writeValueAsString(response);
        long duration = System.currentTimeMillis() - startTime; // 소요시간 측정

        // 요청 종료 시점에 응답 로그 출력
        log.info("[ADMIN API RESPONSE] path={}, duration={}ms, response={}", apiPath, duration, responseJson);

        return response;
    }
}
