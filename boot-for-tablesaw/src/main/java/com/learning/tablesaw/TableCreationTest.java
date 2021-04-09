/**
 * Author:   claire
 * Date:    2021-04-09 - 20:50
 * Description: 表格制作
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-09 - 20:50          V1.17.0          表格制作
 */
package com.learning.tablesaw;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

/**
 * 功能简述 
 * 〈表格制作〉
 *
 * @author claire
 * @date 2021-04-09 - 20:50
 * @since 1.1.0
 */
public class TableCreationTest {

    public static void main(String[] args){
        String[] students = {"小明", "李雷", "小二"};
        double[] scores = {90.1, 84.3, 99.7};
        Table table = Table.create("学生分数统计表").addColumns(
                StringColumn.create("姓名", students),
                DoubleColumn.create("分数", scores));
        System.out.println(table.print());
    }
}
