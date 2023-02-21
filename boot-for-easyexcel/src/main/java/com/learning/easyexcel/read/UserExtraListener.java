/**
 * Author:   claire
 * Date:    2023/2/20 - 2:30 下午
 * Description: 用户额外信息监听器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/2/20 - 2:30 下午          V1.0.0          用户额外信息监听器
 */
package com.learning.easyexcel.read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson2.JSON;
import com.learning.easyexcel.common.UserExtra;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能简述 
 * 〈用户额外信息监听器〉
 *
 * @author claire
 * @date 2023/2/20 - 2:30 下午
 * @since 1.0.0
 */
@Slf4j
public class UserExtraListener implements ReadListener<UserExtra> {
    @Override
    public void invoke(UserExtra data, AnalysisContext context) {}

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {}

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        log.info("读取到了一条额外信息:{}", JSON.toJSONString(extra));
        switch (extra.getType()) {
            case COMMENT:
                log.info("额外信息是批注,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(), extra.getColumnIndex(),
                        extra.getText());
                break;
            case HYPERLINK:
                if ("Sheet1!A1".equals(extra.getText())) {
                    log.info("额外信息是超链接,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(),
                            extra.getColumnIndex(), extra.getText());
                } else if ("Sheet2!A1".equals(extra.getText())) {
                    log.info(
                            "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{},"
                                    + "内容是:{}",
                            extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
                            extra.getLastColumnIndex(), extra.getText());
                } else {
//                    Assert.fail("Unknown hyperlink!");
                }
                break;
            case MERGE:
                log.info(
                        "额外信息是合并单元格,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{}",
                        extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
                        extra.getLastColumnIndex());
                break;
            default:
        }
    }
}
