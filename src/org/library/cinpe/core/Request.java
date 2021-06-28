package org.library.cinpe.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.*;

/**
 * 封装请求协议: 封装请求参数为Map
 *
 * @author 黄灿
 */
public class Request {
    //协议信息
    private String requestInfo;
    //请求方式
    private String method;
    //请求url
    private String url;
    //请求参数
    private String queryStr;
    //存储参数
    parserXml libMaps;

    //返回参数
    Boolean isOK = false;


    private final String CRLF = "\r\n";

    public Request(Socket client) throws IOException {
        this(client.getInputStream());
    }

    public Request(InputStream is) {
        byte[] datas = new byte[1024 * 1024];
        int len;
        try {
            len = is.read(datas);
            System.out.println("len的长度是:" + len);

            if(len!=-1){
            this.requestInfo = new String(datas, 0, len, "utf-8");
            }
//            is.close();
            if(this.requestInfo==null){
                return;
            }
            System.out.println("最根源的返回数据是:" + this.requestInfo);

        } catch (IOException e) {
            e.printStackTrace();
            try {
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return;
        }
        //分解字符串
        parseRequestInfo();
    }

    /**
     * 不会正则,只好切割.真麻烦啊.
     */
    private void parseRequestInfo() {
        System.out.println("------分解-------");
        System.out.println("---1.获取请求方式: 开头到第一个/------");
        this.method = this.requestInfo.substring(0, this.requestInfo.indexOf("/")).toLowerCase();
        this.method = this.method.trim();
        System.out.println("---2、获取请求url: 第一个/ 到 HTTP/------");
        System.out.println("---可能包含请求参数? 前面的为url------");
        //1.获取/的位置
        int startIdx = this.requestInfo.indexOf("/") + 1;
        //2.获取 HTTP/的位置
        int endIdx = this.requestInfo.indexOf("HTTP/");
        //3.分割字符串
        this.url = this.requestInfo.substring(startIdx, endIdx).trim();
        //4.获取？的位置
        int queryIdx = this.url.indexOf("?");
        if (queryIdx >= 0) {//存在GET参数
            String[] urlArray = this.url.split("\\?");
            this.url = urlArray[0];
            queryStr = urlArray[1];
        }
        System.out.println("url:" + this.url);
        System.out.println("GET获取到了请求参数:" + this.queryStr);

        System.out.println("------3.获取请求参数:如果Get已经获取,如果是post可能在请求体中------");

        if (method.equals("post")) {
            String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
            System.out.println(qStr + "-->");
            if (null == queryStr) {
                queryStr = qStr;
            } else {
                queryStr += ("&" + qStr);
            }
        }
        queryStr = null == queryStr ? "" : queryStr;

        if (queryStr == null) {
            if (queryStr == "") {
                //如果是空的,可能是另外一种表单数据

            }
        }

        System.out.println(method + "-->" + url + "-->" + queryStr);
        //转成Map fav=1&fav=2&uname=shsxt&age=18&others=
        convertMap();
    }

    //处理成Map
    private void convertMap() {
        //1.分割字符串 &
        String[] keyValues = this.queryStr.split("&");
        //搬运用map
        Map<String, String> tempMap = new HashMap<>();
        //获取图书存储器对象
        libMaps = parserXml.getInstance();

        for (String queryStr : keyValues) {
            //2.再次分割字符串  =
            System.out.println("请求的字符串是:" + queryStr);

            String[] kv = queryStr.split("=");
            kv = Arrays.copyOf(kv, 2);
            //获取key和value
            String key = kv[0];
            String value = kv[1] == null ? null : decode(kv[1], "utf-8");
            System.out.println("读取到的结果,key:" + key + ",value:" + value);
            /**
             * 存储到map中
             * 传进来是:
             * uName=奥巴马
             * bName=钢铁是怎样炼成的
             * todo=1(0~3,的text类型,0借,1还,2增加,3去除)
             */

            tempMap.put(key, value);

        }


        if (tempMap.get("todo") != null) {
            isOK = searchLib(tempMap);
            //写入硬盘
            libMaps.write();
        }


        //查看解析结果
        System.out.println("解析结果:");
        System.out.println("url:" + this.url);


    }

    /**
     * 处理中文
     *
     * @return
     */
    private String decode(String value, String enc) {
        try {
            return java.net.URLDecoder.decode(value, enc);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getQueryStr() {
        return queryStr;
    }

    /**
     * 查询lib
     *
     * @return 是否可以借, 可以还, 可以新增, 可以删除
     */
    private boolean searchLib(Map<String, String> temp) {


        System.out.println("显示tempMap的值:" + temp);
        if (temp.get("todo") == null) {
            return true;
        }
        switch (temp.get("todo")) {
            case "0":
                /**
                 * 借出
                 *      * <借出 name="钢铁是怎样炼成的">借出者</借出>      outKey=new LinkedHashMap<>()
                 *      * <时间 name="借出者">应归还时间</借出>          timeKey = new LinkedHashMap<>()
                 *      * <书库 name="毛主席语录">所有者</归还>         libKey = new LinkedHashMap<>()
                 */
                if (libMaps.getMap().get(temp.get("bName").trim())!=null) {
                    //如果书库里有这本书
                    Map<String, String> map = libMaps.getMap().get(temp.get("bName"));
                    //1.如果这本书状态为可借
                    if (map.get("state").equals("0")) {
                        //借出者设置

                        map.put("get", temp.get("uName"));

                        //状态设为借出
                        map.put("state", "1");

                        //时间设置为当前
                        map.put("time", new Date().toString());
                        if (map.get("ower").trim().equals(temp.get("uName").trim())) {
                            libMaps.setTip("这是你自己的书...也行吧...借阅成功!");
                        }else {
                            libMaps.setTip("借阅<<" + temp.get("bName") + ">>成功!");
                        }

                        return true;
                    } else {
                        libMaps.setTip("这本书已被借出了");
                    }


                } else {
                    libMaps.setTip("书库中没有这本书哦.");
                }

                return false;
            case "1":
                /**
                 * 还书
                 *      * <借出 name="钢铁是怎样炼成的">借出者</借出>      outKey=new LinkedHashMap<>()
                 *      * <时间 name="借出者">应归还时间</借出>          timeKey = new LinkedHashMap<>()
                 *      * <书库 name="毛主席语录">所有者</归还>         libKey = new LinkedHashMap<>()
                 */
                if (libMaps.getMap().get(temp.get("bName").trim())!=null) {
                    //如果书库里有这本书
                    Map<String, String> map = libMaps.getMap().get(temp.get("bName"));
                    //1.如果这本书状态为借出
                    if (map.get("state").equals("1")) {
                        //2.如果这本书的借出者是操作者
                        if (map.get("get").trim().equals(temp.get("uName").trim())) {
                            //状态设为借出
                            map.put("state", "0");
                            map.put("get", "");

                            //时间设置为当前
                            map.put("time", new Date().toString());
                            libMaps.setTip("这本书归还成功!");
                            return true;
                        } else {
                            libMaps.setTip("这本书不是你借出的哦.");
                        }

                    } else {
                        libMaps.setTip("这本书已经放在书架上了.");
                    }


                } else {
                    libMaps.setTip("这本书不包含在书库里哦.");
                }
                return false;

            case "2":
                /**
                 * 增加
                 *      * <借出 name="钢铁是怎样炼成的">借出者</借出>      outKey=new LinkedHashMap<>()
                 *      * <时间 name="借出者">应归还时间</借出>          timeKey = new LinkedHashMap<>()
                 *      * <书库 name="毛主席语录">所有者</归还>         libKey = new LinkedHashMap<>()
                 */
                if (libMaps.getMap().get(temp.get("bName")) == null) {
                    //如果书库里没有这本书

                    //1.生成这本书的信息
                    Map<String, String> book = new HashMap<>();
                    book.put("bName", temp.get("bName"));
                    book.put("uName", temp.get("uName"));
                    book.put("ower", temp.get("uName"));
                    book.put("state", "0");
                    book.put("get", "");
                    book.put("time", new Date().toString());

                    libMaps.getMap().put(temp.get("bName"), book);

                    libMaps.setTip("书籍<<" + temp.get("bName") + ">>已添加到书库!");

                    return true;

                } else {
                    libMaps.setTip("书库里已经有同名书籍了哦.");
                }
                return false;

            case "3":
                /**
                 * 减少
                 *      * <借出 name="钢铁是怎样炼成的">借出者</借出>      outKey=new LinkedHashMap<>()
                 *      * <时间 name="借出者">应归还时间</借出>          timeKey = new LinkedHashMap<>()
                 *      * <书库 name="毛主席语录">所有者</归还>         libKey = new LinkedHashMap<>()
                 */
                if ((libMaps.getMap().get(temp.get("bName")) != null)) {
                    //如果书库里有这本书
                    Map<String, String> map = libMaps.getMap().get(temp.get("bName"));

                    //如果是这本书的作者进行操作
                    if (map.get("ower").equals(temp.get("uName"))) {
                        libMaps.getMap().remove(temp.get("bName"));
                        libMaps.setTip("书籍<<" + temp.get("bName") + ">>删除成功!");
                        return true;
                    } else {
                        libMaps.setTip("请由书籍所有者进行操作.");
                    }

                } else {
                    libMaps.setTip("书库里并没有这本书哦.");
                }
                return false;
            default:
                return false;

        }
    }
}
