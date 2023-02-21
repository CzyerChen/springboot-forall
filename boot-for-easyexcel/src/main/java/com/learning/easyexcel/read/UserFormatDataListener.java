/**
 * Author:   claire
 * Date:    2023/2/20 - 2:17 下午
 * Description: 字段自定义监听器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/2/20 - 2:17 下午          V1.0.0          字段自定义监听器
 */
package com.learning.easyexcel.read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.learning.easyexcel.common.UserFormat;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 功能简述
 * 〈字段自定义监听器〉
 *
 * @author claire
 * @date 2023/2/20 - 2:17 下午
 * @since 1.0.0
 */
@Slf4j
public class UserFormatDataListener implements ReadListener<UserFormat> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    private List<UserFormat> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(UserFormat data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        log.info("存储数据库成功！");
    }
}
