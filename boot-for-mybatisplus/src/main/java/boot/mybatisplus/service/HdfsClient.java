/**
 * Author:   claire
 * Date:    2021-03-01 - 15:49
 * Description: hdfs操作辅助类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-03-01 - 15:49          V1.17.0          hdfs操作辅助类
 */
package boot.mybatisplus.service;

import boot.mybatisplus.config.HdfsClientConfig;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.PathFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 功能简述
 * 〈hdfs操作辅助类〉
 *
 * @author claire
 * @date 2021-03-01 - 15:49
 * @since 1.17.0
 */
@Service
public class HdfsClient<T> implements HdfsOperation<T> {
    private static final String SALT_KEY = "dmp_file_system";
    private static final String DEFAULT_HDFS_API_URL = "http://ip:port";
    private static final String DEFAULT_HDFS_API_ROUTE = "/webhdfs/v1/tmp";

    private HdfsClientConfig connectionInfo = new HdfsClientConfig();

    public HdfsClient(HdfsClientConfig clientConfig) {
        this.connectionInfo.setDefaultUrl(clientConfig.getDefaultUrl() + DEFAULT_HDFS_API_ROUTE);
    }


    @Override
    public FileSystem getFileSystem() throws IOException {
        return null;
    }

    @Override
    public boolean mkdir(String path) {
        return false;
    }

    @Override
    public void uploadFileToHdfs(String srcFile, String dstPath) {

    }

    @Override
    public void uploadFileToHdfs(boolean delSrc, boolean overwrite, String srcFile, String dstPath) {

    }

    @Override
    public boolean checkExists(String path) {
        return false;
    }

    @Override
    public List<Map<String, Object>> listFiles(String path, PathFilter pathFilter) {
        return null;
    }

    @Override
    public void downloadFileFromHdfs(String srcFile, String dstFile) {

    }

    @Override
    public FSDataInputStream open(String path) {
        return null;
    }

    @Override
    public byte[] openWithBytes(String path) {
        return new byte[0];
    }

    @Override
    public String openWithString(String path) {
        return null;
    }

    @Override
    public <T1> T1 openWithObject(String path, Class<T1> clazz) {
        return null;
    }

    @Override
    public boolean rename(String srcFile, String dstFile) {
        return false;
    }

    @Override
    public boolean delete(String path) {
        return false;
    }

    @Override
    public BlockLocation[] getFileBlockLocations(String path) {
        return new BlockLocation[0];
    }

    @Override
    public String generateHdfsPath(String dstPath) {
        return null;
    }

    @Override
    public void close(FileSystem fileSystem) {

    }
}
