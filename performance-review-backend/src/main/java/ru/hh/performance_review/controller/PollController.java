package ru.hh.performance_review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.hh.performance_review.dto.GetPollResponseDto;
import ru.hh.performance_review.service.PollService;
import ru.hh.performance_review.service.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
@Path("/")
public class PollController {

    private final PollService pollService;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @GET
    @Path("polls")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getPolls(@CookieParam("user-id") String userId) {
        log.info("Получен запрос /polls userRole");
    /*
    TODO:
    1. Если аутентификация уже выполнена - передать в pollService информацию о пользователе: id
     */
        try {
            userId = Optional.ofNullable(userId).orElse(userService.getAnyRespondentId());
            log.info("userId:{}", userId);
            final List<GetPollResponseDto> polls = pollService.getPolls(userId);
            String response = objectMapper.writeValueAsString(polls);
            log.info("Ответ на запрос:{}", response);
            return Response.ok(response).build();
        } catch (Exception e) {
            String errorMsg = String.format("Ошибка обработки запроса /polls %s", e.getLocalizedMessage());
            log.error(errorMsg);
            log.error("", e);
            return Response.serverError().build();
        }
    }
}

