package com.test.admin.conurbations.model.api;
/*
 * Copyright (C) 2016 Johnny Shieh Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.test.admin.conurbations.utils.FileUtils;

/**
 * @author zhouqiong
 * @version 1.0
 */
public interface GankApi {
    String BASE_URL = "https://gank.io/api/";

    String imgPath = FileUtils.getSDPath() + "/MyPictures";

    class status {
        public static final int success = 200;
        public static final int error = -1;
    }
}
