package edu.sustech.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForkCountVO {

    private String title;

    private Integer forkCount;

    private String time;

}
