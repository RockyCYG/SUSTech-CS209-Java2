package edu.sustech.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.sustech.entity.StackOverflowQuestion;
import edu.sustech.vo.TagVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface StackOverflowQuestionMapper extends BaseMapper<StackOverflowQuestion> {

    @Select("select tag from questions q left join question_tags qt on q.id = qt.question_id where id = #{id};")
    List<String> queryTagsById(Integer id);

    List<StackOverflowQuestion> selectQuestionsByConditions(@Param("begin") Integer begin, @Param("size") Integer size, @Param("column") String column, @Param("sort") Integer sort, @Param("id") Integer id, @Param("title") String title, @Param("score") Integer score, @Param("viewCount") Integer viewCount, @Param("createYear") Integer createYear, @Param("tagList") List<String> tagList);

    int selectTotalCount(@Param("column") String column, @Param("sort") Integer sort, @Param("id") Integer id, @Param("title") String title, @Param("score") Integer score, @Param("viewCount") Integer viewCount, @Param("createYear") Integer createYear, @Param("tagList") List<String> tagList);

    List<TagVO> selectAllTags();

    @MapKey("tag")
    Map<String, Object> selectTagsAndViewCount();

    @MapKey("create_year")
    List<Map<Object, Object>> selectTagsViewSum(@Param("tagName") String tagName);

    @MapKey("create_year")
    List<Map<Object, Object>> selectTagsNum(@Param("tagName") String tagName);

    List<String> selectTagsOrderByViewCount();


}
