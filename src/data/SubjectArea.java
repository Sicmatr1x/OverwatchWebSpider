/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import io.Writer;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 论坛主题区帖子列表
 * @author sicmatr1x
 */
public class SubjectArea implements Serializable, Runnable{
    private static final long serialVersionUID = 1L;
    /**
     * 序列化对象存储根目录
     */
    public transient static String fileSavePath = null;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    //--------------------------------------------------------------------------
    /**
     * 待爬取主网页 信息发布专区;http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=38
     * 综合讨论区:http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=62
     * 汉化及配音讨论区:http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=63
     * 战友招募区:http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=65
     *
     * 客户服务区:http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=61
     * 技术支持区:http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=55
     * BUG反馈区：http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=58
     * http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=58&page=1
     */
    private String webAddress = "";
    /**
     * 扫描日期
     */
    private Date scanDate;
    /**
     * 该板块名称
     */
    private String Title;
    /**
     * 获取到的网页
     */
    private transient Document Doc = null;
    /**
     * 主题区下的所有帖子
     */
    private final ArrayList<Post> postsList = new ArrayList<>();
    //--------------------------------------------------------------------------
    /**
     * 等待服务器响应html超时时间
     */
    public static int _getHtmlOverTime = 30 * 1000;
    /**
     * 获取帖子列表的下一页间隔时间
     */
    public static int _nextPageGetInterval = 500;
    //--------------------------------------------------------------------------
    public SubjectArea() {

    }

    public SubjectArea(String webAddress) {
        this();
        this.webAddress = webAddress + "&page=";
    }
    //--------------------------------------------------------------------------
    /**
     * 设置该主题区的名称
     */
    private void getSubject(){
        Elements forumName = this.Doc.getElementsByClass("forumName");
        this.Title = forumName.get(0).text();
        //System.out.println(forumName.get(0).text());
    }

    /**
     * 从address获取html解析成为Document以供Jsoup使用
     *
     * @param address 获取html文件网址
     * @throws IOException
     */
    private Document getHtmlDoc() throws IOException {
        //System.out.println("getHtmlDoc():" + address);
        return Jsoup.connect(this.getWebAddress()).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0").timeout(SubjectArea._getHtmlOverTime).get();
    }
    
    /**
     * 从address获取html解析成为Document以供Jsoup使用
     *
     * @param address 获取html文件网址
     * @throws IOException
     */
    private Document getHtmlDoc(String webAddress) throws IOException {
        //System.out.println("getHtmlDoc():" + address);
        return Jsoup.connect(webAddress).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0").timeout(SubjectArea._getHtmlOverTime).get();
    }

    //--------------------------------------------------------------------------
    /**
     * 获取帖子总数
     *
     * @return 帖子总数
     */
    private int getPostsNum() {
        Elements index = this.Doc.getElementsByClass("pg");
        Elements last = index.get(0).getElementsByClass("last");
        String t = last.html();
        int beg = t.indexOf(" ");
        String num = t.substring(beg + 1, t.length());
        return Integer.parseInt(num);
    }

    /**
     * 获取帖子的网址
     * 例如：http://bbs.ow.blizzard.cn/forum.php?mod=viewthread&tid=582290&extra=page%3D1
     *
     * @return 帖子的网址
     */
    private String getPostAddress(Element e) {
        Elements icn = e.getElementsByClass("topic-icon-wrapper");
        String t = icn.toString();
        int beg = t.indexOf("tid=");
        int end = t.indexOf("&", beg);
        //System.out.println(t);
        return "http://bbs.ow.blizzard.cn/forum.php?mod=viewthread&tid=" + t.substring(beg + "tid=".length(), end) + "&extra=page%3D1";
    }

    /**
     * 获取帖子的标题 例如：【公告】针对《守望先锋》近期外挂行为的回应
     *
     * @param e
     * @return 标题
     */
    private String getPostTitle(Element e) {
        Elements th = e.getElementsByTag("th");
        Elements sxst = th.get(0).getElementsByAttribute("href");
        return sxst.get(1).html();
    }

