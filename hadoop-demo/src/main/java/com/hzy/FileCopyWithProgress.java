package com.hzy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.*;
import java.net.URI;

public class FileCopyWithProgress {
    //URL是一个hdfs地址
    public static void main(String[] args) throws IOException {
        String localSrc = args[0];
        String dst = args[1];
        InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dst),conf);
        OutputStream out = fs.create(new Path(dst), new Progressable() {
            @Override
            public void progress() {
                System.out.println("64k...");
            }
        });
        IOUtils.copyBytes(in,out,4096,false); //close=false，自行关闭数据流
    }
}
