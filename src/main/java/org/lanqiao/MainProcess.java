package org.lanqiao;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lanqiao.util.CommandUtil;

/**
 *
 *  1 目的 解析 SQL 文件 生成 markdown 表格
 *   前置条件： 数据库已经存在 ，通过 mysqldump 导出语句保存成 sql 文件 【绝对路径】
 *   输出结果： 保存 字段名   字段类型  字段描述
 *   要求： 尽少依赖第三方
 *
 */
public class MainProcess {
    public static String filePath ="/home/project/db.sql";
    public static String target ="/home/project/db.md";
    public static void main(String[] args) {
         startBuild(args);
        // main2(args);
    }

    private static void startBuild(String[] args) {

        if(args!=null&&args.length>=2){
            String p ="";
            if(args.length>2){
                p =   " -p"+args[2];
            }
           //导出数据库内容成文件  数据库名，用户名，[密码]
            System.out.println("导出数据库内容成文件，数据库名："+args[0]+"，用户名："+args[1]+"，密码："+p);
            try {  
                CommandUtil.invockCmdLines(new String[]{"mysqldump -u"+args[1]+p+" --opt -d "+args[0]+" > " +filePath});
            } catch (Exception e) {
              
            }
        }
        // 直接将文件转成 MarkDown
        //1 解析SQL语句
        //2 将内容写成 文件
        buildMarkdown(doParseSQL());
        System.out.println("导出 MarkDown 成功！！");

    }

    private static void buildMarkdown(List<String> sqls) {
            String tableName="";
            String tableComment="";
        try {
            BufferedWriter bw =new BufferedWriter(new FileWriter(target));
            bw.append("# 数据库的设计与创建\r\n");
            bw.append("## 数据库表设计\r\n");
            bw.append("根据需求分析，【待补充】\r\n");
            bw.append("实体间的关系如下:【换行后待补充】\r\n");
            bw.append("\r\n");
            bw.append("\r\n");
            bw.append("\r\n");
            bw.append("实体间的 ER 图如下图所示：【图待补充】\r\n");
            bw.append("\r\n");
            bw.append("根据系统的 ER 图，可以设计出【待补充】项目的数据库实体。根据以上实体，可以设计出详细的【待补充】项目的数据库表，具体如下:\r\n");
            bw.append("\r\n");
            for(String s:sqls){
                //1  获取表名 表描述 字段名 字段类型 字段描述 【主键、非空、索引、描述】
                tableName = s.substring(s.indexOf("table")+5,s.indexOf("(")).trim();//表名必然存在
                tableComment = "";//表注释不一定存在

                bw.append("\r\n**表名:"+tableName +"("+(tableComment.isEmpty()?"【待补充】":tableComment)+")**");
                bw.append("\r\n");
                bw.append("| 字段名称     | 数据类型      | 说明                                   |");
                bw.append("\r\n");
                bw.append("| ------------ | --------- | -------------------------------------- |");
                bw.append("\r\n");
                //2  获取字段名 字段类型 字段描述 【主键、非空、索引、描述】
                bw.append("\r\n");
            }
            bw.append("\r\n【可选】其中，【XX】表用来保存【XX】信息，为了更方便的实现对【XX】信息的查询，在设计【XX】表时，冗余了部分字段。\r\n");
            bw.append("\r\n【可选】同时为了更好的提升性能，表与表之前不建立物理外键，关联的逻辑外键在编码时需要注意。\r\n");
            bw.append("\r\n【可选】表中所有的 `modifyDate` 字段设置成 `timestamp` ,且根据时间戳自动更新。\r\n");
            bw.append("\r\n【可选】最终，【待补充】项目的数据库结构如图所示：\r\n");
            bw.append("\r\n");
            bw.append("[数据库素材下载地址](https://labfile.oss.aliyuncs.com/courses/课程号/名称.sql)");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> doParseSQL() {
        List<String> sqls =new ArrayList<>();
   
        // 1 获取文件中的 SQL 内容
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String lineStr;
            StringBuilder sql = new StringBuilder();
            while ((lineStr = input.readLine()) != null) {
                // 2 解决大小写问题，统一为小写
                lineStr = lineStr.toLowerCase().replace("`","");
                // 3 拼接SQL语句以createtable 开头 以 ;号结尾   排除注释和空行
                if (lineStr.trim().length() > 0 && !lineStr.startsWith("--") && !lineStr.startsWith("/*") ) {
                    sql.append(" "+lineStr);
                }
                if (sql.toString().contains(";")) {
                    // 4 获取完整以createtable 开头 以 ;号结尾的建表语句
                    if(sql.substring(0, sql.toString().indexOf(";") + 1).trim().replace(" ","").startsWith("createtable")){
                        sqls.add(sql.substring(0, sql.toString().indexOf(";") + 1).trim());
                    }
                    sql.delete(0, sql.length());
                }
            }
            input.close();
        } catch (IOException e) {
            throw new RuntimeException("请确保指定的路径下存在指定的文件 db.sql，或使用正确的用户名和密码进行导出生成");
        };
        return sqls;
    }
   
}
