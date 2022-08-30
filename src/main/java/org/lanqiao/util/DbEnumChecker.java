package org.lanqiao.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class DbEnumChecker {
    public static String [] canotStartWord ;
    public static String [] fieldTypes ;
    public static String [] canotEndWord ;
    /**
     * 过滤字段名称的结尾是否合法
     * @param fieldName
     * @return
     */
    public static boolean fieldCheckerName(String fieldName) {
        boolean b =false;//默认合法结尾
        for(String str : canotEndWord){
             if(b=fieldName.trim().endsWith(str)){
                 break;
             }
         }
         return !b;
    }

    /**
     * 过滤字段是否在指定列表中
     * @param FieldType
     * @return
     */
    public static boolean fieldCheckerType(String FieldType) {
        boolean b =false;
        for(String str : fieldTypes){
             if(b=FieldType.contains(str)){
                 break;
             }
         }
         return b;
    }
    /**
     * 初次过滤非字段 类型 描述 的语句：包括 主键 外键 检测 索引等
     * @param fd
     * @return
     */
    public static boolean fieldCheckerFirst(String fd) {
       boolean b =false;
       for(String str : canotStartWord){
            if(b=fd.trim().startsWith(str)){
                break;
            }
        }
        return b;
    }

    static{
            // 读取库名  表名  字段名  全路径类名（含包名）
            Properties prop =new Properties();
            try {
                    prop.load(DbEnumChecker.class.getClassLoader().getResourceAsStream("settings.properties"));
                    canotStartWord=prop.getProperty("canotStartWord").trim().split(",");
                    fieldTypes=prop.getProperty("fieldTypes").trim().split(",");
                    canotEndWord=prop.getProperty("canotEndWord").trim().split(",");
            } catch (IOException e) {
                    System.out.println("静态加载出错，修改 settings 配置文件");
            }
    }
}
