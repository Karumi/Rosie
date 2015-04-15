package com.demo.rosie.base.application;

import android.content.Context;
import com.demo.rosie.base.view.transformation.RoundAvatarTransformation;
import com.rosie.daggerutils.ForApplication;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 *
 */
@Module(library = true,
    complete = false,
    injects = { DemoApplication.class })
public class GlobalDemoAndroidModule {

  @Provides @Singleton
  public Picasso providePicasso(@ForApplication Context context) {
    return Picasso.with(context);
  }

  @Provides
  public Transformation provideAvatarTransformation(
      RoundAvatarTransformation roundAvatarTransformation) {
    return roundAvatarTransformation;
  }
}
