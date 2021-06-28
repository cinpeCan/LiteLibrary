package org.library.cinpe.core;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * @author cinpeCan
 * @date 2019/8/1.
 * @description 解析Xml文件
 */
public class parserXml {

    /**
     * xml保存的路径
     */
    private final String xmlPath = "libDates.xml";

    private SAXParserFactory factory;
    private SAXParser parser;
    private String pathOut;
    private String[] pathIn;

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    //显示的提示
    private String tip="欢迎查阅图书角";
    /**
     * 最后输出的总Map
     */
    private LinkedHashMap<String, Map<String, String>> map;

    /**
     * @return 书籍列表
     * 返回参数:JSON
     * {
     * {
     * "bName":"魔道祖师"
     * "ower":"黄灿"
     * "state"1"借出
     * "get":"小白"
     * "time":"xx-xx-xx"(借出时间)
     * }
     * {
     * "bookName":"高等数学"
     * "ower":"嘤嘤怪"
     * "state":"0"可借
     * "get":"小黑"(上一次借书的人)
     * "time":"xx-xx-xx"(上一次归还时间)
     * }
     * ….
     * }
     */
    private Map<String, Object> bookInf = new LinkedHashMap<>();

    public LinkedHashMap<String, Map<String, String>> getMap() {
        return this.map;
    }


    /**
     * 所有操作记录在这
     */
    private LinkedHashSet<String> allDo = new LinkedHashSet<>();

    /**
     * 所有的操作记录
     */
    public void addAllDo(String doing) {
        this.allDo.add(doing);
    }


    /**
     * 单例,构造器
     */
    private static parserXml xml;
    ;

    private parserXml() {
        if (map == null) {
            map=new LinkedHashMap<>();
            read();
        }

    }

    public static parserXml getInstance() {

        if (xml == null) {
            xml = new parserXml();
        }
        return xml;
    }


    /**
     * 写出
     */

    public void write()  {

        String jsonStr = JSON.toJSONString(map);

        System.out.println("输出的JSON:"+jsonStr);


        try {
            writeFile(jsonStr, xmlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 输出到文件
     */
    private void writeFile(String src, String path) throws Exception {
        File file = new File(path);
        if (file.exists()) {

        } else {
            file.createNewFile();
        }

        FileOutputStream out = new FileOutputStream(path);
        byte[] bytes = src.getBytes();
        //写起来.
        out.write(bytes);
        //刷新,关闭流
        out.flush();
        out.close();
    }

    /**
     *
     */
    private void read(){
        try {
            this.map=readFile(xmlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取
     */
    private LinkedHashMap readFile(String path) throws Exception {

        File file = new File(path);

        if (file.exists()) {

        } else {
            file.createNewFile();
        }

        FileInputStream in = new FileInputStream(path);
        byte[] bytes = new byte[(int) file.length() + 10];
        //读起来.
        in.read(bytes);
        String jsonStr = new String(bytes, 0, bytes.length, "utf-8").trim();
        //关闭流
        in.close();


        System.out.println("输入的JSON:"+jsonStr);

        //转化为有序map
        LinkedHashMap<String, Map<String, String>> dummyMap = JSON.parseObject(jsonStr, LinkedHashMap.class, Feature.OrderedField);
        return dummyMap;
    }

}

