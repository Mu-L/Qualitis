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

package com.webank.wedatasphere.qualitis.rule.request.multi;

import com.webank.wedatasphere.qualitis.exception.UnExpectedRequestException;
import com.webank.wedatasphere.qualitis.project.request.CommonChecker;
import com.webank.wedatasphere.qualitis.rule.request.AbstractDeleteRequest;

/**
 * @author howeye
 */
public class DeleteMultiSourceRequest extends AbstractDeleteRequest {

    public DeleteMultiSourceRequest() {
        // Default Constructor
    }

    public DeleteMultiSourceRequest(Long multiRuleId) {
        super.setRuleId(multiRuleId);
    }

    public static void checkRequest(DeleteMultiSourceRequest request) throws UnExpectedRequestException {
        CommonChecker.checkObject(request, "Request");
        CommonChecker.checkObject(request.getRuleId(), "Multi Rule id");
    }

    @Override
    public String toString() {
        return "DeleteMultiSourceRequest{} " + super.toString();
    }
}
