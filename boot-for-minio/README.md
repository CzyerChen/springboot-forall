## 一、安装
办法有很多，可以参照[官网说明](http://minio.org.cn/download.shtml#/linux)

可以区分部署的形式，分为Linux(Docker,wget)/K8S/MasOS/Windows/Source这些形式

以下通过docker进行部署测试

```bash
a@b:~$ docker run -d -p 9000:9000 -p 9001:9001  --name minioserver  -v ~/docker/minio/data:/data   -e "MINIO_ROOT_USER=user"   -e "MINIO_ROOT_PASSWORD=password"   minio/minio server /data  --console-address ":9001" --address ":9000"
```

console at port 9001 ,api at port 9000

## 二、启动

- 启动项目，访问默认地址 127.0.0.1:8080
- 手动通过console或者API形式，新建bucket 
- 访问8080地址将出现index页面，通过文件上传页面，进行minio测试
- 同步check console页面相对应bucket是否上传成功