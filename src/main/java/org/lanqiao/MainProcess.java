package org.lanqiao;

/**
 *
 *  1 目的 解析 SQL 文件 生成 markdown 表格
 *   前置条件： 数据库已经存在 ，通过 mysqldump 导出语句保存成 sql 文件 【绝对路径】
 *   输出结果： 保存 字段名   字段类型  字段描述
 *   要求： 尽少依赖第三方
 *
 */
public class MainProcess {

    public static void main(String[] args) {

         startBuild(args);



    }

    private static void startBuild(String[] args) {
        if(args==null||args.length==0){

        }else{
            for(String s:args){


            }
        }

    }

}
