> 通过上一个简单的DEMO，我们可以调用Http://localhost:8092/web/login进行登录，可是浏览器一旦关闭又要重新登录，可是我们一般的网页是不是可以不用登录，那就是cookie的作用，前端页面存在有cookie保有一个session

### 如何实现rememberMe
1.在shiro配置中添加cookie对象和cookieManager
```text
  public SimpleCookie rememberMeCookie(){
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        //单位为秒
        cookie.setMaxAge(60);
        return  cookie;
    }

    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //cookie加密密钥
        cookieRememberMeManager.setCipherKey(Base64.getDecoder().decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return  cookieRememberMeManager;
    }
```

2.在securityManager里面设置rememberMeManager
```text
 @Bean(name = "securityManager")
    public org.apache.shiro.mgt.SecurityManager securityManager(){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(selfRealm());
        manager.setRememberMeManager(rememberMeManager());
        return manager;
    }
```

3.修改shiro过滤器，使访问不是每次必须认证，而是允许已经认证通过授权的用户
```text
 //user指的是用户认证通过或者配置了Remember Me记住用户登录状态后可访问,而不是每一次都需要认证
        filterChainDefinitionMap.put("/**", "user");
```

4.最后在前端添加remeberMe的选项框，然后再传递参数的时候添加布尔值变量，最终将这个参数设置再Token上
```text
@PostMapping("/login")
    @ResponseBody
    public ResponseBo login(@RequestParam String username, @RequestParam String password,@RequestParam boolean rememberMe){
        password = MD5Utils.encrypt(username, password);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password,rememberMe);

        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);
            return ResponseBo.ok();
        }catch (UnknownAccountException e){
            return ResponseBo.error(e.getMessage());
        }catch (IncorrectCredentialsException e){
            return ResponseBo.error(e.getMessage());
        }catch (LockedAccountException e){
            return ResponseBo.error(e.getMessage());
        }catch (ExcessiveAttemptsException e){
            return ResponseBo.error(e.getMessage());
        }catch (AuthenticationException e){
            return ResponseBo.error(e.getMessage());
        }
    }
```

### 由于对这个参数"setCipherKey"不是很理解，就更深入地了解了一下
- 由于没有看到比较清晰地描述，就看了源码
- 这个密钥从哪里来，这个参数地作用是什么
```text
 cookieRememberMeManager.setCipherKey(Base64.getDecoder().decode("4AvVhmFLUs0KTA3Kprsdag=="));
```
- 源码
```text
* {@code CipherService}'s {@link org.apache.shiro.crypto.AesCipherService#generateNewKey() generateNewKey} method
 * and using that result as the {@link #setCipherKey cipherKey} configuration attribute.
 *

public abstract class AbstractRememberMeManager implements RememberMeManager {

  /**
     * Cipher to use for encrypting/decrypting serialized byte arrays for added security
     */
    private CipherService cipherService;

    /**
     * Cipher encryption key to use with the Cipher when encrypting data
     */
    private byte[] encryptionCipherKey;

    /**
     * Cipher decryption key to use with the Cipher when decrypting data
     */
    private byte[] decryptionCipherKey;
    
    public AbstractRememberMeManager() {
          this.serializer = new DefaultSerializer<PrincipalCollection>();
          AesCipherService cipherService = new AesCipherService();
          this.cipherService = cipherService;
          setCipherKey(cipherService.generateNewKey().getEncoded());
      }
      
  public void setCipherKey(byte[] cipherKey) {
        //Since this method should only be used in symmetric ciphers
        //(where the enc and dec keys are the same), set it on both:
        setEncryptionCipherKey(cipherKey);
        setDecryptionCipherKey(cipherKey);
    }
    }
```
- 一个加密密钥，从代码方法上就可以看出，这个就是对cookie进行加密解密的，那作用应该就很清晰，就是保证前端缓存的cookie能够更为安全
- 类的初始化就用了cipherService.generateNewKey().getEncoded()，设置了一个默认的密钥，上面的注释也说了，初始化时通过AES算法生成的一个key
```text
public class AesCipherService extends DefaultBlockCipherService {
    private static final String ALGORITHM_NAME = "AES";

    public AesCipherService() {
        super("AES");
    }
}
```
- 接口
```text
public interface CipherService {
    ByteSource decrypt(byte[] var1, byte[] var2) throws CryptoException;

    void decrypt(InputStream var1, OutputStream var2, byte[] var3) throws CryptoException;

    ByteSource encrypt(byte[] var1, byte[] var2) throws CryptoException;

    void encrypt(InputStream var1, OutputStream var2, byte[] var3) throws CryptoException;
}
```
- 是一个接口然后提供了这些方法，往下看一下它的实现类
```text
public abstract class AbstractSymmetricCipherService extends JcaCipherService {
    protected AbstractSymmetricCipherService(String algorithmName) {
        super(algorithmName);
    }

    public Key generateNewKey() {
        return this.generateNewKey(this.getKeySize());
    }

    public Key generateNewKey(int keyBitSize) {
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(this.getAlgorithmName());
        } catch (NoSuchAlgorithmException var5) {
            String msg = "Unable to acquire " + this.getAlgorithmName() + " algorithm.  This is required to function.";
            throw new IllegalStateException(msg, var5);
        }

        kg.init(keyBitSize);
        return kg.generateKey();
    }
}

public class AesCipherService extends DefaultBlockCipherService {
    private static final String ALGORITHM_NAME = "AES";

    public AesCipherService() {
        super("AES");
    }
}
```
- 我们到现在看到了，如果不进行显式的设置，这个key其实初始化一次就会变，但是我们的密码肯定已经加密好了放在数据库了
- 对于我们自己产生的key也可以借用系统生成的方法,加密算法和密钥长度都可以自定义，密钥长度（128 256 512 位）
```text
 KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException var5) {
            String msg = "Unable to acquire " + this.getAlgorithmName() + " algorithm.  This is required to function.";
            throw new IllegalStateException(msg, var5);
        }
kg.init(128);
SecretKey secret = kg.generateKey();

String keyStrig = Base64.encodeToString(deskey.getEncoded());

```