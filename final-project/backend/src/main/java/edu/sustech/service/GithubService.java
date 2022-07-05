package edu.sustech.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.entity.GithubRepository;
import edu.sustech.vo.CloudDataVO;
import edu.sustech.vo.ForkCountVO;
import edu.sustech.vo.PieChartVO;
import edu.sustech.vo.TopicCountVO;

import java.util.List;

public interface GithubService extends IService<GithubRepository> {

    List<String> selectTitleList();

    List<CloudDataVO> selectTagAndWatchCount();

    ForkCountVO selectForkByTitleAndYear(String title, String year);

    List<ForkCountVO> selectForkCountByTitle(String title);

    List<GithubRepository> queryReposByConditions(Integer pageNum, Integer pageSize, String column, Integer sort, Integer id, String title, Integer watchCount, Integer score, String createYear, List<String> tagList);

    List<String> queryTagsById(Integer id);

    int selectTotalCount(String column, Integer sort, Integer id, String title, Integer watchCount, Integer forkCount, String createYear, List<String> tagList);

    List<TopicCountVO> selectTenTopics();

    int selectLastYearCount(String topic);

    int selectReposNum(int lowFork, int highFork);

    List<PieChartVO> getPieChart();

    int selectLastMonthCount(String topic);

}
