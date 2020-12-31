/**
 * Author:   claire
 * Date:    2020-12-30 - 15:49
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-30 - 15:49          V1.13.0
 */
package com.es.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName = "modeldata")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountModelData {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String taskId;
    /**
     * host
     */
    @Field(type = FieldType.Text)
    private String host;
    /**
     * 频繁项
     */
    @Field(type = FieldType.Text)
    private String hosts;
    @Field(type = FieldType.Text)
    private String label;
    /**
     * 人群1人数
     */
    @Field(type = FieldType.Long)
    private Long posCountNum;
    /**
     * 人群2人数
     */
    @Field(type = FieldType.Long)
    private Long negCountNum;
    /**
     * 频繁项长度
     */
    @Field(type = FieldType.Integer)
    private Integer hostsLength;
    /**
     * 正样本访问频次
     */
    @Field(type = FieldType.Integer)
    private Integer posFreq;
    /**
     * 对比样本访问频次
     */
    @Field(type = FieldType.Integer)
    private Integer negFreq;
    /**
     * 预估大盘人数
     */
    @Field(type = FieldType.Long)
    private Long totalCountNum = 0L;
    /**
     * tgi
     */
    @Field(type = FieldType.Long)
    private Long tgi;

    @Field(type = FieldType.Long)
    private Long cumulativedUserCount;
}

