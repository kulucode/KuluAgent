package com.tpson.kuluagent.controller;

import com.tpson.kuluagent.VO.ResultVO;
import com.tpson.kuluagent.VO.TableVO;
import com.tpson.kuluagent.domain.WeightBackend;
import com.tpson.kuluagent.service.GroupService;
import com.tpson.kuluagent.service.ProtocalService;
import com.tpson.kuluagent.service.WeightBackendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Zhangka in 2018/04/13
 */
@Controller
@RequestMapping("/loadbalance/weight")
public class WeightBackendController {
    @Autowired
    WeightBackendService weightBackendService;
    @Autowired
    GroupService groupService;
    @Autowired
    ProtocalService protocalService;

    @RequestMapping("/weight.html")
    public String html(Model model) {
        model.addAttribute("groups", groupService.all());
        model.addAttribute("protocals", protocalService.all());
        return "loadbalance/weight/weight";
    }
    
    @ResponseBody
    @RequestMapping(value = "/weight.do", method = RequestMethod.GET)
    public TableVO keys(Integer offset, Integer limit, String search) {
        Long count = weightBackendService.count();
        if (StringUtils.isBlank(search)) {
            Set<WeightBackend> set = weightBackendService.page(offset, limit);
            return TableVO.successResult(count.intValue(), set);
        } else {
            Set<WeightBackend> set = weightBackendService.all();
            List<WeightBackend> list = set.stream()
                    .filter(r -> r.toString().contains(search))
                    .collect(Collectors.toList());

            if (list.size() > offset + limit) {
                list = list.subList(offset, offset + limit);
            }
            return TableVO.successResult(count.intValue(), list);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/weight.do", method = RequestMethod.POST)
    public ResultVO keys(WeightBackend backend) {
        backend.check();
        return weightBackendService.add(backend)
                ? ResultVO.successResult()
                : ResultVO.failResult("添加失败.");
    }

    @ResponseBody
    @RequestMapping(value = "/weight.do", method = RequestMethod.DELETE)
    public ResultVO keys(@RequestBody ArrayList<WeightBackend> backends) {
        backends.forEach(backend -> backend.check());

        return (weightBackendService.remove(backends) > 0)
                ? ResultVO.successResult()
                : ResultVO.failResult("删除失败.");
    }
}
