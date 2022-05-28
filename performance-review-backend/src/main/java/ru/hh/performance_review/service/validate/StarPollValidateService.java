package ru.hh.performance_review.service.validate;

import java.util.List;

public interface StarPollValidateService {

    // TODO: 25.05.2022 Описание  

    /**
     * Валидация userId, pollId, includedIdsString
     *
     * @param userId - идентификатор пользователя
     * @param pollId - идентификатор опроса
     * @param includedIds - массив id участников опроса (не меньше 2)
     */
    void validateDataStartPoll(String pollId, String userId, List<String> includedIds);
}
