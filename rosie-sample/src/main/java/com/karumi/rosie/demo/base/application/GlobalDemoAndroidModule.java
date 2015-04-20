package com.karumi.rosie.demo.base.application;

import android.content.Context;
import com.karumi.rosie.daggerutils.ForApplication;
import com.squareup.picasso.Picasso;
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

}
