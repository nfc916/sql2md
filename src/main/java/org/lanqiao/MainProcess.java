package org.lanqiao;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
         main2(args);
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

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static List<String> doParseSQL(String filePath) {
        List<String> sqls =new ArrayList<>();
        return sqls;
    }
    public static void main2(String[] args) {
        System.out.println("请输入文件路径，例如：E:\\a.sql");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String path = "E:\\ceims.sql";  //文件路径
        File file = new File(input);
        StringBuffer sbf = new StringBuffer();
        BufferedReader reader = null;
        StringBuffer table_name_buff = new StringBuffer();
        String tableString;
        int tablenum = 0;
        FileWriter fileWriter = null;




        //把sql文件按照空行分段，每一段存到map1中，num表示有多少段
        int num = 0;
        Map<String, String> sql_section_map = new HashMap<String,String>();

        Map<String, String> table_section_map = new HashMap<String,String>();    //存储表片段

        Map<String, String> table_name_map = new HashMap<String,String>();    //存储表名
        Map<String, String> table_description_map = new HashMap<String,String>();	//存储表描述
        Map<String, String> row_name_map = new HashMap<String,String>();	//存储表列名
        Map<String, String> map_row_type = new HashMap<String,String>();	//存储列类型
        Map<String, String> map_row_description = new HashMap<String,String>();	//存储列描述



        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;

            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr+"\n");
                if(tempStr.equals("")){
                    sql_section_map.put("duan"+num, sbf.toString());
                    sbf.setLength(0);
                    num++;
                }
            }
            reader.close();

            String aaString = sbf.toString();		//读取sql文件后转为字符串

            String table_name_regex = "CREATE TABLE `(\\S*)`";   //匹配表名
            String table_description_regex = "COMMENT='([\u4e00-\u9fa5_a-zA-Z0-9\\S]*)';";   //匹配表描述
            String regex_row_description = "COMMENT '([\u4e00-\u9fa5_a-zA-Z0-9\\S]*)',";	//匹配列描述
            String table_row_name_regex = "  `(\\S*)`";   //匹配字段
            String regex_type = "  `(\\S*)` (\\S*)";   //匹配字段类型

            // 创建 Pattern 对象
            Pattern table_name_Pattern = Pattern.compile(table_name_regex);
            Pattern table_description_Pattern = Pattern.compile(table_description_regex);
            Pattern table_row_name_Pattern = Pattern.compile(table_row_name_regex);
            Pattern pattern_row_type = Pattern.compile(regex_type);
            Pattern pattern_row_description = Pattern.compile(regex_row_description);
            // 现在创建 matcher 对象

            for (int i = 0; i < num; i++) {
                tableString = sql_section_map.get("duan"+i);
                Matcher m1 = table_name_Pattern.matcher(tableString);

                if(m1.find()){
                    int a = m1.start(1);
                    //m1.find(a);  //find(int start)  //有参函数表示  重新设置该匹配器，然后尝试从指定的索引开始找，start：索引。
                    table_name_buff.append(m1.group(1)+",");
                    table_section_map.put("table"+tablenum, sql_section_map.get("duan"+i));
                    tablenum++;
                }else{
                    sql_section_map.remove("duan"+i);
                }
            }


            /**************************************段处理完成，下面根据获取的表的数量进行解析************************************************/

            String [] table_name_array = table_name_buff.toString().split(","); //表名
            for (int i = 0; i < table_name_array.length; i++) {
                table_name_map.put("table"+i, table_name_array[i]);
            }


            /**
             * 每一行分开，匹配列名
             */
            String aa = "";
            String bb = "";
            String cc = "";
            for (int i = 0; i < table_section_map.size(); i++) {
                String[] aStrings = table_section_map.get("table"+i).split("\n");
                Matcher m2 = table_description_Pattern.matcher(table_section_map.get("table"+i));
                if (m2.find()) {
                    table_description_map.put("table"+i, m2.group(1));
                    //System.out.println("table"+i+":"+table_description_map.get("table"+i));
                }
                for (int j = 0; j < aStrings.length; j++) {
                    Matcher m3 = table_row_name_Pattern.matcher(aStrings[j]);
                    Matcher m4 = pattern_row_type.matcher(aStrings[j]);
                    Matcher m5 = pattern_row_description.matcher(aStrings[j]);
                    //System.out.println(m3.groupCount());
                    while(m3.find()){
                        aa +=m3.group(1)+",";
                    }
                    while(m4.find()){
                        bb +=m4.group(2)+",";
                        if(m5.find()){
                            cc +=m5.group(1)+",";
                        }else {
                            cc += "null"+",";
                        }
                    }

                }
                row_name_map.put("table"+i, aa);
                map_row_type.put("table"+i, bb);
                map_row_description.put("table"+i, cc);
                aa = "";
                bb = "";
                cc = "";
            }

            //组织输出md文件内容
            String text = "# 数据库文档\n\n";
            text += "## 数据表列表\n\n";
            String table_desstr = "";   //表描述
            for (int i = 0; i < table_section_map.size(); i++) {
                table_desstr = table_description_map.get("table"+i);
                String tablenamestr = table_name_map.get("table"+i);
                if (table_desstr != null) {
                    text += "## 表名："+tablenamestr+"("+table_description_map.get("table"+i)+")\n\n";
                }else {
                    text += "## 表名："+tablenamestr+"\n\n";
                }

                //text += "## 表名："+tablenamestr+"("+table_description_map.get("table"+i)+")\n\n";
                text += "字段名|数据类型|字段描述\n";
                text += ":---:|:---:|:---:\n";
                String[] rownames = row_name_map.get("table"+i).split(",");
                String[] rowtypes = map_row_type.get("table"+i).split(",");
                String[] rowdes = map_row_description.get("table"+i).split(",");
                try {
                    for (int j = 0; j < rownames.length; j++) {
                        if(rowdes[j].equals("null")){
                            rowdes[j]="";
                        }
                        text += rownames[j]+"|"+rowtypes[j]+"|"+rowdes[j]+"\n";
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("字段类型与字段描述匹配出错，请查看匹配规则与sql文件内容是否相符，\n测试过程中发现，自动导出的sql文件有可能出现字段名字后出现两个空格，匹配规则为1个空格");
                }
                //System.out.println(text);
            }
            System.out.println("表描述"+table_description_map);

            //文件输出
            fileWriter =  new FileWriter("E:\\a.md",false);
            fileWriter.write(text);
            fileWriter.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
