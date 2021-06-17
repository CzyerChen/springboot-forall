/**
 * Author:   claire
 * Date:    2021-02-09 - 14:50
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 14:50          V1.17.0
 */
package com.learning.core.io;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.io.watch.watchers.DelayWatcher;
import cn.hutool.core.lang.Console;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能简述 
 * 〈IO测试〉
 *
 * @author claire
 * @date 2021-02-09 - 14:50
 */
public class HCoreFileTest {

    public static void main(String[] args){
        FileReader reader = new FileReader("");
        List<String> lines = reader.readLines();
        List<String> list = lines.stream().filter(line -> {
            if (line.contains("")) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());



        /*===============IoUtil======================*/
      /*
       * IoUtil 流操作工具类
       * FileUtil 文件读写和操作的工具类。
       * FileTypeUtil 文件类型判断工具类
       * WatchMonitor 目录、文件监听，封装了JDK1.7中的WatchService
       * ClassPathResource针对ClassPath中资源的访问封装
       * FileReader 封装文件读取
       * FileWriter 封装文件写入
       * BOMInputStream针对含有BOM头的流读取
       * FastByteArrayOutputStream 基于快速缓冲FastByteBuffer的OutputStream，随着数据的增长自动扩充缓冲区（from blade）
       * FastByteBuffer 快速缓冲，将数据存放在缓冲集中，取代以往的单一数组（from blade）
       */

//        readBytes 返回byte数组（读取图片等）
//        readHex 读取16进制字符串
//        readObj 读取序列化对象（反序列化）
//        readLines 按行读取
//        IoUtil.getReader();
//        IoUtil.getWriter();
//        IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE);
//        IoUtil.write();
//        IoUtil.writeObjects();
//        IoUtil.close();

        /*===============FileUtil======================*/
        /*
         * FileUtil 文件工具类
         * ls 列出目录和文件
         * touch 创建文件，如果父目录不存在也自动创建
         * mkdir 创建目录，会递归创建每层目录
         * del 删除文件或目录（递归删除，不判断是否为空），这个方法相当于Linux的delete命令
         * copy 拷贝文件或目录
         */

        /*===============FileTypeUtil======================*/
        /*文件类型判断-FileTypeUtil*/

        /*===============WatchMonitor======================*/
        /*
         * 很多时候我们需要监听一个文件的变化或者目录的变动，包括文件的创建、修改、删除，以及目录下文件的创建、修改和删除，在JDK7前我们只能靠轮询方式遍历目录或者定时检查文件的修改事件，这样效率非常低，性能也很差。因此在JDK7中引入了WatchService。不过考虑到其API并不友好，于是Hutool便针对其做了简化封装，使监听更简单，也提供了更好的功能，这包括：
         * 支持多级目录的监听（WatchService只支持一级目录），可自定义监听目录深度
         * 延迟合并触发支持（文件变动时可能触发多次modify，支持在某个时间范围内的多次修改事件合并为一个修改事件）
         * 简洁易懂的API方法，一个方法即可搞定监听，无需理解复杂的监听注册机制。
         * 多观察者实现，可以根据业务实现多个Watcher来响应同一个事件（通过WatcherChain）
         *
         * WatchMonitor提供的事件有：
         * ENTRY_MODIFY 文件修改的事件
         * ENTRY_CREATE 文件或目录创建的事件
         * ENTRY_DELETE 文件或目录删除的事件
         * OVERFLOW 丢失的事件
         */
        File file = FileUtil.file("example.properties");
        //这里只监听文件或目录的修改事件
        WatchMonitor watchMonitor = WatchMonitor.create(file, WatchMonitor.ENTRY_MODIFY);
        watchMonitor.setWatcher(new Watcher(){
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                Console.log("创建：{}-> {}", currentPath, obj);
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                Console.log("修改：{}-> {}", currentPath, obj);
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                Console.log("删除：{}-> {}", currentPath, obj);
            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {
                Object obj = event.context();
                Console.log("Overflow：{}-> {}", currentPath, obj);
            }
        });

        //设置监听目录的最大深入，目录层级大于制定层级的变更将不被监听，默认只监听当前层级目录
        watchMonitor.setMaxDepth(3);
        //启动监听
        watchMonitor.start();

        //在监听目录或文件时，如果这个文件有修改操作，JDK会多次触发modify方法，为了解决这个问题，我们定义了DelayWatcher，此类通过维护一个Set将短时间内相同文件多次modify的事件合并处理触发，从而避免以上问题
        WatchMonitor monitor = WatchMonitor.createAll("d:/", new DelayWatcher(new Watcher(){

            @Override
            public void onCreate(WatchEvent<?> watchEvent, Path path) {

            }

            @Override
            public void onModify(WatchEvent<?> watchEvent, Path path) {

            }

            @Override
            public void onDelete(WatchEvent<?> watchEvent, Path path) {

            }

            @Override
            public void onOverflow(WatchEvent<?> watchEvent, Path path) {

            }
        }, 500));
        monitor.start();

        /*===============FileReader======================*/
        //FileReader类  更加便捷
        //默认UTF-8编码，可以在构造中传入第二个参数做为编码
        FileReader fileReader = new FileReader("test.properties");
        String result = fileReader.readString();

        /*===============FileWriter======================*/
        FileWriter writer = new FileWriter("test.properties");
        writer.write("test");

        /*=============== 文件追加-FileAppender======================*/

        /*=============== 文件跟随-Tailer======================*/
       //类似于Linux下的tail -f
       //此方法会阻塞当前线程

        /*=============== 文件名工具-FileNameUtil======================*/
        //获取文件名、主文件名、扩展名
    }
}
