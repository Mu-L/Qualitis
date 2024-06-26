/*
 * Copyright 2019 WeBank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
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

package com.webank.wedatasphere.qualitis.response;

import com.webank.wedatasphere.qualitis.entity.TaskResult;
import org.apache.commons.lang.StringUtils;

/**
 * @author howeye
 */
public class MultiRuleResultResponse {
    private String envName;
    private String value;

    public MultiRuleResultResponse() {
    }

    public MultiRuleResultResponse(TaskResult taskResult, String envName) {
        this.value = taskResult == null ? null : taskResult.getValue();
        this.envName = StringUtils.isNotBlank(envName) ? envName : "";
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
