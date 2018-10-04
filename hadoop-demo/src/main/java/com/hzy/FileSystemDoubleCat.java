package com.hzy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class FileSystemDoubleCat {
    //URL是一个hdfs地址
    public static void main(String[] args) throws IOException {
        String uri = args[0];
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri),conf);
        FSDataInputStream in = null;
        try{
            in = fs.open(new Path(uri));
            IOUtils.copyBytes(in,System.out,4096,false); //close=false，自行关闭数据流
            in.seek(0);//复制完后回到起点，再复制一次
            IOUtils.copyBytes(in,System.out,4096,false); //close=false，自行关闭数据流
        }finally{
            IOUtils.closeStream(in);
        }
    }
}
