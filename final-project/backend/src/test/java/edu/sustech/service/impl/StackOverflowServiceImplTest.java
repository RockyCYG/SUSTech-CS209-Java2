package edu.sustech.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.sustech.entity.StackOverflowQuestion;
import edu.sustech.service.StackOverflowService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class StackOverflowServiceImplTest {

    @Resource
    private StackOverflowService stackOverflowService;

    @Test
    void select() {
        List<StackOverflowQuestion> list = stackOverflowService.list();
        System.out.println(list);
    }

    @Test
    void selectPage() {
        IPage<StackOverflowQuestion> page = new Page<>(2, 10);
        stackOverflowService.page(page);
        System.out.println(page.getRecords());
    }

    @Test
    void selectTags() {
        List<String> tags = stackOverflowService.queryTagsById(10333995);
        System.out.println(tags);
    }

}