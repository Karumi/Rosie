package com.karumi.rosie.sample.base.view.renderer;

import android.view.View;
import butterknife.ButterKnife;
import com.pedrogomez.renderers.Renderer;

/**
 * Renderer extension create to provide Butter Knife view injection in a transparent way. Your
 * Renderer classes should extend from this one to be able tu use Butter Knife annotations.
 * Remember to call super in you overridden render method.
 */

public abstract class MarvelRenderer<T> extends Renderer<T> {

    @Override public void render() {
        ButterKnife.bind(this, getRootView());
    }

    @Override protected void setUpView(View view) {

    }

    @Override protected void hookListeners(View view) {

    }
}
