package ru.hh.performance_review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.hh.performance_review.controller.base.CookieConst;
import ru.hh.performance_review.controller.base.HttpRequestHandler;
import ru.hh.performance_review.dto.response.ResponseMessage;
import ru.hh.performance_review.service.UserService;
import ru.hh.performance_review.service.sereliazation.ObjectConvertService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
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
    @GET
    @Path("getallusers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser() {
        log.info("Получен запрос /getallusers");
        NewCookie cookie = new NewCookie(CookieConst.USER_ID, "00000000-0000-0000-0000-000000000001");
        return new HttpRequestHandler<String, ResponseMessage>()
                .validate(v -> Function.identity())
                .process(x -> userService.getAllUsers())
                .convert(objectConvertService::convertToJson)
                .forArgument("00000000-0000-0000-0000-000000000001", cookie);
    }

}