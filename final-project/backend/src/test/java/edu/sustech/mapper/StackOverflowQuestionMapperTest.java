package edu.sustech.mapper;

import edu.sustech.entity.StackOverflowQuestion;
import edu.sustech.vo.TagVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.*;

@SpringBootTest
class StackOverflowQuestionMapperTest {

    @Resource
    private StackOverflowQuestionMapper mapper;

    @Test
    void testSelectByPage() {
        List<String> tagList = new ArrayList<>();
        tagList.add("tomcat");
        List<StackOverflowQuestion> questionList = mapper.selectQuestionsByConditions(0, 10, "id", 1, null, "", 0, 0, null, tagList);
        System.out.println(questionList);
    }

    @Test
    void testSelectAllTags() {
        List<TagVO> tagVOList = mapper.selectAllTags();
        System.out.println(tagVOList);
    }

/*    @Test
    void selectTagsAndViewCount() {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        System.out.println(map);
    }

    @Test
    void selectTagsViewSum() {
        List<Map<Object, Object>> mapList = mapper.selectTagsViewSum();
        for (Map<Object, Object> map : mapList) {
            Set<Map.Entry<Object, Object>> entrySet = map.entrySet();
//            System.out.println(entrySet);
            for (Map.Entry<Object, Object> entry : entrySet) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                System.out.println(entry.getValue());
//                Long key = entry.getKey();
//                System.out.println(entry.getKey());
//                System.out.println(entry.getValue());
//                System.out.println("-------------------");
            }
        }
    }*/

}