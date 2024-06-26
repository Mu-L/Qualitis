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

package com.webank.wedatasphere.qualitis.project.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wedatasphere.qualitis.project.entity.Project;
import com.webank.wedatasphere.qualitis.project.entity.ProjectLabel;
import com.webank.wedatasphere.qualitis.rule.entity.Rule;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author howeye
 */
public class ProjectDetailResponse {
    @JsonProperty("project_detail")
    private ProjectDetail projectDetail;
    @JsonProperty("rule_details")
    private List<HiveRuleDetail> ruleDetails;
    private int total;

    public ProjectDetailResponse() {
    }

    public ProjectDetailResponse(Long notExistsId) {
        projectDetail = new ProjectDetail();
        projectDetail.setProjectId(notExistsId);
    }

    public ProjectDetailResponse(Project project) {
        projectDetail = new ProjectDetail();
        projectDetail.setProjectId(project.getId());
    }

    public ProjectDetailResponse(Project project, List<Rule> ruleList) {
        projectDetail = new ProjectDetail();
        BeanUtils.copyProperties(project, projectDetail);
        projectDetail.setProjectId(project.getId());
        projectDetail.setCnName(project.getCnName());
        projectDetail.setProjectName(project.getName());
        projectDetail.setCreateUser(project.getCreateUser());
        projectDetail.setSubSystemName(project.getSubSystemName());
        projectDetail.setSubSystemId(project.getSubSystemId());
        Set<ProjectLabel> labelSet = project.getProjectLabels();
        if (labelSet != null && ! labelSet.isEmpty()) {
            Set<String> labels = new HashSet<>();
            for (ProjectLabel projectLabel : labelSet) {
                labels.add(projectLabel.getLabelName());
            }
            projectDetail.setProjectLabels(labels);
        }
        ruleDetails = new ArrayList<>();
        if (null != ruleList) {
            for (Rule rule : ruleList) {
                ruleDetails.add(new HiveRuleDetail(rule));
            }
        }
        projectDetail.setGitRepo(project.getGitRepo());
        projectDetail.setGitType(project.getGitType());
        projectDetail.setGitBranch(project.getGitBranch());
        projectDetail.setGitRootDir(project.getGitRootDir());
    }

    public ProjectDetailResponse(List<Map<String, Object>> rules) {
        ruleDetails = new ArrayList<>();
        if (null != rules) {
            for (Map<String, Object> currentRuleMap : rules) {
                Long ruleId = (Long) currentRuleMap.get("rule_id");
                String ruleName = (String) currentRuleMap.get("rule_name");
                Long ruleGroupId = (Long) currentRuleMap.get("rule_group_id");
                String ruleGroupName = (String) currentRuleMap.get("rule_group_name");
                String workFlowName = (String) currentRuleMap.get("work_flow_name");
                String workFlowVersion = (String) currentRuleMap.get("work_flow_version");
                Boolean enable = (Boolean) currentRuleMap.get("rule_enable");
                String workFlowSpace = (String) currentRuleMap.get("work_flow_space");
                String workFlowProject = (String) currentRuleMap.get("work_flow_project");
                String nodeName = (String) currentRuleMap.get("node_name");

                ruleDetails.add(new HiveRuleDetail(ruleId, ruleGroupId, ruleName, ruleGroupName,workFlowName,workFlowVersion,enable,workFlowSpace,workFlowProject,nodeName));
            }
        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ProjectDetail getProjectDetail() {
        return projectDetail;
    }

    public void setProjectDetail(ProjectDetail projectDetail) {
        this.projectDetail = projectDetail;
    }

    public List<HiveRuleDetail> getRuleDetails() {
        return ruleDetails;
    }

    public void setRuleDetails(List<HiveRuleDetail> ruleDetails) {
        this.ruleDetails = ruleDetails;
    }

    @Override
    public String toString() {
        return "ProjectDetailResponse{" +
                "projectDetail=" + projectDetail +
                ", ruleDetails=" + ruleDetails +
                '}';
    }
}
