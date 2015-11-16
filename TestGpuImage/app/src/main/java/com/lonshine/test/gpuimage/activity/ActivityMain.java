/*
 * Copyright (C) 2012 CyberAgent
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

package cn.app.meiya.test.gpuimage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.meiyaapp.meiya.R;

public class ActivityMain extends Activity implements OnClickListener {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpu_activity_main);
        findViewById(R.id.button_gallery).setOnClickListener(this);
        findViewById(R.id.button_camera).setOnClickListener(this);
        findViewById(R.id.button_test).setOnClickListener(this);
        findViewById(R.id.button_test_surface).setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.button_gallery:
                startActivity(ActivityGallery.class);
                break;
            case R.id.button_camera:
                startActivity(ActivityCamera.class);
                break;

            case R.id.button_test:
                startActivity(ActivityTest.class);
                break;


            case R.id.button_test_surface:
                startActivity(ActivitySurfaceTest.class);
                break;

            default:
                break;
        }
    }

    private void startActivity(final Class<?> activityClass) {
        startActivity(new Intent(this, activityClass));
    }
}
