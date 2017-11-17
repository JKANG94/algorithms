package zzzTest.excelLoader.models.FDA;

import zzzTest.excelLoader.excelReader.ExcelReader;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ShfdaIncreasedDataConvert {
    public static void main(String[] args) {

        int count = 0;
        for (int i = 1; i <= 4; i++) {
            File excelFile = new File("/Users/nibnait/Desktop/shiyaojian/"+i+".xlsx");
            ExcelReader excelReader = new ExcelReader(excelFile);
//            List<shfda_increaded_data> modelList = excelReader.loadSheet();
//            int cnt = make(modelList);
//            System.out.println(cnt);
//            count += cnt;
        }
        System.out.println(count);
    }

    private static int make(List<shfda_increaded_data> modelList) {

        Field[] fields = null;
        try {
            Class clz = Class.forName(shfda_increaded_data.class.getName());
            fields = clz.getDeclaredFields();
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuilder baseStr = new StringBuilder("INSERT INTO shfda_increaded_data (");
        for (int i = 0; i < fields.length; i++) {
            if (i == fields.length - 1) {
                baseStr.append(fields[i].getName());
                baseStr.append(") VALUES ");
                continue;
            }
            baseStr.append(fields[i].getName());
            baseStr.append(", ");
        }

        StringBuilder sb = new StringBuilder();
        int length = modelList.size();
        int count = 0;
        for (shfda_increaded_data model : modelList) {
            if (count == length - 1) {
                sb = reflectLastTime(model, sb);
                break;
            }
            sb = reflect(model, sb);
            count++;
        }
        baseStr.append(sb);

        try {
            //创建一个新文件
            String insertSQLFileName = "新增数据";
            Path filePath = Paths.get("/Users/nibnait/Desktop/"+insertSQLFileName+".txt");
            byte[] bytes = null;
            if (Files.exists(filePath)) {
                bytes = Files.readAllBytes(filePath);
            } else {
                Files.createFile(filePath);
            }

            if (bytes != null) {
                String oldString = new String(bytes);
                baseStr.append(oldString);
            }
            //写操作
            Files.write(filePath, baseStr.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  modelList.size();
    }

    private static StringBuilder reflectLastTime(Object obj, StringBuilder sb) {
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            sb.append("(");
            for (int i = 0; i < fields.length; i++) {

                sb.append("\'" + (fields[i].get(obj)!=null?fields[i].get(obj).toString():"")+ "\'");
                if (i != fields.length - 1) {
                    sb.append(", ");
                } else {
                    sb.append(");");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    private static StringBuilder reflect(Object obj, StringBuilder sb) {
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            sb.append("(");
            for (int i = 0; i < fields.length; i++) {

                sb.append("\'" + (fields[i].get(obj)!=null?fields[i].get(obj).toString():"")+ "\'");
                if (i != fields.length - 1) {
                    sb.append(", ");
                } else {
                    sb.append("),");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }
}
