/**
 * Author:   claire
 * Date:    2021-03-01 - 16:01
 * Description: hdfs操作
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-03-01 - 16:01          V1.17.0          hdfs操作
 */
package boot.mybatisplus.service;

import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.PathFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 功能简述
 * 〈hdfs操作〉
 *
 * @author claire
 * @date 2021-03-01 - 16:01
 * @since 1.17.0
 */
public interface HdfsOperation<T> {
    /**
     * 获取HDFS文件系统
     *
     * @return org.apache.hadoop.fs.FileSystem
     */
    FileSystem getFileSystem() throws IOException;

    /**
     * 创建HDFS目录
     *
     * @param path HDFS的相对目录路径，比如：/testDir
     * @return boolean 是否创建成功
     * @author zifangsky
     * @date 2018/7/20 15:08
     * @since 1.0.0
     */
    boolean mkdir(String path);

    /**
     * 上传文件至HDFS
     *
     * @param srcFile 本地文件路径，比如：D:/test.txt
     * @param dstPath HDFS的相对目录路径，比如：/testDir
     * @author zifangsky
     * @date 2018/7/20 15:28
     * @since 1.0.0
     */
    void uploadFileToHdfs(String srcFile, String dstPath);

    /**
     * 上传文件至HDFS
     *
     * @param delSrc    是否删除本地文件
     * @param overwrite 是否覆盖HDFS上面的文件
     * @param srcFile   本地文件路径，比如：D:/test.txt
     * @param dstPath   HDFS的相对目录路径，比如：/testDir
     * @author zifangsky
     * @date 2018/7/20 15:28
     * @since 1.0.0
     */
    void uploadFileToHdfs(boolean delSrc, boolean overwrite, String srcFile, String dstPath) ;
    /**
     * 判断文件或者目录是否在HDFS上面存在
     *
     * @param path HDFS的相对目录路径，比如：/testDir、/testDir/a.txt
     * @return boolean
     * @author zifangsky
     * @date 2018/7/20 15:37
     * @since 1.0.0
     */
   boolean checkExists(String path);

    /**
     * 获取HDFS上面的某个路径下面的所有文件或目录（不包含子目录）信息
     *
     * @param path HDFS的相对目录路径，比如：/testDir
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author zifangsky
     * @date 2018/7/20 18:12
     * @since 1.0.0
     */
    List<Map<String, Object>> listFiles(String path, PathFilter pathFilter);


    /**
     * 从HDFS下载文件至本地
     *
     * @param srcFile HDFS的相对目录路径，比如：/testDir/a.txt
     * @param dstFile 下载之后本地文件路径（如果本地文件目录不存在，则会自动创建），比如：D:/test.txt
     * @author zifangsky
     * @date 2018/7/23 14:01
     * @since 1.0.0
     */
    void downloadFileFromHdfs(String srcFile, String dstFile);
    /**
     * 打开HDFS上面的文件并返回 InputStream
     *
     * @param path HDFS的相对目录路径，比如：/testDir/c.txt
     * @return FSDataInputStream
     * @author zifangsky
     * @date 2018/7/23 17:08
     * @since 1.0.0
     */
    FSDataInputStream open(String path);

    /**
     * 打开HDFS上面的文件并返回byte数组，方便Web端下载文件
     * <p>new ResponseEntity<byte[]>(byte数组, headers, HttpStatus.CREATED);</p>
     * <p>或者：new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(templateFile), headers, HttpStatus.CREATED);</p>
     *
     * @param path HDFS的相对目录路径，比如：/testDir/b.txt
     * @return FSDataInputStream
     * @author zifangsky
     * @date 2018/7/23 17:08
     * @since 1.0.0
     */
    byte[] openWithBytes(String path);
    /**
     * 打开HDFS上面的文件并返回String字符串
     *
     * @param path HDFS的相对目录路径，比如：/testDir/b.txt
     * @return FSDataInputStream
     * @author zifangsky
     * @date 2018/7/23 17:08
     * @since 1.0.0
     */
    String openWithString(String path);

    /**
     * 打开HDFS上面的文件并转换为Java对象（需要HDFS上门的文件内容为JSON字符串）
     *
     * @param path HDFS的相对目录路径，比如：/testDir/c.txt
     * @return FSDataInputStream
     * @author zifangsky
     * @date 2018/7/23 17:08
     * @since 1.0.0
     */
    <T extends Object> T openWithObject(String path, Class<T> clazz);
    /**
     * 重命名
     *
     * @param srcFile 重命名之前的HDFS的相对目录路径，比如：/testDir/b.txt
     * @param dstFile 重命名之后的HDFS的相对目录路径，比如：/testDir/b_new.txt
     * @author zifangsky
     * @date 2018/7/23 15:11
     * @since 1.0.0
     */
    boolean rename(String srcFile, String dstFile);

    /**
     * 删除HDFS文件或目录
     *
     * @param path HDFS的相对目录路径，比如：/testDir/c.txt
     * @return boolean
     * @author zifangsky
     * @date 2018/7/23 15:28
     * @since 1.0.0
     */
    boolean delete(String path);

    /**
     * 获取某个文件在HDFS集群的位置
     *
     * @param path HDFS的相对目录路径，比如：/testDir/a.txt
     * @return org.apache.hadoop.fs.BlockLocation[]
     * @author zifangsky
     * @date 2018/7/23 15:41
     * @since 1.0.0
     */
    BlockLocation[] getFileBlockLocations(String path);


    /**
     * 将相对路径转化为HDFS文件路径
     *
     * @param dstPath 相对路径，比如：/data
     * @return java.lang.String
     * @author zifangsky
     * @date 2018/7/20 15:18
     * @since 1.0.0
     */
    String generateHdfsPath(String dstPath);

    /**
     * close方法
     */
    void close(FileSystem fileSystem);
}
