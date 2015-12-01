/*
 * The MIT License (MIT) Copyright (c) 2014 karumi Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
 * do so, subject to the following conditions: The above copyright notice and this permission
 * notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE
 * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.karumi.rosie.module;

import android.app.Application;
import android.content.Context;
import com.karumi.rosie.application.RosieApplication;
import com.karumi.rosie.daggerutils.ForApplication;
import com.karumi.rosie.domain.usecase.TaskScheduler;
import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.karumi.rosie.domain.usecase.error.ErrorHandler;
import com.karumi.rosie.domain.usecase.jobqueue.TaskSchedulerJobQueue;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to be created.
 */
@Module(library = true, injects = RosieApplication.class) public class RosieAndroidModule {

  private static final int MIN_CONSUMER_COUNT = 1;
  private static final int MAX_CONSUMER_COUNT = 5;
  private static final int LOAD_FACTOR = 1;

  private final Context context;

  public RosieAndroidModule(Application application) {
    validateApplication(application);
    this.context = application;
  }

  /**
   * Allow the application context to be injected but requires it to be annotated with
   * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
   */
  @Provides @ForApplication Context provideApplicationContext() {
    return context;
  }

  @Provides @Singleton public UseCaseHandler provideUseCaseHandler(TaskScheduler taskScheduler,
      ErrorHandler errorHandler) {
    return new UseCaseHandler(taskScheduler, errorHandler);
  }

  @Provides @Singleton public TaskScheduler provideTaskScheduler(JobManager jobManager) {
    return new TaskSchedulerJobQueue(jobManager);
  }

  @Provides @Singleton public JobManager provideJobManager(@ForApplication Context context) {
    Configuration config = new Configuration.Builder(context).minConsumerCount(MIN_CONSUMER_COUNT)
        .maxConsumerCount(MAX_CONSUMER_COUNT)
        .loadFactor(LOAD_FACTOR)
        .build();
    return new JobManager(context, config);
  }

  private void validateApplication(Application application) {
    if (application == null) {
      throw new IllegalArgumentException("The Application passed in construction can't be null.");
    }
  }
}
