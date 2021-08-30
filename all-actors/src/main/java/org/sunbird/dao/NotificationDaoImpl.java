package org.sunbird.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sunbird.JsonKey;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.common.exception.BaseException;
import org.sunbird.utils.ServiceFactory;
import org.sunbird.pojo.NotificationFeed;
import org.sunbird.common.response.Response;

import java.util.*;

public class NotificationDaoImpl implements NotificationDao{
    private static final String NOTIFICATION_FEED = "notification_feed";
    private static final String NOTIFICATION_ACTION_TEMPLATE = "action_template";
    private static final String NOTIFICATION_TEMPLATE = "notification_template";
    private static final String KEY_SPACE_NAME = "sunbird_notifications";
    private CassandraOperation cassandraOperation = ServiceFactory.getInstance();
    private ObjectMapper mapper = new ObjectMapper();

    private static NotificationDao notificationDao = null;

    public static NotificationDao getInstance() {
        if (notificationDao == null) {
            notificationDao = new NotificationDaoImpl();
        }
        return notificationDao;
    }

    @Override
    public Response getTemplate(String templateId, Map<String,Object> reqContext) throws BaseException {


        return cassandraOperation.getRecordsByProperty(KEY_SPACE_NAME,NOTIFICATION_TEMPLATE,JsonKey.TEMPLATE_ID,templateId,reqContext);

    }

    @Override
    public Response getTemplateId(String actionType, Map<String,Object> reqContext) throws BaseException {

        return cassandraOperation.getRecordsByProperty(KEY_SPACE_NAME,NOTIFICATION_ACTION_TEMPLATE,JsonKey.ACTION,actionType,reqContext);
    }

    @Override
    public Response createNotificationFeed(List<NotificationFeed> feeds, Map<String,Object> reqContext) throws BaseException {
        List<Map<String, Object>> feedList =
                mapper.convertValue(feeds, new TypeReference<List<Map<String, Object>>>() {});
        return cassandraOperation.batchInsert(KEY_SPACE_NAME, NOTIFICATION_FEED, feedList, reqContext);

    }

    @Override
    public Response readNotificationFeed(String userId, Map<String,Object> reqContext) throws BaseException {
        Map<String, Object> reqMap = new WeakHashMap<>(2);
        reqMap.put(JsonKey.USER_ID, userId);
        return cassandraOperation.getRecordById(KEY_SPACE_NAME,NOTIFICATION_FEED,reqMap,reqContext);
    }

    @Override
    public Response readV1NotificationFeed(String userId, Map<String,Object> reqContext) throws BaseException {
        Map<String, Object> reqMap = new WeakHashMap<>(2);
        reqMap.put(JsonKey.USER_ID, userId);
        reqMap.put(JsonKey.VERSION,"v1");
        return cassandraOperation.getRecordById(KEY_SPACE_NAME,NOTIFICATION_FEED,reqMap,reqContext);

    }

    @Override
    public Response updateNotificationFeed( List<Map<String,Object>> feeds, Map<String,Object> reqContext) throws BaseException {
        return  cassandraOperation.batchUpdateById(KEY_SPACE_NAME, NOTIFICATION_FEED, feeds,reqContext);

    }

    @Override
    public Response deleteUserFeed(List<NotificationFeed> feeds, Map<String,Object> context) throws BaseException {
        List<Map<String,Object>> properties = new ArrayList<>();
        for (NotificationFeed feed : feeds) {
            Map<String,Object> map = new HashMap<>();
            map.put(JsonKey.ID,feed.getUserId());
            map.put(JsonKey.USER_ID,feed.getId());
            map.put(JsonKey.CATEGORY,feed.getCategory());
            properties.add(map);
        }
       return cassandraOperation.batchDelete(KEY_SPACE_NAME,NOTIFICATION_FEED, properties, context);
    }
}