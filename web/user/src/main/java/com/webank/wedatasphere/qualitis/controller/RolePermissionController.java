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

package com.webank.wedatasphere.qualitis.controller;

import com.webank.wedatasphere.qualitis.constants.ResponseStatusConstants;
import com.webank.wedatasphere.qualitis.request.rolepermission.AddRolePermissionRequest;
import com.webank.wedatasphere.qualitis.request.rolepermission.DeleteRolePermissionRequest;
import com.webank.wedatasphere.qualitis.request.rolepermission.ModifyRolePermissionRequest;
import com.webank.wedatasphere.qualitis.response.RolePermissionResponse;
import com.webank.wedatasphere.qualitis.service.RolePermissionService;
import com.webank.wedatasphere.qualitis.exception.UnExpectedRequestException;
import com.webank.wedatasphere.qualitis.request.PageRequest;
import com.webank.wedatasphere.qualitis.response.GeneralResponse;
import com.webank.wedatasphere.qualitis.response.GetAllResponse;
import com.webank.wedatasphere.qualitis.util.HttpUtils;
import com.webank.wedatasphere.qualitis.exception.UnExpectedRequestException;
import com.webank.wedatasphere.qualitis.request.rolepermission.AddRolePermissionRequest;
import com.webank.wedatasphere.qualitis.request.rolepermission.DeleteRolePermissionRequest;
import com.webank.wedatasphere.qualitis.request.rolepermission.ModifyRolePermissionRequest;
import com.webank.wedatasphere.qualitis.response.GeneralResponse;
import com.webank.wedatasphere.qualitis.response.RolePermissionResponse;
import com.webank.wedatasphere.qualitis.service.RolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @author howeye
 */
@Path("api/v1/admin/role_permission")
public class RolePermissionController {


    @Autowired
    private RolePermissionService rolePermissionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RolePermissionController.class);

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<RolePermissionResponse> addRolePermission(AddRolePermissionRequest request, @Context HttpServletRequest httpServletRequest) throws UnExpectedRequestException {
        String username = null;
        try {
            username = HttpUtils.getUserName(httpServletRequest);
            return rolePermissionService.addRolePermission(request);
        } catch (UnExpectedRequestException e) {
            throw new UnExpectedRequestException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to add role_permission, role_id: {}, permission_id: {}, caused by: {}, current_user: {}", request.getRoleId(),
                    request.getPermissionId(),e.getMessage(), username, e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_ADD_ROLE_PERMISSION}", null);
        }
    }

    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse deleteRolePermission(DeleteRolePermissionRequest request, @Context HttpServletRequest httpServletRequest) throws UnExpectedRequestException {
        String username = null;
        try {
            username = HttpUtils.getUserName(httpServletRequest);
            return rolePermissionService.deleteRolePermission(request);
        } catch (UnExpectedRequestException e) {
            throw new UnExpectedRequestException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to delete role_permission, uuid: {}, caused by: {}, current_user: {}", request.getUuid(), e.getMessage(), username, e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_DELETE_ROLE_PERMISSION}", null);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse modifyRolePermission(ModifyRolePermissionRequest request, @Context HttpServletRequest httpServletRequest) throws UnExpectedRequestException {
        String username = null;
        try {
            username = HttpUtils.getUserName(httpServletRequest);
            return rolePermissionService.modifyRolePermission(request);
        } catch (UnExpectedRequestException e) {
            throw new UnExpectedRequestException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to modify role_permission, uuid: {}, caused by: {}, current_user: {}", request.getUuid(), e.getMessage(), username, e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_MODIFY_ROLE_PERMISSION}", null);
        }
    }

    @POST
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<GetAllResponse<RolePermissionResponse>> findAllRolePermission(PageRequest request, @Context HttpServletRequest httpServletRequest) throws UnExpectedRequestException {
        String username = null;
        try {
            username = HttpUtils.getUserName(httpServletRequest);
            return rolePermissionService.findAllRolePermission(request);
        } catch (UnExpectedRequestException e) {
            throw new UnExpectedRequestException(e.getMessage());
        }catch (Exception e) {
            LOGGER.error("Failed to find all role_permission, page: {}, size: {}, caused by: {}, current_user: {}", request.getPage(), request.getSize(), e.getMessage(), username, e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_FIND_ALL_ROLE_PERMISSION}", null);
        }
    }

}
