package ru.hh.performance_review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.hh.performance_review.controller.base.Cookie;
import ru.hh.performance_review.controller.base.CookieConst;
import ru.hh.performance_review.controller.base.HttpRequestHandler;
import ru.hh.performance_review.dto.GetPollResponseDto;
import ru.hh.performance_review.dto.response.*;
import ru.hh.performance_review.dto.request.UpdateWinnerRequestDto;
import ru.hh.performance_review.service.*;
import ru.hh.performance_review.service.sereliazation.ObjectConvertService;
import ru.hh.performance_review.service.validate.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
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
    private final UserValidateService userValidateService;
    private final PollValidateService pollValidateService;
    private final ObjectConvertService objectConvertService;
    private final StartPollService startPollService;
    private final StarPollValidateService starPollValidateService;

    private final RatingRequestValidateService ratingRequestValidateService;
    private final WinnerCompleteService winnerCompleteService;
    private final GradeService gradeService;
    private final ResultUserValidateService resultUserValidateService;
    private final static String defaultUserId = "00000000-0000-0000-0000-000000000001";


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

    /**
     * endpoint начала опроса. Меняет статус опроса и формирует пары для опроса
     *
     * @param userId - идентификатор пользователя
     * @param pollId - идентификатор опроса
     * @paramRequestBody - массив участников опроса
     */
    @POST
    @Path(value = "/start/{poll_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startPoll(@PathParam("poll_id") String pollId, @CookieParam(CookieConst.USER_ID) String userId, @RequestBody List<String> includedIdsString) {

        log.info("Получен запрос /start/" + pollId + " с телом: {} ", includedIdsString);
        NewCookie cookie = new NewCookie(CookieConst.USER_ID, userId);
        return new HttpRequestHandler<String, EmptyResponseDto>()
                .validate(v -> starPollValidateService.validateDataStartPoll(pollId, userId, includedIdsString))
                .process(x -> startPollService.doStartPoll(pollId, userId, includedIdsString))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    /**
     * endpoint получения оценки данного пользователя по всем вопросам и компетенциям данного опроса
     *
     * @param userId - идентификатор пользователя
     * @param pollId - идентификатор опроса
     * @return - ДТО с вопросами, компетенциями и оценками
     */
    @GET
    @Path(value = "/result/{poll_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResultForUser(@PathParam("poll_id") String pollId, @CookieParam(CookieConst.USER_ID) String userId) {
        log.info("Get result /result/" + pollId + " для пользователя: " + userId);
        NewCookie cookie = new NewCookie(CookieConst.USER_ID, userId);
        return new HttpRequestHandler<String, GradeUserDto>()
                .validate(v -> resultUserValidateService.validateDataResultUser(pollId, userId))
                .process(x -> gradeService.countGrade(userId, pollId))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    /**
     * endpoint получения данных о пользователи по идентификатору пользователя
     *
     * @param userId - идентификатор пользователя
     * @return - ДТО с информацией о пользователе
     */
    @GET
    @Path("getuser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@CookieParam("user-id") String userId) {
        log.info("Получен запрос /getuser");
        NewCookie cookie = new NewCookie(Cookie.USER_ID.getValue(), userId);
        return new HttpRequestHandler<String, UserResponseDto>()
                .validate(v -> userValidateService.userIdValidate(userId))
                .process(x -> userService.getRespondentByUserId(userId))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    /**
     * endpoint получения данных об опросе по идентификатору опроса
     *
     * @param userId - идентификатор пользователя
     * @param pollId - идентификатор опроса
     * @return - ДТО с информацией об опросе
     */
    @GET
    @Path("polls/{poll_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPoolById(@CookieParam("user-id") String userId, @PathParam("poll_id") String pollId) {
        log.info("Получен запрос pools/" + pollId);
        NewCookie cookie = new NewCookie(Cookie.USER_ID.getValue(), userId);
        return new HttpRequestHandler<String, PollByIdResponseDto>()
            .validate(v -> pollValidateService.getPollByIdValidate(userId, pollId))
            .process(x -> pollService.getPollById(pollId))
            .convert(objectConvertService::convertToJson)
            .forArgument(userId, cookie);
    }

    /**
     * endpoint обновления победителя в паре по конкретному вопросу текущего опроса
     *
     * @param userId              - идентификатор пользователя
     * @param updateWinnerRequestDto - данные запроса
     * @return - ДТО с информацией об опросе
     */
    @POST
    @Path("updatewinner")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateWinner(@CookieParam(CookieConst.USER_ID) String userId,
                                 @RequestBody UpdateWinnerRequestDto updateWinnerRequestDto) {
        log.info("Получен запрос /updatewinner с телом: {}", updateWinnerRequestDto);
        NewCookie cookie = new NewCookie(CookieConst.USER_ID, userId);
        return new HttpRequestHandler<String, ResponseMessage>()
                .validate(v -> ratingRequestValidateService.validateUpdateWinnerRequestDto(userId, updateWinnerRequestDto))
                .process(x -> winnerCompleteService.updateWinner(userId, updateWinnerRequestDto))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

}

