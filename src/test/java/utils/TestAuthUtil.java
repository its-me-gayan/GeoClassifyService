package utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;


/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/22/24
 * Time: 6:09 PM
 */
public class TestAuthUtil {
    public static HttpHeaders getAuthHeader(){
        String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("admin:1234").getBytes()));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization",basicDigestHeaderValue);
        return httpHeaders;
    }
}
