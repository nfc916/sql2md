package org.lanqiao.util;

import java.util.*;
import java.io.*;

public class SqlParseUtil {
     /**
     * 获取所有的建表语句
     * @return
     */
    public static List<String> doParseSQL(String filePath) {
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
