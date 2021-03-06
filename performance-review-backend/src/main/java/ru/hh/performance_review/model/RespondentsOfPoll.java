package ru.hh.performance_review.model;

import lombok.*;
import ru.hh.performance_review.model.base.AbstractAuditableBaseEntity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Таблица для хранения респондентов для данного опроса
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "respondents_of_poll")
public class RespondentsOfPoll extends AbstractAuditableBaseEntity {

    /**
     * Уникальный идентификатор записи
     */
    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    /**
     * id опроса
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    /**
     * id респондента
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_id")
    private User respondent;

    /**
     * статус опроса
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PollStatus status;


}
