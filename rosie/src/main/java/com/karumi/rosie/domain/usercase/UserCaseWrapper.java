package com.karumi.rosie.domain.usercase;

import java.lang.reflect.Method;

/**
 * This class envolve the usercase for invoke it.
 */
public class UserCaseWrapper {
  private final Object userCase;
  private final UserCaseParams userCaseParams;
  private final UserCaseFilter userCaseFilter;

  public UserCaseWrapper(Object userCase, UserCaseParams userCaseParams) {
    this.userCase = userCase;
    this.userCaseParams = userCaseParams;
    userCaseFilter = new UserCaseFilter();
  }

  public void execute() throws Exception {
    Method methodToInvoke = userCaseFilter.filter(userCase, userCaseParams);
    methodToInvoke.invoke(userCase, userCaseParams.getArgs());
  }
}
