/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import data.SubjectArea;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 * @author sicmatr1x
 */
public class Writer {

    /**
     * 以覆盖模式将论坛主题区帖子列表对象写入到dir文件里
     *
     * @param file 代写入文件对象
     * @param sa 论坛主题区帖子列表对象
     * @return 写入后dir文件的大小(字节)
     * @throws IOException
     */
    public static long out(File file, SubjectArea sa)
            throws IOException {
        try (
                ObjectOutputStream output = new ObjectOutputStream(
                        new FileOutputStream(file))) {

                    output.writeObject(sa); // 将对象写入文件

                    File tar = new File(file.toString());
                    if (tar.exists()) {
                        return tar.length();
                    } else {
                        throw new IOException("Write.out():" + file.getPath() + " fail");
                    }

                }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
