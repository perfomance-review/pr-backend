package ru.hh.performance_review.model;

import lombok.*;
import ru.hh.performance_review.model.base.AbstractAuditableBaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Опросы
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "poll")
public class Poll extends AbstractAuditableBaseEntity {

    /**
     * Уникальный идентификатор записи
     */
    @Id
    @Column(name = "poll_id")
    private UUID pollId = UUID.randomUUID();

    /**
     * Название опроса
     */
    @Column(name = "name")
    private String name;

    /**
     * Описание опроса
     */
    @Column(name = "description")
    private String description;

    /**
     * id менеджера данног опроса
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private User manager;

    /**
     * дедлайн опроса
     */
    @Column(name = "deadline")
    private LocalDate deadline;

}
