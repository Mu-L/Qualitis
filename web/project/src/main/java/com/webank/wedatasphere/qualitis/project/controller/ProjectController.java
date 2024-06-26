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
import com.webank.wedatasphere.qualitis.project.response.ProjectEventResponse;
import com.webank.wedatasphere.qualitis.project.response.ProjectDetailResponse;
import com.webank.wedatasphere.qualitis.project.constant.ProjectTypeEnum;
import com.webank.wedatasphere.qualitis.project.response.ProjectResponse;
import com.webank.wedatasphere.qualitis.response.GeneralResponse;
import com.webank.wedatasphere.qualitis.project.service.ProjectService;
import com.webank.wedatasphere.qualitis.response.GetAllResponse;
import com.webank.wedatasphere.qualitis.request.PageRequest;
import com.webank.wedatasphere.qualitis.project.request.*;
import com.webank.wedatasphere.qualitis.util.HttpUtils;

import com.webank.wedatasphere.qualitis.util.RequestParametersUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.servlet.http.HttpServletRequest;

/**
 * @author howeye
 */
@Path("api/v1/projector/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<ProjectDetailResponse> addProject(AddProjectRequest request, @Context HttpServletRequest httpServletRequest)
        throws UnExpectedRequestException {
        try {
            Long userId = HttpUtils.getUserId(httpServletRequest);
            RequestParametersUtils.transcoding(request);
            return projectService.addProject(request, userId);
        } catch (UnExpectedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to add project. caused by system error: {}", e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_ADD_PROJECT}", null);
        }
    }

    @POST
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<GetAllResponse<ProjectResponse>> getAllProjectByUser(PageRequest request) throws UnExpectedRequestException {
        try {
            return projectService.getAllProjectByUser(request);
        } catch (UnExpectedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to get project user, caused by system error: {}", e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_GET_ALL_PROJECT}", null);
        }
    }

    /**
     * query projectDetail and all ruleList
     * @param projectId
     * @param pageRequest
     * @return
     * @throws UnExpectedRequestException
     * @throws PermissionDeniedRequestException
     */
    @POST
    @Path("detail/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<ProjectDetailResponse> getProjectDetail(@PathParam("projectId") Long projectId, PageRequest pageRequest)
        throws UnExpectedRequestException, PermissionDeniedRequestException {
        try {
            return projectService.getProjectDetail(projectId, pageRequest);
        } catch (UnExpectedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (PermissionDeniedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to get project detail, Project ID: {}, caused by system error: {}", projectId, e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_GET_PROJECT_DETAIL}", null);
        }
    }

    @POST
    @Path("event/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<GetAllResponse<ProjectEventResponse>> getProjectEvents(@PathParam("projectId") Long projectId, PageRequest pageRequest) throws UnExpectedRequestException, PermissionDeniedRequestException {
        try {
            return projectService.getProjectEvents(projectId, pageRequest);
        } catch (UnExpectedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (PermissionDeniedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to get project event, Project ID: {}, caused by system error: {}", projectId, e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_GET_PROJECT_EVENT}", null);
        }
    }

    @POST
    @Path("modify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<ProjectDetailResponse> modifyProjectDetail(ModifyProjectDetailRequest request) throws UnExpectedRequestException, PermissionDeniedRequestException {
        try {
            RequestParametersUtils.transcoding(request);
            return projectService.modifyProjectDetail(request, false);
        } catch (UnExpectedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (PermissionDeniedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to modify project. Project ID: {}, caused by system error: {}", request.getProjectId(), e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_MODIFY_PROJECT}", null);
        }
    }

    @POST
    @Path("git/modify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<ProjectDetailResponse> modifyProjectGitRelation(ModifyProjectGitRelationRequest request) throws UnExpectedRequestException, PermissionDeniedRequestException {
        try {
            RequestParametersUtils.transcoding(request);
            return projectService.modifyProjectGitRelation(request);
        } catch (UnExpectedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (PermissionDeniedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to modify project git relation. Project ID: {}, caused by system error: {}", request.getProjectId(), e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_MODIFY_PROJECT}", null);
        }
    }

    @POST
    @Path("git/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<ProjectDetailResponse> deleteProjectGitRelation(ModifyProjectGitRelationRequest request) throws UnExpectedRequestException, PermissionDeniedRequestException {
        try {
            return projectService.deleteProjectGitRelation(request);
        } catch (UnExpectedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (PermissionDeniedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to delete project git relation. Project ID: {}, caused by system error: {}", request.getProjectId(), e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "Failed to delete git relation of project", null);
        }
    }

    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse deleteProject(DeleteProjectRequest request) throws UnExpectedRequestException, PermissionDeniedRequestException {
        try {
            return projectService.deleteProject(request, false);
        } catch (UnExpectedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }  catch (PermissionDeniedRequestException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to delete project. Project ID: {}, caused by system error: {}", request.getProjectId(), e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_DELETE_PROJECT}", null);
        }
    }



    @GET
    @Path("all/rules/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<ProjectDetailResponse> getAllRules(@PathParam("projectId") Long projectId)
        throws UnExpectedRequestException, PermissionDeniedRequestException {
        try {
            return projectService.getAllRules(projectId);
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

    @POST
    @Path("query")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<GetAllResponse<ProjectResponse>> getProjectsByCondition(QueryProjectRequest request) throws UnExpectedRequestException {
        request.convertParameter();
        try {
            return projectService.getProjectsByCondition(request, ProjectTypeEnum.NORMAL_PROJECT.getCode());
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
            return projectService.getRulesByCondition(projectId, request);
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
