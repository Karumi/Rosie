/*
 * Copyright (C) 2015 Karumi.
 */

package com.karumi.rosie.sample.characters.view.renderer;

import com.karumi.rosie.sample.characters.view.presenter.CharactersPresenter;
import com.karumi.rosie.sample.characters.view.viewmodel.CharacterViewModel;
import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CharacterRendererBuilder extends RendererBuilder<CharacterViewModel> {

  private Map<Class, Class> rendererMapping = new HashMap<>();

  public CharacterRendererBuilder(CharactersPresenter presenter) {
    List<Renderer<CharacterViewModel>> prototypes = new LinkedList<>();
    prototypes.add(new CharacterRenderer(presenter));
    rendererMapping.put(CharacterViewModel.class, CharacterRenderer.class);
    prototypes.add(new LoadMoreCharactersRenderer());
    setPrototypes(prototypes);
  }

  @Override protected Class getPrototypeClass(CharacterViewModel content) {
    if (content != null) {
      return rendererMapping.get(content.getClass());
    } else {
      return LoadMoreCharactersRenderer.class;
    }
  }
}
