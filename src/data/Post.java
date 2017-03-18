/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;
import java.util.Date;
import org.jsoup.nodes.Document;

/**
 * 帖子
 * @author sicmatr1x
 */
public class Post implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 帖子的网址
     */
    private String webAddress = "";
    /**
     * 帖子的标题
     */
    private String title;
    /**
     * 回复数
     */
    private int replyNum;
    /**
     * 查看数
     */
    private int viewNum;
    /**
     * 获取到的网页
     */
    private Document Doc = null;
    /**
     * 最后获取网页文件时间
     */
    private Date lastGetDate;
    /**
     * 最后回复时间
     */
    private Date lastReplyDate;
    /**
     * 最后回复者暴雪ID
     */
    private String lastReplyID;
    /**
     * 帖子的作者暴雪ID
     */
    private String UP;
    /**
     * 是否是客服创建
     */
    private boolean isCustomerServiceEstablish = false;
    /**
     * 是否有客服回复
     */
    private boolean isCustomerServiceReply = false;
    /**
     * 帖子是否被锁帖
     */
    private boolean isLocked = false;
    /**
     * 帖子是否含有图片
     */
    private boolean isContainImg = false;
    //--------------------------------------------------------------------------
    public Post(){
        
    }
    
    public Post(String webAddress){
        this();
        this.webAddress = webAddress;
    }
    //--------------------------------------------------------------------------
    
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the Doc
     */
    public Document getDoc() {
        return Doc;
    }

    /**
     * @return the lastGetDate
     */
    public Date getLastGetDate() {
        return lastGetDate;
    }

    /**
     * @return the lastReplyDate
     */
    public Date getLastReplyDate() {
        return lastReplyDate;
    }

    /**
     * @param lastReplyDate the lastReplyDate to set
     */
    public void setLastReplyDate(Date lastReplyDate) {
        this.lastReplyDate = lastReplyDate;
    }

    /**
     * @return the UP
     */
    public String getUP() {
        return UP;
    }

    /**
     * @return the replyNum
     */
    public int getReplyNum() {
        return replyNum;
    }

    /**
     * @param replyNum the replyNum to set
     */
    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    /**
     * @return the viewNum
     */
    public int getViewNum() {
        return viewNum;
    }

    /**
     * @param viewNum the viewNum to set
     */
    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    /**
     * @return the lastReplyID
     */
    public String getLastReplyID() {
        return lastReplyID;
    }

    /**
     * @param lastReplyID the lastReplyID to set
     */
    public void setLastReplyID(String lastReplyID) {
        this.lastReplyID = lastReplyID;
    }

    /**
     * @param UP the UP to set
     */
    public void setUP(String UP) {
        this.UP = UP;
    }

    /**
     * @return the isCustomerServiceReply
     */
    public boolean isIsCustomerServiceReply() {
        return isCustomerServiceReply;
    }

    /**
     * @param isCustomerServiceReply the isCustomerServiceReply to set
     */
    public void setIsCustomerServiceReply(boolean isCustomerServiceReply) {
        this.isCustomerServiceReply = isCustomerServiceReply;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the isLocked
     */
    public boolean isIsLocked() {
        return isLocked;
    }

    /**
     * @param isLocked the isLocked to set
     */
    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * @return the isContainImg
     */
    public boolean isIsContainImg() {
        return isContainImg;
    }

    /**
     * @param isContainImg the isContainImg to set
     */
    public void setIsContainImg(boolean isContainImg) {
        this.isContainImg = isContainImg;
    }

    /**
     * @return the isCustomerServiceEstablish
     */
    public boolean isIsCustomerServiceEstablish() {
        return isCustomerServiceEstablish;
    }

    /**
     * @param isCustomerServiceEstablish the isCustomerServiceEstablish to set
     */
    public void setIsCustomerServiceEstablish(boolean isCustomerServiceEstablish) {
        this.isCustomerServiceEstablish = isCustomerServiceEstablish;
    }
    
}
