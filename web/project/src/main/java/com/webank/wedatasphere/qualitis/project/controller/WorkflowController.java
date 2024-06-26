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

package com.webank.wedatasphere.qualitis.project.controller;

import com.webank.wedatasphere.qualitis.constants.ResponseStatusConstants;
import com.webank.wedatasphere.qualitis.exception.PermissionDeniedRequestException;
import com.webank.wedatasphere.qualitis.exception.UnExpectedRequestException;
import com.webank.wedatasphere.qualitis.project.constant.ProjectTypeEnum;
import com.webank.wedatasphere.qualitis.project.request.QueryProjectRequest;
import com.webank.wedatasphere.qualitis.project.request.QueryRuleRequest;
import com.webank.wedatasphere.qualitis.project.response.ProjectDetailResponse;
import com.webank.wedatasphere.qualitis.project.response.ProjectResponse;
import com.webank.wedatasphere.qualitis.project.service.WorkflowProjectService;
import com.webank.wedatasphere.qualitis.request.PageRequest;
import com.webank.wedatasphere.qualitis.response.GeneralResponse;
import com.webank.wedatasphere.qualitis.response.GetAllResponse;
import com.webank.wedatasphere.qualitis.project.response.ProjectDetailResponse;
import com.webank.wedatasphere.qualitis.project.service.WorkflowProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author howeye
 */
@Path("api/v1/projector/project/workflow")
public class WorkflowController {

    @Autowired
    private WorkflowProjectService workflowProjectService;

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowController.class);

    @POST
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<GetAllResponse<ProjectResponse>> getWorkflowProjectByUser(PageRequest request) throws UnExpectedRequestException {
        try {
            return workflowProjectService.getWorkflowProjectByUser(request);
        } catch (UnExpectedRequestException e) {
            throw new UnExpectedRequestException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to get workflow project, caused by: {}", e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_GET_ALL_PROJECT}", null);
        }
    }

    /**
     * Maybe were deprecated
     * @param projectId
     * @param pageRequest
     * @return
     * @throws UnExpectedRequestException
     */
    @POST
    @Path("detail/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public GeneralResponse<ProjectDetailResponse> getWorkflowProjectDetail(@PathParam("projectId") Long projectId, PageRequest pageRequest) throws UnExpectedRequestException {
        try {
            return workflowProjectService.getWorkflowProjectDetail(projectId, pageRequest);
        } catch (UnExpectedRequestException e) {
            throw new UnExpectedRequestException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to get workflow project detail, project_id: {}, caused by: {}", projectId, e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_GET_PROJECT_DETAIL}", null);
        }
    }

    @POST
    @Path("query")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<GetAllResponse<ProjectResponse>> query(QueryProjectRequest request) throws UnExpectedRequestException {
        try {
            request.convertParameter();
            return workflowProjectService.getWorkflowProjectByUser(request);
        } catch (UnExpectedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to get project user, caused by system error: {}", e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_GET_ALL_PROJECT}", null);
        }
    }
    /**
     * query projectDetail and ruleList by condition
     * @param projectId
     * @param request
     * @return
     * @throws UnExpectedRequestException
     * @throws PermissionDeniedRequestException
     */
    @POST
    @Path("query/rules/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<ProjectDetailResponse> getRulesByCondition(@PathParam("projectId") Long projectId, QueryRuleRequest request)
            throws UnExpectedRequestException, PermissionDeniedRequestException {
        request.convertParameter();
        try {
            return workflowProjectService.getWorkflowRuleByCondition(projectId, request);
        } catch (UnExpectedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }  catch (PermissionDeniedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to get project rules, caused by system error: {}", e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_GET_PROJECT_DETAIL}", null);
        }
    }

}
