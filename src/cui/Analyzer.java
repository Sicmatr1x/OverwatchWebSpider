/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cui;

import data.Post;
import data.SubjectArea;
import io.Reader;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sicmatr1x
 */
public class Analyzer {
    
    private SubjectArea sa;
    
    //--------------------------------------------------------------------------
    /**
     * 初始化读取对象
     * @param path 
     */
    public void intSA(String path){
        try {
            this.sa = Reader.in(new File(path));
        } catch (IOException ex) {
            System.out.println("intSA():" + "读取文件失败");
            Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("intSA():" + "对象序列化失败");
            Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 获取对象基本信息
     */
    public void showBasiInf(){
        System.out.println("对象序列化号serialVersionUID=" + SubjectArea.getSerialVersionUID());
        System.out.println("板块名称：" + this.sa.getTitle());
        System.out.println("板块网址：" + this.sa.getWebAddress().replaceAll("&page=", ""));
        System.out.println("扫描日期：" + this.sa.getScanDate());
        System.out.println("总共帖子数：" + this.sa.getPostsList().size());
        System.out.println("====================================================");
    }
    
    //--------------------------------------------------------------------------
    /**
     * 按回复数从大到小排序
     */
    public void sortByReplyNum(){
        for(int i =  this.sa.getPostsList().size() - 1; i > 0; --i){
            for(int j = 0; j < i; ++j){
                if(this.sa.getPostsList().get(j).getReplyNum() < this.sa.getPostsList().get(j + 1).getReplyNum()){
                    Collections.swap(this.sa.getPostsList(), j, j + 1);
                }
            }
        }
    }
    
    /**
     * 按浏览数从大到小排序
     */
    public void sortByViewNum(){
        for(int i =  this.sa.getPostsList().size() - 1; i > 0; --i){
            for(int j = 0; j < i; ++j){
                if(this.sa.getPostsList().get(j).getViewNum() < this.sa.getPostsList().get(j + 1).getViewNum()){
                    Collections.swap(this.sa.getPostsList(), j, j + 1);
                }
            }
        }
    }
    //--------------------------------------------------------------------------
    
    public void showListBasic(){
        for(int i = 0; i < this.sa.getPostsList().size(); i++){
            Post p = this.sa.getPostsList().get(i);
            System.out.println(i + p.getTitle() + " " + p.getWebAddress());
        }
    }
    
    public void showListAll(){
        for (int i = 0; i < this.sa.getPostsList().size(); i++) {
            Post p = this.sa.getPostsList().get(i);
            System.out.println("Address=" + p.getWebAddress());
            System.out.println("Title=" + p.getTitle());
            System.out.println(p.isIsCustomerServiceEstablish() + "UP=" + p.getUP());
            System.out.println("回复数:" + p.getReplyNum() + " 浏览数:" + p.getViewNum());
            System.out.println(p.isIsCustomerServiceReply() + "最后回复:" + p.getLastReplyID() + " " + p.getLastReplyDate());
            System.out.println("=============================================================");
        }
    }
    
    //--------------------------------------------------------------------------
    public static void main(String[] args) {
        Analyzer ar = new Analyzer();
        //ar.intSA("E:\\JAVA_PROGRAM\\OverWatchForumSpider\\objects\\汉化及配音讨论区.dir");
        //ar.intSA("E:\\JAVA_PROGRAM\\OverWatchForumSpider\\objects\\技术支持区.dir");
        //ar.intSA("E:\\JAVA_PROGRAM\\OverWatchForumSpider\\objects\\综合讨论区.dir");
        //ar.intSA("E:\\JAVA_PROGRAM\\OverWatchForumSpider\\objects\\客户服务区.dir");
        ar.intSA("E:\\JAVA_PROGRAM\\OverWatchForumSpider\\objects\\BUG反馈区.dir");
        ar.showBasiInf();
        ar.sortByReplyNum();
        //ar.sortByViewNum();
        //ar.showListAll();
        ar.showListBasic();
    }
}
