/**
 * Author:   claire
 * Date:    2021-06-15 - 16:31
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-06-15 - 16:31          V1.0.0
 */
package com.learning.core.io;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import java.util.List;

/**
 * 功能简述
 * 〈〉
 *
 * @author claire
 * @date 2021-06-15 - 16:31
 * @since 1.0.0
 */
public class HFileTest {

    public static void main(String[] args) {
        ExcelReader excelReader = ExcelUtil.getReader("test.xlsx");
        List<List<Object>> lines = excelReader.read();
        lines.forEach(l -> {
            String line = String.valueOf(l.get(0));
            System.out.println(line);
        });

    }
}
