package edu.sustech.mapper;

import edu.sustech.vo.CloudDataVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GithubMapperTest {

    @Resource
    private GithubMapper mapper;

    @Test
    void testSelectTag() {
        List<CloudDataVO> dataVOList = mapper.selectTagAndWatchCount();
        System.out.println(dataVOList);
    }
}