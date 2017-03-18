/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import data.SubjectArea;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author sicmatr1x
 */
public class Reader {
    
    
    
    //--------------------------------------------------------------------------

    /**
     * 从dir文件读取论坛主题区帖子列表对象
     * @param dir dir文件
     * @return 论坛主题区帖子列表对象
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static SubjectArea in(File dir)
            throws IOException, ClassNotFoundException {
        SubjectArea tar = null;
        System.out.println("Reader.in():" + dir.getPath());
        try (
                FileInputStream inStream = new FileInputStream(dir);
                ObjectInputStream input = new ObjectInputStream(inStream)
                ) {
                    tar = (SubjectArea) (input.readObject()); // 从文件读取对象
                }
                return tar;
    }
}
