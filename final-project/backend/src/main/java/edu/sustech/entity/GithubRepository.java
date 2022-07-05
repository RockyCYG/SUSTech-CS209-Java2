package edu.sustech.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("repos")
public class GithubRepository {

    @TableId
    private Integer id;

    private String title;

    private Integer watchers;

    private Integer forks;

    private String createdTime;

    private List<String> tags;

}
