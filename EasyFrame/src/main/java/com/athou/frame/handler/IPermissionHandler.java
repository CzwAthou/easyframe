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

package com.athou.frame.handler;

import android.Manifest;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;

/**
 * Created by athou on 2016/11/14.
 */

public interface IPermissionHandler {
    /**
     * 请求SD卡权限
     *
     * @param permissionHandler
     */
    void requestSDCardPermission(PermissionCallback permissionHandler);

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void handleSDCardPermission();

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showWhyNeedSDCardPermission(PermissionRequest request);

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void deniedSDCardPermission();

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void OnSDCardNeverAskAgain();

    //-----------------------------------------------------------

    /**
     * 请求录音权限
     *
     * @param permissionHandler
     */
    void requestRecordPermission(PermissionCallback permissionHandler);

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    void handleRecordPermission();

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    void showWhyNeedRecordPermission(PermissionRequest request);

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    void deniedRecordPermission();

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    void OnRecordNeverAskAgain();

    //-----------------------------------------------------------

    /**
     * 请求相机权限
     *
     * @param permissionHandler
     */
    void requestCameraPermission(PermissionCallback permissionHandler);

    @NeedsPermission(Manifest.permission.CAMERA)
    void handleCameraPermission();

    @OnShowRationale(Manifest.permission.CAMERA)
    void showWhyNeedCameraPermission(PermissionRequest request);

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void deniedCameraPermission();

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void OnCameraNeverAskAgain();

    //-----------------------------------------------------------

    /**
     * 请求电话权限
     *
     * @param permissionHandler
     */
    void requestCallPermission(PermissionCallback permissionHandler);

    void handleCallPermission();

    void showWhyNeedCallPermission(PermissionRequest request);

    void deniedCallPermission();

    void OnCallNeverAskAgain();

    //-----------------------------------------------------------

    /**
     * 请求电话权限
     *
     * @param permissionHandler
     */
    void requestPhoneStatePermission(PermissionCallback permissionHandler);

    void handlePhoneStatePermission();

    void showWhyNeedPhoneStatePermission(PermissionRequest request);

    void deniedPhoneStatePermission();

    void OnPhoneStateNeverAskAgain();

    /**
     * 当用户拒绝权限后，弹出提示框
     *
     * @param permission
     */
    void showPermissionDialog(String permission);

    /**
     * 提示用户为什么需要此权限
     */
    void showWhyPermissionDialog(final PermissionRequest request, String label);

    /**
     * 权限回调接口
     */
    abstract class PermissionCallback {
        /**
         * 权限通过
         */
        public abstract void onGranted();

        /**
         * 权限拒绝
         */
        public void onDenied() {
        }
    }
}
