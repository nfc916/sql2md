package org.lanqiao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    }

    private static void startBuild(String[] args) {
        if(args!=null&&args.length>0){
           //导出数据库内容成文件  数据库名，用户名，密码
            System.out.println(" 导出数据库内容成文件 ");
        }
        // 直接将文件转成 MarkDown
        //1 解析SQL语句
        buildMarkdown(doParseSQL(filePath));
        //2 将内容写成 文件

    }

    private static void buildMarkdown(List<String> sqls) {
        try {
            BufferedWriter bw =new BufferedWriter(new FileWriter(target));


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static List<String> doParseSQL(String filePath) {
        List<String> sqls =new ArrayList<>();
        return sqls;
    }

}
