package com.karumi.rosie.module;

import android.app.Activity;
import android.content.Context;
import com.karumi.rosie.daggerutils.ForActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Dagger module created to provide activity scope dependencies like the Context associated to the
 * current Activity. Require a an instance to the current Activity to be able to provde it.
 */
@Module(library = true) public class RosieActivityModule {

  private final Context context;

  public RosieActivityModule(Activity activity) {
    validateActivity(activity);
    this.context = activity;
  }

  @ForActivity @Provides Context provideCurrentActivityContext() {
    return context;
  }

  private void validateActivity(Activity activity) {
    if (activity == null) {
      throw new IllegalArgumentException("The Activity passed in construction can't be null.");
    }
  }
}
