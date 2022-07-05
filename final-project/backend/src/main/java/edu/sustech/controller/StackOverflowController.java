package edu.sustech.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import edu.sustech.entity.PageBean;
import edu.sustech.entity.StackOverflowQuestion;
import edu.sustech.service.StackOverflowService;
import edu.sustech.vo.CloudDataVO;
import edu.sustech.vo.TagVO;
import edu.sustech.vo.TagViewSumVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stackoverflow")
public class StackOverflowController {
    @Resource
    private StackOverflowService stackOverflowService;

    @GetMapping
    List<StackOverflowQuestion> selectAll() {
        QueryWrapper<StackOverflowQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "title", "score", "view_count", "create_year");
        List<StackOverflowQuestion> questionList = stackOverflowService.list(queryWrapper);
        for (StackOverflowQuestion question : questionList) {
            List<String> tags = stackOverflowService.queryTagsById(question.getId());
            question.setTags(tags);
        }
        return questionList;
    }

    @GetMapping("/table/pages")
    PageBean<StackOverflowQuestion> selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam(defaultValue = "id") String column,
                                               @RequestParam(defaultValue = "0") Integer sort,
                                               @RequestParam(defaultValue = "") Integer id,
                                               @RequestParam(defaultValue = "") String title,
                                               @RequestParam(defaultValue = "0") Integer score,
                                               @RequestParam(defaultValue = "0") Integer viewCount,
                                               @RequestParam(defaultValue = "") String createYear,
                                               @RequestParam(defaultValue = "") String dynamicTags) {
        List<String> tagList = new ArrayList<>();
        Integer year = createYear.isEmpty() ? null : Integer.parseInt(createYear.substring(0, 4)) + 1;
        if (!dynamicTags.isEmpty()) {
//            tagList = Arrays.stream(dynamicTags.split(",")).toList();
            String[] tags = dynamicTags.split(",");
            tagList.addAll(Arrays.asList(tags));
            tagList = tagList.stream().distinct().collect(Collectors.toList());
        }
        column = StringUtils.camelToUnderline(column);
        List<StackOverflowQuestion> questionList = stackOverflowService.queryQuestionsByConditions(pageNum, pageSize, column, sort, id, title, score, viewCount, year, tagList);
        for (StackOverflowQuestion question : questionList) {
            List<String> tags = stackOverflowService.queryTagsById(question.getId());
            question.setTags(tags);
        }
        int totalSize = stackOverflowService.selectTotalCount(column, sort, id, title, score, viewCount, year, tagList);
        return new PageBean<>(totalSize, questionList);
    }

    @GetMapping("/table/export")
    public void exportData(HttpServletResponse response) throws IOException {
        List<StackOverflowQuestion> questionList = this.selectAll();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(questionList);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String filename = URLEncoder.encode("StackOverflowQuestions", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    @GetMapping("/chart/tags")
    public List<TagVO> getTagList() {
        return stackOverflowService.selectAllTags();
    }

    @GetMapping("/chart/wordcloud")
    public List<CloudDataVO> getTagListAndViewCount() {
        List<CloudDataVO> dataList = new ArrayList<>();
        Map<String, Object> result = stackOverflowService.selectTagsAndViewCount();
        for (Map.Entry<String, Object> entry : result.entrySet()) {
            Map<String, Long> value = (Map<String, Long>) entry.getValue();
            Long viewCount = value.get("viewcount");
            dataList.add(new CloudDataVO(entry.getKey(), viewCount));
        }
        return dataList;
    }

    @GetMapping("/chart/tagnum")
    public List<TagViewSumVO> selectTagsNum() {
        List<TagViewSumVO> tagList = new ArrayList<>();
        List<String> tagNameList = stackOverflowService.selectTagsOrderByViewCount();
        for (String tagName : tagNameList) {
            List<Map<Object, Object>> mapList = stackOverflowService.selectTagsNum(tagName);
            TagViewSumVO tag = new TagViewSumVO();
            tag.setName(tagName);
            Map<Integer, Long> yearCountViewMap = new HashMap<>();
            for (Map<Object, Object> map : mapList) {
                Set<Map.Entry<Object, Object>> entrySet = map.entrySet();
                Integer year = 0;
                Long viewCount = 0L;
                for (Map.Entry<Object, Object> entry : entrySet) {
                    String key = (String) entry.getKey();
                    if ("create_year".equals(key)) {
                        year = (Integer) entry.getValue();
                    } else {
                        viewCount = (Long) entry.getValue();
                    }
                }
                yearCountViewMap.put(year, viewCount);
            }
            tag.setTagTotalNum(yearCountViewMap);
            tagList.add(tag);
        }
        return tagList;
    }
}
