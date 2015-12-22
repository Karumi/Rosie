/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.rosie.sample.comics.view.renderer;

import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesDetailViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesHeaderDetailViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;
import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ComicSeriesDetailRendererBuilder extends RendererBuilder<ComicSeriesDetailViewModel> {

  private Map<Class, Class> rendererMapping = new HashMap<>();

  public ComicSeriesDetailRendererBuilder() {
    List<Renderer<ComicSeriesDetailViewModel>> prototypes = new LinkedList<>();
    prototypes.add(new ComicRenderer());
    rendererMapping.put(ComicViewModel.class, ComicRenderer.class);
    prototypes.add(new ComicSeriesHeaderDetailRenderer());
    rendererMapping.put(ComicSeriesHeaderDetailViewModel.class,
        ComicSeriesHeaderDetailRenderer.class);
    setPrototypes(prototypes);
  }

  @Override protected Class getPrototypeClass(ComicSeriesDetailViewModel content) {
    return rendererMapping.get(content.getClass());
  }
}
