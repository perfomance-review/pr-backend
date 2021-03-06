package ru.hh.performance_review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.hh.performance_review.controller.base.CookieConst;
import ru.hh.performance_review.controller.base.HttpRequestHandler;
import ru.hh.performance_review.dto.response.ResponseMessage;
import ru.hh.performance_review.security.annotation.JwtTokenCookie;
import ru.hh.performance_review.security.annotation.PerformanceReviewSecured;
import ru.hh.performance_review.security.context.SecurityContext;
import ru.hh.performance_review.security.context.SecurityRole;
import ru.hh.performance_review.service.UserService;
import ru.hh.performance_review.service.sereliazation.ObjectConvertService;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
@Path("/")
public class TechController {

    private final UserService userService;
    private final ObjectConvertService objectConvertService;

    /**
     * endpoint получения данных о всех пользователях
     *
     * @return - ДТО с информацией о пользователе
     */
    @PerformanceReviewSecured(roles = {SecurityRole.ADMINISTRATOR, SecurityRole.MANAGER, SecurityRole.RESPONDENT})
    @GET
    @Path("getallusers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@JwtTokenCookie @CookieParam(CookieConst.ACCESS_TOKEN) String jwtToken) {
        String userId = SecurityContext.getUserId();
        log.info("Получен запрос /getallusers");
        return new HttpRequestHandler<String, ResponseMessage>()
                .validate(v -> Function.identity())
                .process(x -> userService.getAllUsers())
                .convert(objectConvertService::convertToJson)
                .forArgument(jwtToken);
    }

}
