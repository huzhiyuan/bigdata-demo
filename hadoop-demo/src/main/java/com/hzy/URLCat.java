package com.hzy;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class URLCat {
    //URL是一个hdfs地址
    public static void main(String[] args) throws IOException {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
        InputStream in = null;
        try{
            in = new URL(args[0]).openStream();
            IOUtils.copyBytes(in,System.out,4096,false); //close=false，自行关闭数据流
        }finally{
            IOUtils.closeStream(in);
        }
    }
}
