/**
 * Author:   claire
 * Date:    2021-04-06 - 15:20
 * Description: excel导出控制器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-04-06 - 15:20          V1.17.0          excel导出控制器
 */
package com.learning.easypoi.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import com.learning.easypoi.entity.MarketingDetailsExcel;
import com.learning.easypoi.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 功能简述
 * 〈excel导出控制器〉
 *
 * @author claire
 * @date 2021-04-06 - 15:20
 * @since 1.1.0
 */
@RestController
@RequestMapping("excel")
public class ExcelController {

    @GetMapping
    public void exportData(HttpServletRequest request, HttpServletResponse response) {
        MarketingDetailsExcel overview = new MarketingDetailsExcel();
        overview.setSentDate(new Date());
        overview.setProductName("测试产品A");
        overview.setOperatorName("联通");
        overview.setActualSent(1);
        overview.setActualSuccess(1);
        overview.setActualSuccessRate(1.0);
        overview.setClickCount(1);
        overview.setClickRate(1.0);
        overview.setHtmlClickCount(1);
        overview.setHtmlConversionRate(1.0);
        overview.setActivatedCount(1);
        overview.setRegisteredCount(1);
        overview.setExpectedRevenue(65);
        overview.setProfit(55);
        overview.setMarketingCosts(10);
        overview.setPaidAmount(1);
        overview.setPaymentCount(55);
        overview.setRegisteredRate(1.0);
        overview.setCostToRevenueRatio(0.846);
        ArrayList<MarketingDetailsExcel> record = new ArrayList<MarketingDetailsExcel>();
        record.add(overview);

//        List<MarketingDetailsExcel> record = Collections.singletonList(overview);

        Workbook workbook = null;
        try {
            String sheetName = "营销详表-24小时";

            ExportParams params = new ExportParams(null, null, sheetName);
            List<ExcelExportEntity> excelParams = new ArrayList<ExcelExportEntity>();
            // 得到所有字段
            Field[] fileds = PoiPublicUtil.getClassFields(MarketingDetailsExcel.class);
            ExcelTarget target = MarketingDetailsExcel.class.getAnnotation(ExcelTarget.class);
            String targetId = target == null ? null : target.value();
            ExcelUtils.getAllExcelField(params.getExclusions(), targetId, fileds, excelParams, MarketingDetailsExcel.class,
                    null, null);
            workbook = ExcelExportUtil.exportExcel(params, excelParams, record);
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workbook = null;
        }
    }
}
