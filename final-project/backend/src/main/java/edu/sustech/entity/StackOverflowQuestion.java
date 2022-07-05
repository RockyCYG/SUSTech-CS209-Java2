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
@TableName("questions")
public class StackOverflowQuestion {

    @TableId
    private Integer id;

    private String title;

    private Integer score;

    private Integer viewCount;

    private Integer createYear;

    private List<String> tags;

}
