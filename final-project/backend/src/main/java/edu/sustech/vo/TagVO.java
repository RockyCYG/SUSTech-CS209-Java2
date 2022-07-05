package edu.sustech.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagVO {

    private String name;

    private Integer scoreCount;

    private Integer viewCount;
}
