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

package com.karumi.rosie.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

/**
 * Base Fragment created to implement some common functionality to every Fragment using this
 * library. All Fragments in this project should extend from this one to be able to use core
 * features like view injection, dependency injection or Rosie presenters.
 */
public abstract class RosieSupportFragment extends Fragment implements RosiePresenter.View {

    private PresenterLifeCycleLinker presenterLifeCycleLinker = new PresenterLifeCycleLinker();

    private boolean injected;

    /**
     * Injects the Fragment dependencies if this injection wasn't performed previously in other
     * Fragment life cycle event.
     */
    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        injectDependencies();
    }

    /**
     * Injects the Fragment dependencies if this injection wasn't performed previously in other
     * Fragment life cycle event.
     */
    @Override public void onAttach(Context context) {
        super.onAttach(context);
        injectDependencies();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                                 Bundle savedInstanceState) {
        injectDependencies();
        int layoutId = getLayoutId();
        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * Injects the Fragment views using Butter Knife library and initializes the presenter lifecycle.
     */
    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onPreparePresenter();
        presenterLifeCycleLinker.initializeLifeCycle(this, this);
    }

    /**
     * Called before to initialize all the presenter instances linked to the component lifecycle.
     * Override this method to configure your presenter with extra data if needed.
     */
    protected void onPreparePresenter() {

    }

    /**
     * Connects the Fragment onResume method with the presenters associated to the fragment.
     */
    @Override public void onResume() {
        super.onResume();
        presenterLifeCycleLinker.updatePresenters(this);
    }

    /**
     * Connects the Fragment onPause method with the presenters associated to the fragment.
     */
    @Override public void onPause() {
        super.onPause();
        presenterLifeCycleLinker.pausePresenters();
    }

    /**
     * Connects the Fragment onDestroy method with the presenters associated to the fragment.
     */
    @Override public void onDestroy() {
        super.onDestroy();
        presenterLifeCycleLinker.destroyPresenters();
    }

    /**
     * Indicates if the class has to be injected or not. Override this method and return false to use
     * RosieActivity without inject any dependency.
     */
    protected boolean shouldInjectFragment() {
        return true;
    }

    /**
     * Returns the layout id associated to the layout used in the fragment.
     */
    protected abstract int getLayoutId();

    /**
     * Registers a presenter to link to this fragment.
     */
    protected final void registerPresenter(RosiePresenter presenter) {
        presenterLifeCycleLinker.registerPresenter(presenter);
    }

    private void injectDependencies() {
        Activity activity = getActivity();
        if (!injected && activity instanceof Injectable && shouldInjectFragment()) {
            ((Injectable) activity).inject(this);
            injected = true;
        }
    }
}