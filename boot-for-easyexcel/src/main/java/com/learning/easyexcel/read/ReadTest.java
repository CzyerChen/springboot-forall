/**
 * Author:   claire
 * Date:    2023/2/20 - 11:40 上午
 * Description: 读取测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/2/20 - 11:40 上午          V1.0.0          读取测试
 */
package com.learning.easyexcel.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.converters.DefaultConverterLoader;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.learning.easyexcel.common.CellDataUser;
import com.learning.easyexcel.common.User;
import com.learning.easyexcel.common.UserExtra;
import com.learning.easyexcel.common.UserFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;

/**
 * 功能简述
 * 〈读取测试〉
 *
 * @author claire
 * @date 2023/2/20 - 11:40 上午
 * @since 1.0.0
 */
@Slf4j
public class ReadTest {

    /**
     * 最简单的读
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link User}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link }
     * <p>
     * 3. 直接读即可
     */
    public void simpleRead1() {
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1
        String fileName = "demo.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
        // 具体需要返回多少行可以在`PageReadListener`的构造函数设置
        EasyExcel.read(fileName, User.class, new PageReadListener<User>(dataList -> {
            for (User user : dataList) {
                log.info("读取到一条数据{}", JSON.toJSONString(user));
            }
        })).sheet().doRead();
    }

