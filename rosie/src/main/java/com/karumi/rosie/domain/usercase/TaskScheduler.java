package com.karumi.rosie.domain.usercase;

/**
 *
 */
public interface TaskScheduler {
  void execute(Object userCase, UserCaseParams userCaseParams);
}
