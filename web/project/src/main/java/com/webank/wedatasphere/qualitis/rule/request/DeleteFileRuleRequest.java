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

package com.webank.wedatasphere.qualitis.rule.request;

import com.webank.wedatasphere.qualitis.exception.UnExpectedRequestException;
import com.webank.wedatasphere.qualitis.project.request.CommonChecker;

/**
 * @author allenzhou
 */
public class DeleteFileRuleRequest extends AbstractDeleteRequest{

    public DeleteFileRuleRequest() {
        // Default Constructor
    }

    public DeleteFileRuleRequest(Long ruleId) {
        super.setRuleId(ruleId);
    }

    public static void checkRequest(DeleteFileRuleRequest request) throws UnExpectedRequestException {
        CommonChecker.checkObject(request, "request");
    }

    @Override
    public String toString() {
        return "DeleteFileRuleRequest{} " + super.toString();
    }
}
