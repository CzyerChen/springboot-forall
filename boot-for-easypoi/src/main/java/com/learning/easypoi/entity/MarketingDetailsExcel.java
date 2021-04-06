/**
 * Author:   claire
 * Date:    2021-04-06 - 13:33
 * Description: 营销详表Excel对象
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-06 - 13:33          V1.17.0          营销详表Excel对象
 */
package com.learning.easypoi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import java.util.Date;

/**
 * 功能简述 
 * 〈营销详表Excel对象〉
 *
 * @author claire
 * @date 2021-04-06 - 13:33
 * @since 1.1.0
 */
public class MarketingDetailsExcel {
    @Excel(isWrap = false,name = "发送日期",orderNum = "1",width = 20,format = "yyyy-MM-dd HH:mm")
    private Date sentDate;
    @Excel(isWrap = false,name = "产品名称",orderNum = "2",width = 20)
    private String productName;
    @Excel(isWrap = false,name = "运营商",orderNum = "3",width = 10)
    private String operatorName;
    @Excel(isWrap = false,name = "$1实际发送量",orderNum = "5",width = 15)
    private Integer actualSent;
    @Excel(isWrap = false,name = "$1发送成功数",orderNum = "6",width = 15)
    private Integer actualSuccess;
    private Double actualSuccessRate;
    @Excel(isWrap = false,name = "$1发送成功率",orderNum = "7",width = 15)
    private String actualSuccessRateStr;
    @Excel(isWrap = false,name = "$1短信点击量",orderNum = "8",width = 15)
    private Integer clickCount;
    private Double clickRate;
    @Excel(isWrap = false,name = "$1短信点击率",orderNum = "9",width = 15)
    private String clickRateStr;
    @Excel(isWrap = false,name = "H5下载点击数",orderNum = "10",width = 15)
    private Integer htmlClickCount;
    private Double htmlConversionRate;
    @Excel(isWrap = false,name = "H5下载转化率",orderNum = "11",width = 15)
    private String htmlConversionRateStr;
    @Excel(isWrap = false,name = "$1激活数",orderNum = "12",width = 15)
    private Integer  activatedCount;
    @Excel(isWrap = false,name = "$1注册数",orderNum = "13",width = 15)
    private Integer registeredCount;
    private Double registeredRate;
    @Excel(isWrap = false,name = "$1注册率",orderNum = "14",width = 15)
    private String registeredRateStr;
    @Excel(isWrap = false,name = "$1收入",orderNum = "15",width = 15)
    private Integer expectedRevenue;
    @Excel(isWrap = false,name = "$1成本",orderNum = "16",width = 15)
    private Integer marketingCosts;
    @Excel(isWrap = false,name = "$1毛利",orderNum = "17",width = 15)
    private Integer profit;
    private Double costToRevenueRatio;
    @Excel(isWrap = false,name = "$1成本占收比",orderNum = "18",width = 15)
    private String costToRevenueRatioStr;
    @Excel(isWrap = false,name = "$1消费数",orderNum = "19",width = 15)
    private Integer paymentCount;
    @Excel(isWrap = false,name = "$1消费金额",orderNum = "20",width = 15)
    private Integer paidAmount;

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getActualSent() {
        return actualSent;
    }

    public void setActualSent(Integer actualSent) {
        this.actualSent = actualSent;
    }

    public Integer getActualSuccess() {
        return actualSuccess;
    }

    public void setActualSuccess(Integer actualSuccess) {
        this.actualSuccess = actualSuccess;
    }

    public Double getActualSuccessRate() {
        return actualSuccessRate;
    }

    public void setActualSuccessRate(Double actualSuccessRate) {
        this.actualSuccessRate = actualSuccessRate;
    }

    public String getActualSuccessRateStr() {
        return actualSuccessRateStr;
    }

    public void setActualSuccessRateStr(String actualSuccessRateStr) {
        this.actualSuccessRateStr = actualSuccessRateStr;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Double getClickRate() {
        return clickRate;
    }

    public void setClickRate(Double clickRate) {
        this.clickRate = clickRate;
    }

    public String getClickRateStr() {
        return clickRateStr;
    }

    public void setClickRateStr(String clickRateStr) {
        this.clickRateStr = clickRateStr;
    }

    public Integer getHtmlClickCount() {
        return htmlClickCount;
    }

    public void setHtmlClickCount(Integer htmlClickCount) {
        this.htmlClickCount = htmlClickCount;
    }

    public Double getHtmlConversionRate() {
        return htmlConversionRate;
    }

    public void setHtmlConversionRate(Double htmlConversionRate) {
        this.htmlConversionRate = htmlConversionRate;
    }

    public String getHtmlConversionRateStr() {
        return htmlConversionRateStr;
    }

    public void setHtmlConversionRateStr(String htmlConversionRateStr) {
        this.htmlConversionRateStr = htmlConversionRateStr;
    }

    public Integer getActivatedCount() {
        return activatedCount;
    }

    public void setActivatedCount(Integer activatedCount) {
        this.activatedCount = activatedCount;
    }

    public Integer getRegisteredCount() {
        return registeredCount;
    }

    public void setRegisteredCount(Integer registeredCount) {
        this.registeredCount = registeredCount;
    }

    public Double getRegisteredRate() {
        return registeredRate;
    }

    public void setRegisteredRate(Double registeredRate) {
        this.registeredRate = registeredRate;
    }

    public String getRegisteredRateStr() {
        return registeredRateStr;
    }

    public void setRegisteredRateStr(String registeredRateStr) {
        this.registeredRateStr = registeredRateStr;
    }

    public Integer getExpectedRevenue() {
        return expectedRevenue;
    }

    public void setExpectedRevenue(Integer expectedRevenue) {
        this.expectedRevenue = expectedRevenue;
    }

    public Integer getMarketingCosts() {
        return marketingCosts;
    }

    public void setMarketingCosts(Integer marketingCosts) {
        this.marketingCosts = marketingCosts;
    }

    public Integer getProfit() {
        return profit;
    }

    public void setProfit(Integer profit) {
        this.profit = profit;
    }

    public Double getCostToRevenueRatio() {
        return costToRevenueRatio;
    }

    public void setCostToRevenueRatio(Double costToRevenueRatio) {
        this.costToRevenueRatio = costToRevenueRatio;
    }

    public String getCostToRevenueRatioStr() {
        return costToRevenueRatioStr;
    }

    public void setCostToRevenueRatioStr(String costToRevenueRatioStr) {
        this.costToRevenueRatioStr = costToRevenueRatioStr;
    }

    public Integer getPaymentCount() {
        return paymentCount;
    }

    public void setPaymentCount(Integer paymentCount) {
        this.paymentCount = paymentCount;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }
}
