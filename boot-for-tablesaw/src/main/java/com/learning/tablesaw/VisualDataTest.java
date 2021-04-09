/**
 * Author:   claire
 * Date:    2021-04-09 - 21:05
 * Description: 数据可视化测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-09 - 21:05          V1.17.0          数据可视化测试
 */
package com.learning.tablesaw;

import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.AreaPlot;

import java.io.IOException;

/**
 * 功能简述 
 * 〈数据可视化测试〉
 *
 * @author claire
 * @date 2021-04-09 - 21:05
 * @since 1.1.0
 */
public class VisualDataTest {

    public static void main(String[] args) throws IOException {
        Table robberies = Table.read().csv("~/springboot-forall/boot-for-tablesaw/src/main/resources/data/area.csv");
        Plot.show(
                AreaPlot.create(
                        "Scores by month: Jan 1966-Oct 1975",
                        robberies, "Record", "Scores"));

    }
}
