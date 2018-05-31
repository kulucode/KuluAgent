package com.tpson.kuluagent.controller;

import com.tpson.kuluagent.VO.ResultVO;
import com.tpson.kuluagent.VO.TableVO;
import com.tpson.kuluagent.domain.Protocal;
import com.tpson.kuluagent.service.ProtocalService;
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
 * Created by Zhangka in 2018/04/16
 */
@Controller
@RequestMapping("/protocal")
public class ProtocalController {
    @Autowired
    ProtocalService protocalService;

    @RequestMapping(value = "/protocal.html", method = RequestMethod.GET)
    public String html() {
        return "protocal/protocal";
    }

    @ResponseBody
    @RequestMapping(value = "/protocal.do", method = RequestMethod.GET)
    public TableVO list(Integer offset, Integer limit, String search) {
        Long count = protocalService.count();
        if (StringUtils.isBlank(search)) {
            Set<Protocal> set = protocalService.page(offset, limit);
            return TableVO.successResult(count.intValue(), set);
        } else {
            Set<Protocal> set = protocalService.all();
            List<Protocal> list = set.stream()
                    .filter(p -> p.toString().contains(search))
                    .collect(Collectors.toList());

            if (list.size() > offset + limit) {
                list = list.subList(offset, offset + limit);
            }
            return TableVO.successResult(count.intValue(), list);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/protocal.do", method = RequestMethod.POST)
    public ResultVO add(Protocal protocal) {
        protocal.check();
        return protocalService.add(protocal) ? ResultVO.successResult() : ResultVO.failResult("添加失败.");
    }

    @ResponseBody
    @RequestMapping(value = "/protocal.do", method = RequestMethod.DELETE)
    public ResultVO delete(@RequestBody ArrayList<Protocal> protocals) {
        protocals.forEach(protocal -> protocal.check());

        return (protocalService.remove(protocals) > 0)
                ? ResultVO.successResult()
                : ResultVO.failResult("删除失败.");
    }
}
