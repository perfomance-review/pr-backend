package ru.hh.performance_review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.hh.performance_review.model.PollStatus;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class GetPollResponseDto {
  private String title;
  private LocalDate deadline;
  private long questionsCount;
  private long respondentsCount;
  private PollStatus status;
}