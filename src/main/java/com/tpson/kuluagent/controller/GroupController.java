package com.tpson.kuluagent.controller;

import com.tpson.kuluagent.VO.ResultVO;
import com.tpson.kuluagent.VO.TableVO;
import com.tpson.kuluagent.domain.Group;
import com.tpson.kuluagent.service.GroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Zhangka in 2018/04/17
 */
@Controller
@RequestMapping("/group/")
public class GroupController {
    @Autowired
    GroupService groupService;

    @RequestMapping(value = "/group.html", method = RequestMethod.GET)
    public String html() {
        return "group/group";
    }

    @ResponseBody
    @RequestMapping(value = "/group.do", method = RequestMethod.GET)
    public TableVO list(Integer offset, Integer limit, String search) {
        Long count = groupService.count();
        if (StringUtils.isBlank(search)) {
            Set<Group> set = groupService.page(offset, limit);
            return TableVO.successResult(count.intValue(), set);
        } else {
            Set<Group> set = groupService.all();
            List<Group> list = set.stream()
                    .filter(g -> g.toString().contains(search))
                    .collect(Collectors.toList());

            if (list.size() > offset + limit) {
                list = list.subList(offset, offset + limit);
            }
            return TableVO.successResult(count.intValue(), list);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/group.do", method = RequestMethod.POST)
    public ResultVO add(Group group) {
        group.check();
        return groupService.add(group) ? ResultVO.successResult() : ResultVO.failResult("添加失败.");
    }

    @ResponseBody
    @RequestMapping(value = "/group.do", method = RequestMethod.DELETE)
    public ResultVO delete(@RequestBody ArrayList<Group> groups) {
        groups.forEach(group -> group.check());

        return (groupService.remove(groups) > 0)
                ? ResultVO.successResult()
                : ResultVO.failResult("删除失败.");
    }
}
