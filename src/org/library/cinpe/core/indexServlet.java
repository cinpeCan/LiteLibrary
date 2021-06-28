package org.library.cinpe.core;

import org.library.cinpe.core.Request;
import org.library.cinpe.core.Response;
import org.library.cinpe.core.Servlet;
import org.library.cinpe.core.parserXml;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author 黄灿
 */
public class indexServlet implements Servlet {

    /**
     * <html>
     * <head>
     * <title>登录图书馆0 0</title>
     * <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
     * <style type="text/css">
     * .divForm {
     * text-align: center;
     * }
     *
     * </style>
     * </head>
     * <body>
     *
     * <h1>#1 学而时习之，不亦悦乎！----------by 孔子</h1>
     * <h2>#2 人的精神有三种境界：骆驼、狮子和婴儿。第一境界骆驼，忍辱负重，被动地听命于别人或命运的安排；第二境界狮子，把被动变成主动，由“你应该”到“我要”，一切由我主动争取，主动负起人生责任；第三境界婴儿 ，这是一种
     * “我是”的状态，活在当下，享受现在的一切。
     * ----------by 采尼</h2>
     * <h2>#3 真的男人，敢于追求星辰大海，敢于骚扰女同事!----------by 鲁迅</h2>
     * <h6>#4 鲁迅:我没说过这话,不过我觉得楼上说得对!----------by 鲁迅</h6>
     * <h6>#5 3楼66666----------by 陀思妥耶夫斯基</h6>
     * <h6>#6 LZSB----------by 托尔斯泰</h6>
     * <h6>#7 LSSB----------by 孔子</h6>
     * <div class="divForm">
     *     <form method="get" action="http://localhost:10160/index.html" enctype="multipart/form-data" accept-charset="UTF-8">
     *         用户名:<input type="text" name="uname" id="uname"/>
     *         书名:<input type="text" name="bookname" id="bookname"/>
     * <p>
     *         我要:<select name="todo" id="_select" typeof="text">
     *         <option value="1">借书</option>
     *         <option value="2">还书</option>
     *     </select>
     *
     *         <input type="submit" value="提交"/>
     *     </form>
     *     <table  typeof="text"  border="1" align="center">
     * 		<tr>
     * 			<td>书名</td>
     * 			<td>所有者</td>
     * 			<td>状态</td>
     * 			<td>借阅者</td>
     * 			<td>上次操作时间</td>
     * 		</tr>
     * 		<tr>
     * 			<td>钢铁是怎样炼成的</td>
     * 			<td>爱因斯坦</td>
     * 			<td>借出</td>
     * 			<td>阿不思邓布利多</td>
     * 			<td>2019-8-14</td>
     * 		</tr>
     *     </table>
     *
     * </div>
     * </body>
     * </html>
     *
     * @param request
     * @param response
     */
    @Override
    public void service(Request request, Response response) {
        LinkedHashMap<String, Map<String, String>> map = parserXml.getInstance().getMap();
        String tip=parserXml.getInstance().getTip();

        response.println("<html>");
        response.println("<head>");
        response.println("<title>登录图书馆0 0</title>");
        response.println("<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">");
        response.println("    <style type=\"text/css\">        .divForm {");
        response.println("        text-align: center;");
        response.println("    }    </style>");
        response.println("</head>");
        response.println("<body>");
        response.println("<h1>#1 学而时习之，不亦悦乎！----------by 孔子</h1>");
        response.println("<h2>#2 人的精神有三种境界：骆驼、狮子和婴儿。\n" +
                "\t第一境界骆驼，忍辱负重，被动地听命于别人或命运的安排；\n" +
                "\t第二境界狮子，把被动变成主动，由“你应该”到“我要”，一切由我主动争取，主动负起人生责任；\n" +
                "\t第三境界婴儿 ，这是一种    “我是”的状态，活在当下，享受现在的一切。\n" +
                "\t----------by 采尼</h2>");
        response.println("<h2>#3 真的男人，敢于追求星辰大海，敢于骚扰女同事!----------by 鲁迅</h2>");
        response.println("<h6>#4 鲁迅:我没说过这话,不过我觉得言之有理!----------by 鲁迅</h6>");
        response.println("<h6>#5 3楼66666----------by 陀思妥耶夫斯基</h6>");
        response.println("<h6>#6 LZSB----------by 托尔斯泰</h6>");
        response.println("<h6>#7 LSSB----------by 孔子</h6>");
        response.println("<div class=\"divForm\">");
        response.println("    <form method=\"get\" action=\"http://localhost:10160/index.html\" enctype=\"multipart/form-data\" accept-charset=\"UTF-8\">");
        response.println("        用户名:<input type=\"text\" name=\"uName\" id=\"uName\"/>");
        response.println("        书名:<input type=\"text\" name=\"bName\" id=\"bName\"/>");
        response.println("        我要:<select name=\"todo\" id=\"todo\" typeof=\"text\">");
        response.println("        <option value=\"0\">借书</option>");
        response.println("        <option value=\"1\" style=\"color: blueviolet\">还书</option>");
        response.println("        <option value=\"2\">添加书籍</option>");
        response.println("        <option value=\"3\" style=\"color:red\">移除书籍</option>");
        response.println("    </select>");
        response.println("        <input type=\"submit\" value=\"提交\"/>");
        response.println("    </form>");
        response.println("    <table typeof=\"text\" border=\"1\" align=\"center\">");
        response.println("        <tr><Font color=\"blue\">"+tip+"</Font></tr>");
        response.println("        <tr>");
        response.println("            <td>书名</td>");
        response.println("            <td>所有者</td>");
        response.println("            <td>状态</td>");
        response.println("            <td>借阅者</td>");
        response.println("            <td>上次操作时间</td>");
        response.println("        </tr>");

        for (Map.Entry<String,Map<String,String>> entry:map.entrySet()
        ) {
            if(entry.getValue().get("state").equals("0")){
                response.println("        <tr bgcolor=\"#00fa9a\">");
            }else{
                response.println("        <tr>");
            }
			response.println("            <td><font color=\"#8a2be2\" onclick=\"document.getElementById('bName').value = this.innerHTML\">"+entry.getValue().get("bName")+"</font></td>");
			response.println("            <td><font color=\"#6495ed\" onclick=\"document.getElementById('uName').value = this.innerHTML\">"+entry.getValue().get("ower")+"</font></td>");
			if(entry.getValue().get("state").equals("0")){
                response.println("            <td>可借</td>");
            }else {
                response.println("            <td>借出</td>");
            }
            response.println("            <td><font color=\"#8a2be2\" onclick=\"document.getElementById('uName').value = this.innerHTML\">"+entry.getValue().get("get")+"</font></td>");
			response.println("            <td>"+entry.getValue().get("time")+"</td>");
			response.println("        </tr>");
		}


        response.println("    </table>");
        response.println("</div>");
        response.println("</body>");
        response.println("</html>");

    }


}
