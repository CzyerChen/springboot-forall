/**
 * Author:   claire
 * Date:    2021-02-09 - 18:12
 * Description: office操作测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 18:12          V1.17.0          office操作测试
 */
package com.learning.office;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.word.Word07Writer;

import java.io.IOException;

/**
 * 功能简述 
 * 〈office操作测试〉
 *
 * @author claire
 * @date 2021-02-09 - 18:12
 */
public class HCoreOfficeTest {
    public static void main(String[] args) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file("test.xlsx"));
        ExcelReader reader2 = ExcelUtil.getReader("d:/aaa.xlsx");
        //ExcelUtil.read03BySax("aaa.xls", 1, createRowHandler());
        //ExcelUtil.read07BySax("aaa.xlsx", 0, createRowHandler());
        ExcelWriter writer = ExcelUtil.getWriter("d:/writeTest.xlsx");
        BigExcelWriter writer2= ExcelUtil.getBigWriter("e:/xxx.xlsx");
        Word07Writer writer3 = new Word07Writer();

    }
}
