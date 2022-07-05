package edu.sustech.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.sustech.entity.GithubRepository;
import edu.sustech.vo.CloudDataVO;
import edu.sustech.vo.ForkCountVO;
import edu.sustech.vo.PieChartVO;
import edu.sustech.vo.TopicCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GithubMapper extends BaseMapper<GithubRepository> {

    @Select("select distinct title from record_forks;")
    List<String> selectTitleList();

    List<CloudDataVO> selectTagAndWatchCount();

    @Select("""
            select time, count(*) forkCount
            from (select title, substr(time, 0, 8) as time
                  from record_forks
                  where title = #{title}) t
            where time = #{year}
            group by time
            order by time;""")
    ForkCountVO selectForkByTitleAndYear(@Param("title") String title, @Param("year") String year);

    @Select("""
            select time, count(*) forkCount
            from (select title, substr(time, 0, 8) as time
                  from record_forks
                  where title = #{title}) t
            group by time
            order by time;""")
    List<ForkCountVO> selectForkCountByTitle(String title);

    List<GithubRepository> selectReposByConditions(@Param("begin") int begin, @Param("pageSize") Integer pageSize, @Param("column") String column, @Param("sort") Integer sort, @Param("id") Integer id, @Param("title") String title, @Param("watchCount") Integer watchCount, @Param("forkCount") Integer forkCount, @Param("createYear") String createYear, @Param("tagList") List<String> tagList);

    @Select("select tag from repos r join repos_tags rt on r.id = rt.repo_id where id = #{id};")
    List<String> queryTagsById(Integer id);

    int selectTotalCount(@Param("column") String column, @Param("sort") Integer sort, @Param("id") Integer id, @Param("title") String title, @Param("watchCount") Integer watchCount, @Param("forkCount") Integer forkCount, @Param("createYear") String createYear, @Param("tagList") List<String> tagList);

    @Select("""
            select tag as topic, totalCount
            from (select tag, count(*) totalCount
                  from repos r
                           join repos_tags rt on r.id = rt.repo_id
                  group by tag) t
            order by totalCount desc
            limit 10 offset 1;
            """)
    List<TopicCountVO> selectTenTopics();

    @Select("""
            select lastYearCount
            from (select tag, count(*) lastYearCount
                  from repos r
                           join repos_tags rt on r.id = rt.repo_id
                  where substr(updated_time, 1, 7) > '2022-01'
                  group by tag) t
            where tag = #{topic};
                        """)
    int selectLastYearCount(String topic);

    @Select("""
            select lastYearCount
            from (select tag, count(*) lastYearCount
                  from repos r
                           join repos_tags rt on r.id = rt.repo_id
                  where substr(updated_time, 1, 7) > '2022-04'
                  group by tag) t
            where tag = #{topic};
                        """)
    int selectLastMonthCount(String topic);

    @Select("select count(*) from repos where forks >= #{lowFork} and forks < #{highFork}")
    int selectReposNum(int lowFork, int highFork);

    @Select("""
            select year as name, count(*) as value
            from (select substr(created_time, 1, 4) as year
                  from repos) t
            group by year;
            """)
    List<PieChartVO> getPieChart();
}
