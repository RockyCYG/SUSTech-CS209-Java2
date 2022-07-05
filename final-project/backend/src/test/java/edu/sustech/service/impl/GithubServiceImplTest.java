package edu.sustech.service.impl;

import edu.sustech.entity.GithubRepository;
import edu.sustech.service.GithubService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GithubServiceImplTest {

    @Resource
    private GithubService service;

    @Test
    void testSelectAll() {
        List<GithubRepository> list = service.list();
        System.out.println(list);
    }
}