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

package com.webank.wedatasphere.qualitis.bean;

import java.util.List;

/**
 * @author howeye
 */
public class TaskRule {
    private Long ruleId;
    private String ruleName;
    private String cnName;
    private String ruleDetail;
    private String templateName;
    private String templateEnName;
    private String ruleGroupName;
    private Integer ruleType;
    private Integer alertLevel;
    private String alertReceiver;
    private String midTableName;
    private List<TaskRuleDataSource> taskRuleDataSourceList;
    private Long projectId;
    private String projectName;
    private String projectCnName;
    private String projectCreator;
    private List<TaskRuleAlarmConfigBean> taskRuleAlarmConfigBeans;
    private Boolean deleteFailCheckResult;

    public TaskRule() {
        // Default Constructor
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateEnName() {
        return templateEnName;
    }

    public void setTemplateEnName(String templateEnName) {
        this.templateEnName = templateEnName;
    }

    public String getRuleDetail() {
        return ruleDetail;
    }

    public void setRuleDetail(String ruleDetail) {
        this.ruleDetail = ruleDetail;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleGroupName() {
        return ruleGroupName;
    }

    public void setRuleGroupName(String ruleGroupName) {
        this.ruleGroupName = ruleGroupName;
    }

    public List<TaskRuleDataSource> getTaskRuleDataSourceList() {
        return taskRuleDataSourceList;
    }

    public void setTaskRuleDataSourceList(List<TaskRuleDataSource> taskRuleDataSourceList) {
        this.taskRuleDataSourceList = taskRuleDataSourceList;
    }

    public String getMidTableName() {
        return midTableName;
    }

    public void setMidTableName(String midTableName) {
        this.midTableName = midTableName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCnName() {
        return projectCnName;
    }

    public void setProjectCnName(String projectCnName) {
        this.projectCnName = projectCnName;
    }

    public List<TaskRuleAlarmConfigBean> getTaskRuleAlarmConfigBeans() {
        return taskRuleAlarmConfigBeans;
    }

    public void setTaskRuleAlarmConfigBeans(List<TaskRuleAlarmConfigBean> taskRuleAlarmConfigBeans) {
        this.taskRuleAlarmConfigBeans = taskRuleAlarmConfigBeans;
    }

    public String getProjectCreator() {
        return projectCreator;
    }

    public void setProjectCreator(String projectCreator) {
        this.projectCreator = projectCreator;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(Integer alertLevel) {
        this.alertLevel = alertLevel;
    }

    public String getAlertReceiver() {
        return alertReceiver;
    }

    public void setAlertReceiver(String alertReceiver) {
        this.alertReceiver = alertReceiver;
    }

    public Boolean getDeleteFailCheckResult() {
        return deleteFailCheckResult;
    }

    public void setDeleteFailCheckResult(Boolean deleteFailCheckResult) {
        this.deleteFailCheckResult = deleteFailCheckResult;
    }
}
