/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.comics.view.renderer;

import com.karumi.rosie.sample.comics.view.presenter.ComicsPresenter;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;
import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ComicRendererBuilder extends RendererBuilder<ComicViewModel> {

  private Map<Class, Class> rendererMapping = new HashMap<>();

  public ComicRendererBuilder(ComicsPresenter presenter) {
    List<Renderer<ComicViewModel>> prototypes = new LinkedList<>();
    prototypes.add(new ComicRenderer(presenter));
    rendererMapping.put(ComicViewModel.class, ComicRenderer.class);
    prototypes.add(new LoadMoreComicsRenderer());
    setPrototypes(prototypes);
  }

  @Override protected Class getPrototypeClass(ComicViewModel content) {
    if (content != null) {
      return rendererMapping.get(content.getClass());
    } else {
      return LoadMoreComicsRenderer.class;
    }
  }
}
