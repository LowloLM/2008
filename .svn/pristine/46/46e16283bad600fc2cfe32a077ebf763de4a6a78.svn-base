package com.fh.shop.admin.controller.log;

import com.fh.shop.admin.biz.log.ILogService;
import com.fh.shop.admin.controller.BaseController;
import com.fh.shop.admin.param.LogQueryParam;
import com.fh.shop.admin.po.log.Log;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.util.DateUtil;
import com.fh.shop.util.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller
public class LogController extends BaseController {

    @Resource(name = "logService")
    private ILogService logService;

    /***
     * 跳转到列表展示页面
     * @return
     */
    @RequestMapping(value = "/log/toList" ,method = RequestMethod.GET)
    public String toList(){
        return "/log/list";
    }

    /***
     * 查询
     * @param LogQueryParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/log/findList" ,method = RequestMethod.POST)
    public DataTableResult findList(LogQueryParam LogQueryParam){
        return logService.findList(LogQueryParam);
    }

    /***
     * 导出Word
     * @param logQueryParam
     * @param response
     */
    @RequestMapping(value = "/log/exportWord",method = RequestMethod.POST)
    public void exportWord(LogQueryParam logQueryParam, HttpServletResponse response, HttpServletRequest request){
        //查询符合条件的数据
        List<Log> logList = logService.findAllList(logQueryParam);
        //将数据转换为对应格式
        Configuration configuration = new Configuration();
        //设置utf-8的编码，处理中文
        configuration.setDefaultEncoding(SystemConstant.CODE_UTF_8);
        //指明模板文件夹的位置
        configuration.setClassForTemplateLoading(this.getClass(),SystemConstant.TEMPlATE_FOLDER);
        String fileName = SystemConstant.PREFIX_WORD+UUID.randomUUID().toString()+SystemConstant.SUFFIX_WORD;
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            //获取模板
            Template template = configuration.getTemplate(SystemConstant.TEMPlATE_FILE_XML);
            //设置数据
            HashMap data = new HashMap<>();
            data.put("logs",logList);
            //将数据与模板相结合
            fileOutputStream = new FileOutputStream(fileName);
            //特别重要，要指定utf-8的编码格式，否则效果出不来
            outputStreamWriter = new OutputStreamWriter(fileOutputStream,SystemConstant.CODE_UTF_8);
            template.process(data,outputStreamWriter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            if(null != outputStreamWriter){
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != fileOutputStream){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //下载
        File file = new File(fileName);
        FileUtil.downloadFile(request,response,file);
        //删除垃圾文件
        FileUtil.deleteFile(file);
    }

    /***
     * 导出Pdf
     * @param logQueryParam
     * @param response
     */
    @RequestMapping(value = "/log/exportPdf",method = RequestMethod.POST)
    public void exportPdf(LogQueryParam logQueryParam, HttpServletResponse response){
        //查询符合条件的数据
        List<Log> logList = logService.findAllList(logQueryParam);
        //将数据转换为对应格式
        Configuration configuration = new Configuration();
        //设置utf-8的编码，处理中文
        configuration.setDefaultEncoding(SystemConstant.CODE_UTF_8);
        //指明模板文件夹的位置
        configuration.setClassForTemplateLoading(this.getClass(),SystemConstant.TEMPlATE_FOLDER);
        try {
            //获取模板
            Template template = configuration.getTemplate(SystemConstant.TEMPlATE_FILE_HTML);
            //设置数据
            Map data = new HashMap<>();
            data.put("logs",logList);
            data.put("title",SystemConstant.LOG_LIST);
            String s = DateUtil.date2str(new Date(), DateUtil.Y_M_D);
            data.put("date",s);
            //将数据与模板相结合
            StringWriter result = new StringWriter();
            template.process(data,result);
            //导出Pdf
            String htmlContent = result.toString();
            FileUtil.pdfDownloadFile(response,htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    /***
     * 导出Excel
     * @param logQueryParam
     * @param response
     */
    @RequestMapping(value = "/log/exportExcel",method = RequestMethod.POST)
    public void exportExcel(LogQueryParam logQueryParam, HttpServletResponse response){
        //查询符合条件的数据
        List<Log> logList = logService.findAllList(logQueryParam);
        //将数据转换为对应格式【workbook】
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        //创建日期样式
        XSSFCellStyle dateStyle = workbook.createCellStyle();
        short format = workbook.createDataFormat().getFormat(DateUtil.FULL_YEAR);
        dateStyle.setDataFormat(format);
        //创建标题行的样式
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleStyle.setFont(titleFont);
        int rowIndex = 0;
        int colIndex = 0;
        String[] headers = {"编号","用户名","真实姓名","操作信息","操作时间"};
        //创建大标题
        XSSFRow titleRow = sheet.createRow(rowIndex++);
        XSSFCell titleRowCell = titleRow.createCell(0);
        titleRowCell.setCellValue(SystemConstant.LOG_LIST);
        titleRowCell.setCellStyle(titleStyle);
        //设置行的高度
        titleRow.setHeightInPoints(50);
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, headers.length-1);
        sheet.addMergedRegion(cellRangeAddress);
        //创建表头
        XSSFRow headerRow = sheet.createRow(rowIndex++);
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i];
            XSSFCell headerRowCell = headerRow.createCell(i);
            headerRowCell.setCellValue(header);
            //设置列的宽度
            sheet.setColumnWidth(i,20*256);
        }
        //创建内容
        for (Log log : logList) {
            XSSFRow bodyRow = sheet.createRow(rowIndex++);
            bodyRow.createCell(colIndex++).setCellValue(log.getId());
            bodyRow.createCell(colIndex++).setCellValue(log.getUserName());
            bodyRow.createCell(colIndex++).setCellValue(log.getRealName());
            bodyRow.createCell(colIndex++).setCellValue(log.getInfo());
            XSSFCell cell = bodyRow.createCell(colIndex++);
            cell.setCellStyle(dateStyle);
            cell.setCellValue(log.getInsertTime());
            //还原
            colIndex = 0;
        }
        //下载
        FileUtil.xlsDownloadFile(response,workbook);
    }

}
