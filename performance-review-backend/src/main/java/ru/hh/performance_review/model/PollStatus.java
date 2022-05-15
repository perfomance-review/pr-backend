package ru.hh.performance_review.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PollStatus {
  OPEN("OPEN"), PROGRESS("PROGRESS"), COMPLETED("COMPLETED"), CLOSED("CLOSED");

  private final String value;

}