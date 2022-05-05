package ru.hh.performance_review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hh.performance_review.model.base.AbstractAuditableBaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Таблица для хранения персон, исключенных данным респондентом из данного опроса
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "excluded_respondents_of_poll", schema = "performance_review")
public class ExcludedRespondentsOfPoll extends AbstractAuditableBaseEntity {

    /**
     * Уникальный идентификатор записи
     */
    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    /**
     * id опроса
     */
    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;

    /**
     * id респондента
     */
    @ManyToOne
    @JoinColumn(name = "respondent_id")
    private User respondent;

    /**
     * id персоны, исключенной из данного опроса для данного респондента
     */
    @ManyToOne
    @JoinColumn(name = "excluded_id")
    private User excluded;

}