    public void simpleRead2() {
        // 写法2：
        // 匿名内部类 不用额外写一个DemoDataListener
        String fileName = "demo.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, User.class, new ReadListener<User>() {

            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 100;
            /**
             * 临时存储
             */
            private List<User> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(User data, AnalysisContext context) {
                cachedDataList.add(data);
                if (cachedDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 存储完成清理 list
                    cachedDataList = Lists.newArrayList();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                saveData();
            }

            /**
             * 加上存储数据库
             */
            private void saveData() {
                log.info("{}条数据，开始存储数据库！", cachedDataList.size());
                log.info("存储数据库成功！");
            }
        }).sheet().doRead();
    }

    public void simpleRead3() {

        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法3：
        String fileName = "demo.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, User.class, new UserDataListener()).sheet().doRead();
    }

    public void simpleRead4() {
        // 写法4
        String fileName = "demo.xlsx";
        // 一个文件一个reader
        try (ExcelReader excelReader = EasyExcel.read(fileName, User.class, new UserDataListener()).build()) {
            // 构建一个sheet 这里可以指定名字或者no
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            // 读取一个sheet
            excelReader.read(readSheet);
        }
    }

    /**
     * 指定列的下标或者列名
     *
     * <p>
     * 1. 创建excel对应的实体对象,并使用{@link ExcelProperty}注解. 参照{@link }
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link }
     * <p>
     * 3. 直接读即可
     */
    public void indexOrNameRead() {
        String fileName ="demo.xlsx";
        // 这里默认读取第一个sheet
        EasyExcel.read(fileName, User.class, new UserWithAnnoDataListener()).sheet().doRead();
    }

    /**
     * 读多个或者全部sheet,这里注意一个sheet不能读取多次，多次读取需要重新读取文件
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link User}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UserDataListener}
     * <p>
     * 3. 直接读即可
     */
    public void multiSheetRead(){
        String fileName = "demo.xlsx";
        // 读取全部sheet
        // 这里需要注意 DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个DemoDataListener里面写
        EasyExcel.read(fileName, User.class, new UserDataListener()).doReadAll();

        // 读取部分sheet
        fileName = "demo.xlsx";

        // 写法1
        try (ExcelReader excelReader = EasyExcel.read(fileName).build()) {
            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
            ReadSheet readSheet1 =
                    EasyExcel.readSheet(0).head(User.class).registerReadListener(new UserDataListener()).build();
            ReadSheet readSheet2 =
                    EasyExcel.readSheet(1).head(User.class).registerReadListener(new UserDataListener()).build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet1, readSheet2);
        }
    }

    /**
     * 日期、数字或者自定义格式转换
     * <p>
     * 默认读的转换器{@link DefaultConverterLoader#loadDefaultReadConverter()}
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link UserFormat}.里面可以使用注解{@link DateTimeFormat}、{@link NumberFormat}或者自定义注解
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link }
     * <p>
     * 3. 直接读即可
     */
    public void contentFormatWithAnnoRead(){
        String fileName = "demo.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(fileName, UserFormat.class, new UserFormatDataListener())
                // 这里注意 我们也可以registerConverter来指定自定义转换器， 但是这个转换变成全局了， 所有java为string,excel为string的都会用这个转换器。
                // 如果就想单个字段使用请使用@ExcelProperty 指定converter
                // .registerConverter(new CustomStringStringConverter())
                // 读取sheet
                .sheet().doRead();
    }

    /**
     * 多行头
     *
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link User}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UserDataListener}
     * <p>
     * 3. 设置headRowNumber参数，然后读。 这里要注意headRowNumber如果不指定， 会根据你传入的class的{@link ExcelProperty#value()}里面的表头的数量来决定行数，
     * 如果不传入class则默认为1.当然你指定了headRowNumber不管是否传入class都是以你传入的为准。
     */
    public void groupHeaderRead(){
        String fileName = "demo.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(fileName, User.class, new UserDataListener()).sheet()
                // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
                .headRowNumber(3).doRead();
    }


    /**
     * 读取表头数据
     *
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link User}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UserDataListener}
     * <p>
     * 3. 直接读即可
     */
    public void headerRead() {
        String fileName ="demo.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(fileName, User.class, new UserDataListener()).sheet().doRead();
    }


    /**
     * 额外信息（批注、超链接、合并单元格信息读取）
     * <p>
     * 由于是流式读取，没法在读取到单元格数据的时候直接读取到额外信息，所以只能最后通知哪些单元格有哪些额外信息
     *
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link UserExtra}
     * <p>
     * 2. 由于默认异步读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UserExtraListener}
     * <p>
     * 3. 直接读即可
     *
     * @since 2.2.0-beat1
     */
    public void extraRead() {
        String fileName ="extra.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(fileName, UserExtra.class, new UserExtraListener())
                // 需要读取批注 默认不读取
                .extraRead(CellExtraTypeEnum.COMMENT)
                // 需要读取超链接 默认不读取
                .extraRead(CellExtraTypeEnum.HYPERLINK)
                // 需要读取合并单元格信息 默认不读取
                .extraRead(CellExtraTypeEnum.MERGE).sheet().doRead();
    }

    /**
     * 读取公式和单元格类型
     *
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link CellDataUser}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link CellDataUserHeadListener}
     * <p>
     * 3. 直接读即可
     *
     * @since 2.2.0-beat1
     */
    public void cellDataRead() {
        String fileName = "cellDataDemo.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(fileName, CellDataUser.class, new CellDataUserHeadListener()).sheet().doRead();
    }

    /**
     * 数据转换等异常处理
     *
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link }
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link }
     * <p>
     * 3. 直接读即可
     */
    public void exceptionConvertRead(){
        //遇到异常转换为有含义的数据，在listen中转换
//        @Override
//        public void onException(Exception exception, AnalysisContext context) {
//            log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
//            // 如果是某一个单元格的转换异常 能获取到具体行号
//            // 如果要获取头的信息 配合invokeHeadMap使用
//            if (exception instanceof ExcelDataConvertException) {
//                ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
//                log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
//                        excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
//            }
//        }
    }

    /**
     * 同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
     */
    public void synchronousRead() {
        String fileName ="demo.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<User> list = EasyExcel.read(fileName).head(User.class).sheet().doReadSync();
        for (User data : list) {
            log.info("读取到数据:{}", JSON.toJSONString(data));
        }

        // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
        List<Map<Integer, String>> listMap = EasyExcel.read(fileName).sheet().doReadSync();
        for (Map<Integer, String> data : listMap) {
            // 返回每条数据的键值对 表示所在的列 和所在列的值
            log.info("读取到数据:{}", JSON.toJSONString(data));
        }
    }

    /**
     * 不创建对象的读，逻辑里面入DB或入缓存，用MAP来承载数据
     */
    public void noModelRead() {
        String fileName = "demo.xlsx";
        // 这里 只要，然后读取第一个sheet 同步读取会自动finish
//        EasyExcel.read(fileName, new NoModelDataListener()).sheet().doRead();
    }

}
