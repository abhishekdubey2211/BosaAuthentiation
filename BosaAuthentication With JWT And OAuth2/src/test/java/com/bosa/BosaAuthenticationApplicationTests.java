//package com.bosa;
//
//import io.jsonwebtoken.Jwts;
//import jakarta.xml.bind.DatatypeConverter;
//import javax.crypto.SecretKey;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class BosaAuthenticationApplicationTests {
//
//    @Test
//    void contextLoads() {
//
//        SecretKey key = Jwts.SIG.HS512.key().build();
//        String encodedkey = DatatypeConverter.printHexBinary(key.getEncoded());
//        System.out.print(key);
//
//    }
//
//}
