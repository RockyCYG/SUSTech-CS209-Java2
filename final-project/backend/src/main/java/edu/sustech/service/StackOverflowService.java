package edu.sustech.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.entity.StackOverflowQuestion;
import edu.sustech.vo.TagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StackOverflowService extends IService<StackOverflowQuestion> {

    List<String> queryTagsById(Integer id);

    List<StackOverflowQuestion> queryQuestionsByConditions(Integer pageNum, Integer pageSize, String column, Integer sort, Integer id, String title, Integer score, Integer viewCount, Integer createYear, List<String> tagList);

    int selectTotalCount(String column, Integer sort, Integer id, String title, Integer score, Integer viewCount, Integer createYear, List<String> tagList);

    List<TagVO> selectAllTags();

    Map<String, Object> selectTagsAndViewCount();

    List<Map<Object, Object>> selectTagsViewSum(String tagName);

    List<String> selectTagsOrderByViewCount();

    List<Map<Object, Object>> selectTagsNum(@Param("tagName") String tagName);

}