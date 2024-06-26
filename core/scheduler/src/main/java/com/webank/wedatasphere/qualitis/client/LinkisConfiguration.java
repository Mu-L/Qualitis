package com.webank.wedatasphere.qualitis.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.webank.wedatasphere.qualitis.config.LinkisConfig;
import com.webank.wedatasphere.qualitis.constant.SpecCharEnum;
import com.webank.wedatasphere.qualitis.constants.QualitisConstants;
import com.webank.wedatasphere.qualitis.dao.ClusterInfoDao;
import com.webank.wedatasphere.qualitis.entity.ClusterInfo;
import com.webank.wedatasphere.qualitis.exception.UnExpectedRequestException;
import com.webank.wedatasphere.qualitis.response.ConfigurationResponse;
import com.webank.wedatasphere.qualitis.response.SettingsResponse;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author allenzhou@webank.com
 * @date 2021/5/7 14:00
 */
@Component
public class LinkisConfiguration {
    @Autowired
    private LinkisConfig linkisConfig;

    @Autowired
    private ClusterInfoDao clusterInfoDao;

    @Autowired
    @Qualifier("linkisRestTemplate")
    private RestTemplate linkisRestTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkisConfiguration.class);

    public Map<String, Map<String, Object>> getTree(String clusterName, String user) throws UnExpectedRequestException {
        ClusterInfo clusterInfoInDb = clusterInfoDao.findByClusterName(clusterName);
        if (clusterInfoInDb == null) {
            throw new UnExpectedRequestException("cluster name {&DOES_NOT_EXIST}");
        }
        Map<String, Map<String, Object>> responseMap = new HashMap<>(2);
        responseMap.put("full_tree_queue_name", getFullTreeQueuename(clusterInfoInDb, user));
        responseMap.put("fule_tree", getFullTree(clusterInfoInDb, user));
        return responseMap;
    }

    private Map<String, Object> getFullTreeQueuename(ClusterInfo clusterInfoInDb, String user) {
        String urlQueueName = UriBuilder.fromUri(clusterInfoInDb.getLinkisAddress()).path(linkisConfig.getPrefix()).path(linkisConfig.getGetFullTree())
            .queryParam("creator", "GlobalSettings").toString();

        HttpHeaders headerQueueNames = new HttpHeaders();
        headerQueueNames.setContentType(MediaType.APPLICATION_JSON);

        headerQueueNames.add("Token-User", user);
        headerQueueNames.add("Token-Code", clusterInfoInDb.getLinkisToken());

        HttpEntity entityQueueName = new HttpEntity<>(headerQueueNames);
        Map<String, Object> responseQueueName = null;
        try {
            LOGGER.info("Start to get configuration queue name from linkis. url: {}, method: {}, body: {}", urlQueueName, javax.ws.rs.HttpMethod.GET, entityQueueName);
            responseQueueName = linkisRestTemplate.exchange(urlQueueName, HttpMethod.GET, entityQueueName, Map.class).getBody();
            Integer codeQueueName = (Integer) responseQueueName.get("status");
            if (codeQueueName != 0) {
                throw new UnExpectedRequestException("Failed to get configuration queue name from linkis.");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.info("Failed to get configuration queue name from linkis.");
            extractMessage(e);
        }
        Map<String, Object> mapQueue = Maps.newHashMap();
        if (responseQueueName != null) {
            // 队列资源
            List<Map<String, Object>> collect = (List<Map<String, Object>>) ((Map) responseQueueName.get("data")).get("fullTree");
            for (Map<String, Object> temp : collect) {
                List<Map<String, Object>> settings = (List<Map<String, Object>>) temp.get("settings");
                // 将字符串首个字符改为大写
                List<Map<String, Object>> result = settings.stream().map((item) -> {
                    if (item.get("name") != null) {
                        item.put("name", StringUtils.capitalize((String) item.get("name")));
                    }
                    return item;
                }).collect(Collectors.toList());
                temp.put("settings", result);
            }
            mapQueue.put("fullTree", collect);
        }
        return mapQueue;
    }

    private Map<String, Object> getFullTree(ClusterInfo clusterInfoInDb, String user) {
        String url = UriBuilder.fromUri(clusterInfoInDb.getLinkisAddress()).path(linkisConfig.getPrefix()).path(linkisConfig.getGetFullTree())
            .queryParam("creator", linkisConfig.getAppName())
            .queryParam("engineType", linkisConfig.getEngineName())
            .queryParam("version", linkisConfig.getEngineVersion()).toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Token-User", user);
        headers.add("Token-Code", clusterInfoInDb.getLinkisToken());

        HttpEntity entity = new HttpEntity<>(headers);
        Map<String, Object> response = null;
        try {
            LOGGER.info("Start to get configuration from linkis. url: {}, method: {}, body: {}", url, javax.ws.rs.HttpMethod.GET, entity);
            response = linkisRestTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();
            LOGGER.info("Finish to get configuration from linkis. response: {}", response);
            Integer code = (Integer) response.get("status");
            if (code != 0) {
                throw new UnExpectedRequestException("Failed to get configuration from linkis.");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.info("Failed to get configuration from linkis.");
            extractMessage(e);
        }
        Map<String, Object> maps = Maps.newHashMap();
        if (response != null) {
            List<Map<String, Object>> mapList = (List<Map<String, Object>>) ((Map) response.get("data")).get("fullTree");
            List<SettingsResponse> settingsResponses = Lists.newArrayList();
            for (Map<String, Object> item : mapList) {
                if (QualitisConstants.ENGINE_CONFIGURATION.stream().anyMatch(temp -> temp.equals(item.get("name") + ""))) {
                    List<Map<String, Object>> settings = (List<Map<String, Object>>) item.get("settings");
                    for (Map<String, Object> temp : settings) {
                        SettingsResponse settingsResponse = new SettingsResponse();
                        settingsResponse.setId(temp.get("id") != null ? (Integer) temp.get("id") : null);
                        settingsResponse.setKey(temp.get("key") != null ? (String) temp.get("key") : "");
                        settingsResponse.setDescription(temp.get("description") != null ? (String) temp.get("description") : "");

                        if (temp.get("name") != null) {
                            if ("worker并发数".equals((String) temp.get("name"))) {
                                settingsResponse.setName("Spark Executor并发数");
                            } else if ("worker内存大小".equals((String) temp.get("name"))) {
                                settingsResponse.setName("Spark Executor内存数");
                            } else {
                                settingsResponse.setName(StringUtils.capitalize((String) temp.get("name")));
                            }
                        } else {
                            settingsResponse.setName("");
                        }

                        settingsResponse.setDefaultValue(temp.get("defaultValue") != null ? (String) temp.get("defaultValue") : "");
                        settingsResponse.setValidateType(temp.get("validateType") != null ? (String) temp.get("validateType") : "");
                        settingsResponse.setValidateRange(temp.get("validateRange") != null ? (String) temp.get("validateRange") : "");
                        settingsResponse.setLevel(temp.get("level") != null ? (Integer) temp.get("level") : null);
                        settingsResponse.setEngineType(temp.get("engineType") != null ? (String) temp.get("engineType") : "");
                        settingsResponse.setTreeName(temp.get("treeName") != null ? (String) temp.get("treeName") : "");
                        settingsResponse.setValueId(temp.get("valueId") != null ? (Integer) temp.get("valueId") : null);
                        settingsResponse.setConfigValue(temp.get("configValue") != null ? (String) temp.get("configValue") : "");
                        settingsResponse.setConfigLabelId(temp.get("configLabelId") != null ? (Integer) temp.get("configLabelId") : null);
                        settingsResponse.setUnit(temp.get("unit") != null ? (String) temp.get("unit") : "");
                        settingsResponse.setIsUserDefined(temp.get("isUserDefined") != null ? (Boolean) temp.get("isUserDefined") : false);
                        settingsResponse.setHidden(temp.get("hidden") != null ? (Boolean) temp.get("hidden") : false);
                        settingsResponse.setAdvanced(temp.get("advanced") != null ? (Boolean) temp.get("advanced") : false);
                        settingsResponses.add(settingsResponse);
                    }
                }
            }
            List<ConfigurationResponse> configurationResponseList = Lists.newArrayList();
            ConfigurationResponse configurationResponse = new ConfigurationResponse();
            configurationResponse.setName("Spark参数");
            configurationResponse.setDescription("");
            configurationResponse.setSettings(settingsResponses);
            configurationResponseList.add(configurationResponse);
            maps.put("fullTree", configurationResponseList);
        }
        return maps;
    }

    public void saveTree(String clusterName, List<Map<String, Object>> fullTree, String userName) throws UnExpectedRequestException {
        ClusterInfo clusterInfoInDb = clusterInfoDao.findByClusterName(clusterName);
        if (clusterInfoInDb == null) {
            throw new UnExpectedRequestException("cluster name {&ALREADY_EXIST}");
        }
        String url = UriBuilder.fromUri(clusterInfoInDb.getLinkisAddress()).path(linkisConfig.getPrefix()).path(linkisConfig.getSaveFullTree())
                .toString();
        Gson gson = new Gson();

        List<Map<String, Object>> finalQueueNameMapList = new ArrayList<>();
        List<Map<String, Object>> finalMapList = new ArrayList<>();
        for (Map<String, Object> map : fullTree) {
            if ("队列资源".equals(map.get("name"))) {
                List<Map<String, Object>> settings = (List<Map<String, Object>>) map.get("settings");
                Map<String, List<Map<String, Object>>> treeName = settings.stream().collect(Collectors.groupingBy(e -> e.get("treeName").toString()));

                for (Map.Entry<String, List<Map<String, Object>>> entry : treeName.entrySet()) {
                    Map<String, Object> maps = Maps.newLinkedHashMap();
                    maps.put("description", "");
                    maps.put("name", entry.getKey());
                    maps.put("settings", treeName.get(entry.getKey()));
                    finalQueueNameMapList.add(maps);
                }
            } else if ("Spark参数".equals(map.get("name"))) {
                List<Map<String, Object>> settings = (List<Map<String, Object>>) map.get("settings");
                Map<String, List<Map<String, Object>>> treeName = settings.stream().collect(Collectors.groupingBy(e -> e.get("treeName").toString()));

                for (Map.Entry<String, List<Map<String, Object>>> entry : treeName.entrySet()) {
                    Map<String, Object> maps = Maps.newLinkedHashMap();
                    maps.put("description", "");
                    maps.put("name", entry.getKey());
                    maps.put("settings", treeName.get(entry.getKey()));
                    finalMapList.add(maps);
                }
            }
        }
        setFullTreeQueuename(finalQueueNameMapList, url, userName, clusterInfoInDb.getLinkisToken(), gson);
        saveFullTree(finalMapList, url, userName, clusterInfoInDb.getLinkisToken(), gson);
    }

    private void setFullTreeQueuename(List<Map<String, Object>> finalQueueNameMapList, String url, String userName, String linkisToken, Gson gson)
        throws UnExpectedRequestException {
        Map<String, Object> requestMap = new HashMap<>(2);
        requestMap.put("creator", "GlobalSettings");
        requestMap.put("fullTree", finalQueueNameMapList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Token-User", userName);
        headers.add("Token-Code", linkisToken);

        HttpEntity entity = new HttpEntity<>(gson.toJson(requestMap), headers);
        Map<String, Object> response;

        LOGGER.info("Start to save configuration queue name to linkis. url: {}, method: {}, body: {}", url, javax.ws.rs.HttpMethod.POST, entity);
        try {
            response = linkisRestTemplate.exchange(url, HttpMethod.POST, entity, Map.class).getBody();
            LOGGER.info("Finish to save configuration queue name to linkis. response: {}", response);
            Integer code = (Integer) response.get("status");
            if (code != 0) {
                throw new UnExpectedRequestException("Failed to save configuration queue name from linkis.");
            }
        } catch (Exception e) {
            Map<String, Object> msgMap = extractMessage(e);
            throw new UnExpectedRequestException("Failed to save configuration queue name from linkis: " + (!msgMap.isEmpty() ? msgMap.get("message").toString() : StringUtils.EMPTY));
        }
    }

    private void saveFullTree(List<Map<String, Object>> finalMapList, String url, String userName, String linkisToken, Gson gson)
        throws UnExpectedRequestException {
        Map<String, Object> requestMap = new HashMap<>(4);
        requestMap.put("creator", linkisConfig.getAppName());
        requestMap.put("engineType", linkisConfig.getEngineName() + SpecCharEnum.MINUS.getValue() + linkisConfig.getEngineVersion());
        requestMap.put("fullTree", finalMapList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Token-User", userName);
        headers.add("Token-Code", linkisToken);

        HttpEntity entity = new HttpEntity<>(gson.toJson(requestMap), headers);
        Map<String, Object> response;

        LOGGER.info("Start to save configuration to linkis. url: {}, method: {}, body: {}", url, javax.ws.rs.HttpMethod.POST, entity);
        try {
            response = linkisRestTemplate.exchange(url, HttpMethod.POST, entity, Map.class).getBody();
            LOGGER.info("Finish to save configuration to linkis. response: {}", response);
            Integer code = (Integer) response.get("status");
            if (code != 0) {
                throw new UnExpectedRequestException("Failed to save configuration from linkis.");
            }
        } catch (Exception e) {
            Map<String, Object> msgMap = extractMessage(e);
            throw new UnExpectedRequestException("Failed to save configuration from linkis: " + (!msgMap.isEmpty() ? msgMap.get("message").toString() : StringUtils.EMPTY));
        }
    }

    private Map<String, Object> extractMessage(Exception e) {
        String messageJson = e.getCause().getMessage();
        Map<String, Object> msgMap = Maps.newHashMapWithExpectedSize(1);
        if (StringUtils.isNotEmpty(messageJson)) {
            try {
                msgMap = objectMapper.readValue(messageJson, Map.class);
                if (msgMap.containsKey("message")) {
                    msgMap.put("message", msgMap.get("message"));
                }
            } catch (IOException exception) {
                LOGGER.error(exception.getMessage(), exception);
            }
        }
        LOGGER.error(e.getMessage(), e);
        LOGGER.info("Failed to get configuration from linkis: ", !msgMap.isEmpty() ? msgMap.get("message").toString() : StringUtils.EMPTY);
        return msgMap;
    }
}
