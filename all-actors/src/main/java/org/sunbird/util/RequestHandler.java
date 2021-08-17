package org.sunbird.util;

import org.apache.commons.lang3.StringUtils;
import org.sunbird.JsonKey;
import org.sunbird.common.request.Request;

public class RequestHandler {


    public String getRequestedBy(Request actorMessage) {
        String contextUserId = (String) actorMessage.getContext().get(JsonKey.USER_ID);
        String managedFor = (String) actorMessage.getContext().get(JsonKey.MANAGED_FOR);

      /*  logger.info(actorMessage.getContext(),JsonKey.USER_ID + " in getRequestedBy(): " + contextUserId);
        logger.info(actorMessage.getContext(), JsonKey.MANAGED_FOR + " in getRequestedBy(): " + managedFor);
*/
        // If MUA, then use that userid for createdby and updateby
        if (StringUtils.isNotEmpty(managedFor)) {
            contextUserId = managedFor;
        }
        return contextUserId;
    }
}