    /**
     * 获取帖子的帖子的作者(不含#编号),是否为暴雪员工创建
     *
     * @param e
     * @return 作者暴雪昵称(不含#编号)
     */
    private String getPostUP(Element e, Post p) {
        Elements frm = e.getElementsByClass("frm");
        //System.out.println(frm);
        Elements span = null;
        span = frm.get(0).getElementsByClass("admincol");
        if (span.size() < 1) { // 为用户创建
            span = frm.get(0).getElementsByClass("user");
        } else { // 为暴雪员工创建
            p.setIsCustomerServiceEstablish(true);
        }
        //System.out.println(p.isIsCustomerServiceEstablish()  + ":"+ span);
        return span.html();
    }

    /**
     * 获取帖子的回复数与查看数
     *
     * @param e
     * @param p
     * @return
     */
    private void getPostReplyNum(Element e, Post p) {
        Elements num = e.getElementsByClass("num");
        String t = num.get(0).text();
        if(t.equals("-")){
            t = "0";
        }
        int replyNum = Integer.parseInt(t);
        t = num.get(0).text();
        if(t.equals("-")){
            t = "0";
        }
        int viewNum = Integer.parseInt(t);
        p.setReplyNum(replyNum);
        p.setViewNum(viewNum);
//        System.out.println("replyTemp=" + replyNum);
//        System.out.println("viewNum=" + viewNum);
    }

    /**
     * 获取帖子的最后回复ID,是否为暴雪员工最后回复，与最后回复时间
     *
     * @param e
     * @return
     */
    private String getLastReply(Element e, Post p) throws ParseException {
        Elements by = e.getElementsByClass("by");
        Elements cite = by.get(0).getElementsByTag("cite");
        Elements span = null;
        span = cite.get(0).getElementsByClass("admincol");
        if (span.size() < 1) { // 为用户最后回复
            span = cite.get(0).getElementsByClass("user");
        } else { // 为暴雪员工最后回复
            p.setIsCustomerServiceReply(true);
        }
        // 最后回复时间
        Elements em = by.get(0).getElementsByTag("em");
        Elements span1 = em.get(0).getElementsByTag("span");
        String dateString = null;
        if (span1.size() < 1) { // <em>2016-6-2 10:36</em>
            String t = em.toString();
            int beg = t.indexOf("<em>");
            int end = t.indexOf("</em>", beg);
            //System.out.println("span1" + t);
            dateString = t.substring(beg + "<em>".length(), end);
            //System.out.println("dateString=" + dateString);
        } else { // <em><span title="2016-7-19 19:40">7&nbsp;天前</span></em>
            String t = span1.toString();
            int beg = t.indexOf("title=\"");
            int end = t.indexOf("\">", beg);
            //System.out.println("em=" + t);
            dateString = t.substring(beg + "title=\"".length(), end);
            //System.out.println("dateString=" + dateString);
        }

        // 时间字符串转date对象
        //SimpleDateFormat 取得本地系统的时区（我的时区为GMT+8北京），然后按照pattern（"yyyy-MM-dd HH:mm"）格式化date，此时输出的就是GMT+8区的时间了。如果想支持国际化时间，则先指定时区，然后再格式化date数据。
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = sdf.parse(dateString);
        p.setLastReplyDate(date);
        //System.out.println("date=" + date);

        return span.html();
    }
    //--------------------------------------------------------------------------
    /**
     * 扫描单页
     * @param pageAddress 单页网址
     * @throws IOException 无法获取html文件
     * @throws ParseException 获取最后回复时间错误
     */
    private void scanPage(String pageAddress) throws IOException, ParseException {
        // 获取页面
        this.Doc = this.getHtmlDoc(pageAddress);
        //System.out.println(doc.html());
        // 获取该页面上的帖子总数
        Element form = this.Doc.getElementById("moderate");
        Element table = form.getElementById("threadlisttableid");
        Elements list = table.getElementsByTag("tbody");
        System.out.print("，共" + list.size() + "个元素");
        //System.out.println(table);
        // 开始扫描
        //System.out.println("list.size()=" + list.size());
        for (int i = 0; i < /*1*/ list.size(); i++) { // 扫描每个帖子的信息
            //System.out.println(list.get(i).html());
            // 使用帖子网址创建帖子对象
            Post p = new Post(this.getPostAddress(list.get(i)));
            //System.out.println("Address=" + p.getWebAddress());
            // 获取帖子的标题
            p.setTitle(this.getPostTitle(list.get(i)));
            //System.out.println("Title=" + this.getPostTitle(list.get(i)));
            // 获取帖子的帖子的作者(不含#编号)
            p.setUP(this.getPostUP(list.get(i), p));
            //System.out.println("UP=" + this.getPostUP(list.get(i), p));
            // 获取帖子的回复数与查看数
            this.getPostReplyNum(list.get(i), p);
            //System.out.println("回复数:" + p.getReplyNum() + " 浏览数:" + p.getViewNum());
            // 获取帖子的最后回复ID与时间
            p.setLastReplyID(this.getLastReply(list.get(i), p));
            //System.out.println("最后回复:" + this.getLastReply(list.get(i), p) + " " + p.getLastReplyDate());
            this.postsList.add(p);
            //System.out.println("=============================================================");
        }
    }

