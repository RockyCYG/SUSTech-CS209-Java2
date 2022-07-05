package edu.sustech.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import edu.sustech.entity.GithubRepository;
import edu.sustech.entity.PageBean;
import edu.sustech.service.GithubService;
import edu.sustech.vo.CloudDataVO;
import edu.sustech.vo.ForkCountVO;
import edu.sustech.vo.PieChartVO;
import edu.sustech.vo.TopicCountVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/github")
public class GithubController {

    @Resource
    private GithubService service;

    @GetMapping("/chart/wordcloud")
    public List<CloudDataVO> getTagListAndViewCount() {
        return service.selectTagAndWatchCount();
    }

    @GetMapping("/chart/fork")
    public List<ForkCountVO> selectForkCount() {
        List<ForkCountVO> forkCountVOList = new ArrayList<>();
        List<String> titleList = service.selectTitleList();
        for (String title : titleList) {
            List<ForkCountVO> forkCountList = service.selectForkCountByTitle(title);
            int forkCount = 0;
            for (ForkCountVO fork : forkCountList) {
                forkCount += fork.getForkCount();
                forkCountVOList.add(new ForkCountVO(title, forkCount, fork.getTime()));
            }
        }
        return forkCountVOList;
    }

    @GetMapping("/table/pages")
    PageBean<GithubRepository> selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          @RequestParam(defaultValue = "id") String column,
                                          @RequestParam(defaultValue = "0") Integer sort,
                                          @RequestParam(defaultValue = "") Integer id,
                                          @RequestParam(defaultValue = "") String title,
                                          @RequestParam(defaultValue = "0") Integer watchers,
                                          @RequestParam(defaultValue = "0") Integer forks,
                                          @RequestParam(defaultValue = "") String createdTime,
                                          @RequestParam(defaultValue = "") String dynamicTags) {
        List<String> tagList = new ArrayList<>();
        if (!createdTime.isEmpty()) {
            int year = Integer.parseInt(createdTime.substring(0, 4));
            int month = Integer.parseInt(createdTime.substring(5, 7));
            year += month / 12;
            month %= 12;
            month++;
            createdTime = year + "-" + ((month <= 9) ? "0" + month : month);
        }
        if (!dynamicTags.isEmpty()) {
            String[] tags = dynamicTags.split(",");
            tagList.addAll(Arrays.asList(tags));
            tagList = tagList.stream().distinct().collect(Collectors.toList());
        }
        column = StringUtils.camelToUnderline(column);
        List<GithubRepository> reposList = service.queryReposByConditions(pageNum, pageSize, column, sort, id, title, watchers, forks, createdTime, tagList);
        for (GithubRepository repos : reposList) {
            List<String> tags = service.queryTagsById(repos.getId());
            repos.setTags(tags);
        }
        int totalSize = service.selectTotalCount(column, sort, id, title, watchers, forks, createdTime, tagList);
        return new PageBean<>(totalSize, reposList);
    }

    @GetMapping("/chart/topic")
    public List<TopicCountVO> getRepos() {
        return service.selectTenTopics().stream().peek(topic -> {
            topic.setLastYearCount(service.selectLastYearCount(topic.getTopic()));
            topic.setLastMonthCount(service.selectLastMonthCount(topic.getTopic()));
        }).collect(Collectors.toList());
    }

    @GetMapping("/chart/forkrepos")
    public List<Integer> getForkRepos() {
        List<Integer> list = new ArrayList<>();
        for (int num = 0; num < 1000; num += 100) {
            int ans = service.selectReposNum(num, num + 100);
            list.add(ans);
        }
        list.add(service.selectReposNum(1000, 40000));
        return list;
    }

    @GetMapping("/chart/piechart")
    public List<PieChartVO> getPieChart() {
        return service.getPieChart();
    }

}
