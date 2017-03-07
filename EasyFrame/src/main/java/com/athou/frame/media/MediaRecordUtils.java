/*
 * Copyright (c) 2017  athou（cai353974361@163.com）.
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

package com.athou.frame.media;

import android.media.MediaRecorder;

import com.athou.frame.util.L;

import java.io.File;
import java.io.IOException;

/**
 * 录制amr工具类
 *
 * @author Administrator
 */
public class MediaRecordUtils {

    private static final int SAMPLE_RATE_IN_HZ = 8000;
    private MediaRecorder recorder;
    // 录音的路径
    private String mPath;

    public MediaRecordUtils(String path) {
        mPath = path;
    }

    /**
     * 开始录音
     */
    public void start() {
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            L.e("SD Card is not mounted,It is  " + state + ".");
            return;
        }
        File directory = new File(mPath).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            L.e("Path to file could not be created");
            return;
        }
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        if (android.os.Build.VERSION.SDK_INT >= 10) {
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        } else {
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        }
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
        recorder.setOutputFile(mPath);
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
    }

    /**
     * 结束录音
     */
    public String stop() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
        L.i(mPath);
        return mPath;
    }

    /**
     * 获取录音时间
     *
     * @return
     */
    public double getAmplitude() {
        if (recorder != null) {
            return (recorder.getMaxAmplitude());
        }
        return 0;
    }
}
