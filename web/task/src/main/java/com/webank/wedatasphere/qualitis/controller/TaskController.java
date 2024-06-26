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
import com.webank.wedatasphere.qualitis.exception.UnExpectedRequestException;
import com.webank.wedatasphere.qualitis.response.GeneralResponse;
import com.webank.wedatasphere.qualitis.response.TaskCheckResultResponse;
import com.webank.wedatasphere.qualitis.service.TaskService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.core.MediaType;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author howeye
 */
@Path("/api/v1/projector/application/task")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @GET
    @Path("detail/{task_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralResponse<TaskCheckResultResponse> getTaskDetail(@PathParam("task_id")Long taskId) throws UnExpectedRequestException {
        try {
            return taskService.getTaskDetail(taskId);
        } catch (UnExpectedRequestException e) {
            throw new UnExpectedRequestException(e.getResponse().getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to get task detail. task_id: {}, caused by: {}", taskId, e.getMessage(), e);
            return new GeneralResponse<>(ResponseStatusConstants.SERVER_ERROR, "{&FAILED_TO_GET_TASK_DETAIL}"+ e.getMessage() + ".", null);
        }
    }

}
