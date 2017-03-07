/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.athou.frame.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 保存状态的fragment
 * 
 * @author 菜菜
 * 
 */
public class StateSaveFragment extends Fragment {
	public StateSaveFragment() {
		super();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Restore State Here
		if (!restoreStateFromArguments()) {
			// First Time, Initialize something here
			onFirstTimeLaunched();
		}
	}

	/**
	 * First Time, Initialize something here
	 * 
	 * @author 菜菜
	 * 
	 */
	protected void onFirstTimeLaunched() {
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save State Here
		saveStateToArguments();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// Save State Here
		saveStateToArguments();
	}

	/**
	 * Save State to Arguments
	 * 
	 * @author 菜菜
	 * 
	 */
	private void saveStateToArguments() {
        Bundle savedState = null;
		if (getView() != null) {
            savedState = saveState();
        }
		if (savedState != null) {
			Bundle b = getArguments();
			b.putBundle("internalSavedViewState8954201239547", savedState);
		}
	}

	/**
	 * restore state from Arguments
	 * 
	 * @author 菜菜
	 * @return
	 * 
	 */
	private boolean restoreStateFromArguments() {
		Bundle b = getArguments();
        if (b == null){
            return false;
        }
		Bundle savedState = b.getBundle("internalSavedViewState8954201239547");
		if (savedState != null) {
            onRestoreState(savedState);
			return true;
		}
		return false;
	}

	/**
	 * Restore State from bundle
	 * 
	 * @author 菜菜
	 * @param savedInstanceState
	 * 
	 */
	protected void onRestoreState(Bundle savedInstanceState) {

	}

	// ////////////////////////////
	// Save Instance State Here
	// ////////////////////////////
	private Bundle saveState() {
		Bundle state = new Bundle();
		onSaveState(state);
		return state;
	}

	/**
	 * save state to bundle
	 * 
	 * @author 菜菜
	 * @param outState
	 * 
	 */
	protected void onSaveState(Bundle outState) {

	}
}
