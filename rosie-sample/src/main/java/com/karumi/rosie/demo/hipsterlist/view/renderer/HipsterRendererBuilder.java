package com.karumi.rosie.demo.hipsterlist.view.renderer;

import android.content.Context;
import com.karumi.rosie.demo.hipsterlist.view.model.Hipster;
import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;
import java.util.ArrayList;
import java.util.Collection;

/**
 * RendererBuilder extension created to map Hipster instances to the Renderer used to render the
 * Hipster information into a ListView or RecyclerView.
 */
public class HipsterRendererBuilder extends RendererBuilder<Hipster> {

  private final Context context;

  public HipsterRendererBuilder(Context context) {
    this.context = context;
    initializePrototypes();
  }

  private void initializePrototypes() {
    Collection<Renderer<Hipster>> prototypes = new ArrayList<>();
    prototypes.add(new HipsterRenderer(context));
    setPrototypes(prototypes);
  }

  @Override protected Class getPrototypeClass(Hipster hipster) {
    return HipsterRenderer.class;
  }
}
