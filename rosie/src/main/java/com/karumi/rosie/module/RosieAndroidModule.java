package com.karumi.rosie.module;

import android.app.Application;
import android.content.Context;
import com.karumi.rosie.daggerutils.ForApplication;
import dagger.Module;
import dagger.Provides;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */
@Module(library = true)
public class RosieAndroidModule {
  private final Context context;

  public RosieAndroidModule(Application application) {
    this.context = application;
  }

  /**
   * Allow the application context to be injected but require that it be annotated with
   * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
   */
  @Provides @ForApplication Context provideApplicationContext() {
    return context;
  }
}
