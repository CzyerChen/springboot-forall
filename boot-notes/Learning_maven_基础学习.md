## 命令
- mvn clean
- mvn install
- mvn compile 
- mvn package

## package、install、deploy的联系与区别
- package只是简单的打包到target目录下，一个jar或者一个war
- install将package打好的包放到本地maven仓库，可能别的依赖的项目需要使用
- deploy就是将对应的包放到本地仓库，并且进行发布，可能是发布到私服上，这个地址可能是pom文件或者是maven的setting当中配置好的

#### mvn clean package
- 依次执行了clean、resources、compile、testResources、testCompile、test、jar(打包)等７个阶段。

#### mvn clean install
- 依次执行了clean、resources、compile、testResources、testCompile、test、jar(打包)、install等8个阶段。

#### mvn clean deploy
- 依次执行了clean、resources、compile、testResources、testCompile、test、jar(打包)、install、deploy等９个阶段
#### java maven编译指定jdk版本
