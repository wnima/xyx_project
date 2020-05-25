package util;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.security.InvalidKeyException;  
import java.security.NoSuchAlgorithmException;  
import javax.crypto.Mac;  
import javax.crypto.spec.SecretKeySpec;  
import org.apache.commons.codec.binary.Base64; 

import org.apache.commons.codec.digest.DigestUtils;


public class MD5Util {
	

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";  
  
    /** 
     * 使用 HMAC-SHA1 签名方法对data进行签名 
     *  
     * @param data 
     *            被签名的字符串 
     * @param key 
     *            密钥      
     * @return  
                      加密后的字符串 
     */  
    public static String genHMAC(String data, String key) {  
        byte[] result = null;  
        try {  
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称    
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);  
            //生成一个指定 Mac 算法 的 Mac 对象    
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);  
            //用给定密钥初始化 Mac 对象    
            mac.init(signinKey);  
            //完成 Mac 操作     
            byte[] rawHmac = mac.doFinal(data.getBytes());  
            result = Base64.encodeBase64(rawHmac);  
  
        } catch (NoSuchAlgorithmException e) {  
            System.err.println(e.getMessage());  
        } catch (InvalidKeyException e) {  
            System.err.println(e.getMessage());  
        }  
        if (null != result) {  
            return new String(result);  
        } else {  
            return null;  
        }  
    }  
	
	
	public final static String MD5(String pwd) {  
        //用于加密的字符  
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  
                'A', 'B', 'C', 'D', 'E', 'F' };  
        try {  
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中  
            byte[] btInput = pwd.getBytes();  
               
            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。  
            MessageDigest mdInst = MessageDigest.getInstance("MD5");  
               
            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要  
            mdInst.update(btInput);  
               
            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文  
            byte[] md = mdInst.digest();  
               
            // 把密文转换成十六进制的字符串形式  
            int j = md.length;  
            char str[] = new char[j * 2];  
            int k = 0;  
            for (int i = 0; i < j; i++) {   //  i = 0  
                byte byte0 = md[i];  //95  
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5   
                str[k++] = md5String[byte0 & 0xf];   //   F  
            }  
            //返回经过加密后的字符串  
            return new String(str);  
        } catch (Exception e) {  
            return null;  
        }  
    }  
	

	/**
	 * MD5方法
	 * 
	 * @param text
	 *            明文
	 * @param key
	 *            密钥
	 * @return 密文
	 * @throws Exception
	 */
	public static String md5(String text, String key) throws Exception {
		// 加密后的字符串
		String encodeStr = DigestUtils.md5Hex(text + key);
		System.out.println("MD5加密后的字符串为:encodeStr=" + encodeStr);
		return encodeStr;
	}

	/**
	 * MD5验证方法
	 * 
	 * @param text
	 *            明文
	 * @param key
	 *            密钥
	 * @param md5
	 *            密文
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean verify(String text, String key, String md5) throws Exception {
		// 根据传入的密钥进行验证
		String md5Text = md5(text, key);
		if (md5Text.equalsIgnoreCase(md5)) {
			System.out.println("MD5验证通过");
			return true;
		}

		return false;
	}
	
	
	
	public static void main(String[] args) {
		String url = "http://ysdktest.qq.com/auth/wx_check_token";
		String appid = "wx594b7e1c3b9e6300";
		String appKey = "4b87543d11b4aa061b91958d69912d0e";
		
		String timestamp = "1569768555";
		String accessToken = "4b87543d11b4aa061b91958d69912d0e";
		String openId = "onI2Zt4rN_AeILympVfAq8L8opns";
		String sign = MD5Util.MD5(appKey + timestamp);
		
		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put("appid", appid);// 应用的唯一ID。可以通过appid查找APP基本信息
		parameter.put("openid", openId);// 从手Q登录态或微信登录态中获取的openid的值
		parameter.put("openkey", accessToken);// 从手Q登录态或微信登录态中获取的access_token的值
		parameter.put("sig", sign);// 从手Q登录态或微信登录态中获取的access_token的值
		parameter.put("timestamp", timestamp);
		
		String result = HttpUtil.sendGet(url, parameter);
		System.out.println(result);
		
	}
}
