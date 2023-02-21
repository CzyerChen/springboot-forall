/**
 * Author:   claire
 * Date:    2023/2/20 - 1:14 下午
 * Description: 用户数据监听器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/2/20 - 1:14 下午          V1.0.0          用户数据监听器
 */
package com.learning.easyexcel.read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.learning.easyexcel.common.User;
import com.learning.easyexcel.common.UserDao;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 功能简述 
 * 〈用户数据监听器〉
 *
 * @author claire
 * @date 2023/2/20 - 1:14 下午
 * @since 1.0.0
 */
@Slf4j
public class UserDataListener implements ReadListener<User> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<User> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private UserDao userDao;

    public UserDataListener() {
        // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
        userDao = new UserDao();
    }

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param userDao
     */
    public UserDataListener(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(User data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        userDao.save(cachedDataList);
        log.info("存储数据库成功！");
    }
}
