package ru.hh.performance_review.model;

import lombok.*;
import lombok.experimental.Accessors;
import ru.hh.performance_review.model.base.AbstractAuditableBaseEntity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Вопросы
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "question")
@Accessors(chain = true)
public class Question extends AbstractAuditableBaseEntity {

    /**
     * Уникальный идентификатор записи
     */
    @Id
    @Column(name = "question_id")
    private UUID questionId = UUID.randomUUID();

    /**
     * Текст вопроса
     */
    @Column(name = "text", unique = true)
    private String text;

    /**
     * компетенция
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "competence_id")
    private Competence competence;

    /**
     * менеджер, который создал этот вопрос
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private User manager;

}
