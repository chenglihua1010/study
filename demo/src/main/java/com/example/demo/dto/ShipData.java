package com.example.demo.dto;


import lombok.Data;

@Data
public class ShipData {


    /**
     * 船标记，唯一
     */
    private String shipSign;

    /**
     * 出行记录id
     */
    private String record;

    /**
     * 当前时间戳
     */
    private Long time;

    /**
     * 经度
     */
    private String n;
    /**
     * 维度
     */
    private String e;

    /**
     * 风向
     */
    private String windDirection;
    /**
     * 风俗
     */
    private Integer windSpeed;

    /**
     * 大气压力
     */
    private Integer pa;
    /**
     * 光学雨量
     */
    private Integer rain;
    /**
     * 航速
     */
    private Integer speed;


}
