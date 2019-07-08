> 7种获取参数的方式

### 一、GET --- 获取表单中内容
```text
http://127.0.0.1:8080?username=claire&age=20

   @GetMapping
    public void test1(String username,String age){
        
    }
```

### 二、GET/POST --- HttpServletRequest 接受参数
```text
    @GetMapping
    public void test2(HttpServletRequest request){
        String username = request.getParameter("username");
        String age = request.getParameter("age");

    }
``` 

### 三、GET/POST --- 参数比较多的情况下，利用一个定义的实体类接受参数
```text
  @Date
  class userDTO{
        private String username;
        private String age;
        
        
    }
    
    只要前端提交的字段名和实体的字段名一致就可以了
      @GetMapping
      public void test3(UserDTO dto){
  
      }
```
### 四、GET --- 利用@PathVariable接受参数
```text
   http://127.0.0.1:8080/username/age
   @GetMapping("/{username}/{age}")
    public void test4(@PathVariable String username,@PathVariable String age){

    }
```

### 五、POST --- 使用@ModelAttribute注解获取POST请求的FORM表单数据
```text
    @RequestMapping(value="/",method=RequestMethod.POST)
    public void test5(@ModelAttribute("user") UserDTO user) {
    }
```

### 六、GET --- @RequetParam 接受参数
```text
    @GetMapping
    public void test6(@RequestParam(value = "username") String username,@RequestParam(value = "age") String age) {
    
    }
```

### 七、POST --- @RequestBody 接受表单数据
```text
    @RequestMapping(value="/",method=RequestMethod.POST)
    public void test6(@RequestBody UserDTO userDTO) {
      
    }
``` 