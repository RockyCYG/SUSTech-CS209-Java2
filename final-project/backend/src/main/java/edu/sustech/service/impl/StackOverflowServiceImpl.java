package edu.sustech.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.entity.StackOverflowQuestion;
import edu.sustech.mapper.StackOverflowQuestionMapper;
import edu.sustech.service.StackOverflowService;
import edu.sustech.vo.TagVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class StackOverflowServiceImpl extends ServiceImpl<StackOverflowQuestionMapper, StackOverflowQuestion> implements StackOverflowService {

    @Resource
    private StackOverflowQuestionMapper mapper;

    public List<String> queryTagsById(Integer id) {
        return mapper.queryTagsById(id);
    }

    @Override
    public List<StackOverflowQuestion> queryQuestionsByConditions(Integer pageNum, Integer pageSize, String column, Integer sort, Integer id, String title, Integer score, Integer viewCount, Integer createYear, List<String> tagList) {
        int begin = (pageNum - 1) * pageSize;
        return mapper.selectQuestionsByConditions(begin, pageSize, column, sort, id, title, score, viewCount, createYear, tagList);
    }

    @Override
    public int selectTotalCount(String column, Integer sort, Integer id, String title, Integer score, Integer viewCount, Integer createYear, List<String> tagList) {
        return mapper.selectTotalCount(column, sort, id, title, score, viewCount, createYear, tagList);
    }

    @Override
    public List<TagVO> selectAllTags() {
        return mapper.selectAllTags();
    }

    @Override
    public Map<String, Object> selectTagsAndViewCount() {
        return mapper.selectTagsAndViewCount();
    }

    @Override
    public List<Map<Object, Object>> selectTagsViewSum(String tagName) {
        return mapper.selectTagsViewSum(tagName);
    }

    @Override
    public List<String> selectTagsOrderByViewCount() {
        return mapper.selectTagsOrderByViewCount();
    }

    @Override
    public List<Map<Object, Object>> selectTagsNum(String tagName) {
        return mapper.selectTagsNum(tagName);
    }

}