    /**
     * 扫描该区的所有帖子
     * @return 扫描到的帖子数
     */
    private int scanAllPage() {
        try {
            this.Doc = this.getHtmlDoc();
        } catch (IOException ex) {
            System.out.println("获取第一页失败");
            Logger.getLogger(SubjectArea.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        // 获取该板块名称
        this.getSubject();
        int postsNum = this.getPostsNum();
        int i = 0;
        for (i = 1; i <= postsNum; i++) {
            try {
                // 扫描页面
                System.out.print("[" + this.Title + "]" + "开始扫描第" + i + "页");
                //System.out.println(this.getWebAddress() + i);
                this.scanPage(this.getWebAddress() + i);
                System.out.println("[完成]");
                Thread.sleep(SubjectArea._nextPageGetInterval);
            } catch (IOException ex) {
                Logger.getLogger(SubjectArea.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                System.out.println("获取最后回复时间错误");
                Logger.getLogger(SubjectArea.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                System.out.println("scanAllPage()线程挂起失败");
                Logger.getLogger(SubjectArea.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return i;
    }
    
    //--------------------------------------------------------------------------
    
    @Override
    public void run() {
    	if(SubjectArea.fileSavePath == null || SubjectArea.fileSavePath.length() ==0){
    		System.out.println("未设置文件保存路径，该线程已结束");
    		return;
    	}
        this.scanDate = new Date();
        // 扫描该区的所有帖子
        System.out.println("run()" + "开始扫描:" + this.getWebAddress());
        this.scanAllPage();
        System.out.println("run()" + "扫描结束，共扫描到" + this.getPostsList().size() + "个结果");
        // 保存结果到对象
        File f = new File(SubjectArea.fileSavePath + this.Title + ".dir");
        try {
            System.out.println("开始写入文件");
            Writer.out(f, this);
            System.out.println("文件写入成功！" + SubjectArea.fileSavePath + this.Title + ".dir");
        } catch (IOException ex) {
            System.out.println("run():" + "对象序列化错误");
            Logger.getLogger(SubjectArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //--------------------------------------------------------------------------
    public static void main(String[] args) {
        SubjectArea sp = new SubjectArea("http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=58");
        try {
            sp.scanPage("http://bbs.ow.blizzard.cn/forum.php?mod=forumdisplay&fid=58&page=554");
            
            
        } catch (IOException ex) {
            Logger.getLogger(SubjectArea.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            System.out.println("获取最后回复时间错误");
            Logger.getLogger(SubjectArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

    //--------------------------------------------------------------------------

    /**
     * @return the webAddress
     */
    public String getWebAddress() {
        return webAddress;
    }

    /**
     * @param webAddress the webAddress to set
     */
    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    /**
     * @return the scanDate
     */
    public Date getScanDate() {
        return scanDate;
    }

    /**
     * @return the Title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * @return the postsList
     */
    public ArrayList<Post> getPostsList() {
        return postsList;
    }
    
}
