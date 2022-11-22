package com.javaweb.canteen.common;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

/**
 * 打印 Excel 工具类
 */
public class MyExcelUtils {

    // 打印
    public static void printExcel(HttpServletResponse response, ArrayList<Map<String, Object>> data) {
        ServletOutputStream out = null;

        // 通过工具类创建writer，默认创建xls格式
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(data, true);
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=test.xls");
            // out为OutputStream，需要写出到的目标流
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭writer，释放内存
            if (out != null) {
                // 关闭输出Servlet流
                IoUtil.close(out);
            }
        }
    }

}
