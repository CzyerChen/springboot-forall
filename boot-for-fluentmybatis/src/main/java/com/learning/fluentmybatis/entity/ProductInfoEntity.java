package com.learning.fluentmybatis.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * ProductInfoEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@FluentMybatis(
    table = "product_info",
    schema = "public",
    dbType = DbType.POSTGRE_SQL
)
public class ProductInfoEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  /**
   */
  @TableId("id")
  private Long id;

  /**
   * 协议
   */
  @TableField("agreement")
  private String agreement;

  /**
   * 是否开启异步下单 0 否 1 是
   */
  @TableField("async_order_flag")
  private Short asyncOrderFlag;

  /**
   * 是否自动选号
   */
  @TableField("auto_phone_num_flag")
  private Short autoPhoneNumFlag;

  /**
   * 背景颜色
   */
  @TableField("background_color")
  private String backgroundColor;

  /**
   * 简介
   */
  @TableField("brief_introduction")
  private String briefIntroduction;

  /**
   * 业务名称-关联操作类
   */
  @TableField("business_name")
  private String businessName;

  /**
   * 按钮
   */
  @TableField("button")
  private String button;

  /**
   * 是否开启校验运营商身份证下单天数
   */
  @TableField("check_operator_cert_flag")
  private Short checkOperatorCertFlag;

  /**
   * 是否有触点
   */
  @TableField("contacts_flag")
  private Short contactsFlag;

  /**
   * 详细说明
   */
  @TableField("content")
  private String content;

  /**
   * 创建人
   */
  @TableField("create_by")
  private String createBy;

  /**
   */
  @TableField("create_ip")
  private String createIp;

  /**
   * 创建时间
   */
  @TableField("create_time")
  private Date createTime;

  /**
   * 产品描述
   */
  @TableField("description")
  private String description;

  /**
   * 产品展示名称
   */
  @TableField("display_name")
  private String displayName;

  /**
   * 热词 188 520 等
   */
  @TableField("hot_key")
  private String hotKey;

  /**
   * 是否删除 0 不删除 1 删除
   */
  @TableField("is_del")
  private Short isDel;

  /**
   * 产品名称
   */
  @TableField("name")
  private String name;

  /**
   * 操作类
   */
  @TableField("operate_class")
  private String operateClass;

  /**
   * 运营商  电信0  移动1  联通2 
   */
  @TableField("operator")
  private Short operator;

  /**
   * 原价
   */
  @TableField("original_price")
  private String originalPrice;

  /**
   * 页面链接
   */
  @TableField("page_url")
  private String pageUrl;

  /**
   * 是否开启号码池 0 否 1 是
   */
  @TableField("phone_pool_flag")
  private Short phonePoolFlag;

  /**
   */
  @TableField("product_json")
  private String productJson;

  /**
   * 发展人编码
   */
  @TableField("referrer_code")
  private String referrerCode;

  /**
   * 发展人名称
   */
  @TableField("referrer_name")
  private String referrerName;

  /**
   * 备注
   */
  @TableField("remark")
  private String remark;

  /**
   * 是否有搜索 0 否 1 是
   */
  @TableField("search_flag")
  private Short searchFlag;

  /**
   * 选号样式  1 2 3
   */
  @TableField("select_style")
  private Short selectStyle;

  /**
   * 是否发短信
   */
  @TableField("sms_flag")
  private Short smsFlag;

  /**
   * 特别说明
   */
  @TableField("special_remarks")
  private String specialRemarks;

  /**
   * 状态
   */
  @TableField("status")
  private Short status;

  /**
   * 有效期结束时间
   */
  @TableField("status_end_time")
  private Date statusEndTime;

  /**
   * 有效期开始时间
   */
  @TableField("status_start_time")
  private Date statusStartTime;

  /**
   * 促充状态 
   */
  @TableField("tocharge_state_code")
  private String tochargeStateCode;

  /**
   * 修改人
   */
  @TableField("update_by")
  private String updateBy;

  /**
   */
  @TableField("update_ip")
  private String updateIp;

  /**
   * 修改时间
   */
  @TableField("update_time")
  private Date updateTime;

  /**
   * 上游附加产品id1
   */
  @TableField("upstream_additional_product_id")
  private String upstreamAdditionalProductId;

  /**
   * 上游附加产品id2
   */
  @TableField("upstream_additional_product_id2")
  private String upstreamAdditionalProductId2;

  /**
   * 上游附加产品名称1
   */
  @TableField("upstream_additional_product_name")
  private String upstreamAdditionalProductName;

  /**
   * 上游附加产品名称2
   */
  @TableField("upstream_additional_product_name2")
  private String upstreamAdditionalProductName2;

  /**
   * 上游主产品id
   */
  @TableField("upstream_base_product_id")
  private String upstreamBaseProductId;

  /**
   * 上游主产品名称
   */
  @TableField("upstream_base_product_name")
  private String upstreamBaseProductName;

  /**
   * 上游商品id
   */
  @TableField("upstream_good_id")
  private String upstreamGoodId;

  /**
   * 上游商品名称
   */
  @TableField("upstream_good_name")
  private String upstreamGoodName;

  /**
   * 上游信息
   */
  @TableField("upstream_info")
  private String upstreamInfo;

  @Override
  public final Class entityClass() {
    return ProductInfoEntity.class;
  }
}
