package com.tpson.kuluagent.controller;

import com.tpson.kuluagent.VO.ResultVO;
import com.tpson.kuluagent.domain.HashBackend;
import com.tpson.kuluagent.domain.Protocal;
import com.tpson.kuluagent.domain.RandomBackend;
import com.tpson.kuluagent.domain.WeightBackend;
import com.tpson.kuluagent.service.HashBackendService;
import com.tpson.kuluagent.service.ProtocalService;
import com.tpson.kuluagent.service.RandomBackendService;
import com.tpson.kuluagent.service.WeightBackendService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Zhangka in 2018/04/20
 */
@Controller
@RequestMapping("/file")
public class FileController {
    @Autowired
    ProtocalService protocalService;
    @Autowired
    HashBackendService hashBackendService;
    @Autowired
    WeightBackendService weightBackendService;
    @Autowired
    RandomBackendService randomBackendService;

    @RequestMapping(value = "/template/{type}.do", method = RequestMethod.GET)
    public void template(@PathVariable String type, HttpServletResponse resp) {
        try {
            switch (type) {
                case "protocal":
                    String protocal = "#名称,起始标记,结束标记,分隔符(逗号用*代替),偏移量(分隔符和偏移量都填写，优先使用分隔符),长度\nwatch,@G#@,@R#@,*,,5\n";
                    writeTemplate("protocal.csv", protocal.getBytes("UTF-8"), resp);
                    break;
                case "random":
                    String random = "#IP,PORT,系统名称,分组名称\n192.168.1.249,8809,watch,watch_group1\n";
                    writeTemplate("random.csv", random.getBytes("UTF-8"), resp);
                    break;
                case "weight":
                    String weight = "#IP,PORT,权重,系统名称,分组名称\n192.168.1.249,8809,2,watch,watch_group1\n";
                    writeTemplate("weight.csv", weight.getBytes("UTF-8"), resp);
                    break;
                case "hash":
                    String hash = "#IP,PORT,KEY,系统名称,分组名称\n192.168.1.249,8809,1234567890123456,watch,watch_group1\n";
                    writeTemplate("hash.csv", hash.getBytes("UTF-8"), resp);
                    break;
                default:
                    throw new RuntimeException("无法下载模板.");
            }
        } catch (Exception e) {
            throw new RuntimeException("无法下载模板.");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/template/{type}.do", method = RequestMethod.POST)
    public ResultVO template(@PathVariable String type, MultipartFile p) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            List<String> lines = reader.lines().map(String::trim).filter(e -> !e.contains("#") && StringUtils.isNotBlank(e)).collect(Collectors.toList());
            Long count = readTemplate(type, lines);

            return count != null && count > 0 ? ResultVO.successResult() : ResultVO.failResult("上传失败.");
        } catch (IOException e) {
            throw new RuntimeException("无法导入文件.");
        }
    }

    protected void writeTemplate(String filename, byte[] bytes, HttpServletResponse resp) {
        try (ServletOutputStream out = resp.getOutputStream()) {
            resp.setContentType("application/octet-stream");
            resp.setCharacterEncoding("UTF-8");
            int length = bytes.length;
            resp.setContentLength(length);
            resp.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

            out.write(bytes, 0, length);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("无法下载模板.");
        }
    }

    public Long readTemplate(String type, List<String> lines) {
        Long count = 0L;
        switch (type) {
            case "protocal":
                List<Protocal> protocals = new ArrayList<>();
                lines.forEach(e -> {
                     // watch,@G#@,@R#@,*,,5
                    String[] array = e.split(",");
                    if (array.length == 6) {
                        Protocal protocal = new Protocal();
                        protocal.setName(array[0]);
                        protocal.setStartFlag(array[1]);
                        protocal.setEndFlag(array[2]);
                        protocal.setSplit("*".equals(array[3]) ? "," : array[3]);
                        protocal.setOffset(NumberUtils.isDigits(array[4]) ? Integer.valueOf(array[4]) : 0);
                        protocal.setLength(NumberUtils.isDigits(array[5]) ? Integer.valueOf(array[5]) : 0);
                        try {
                            protocal.check();
                            protocals.add(protocal);
                        } catch (Exception ep) {}
                    }
                });
                if (protocals.size() > 0) {
                    count = protocalService.add(protocals);
                }
                break;
            case "random":
                List<RandomBackend> randoms = new ArrayList<>();
                lines.forEach(e -> {
                    // 192.168.1.249,8809,watch,watch_group1
                    String[] array = e.split(",");
                    if (array.length == 4) {
                        RandomBackend b = new RandomBackend();
                        b.setIp(array[0]);
                        b.setPort(NumberUtils.isDigits(array[1]) ? Integer.valueOf(array[1]) : 0);
                        b.setProtocalName(array[2]);
                        b.setGroupName(array[3]);
                        try {
                            b.check();
                            randoms.add(b);
                        } catch (Exception er) {}
                    }
                });
                if (randoms.size() > 0) {
                   count = randomBackendService.add(randoms);
                }
                break;
            case "weight":
                List<WeightBackend> weights = new ArrayList<>();
                lines.forEach(e -> {
                    // 192.168.1.249,8809,2,watch,watch_group1
                    String[] array = e.split(",");
                    if (array.length == 5) {
                        WeightBackend b = new WeightBackend();
                        b.setIp(array[0]);
                        b.setPort(NumberUtils.isDigits(array[1]) ? Integer.valueOf(array[1]) : 0);
                        b.setWeight(NumberUtils.isDigits(array[2]) ? Integer.valueOf(array[2]) : 0);
                        b.setProtocalName(array[3]);
                        b.setGroupName(array[4]);
                        try {
                            b.check();
                            weights.add(b);
                        } catch (Exception ew) {}
                    }
                });
                if (weights.size() > 0) {
                    count = weightBackendService.add(weights);
                }
                break;
            case "hash":
                List<HashBackend> hashs = new ArrayList<>();
                lines.forEach(e -> {
                    // 192.168.1.249,8809,1234567890123456,watch,watch_group1
                    String[] array = e.split(",");
                    if (array.length == 5) {
                        HashBackend b = new HashBackend();
                        b.setIp(array[0]);
                        b.setPort(NumberUtils.isDigits(array[1]) ? Integer.valueOf(array[1]) : 0);
                        b.setKey(array[2]);
                        b.setProtocalName(array[3]);
                        b.setGroupName(array[4]);
                        try {
                            b.check();
                            hashs.add(b);
                        } catch (Exception eh) {}
                    }
                });
                if (hashs.size() > 0) {
                    count = hashBackendService.add(hashs);
                }
                break;
            default:throw new RuntimeException("类型错误.");
        }

        return count;
    }
}
