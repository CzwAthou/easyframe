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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义fragmentadapter
 *
 * @author 菜菜
 * @category 子fragment需要重写setMenuVisibility方法:<br>
 * public void setMenuVisibility(boolean menuVisible) {<br>
 * super.setMenuVisibility(menuVisible);<br>
 * if (this.getView() != null)<br>
 * this.getView().setVisibility(menuVisible ? View.VISIBLE :
 * View.GONE); <br>
 * }
 */
public abstract class FinalFragmentAdapter {
    Map<Integer, String> map = new HashMap<>();

    private int position = 0;
    private Fragment mContent = null;

    FragmentManager fm = null;

    public FinalFragmentAdapter(FragmentManager fm) {
        super();
        this.fm = fm;
    }

    public Fragment switchFragment(int position) {
        Fragment fragment = instantiateItem(this.position = position);
        if (fragment != mContent && mContent != null) {
            mContent.setMenuVisibility(false);
            mContent.setUserVisibleHint(false);
        }
        return mContent = fragment;
    }

    private Fragment instantiateItem(int position) {
        FragmentTransaction transaction = fm.beginTransaction();

        for (Integer key : map.keySet()) {
            String tag = map.get(key);
            Fragment of = fm.findFragmentByTag(tag);
            transaction.hide(of);
        }
        String tag = makeFragmentName(requestContainerId(), position);
        Fragment f = fm.findFragmentByTag(tag);
        if (f == null) {
            map.put(position, tag);
            f = getItem(position);
            transaction.add(requestContainerId(), f, tag);
        }
        transaction.show(f);
        transaction.commitAllowingStateLoss();
        fm.executePendingTransactions();
        return f;
    }

    public Fragment getFragment(int position) {
        String tag = map.get(position);
        return fm.findFragmentByTag(tag);
    }

    public Fragment getCurrentFragment() {
        return mContent;
    }

    public int getCurrentItemPosition() {
        return position;
    }

    public int size() {
        return map.size();
    }

    public void clear() {
        List<Fragment> fragmentList = fm.getFragments();
        if (fragmentList == null) {
            return;
        }
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {
            transaction.remove(fragmentList.get(i));
        }
        transaction.commitAllowingStateLoss();
        fm.executePendingTransactions();
    }

    /**
     * FinalFragmentAdapter需要一个容器ID(布局XML中frameLayout的资源ID)
     *
     * @return
     * @author 菜菜
     */
    protected abstract int requestContainerId();

    protected abstract Fragment getItem(int position);

    private static String makeFragmentName(int viewId, long id) {
        return "finalframe:switcher:" + viewId + ":" + id;
    }
}
