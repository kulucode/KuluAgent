package com.tpson.kuluagent.service.impl;

import com.tpson.kuluagent.domain.Group;
import com.tpson.kuluagent.service.GroupService;
import org.springframework.stereotype.Service;

/**
 * Created by Zhangka in 2018/04/17
 */
@Service
public class GroupServiceImpl extends RedisBaseServiceImpl<Group> implements GroupService {
    @Override
    public String getPrefix() {
        return "KULUAGENT_ZSET_GROUP";
    }
}
