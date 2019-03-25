- MapStruct是一种类型安全的bean映射类生成java注释处理器
- MapStruct节省了时间，通过生成代码完成繁琐和容易出错的代码逻辑
- 这种方法应用的场景是很普遍的，比如在前后端或后端数据传递的时候，不同对象间字段不同，但是属于同一个实体类，字段数量字段类型一致，但是字段名可能不一样，就可以这个做映射
- 比较典型的就是做PO - DTO - VO这种相关的映射

### 引入maven依赖和maven编译插件
```text
 <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-jdk8</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>


 <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.5.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${mapstruct.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

### 建立mapper类,做字段映射
- mapper的实现默认类型是通过静态类方式
```text
 @Mapper
 public interface ProfileMapper {
     @Mappings({
             @Mapping(source = "id",target = "id"),
             @Mapping(source = "name",target = "name"),
             @Mapping(source = "age",target = "age")
     })
     ProfileDTO profilePOToProfileDTO(ProfilePO profilePO);
 
     void updateProfileDTOFromProfilePO(ProfileDTO profileDTO, @MappingTarget ProfilePO profilePO);
     
     public static ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);
 }
 
 使用：ProfileMapper.INSTANCE.profilePOToProfileDTO(.......)
```
- 本示例使用spring容器管理模式
```text
@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDTO profilePOToProfileDTO(ProfilePO profilePO);

    void updateProfileDTOFromProfilePO(ProfileDTO profileDTO, @MappingTarget ProfilePO profilePO);
}

使用：@Autowired 
     private ProfileMapper profileMapper;

```