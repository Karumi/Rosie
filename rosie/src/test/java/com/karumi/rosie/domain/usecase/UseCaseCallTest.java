package com.karumi.rosie.domain.usecase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class) public class UseCaseCallTest {

  @Test public void shouldExecuteUseCaseUsingTheUseCaseHandler() {
    RosieUseCase anyUseCase = mock(RosieUseCase.class);
    UseCaseHandler useCaseHandler = mock(UseCaseHandler.class);
    UseCaseCall useCaseCall = new UseCaseCall(anyUseCase, useCaseHandler);

    useCaseCall.execute();

    verify(useCaseHandler).execute(eq(anyUseCase), any(UseCaseParams.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionWhenOnSucessCallbackIsNull() {
    RosieUseCase anyUseCase = mock(RosieUseCase.class);
    UseCaseHandler useCaseHandler = mock(UseCaseHandler.class);
    UseCaseCall useCaseCall = new UseCaseCall(anyUseCase, useCaseHandler);

    useCaseCall.onSuccess(null).execute();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionWhenOnErrorCallbackIsNull() {
    RosieUseCase anyUseCase = mock(RosieUseCase.class);
    UseCaseHandler useCaseHandler = mock(UseCaseHandler.class);
    UseCaseCall useCaseCall = new UseCaseCall(anyUseCase, useCaseHandler);

    useCaseCall.onError(null).execute();
  }
}