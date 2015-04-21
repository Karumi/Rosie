package com.karumi.rosie.domain.usercase;

/**
 * Created by flipper83 on 21/04/15.
 */
public class UserCaseParams {
  private String userCaseName = "";
  private Object[] args;

  private UserCaseParams(String userCaseName, Object[] args) {
    this.args = args;
    this.userCaseName = userCaseName;
  }

  String getUserCaseName() {
    return userCaseName;
  }

  Object[] getArgs() {
    return args;
  }

  public static class Builder {
    private String userCaseName = "";
    private Object[] args;

    public Builder name(String name) {
      userCaseName = name;
      return this;
    }

    public Builder args(Object... args) {
      this.args = args;
      return this;
    }

    public UserCaseParams build() {
      if (this.args == null) {
        args = new Object[0];
      }
      return new UserCaseParams(userCaseName, args);
    }
  }
}
