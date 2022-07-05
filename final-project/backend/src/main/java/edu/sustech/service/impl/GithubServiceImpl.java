package edu.sustech.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.entity.GithubRepository;
import edu.sustech.mapper.GithubMapper;
import edu.sustech.service.GithubService;
import edu.sustech.vo.CloudDataVO;
import edu.sustech.vo.ForkCountVO;
import edu.sustech.vo.PieChartVO;
import edu.sustech.vo.TopicCountVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GithubServiceImpl extends ServiceImpl<GithubMapper, GithubRepository> implements GithubService {

    @Resource
    private GithubMapper mapper;

    @Override
    public List<String> selectTitleList() {
        return mapper.selectTitleList();
    }

    @Override
    public List<CloudDataVO> selectTagAndWatchCount() {
        return mapper.selectTagAndWatchCount();
    }

    @Override
    public ForkCountVO selectForkByTitleAndYear(String title, String year) {
        return mapper.selectForkByTitleAndYear(title, year);
    }

    @Override
    public List<ForkCountVO> selectForkCountByTitle(String title) {
        return mapper.selectForkCountByTitle(title);
    }

    @Override
    public List<GithubRepository> queryReposByConditions(Integer pageNum, Integer pageSize, String column, Integer sort, Integer id, String title, Integer watchCount, Integer score, String createYear, List<String> tagList) {
        int begin = (pageNum - 1) * pageSize;
        return mapper.selectReposByConditions(begin, pageSize, column, sort, id, title, watchCount, score, createYear, tagList);
    }

    @Override
    public List<String> queryTagsById(Integer id) {
        return mapper.queryTagsById(id);
    }

    @Override
    public int selectTotalCount(String column, Integer sort, Integer id, String title, Integer watchCount, Integer forkCount, String createYear, List<String> tagList) {
        return mapper.selectTotalCount(column, sort, id, title, watchCount, forkCount, createYear, tagList);
    }

    @Override
    public List<TopicCountVO> selectTenTopics() {
        return mapper.selectTenTopics();
    }

    @Override
    public int selectLastYearCount(String topic) {
        return mapper.selectLastYearCount(topic);
    }

    @Override
    public int selectReposNum(int lowFork, int highFork) {
        return mapper.selectReposNum(lowFork, highFork);
    }

    @Override
    public List<PieChartVO> getPieChart() {
        return mapper.getPieChart();
    }

    @Override
    public int selectLastMonthCount(String topic) {
        return mapper.selectLastMonthCount(topic);
    }


}
