/**
 * Author:   claire
 * Date:    2021-04-09 - 20:51
 * Description: csv数据读取测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-09 - 20:51          V1.17.0          csv数据读取测试
 */
package com.learning.tablesaw;

import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.NumberColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.numbers.NumberColumnFormatter;

import java.io.IOException;

/**
 * 功能简述 
 * 〈csv数据读取测试〉
 *
 * @author claire
 * @date 2021-04-09 - 20:51
 * @since 1.1.0
 */
public class CsvDataReadTest {
    public static void main(String[] args) throws IOException {
        Table table = Table.read().csv("~/springboot-forall/boot-for-tablesaw/src/main/resources/data/people.csv");
        Table whoPercents = table.xTabPercents("who");
        whoPercents.columnsOfType(ColumnType.DOUBLE)
                .forEach(x -> ((NumberColumn) x).setPrintFormatter(
                        NumberColumnFormatter.percent(0)));
        System.out.println(whoPercents.toString());
    }
}
