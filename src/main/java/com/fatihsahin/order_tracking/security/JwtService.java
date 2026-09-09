package com.fatihsahin.order_tracking.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;

@Service
public class JwtService {

    // Header Payload Signature alanlarından oluşan JWT tokenini oluşturmak için gerekli metodları burada yazacağız.
    private final String SECRET_KEY ="c2ViemVsaW1ha2FybmF5YW7EsW5ha2FyZGllc2xpc3XFn2k==";// Base64 ile encode edilmiş bir secret key oluşturuyoruz. Bu key ile JWT tokenini imzalayacağız.

    public String generateToken(UserDetails userDetails) {

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();// kullanıcı yetkilerini alıyoruz
        String role = authorities.stream().map(validator -> validator.getAuthority()).findFirst().orElse(null);// kullanıcı yetkilerini alıyoruz ve ilk yetkiyi role değişkenine atıyoruz
        return Jwts
                .builder()
                .subject(userDetails.getUsername())// tokenin subject alanına kullanıcı adını ekliyoruz
                .signWith(getSigningKey(SECRET_KEY))// tokeni imzalamak için secret keyi kullanıyoruz
                .issuedAt(new Date(System.currentTimeMillis()))// tokenin oluşturulma tarihini belirliyoruz
                .expiration(new Date(System.currentTimeMillis()+1000*60*60*10)) // 10 saat geçerli olacak
                .claim("role", role)// tokenin claim alanına kullanıcı yetkisini ekliyoruz
                .compact();// tokeni oluşturuyoruz
    }

    private SecretKey getSigningKey(String secretKey) { // secret keyi decode ederek SecretKey nesnesi oluşturuyoruz
        byte[] decode = Decoders.BASE64.decode(secretKey);//    Base64 ile decode ediyoruz
        SecretKey secretKey1 = Keys.hmacShaKeyFor(decode);//    decode edilmiş byte dizisini kullanarak SecretKey nesnesi oluşturuyoruz
        return secretKey1; // SecretKey nesnesini döndürüyoruz
    }
    public String getUsernameFromToken( String token) {// tokenin name alanını almak için bir metod oluşturuyoruz
        return Jwts
                .parser()// tokeni parse etmek için parser() metodunu çağırıyoruz
                .verifyWith(getSigningKey(SECRET_KEY))// tokeni imzalamak için secret keyi kullanıyoruz
                .build()// tokeni parse etmek için build() metodunu çağırıyoruz
                .parseSignedClaims(token)// tokeni parse ediyoruz
                .getPayload()// tokenin payload alanını alıyoruz
                .getSubject();// tokenin subject alanını döndürüyoruz

    }
    public String getUserRoleFromToken( String token) {// tokenin role alanını almak için bir metod oluşturuyoruz
        return Jwts
                .parser()// tokeni parse etmek için parser() metodunu çağırıyoruz
                .verifyWith(getSigningKey(SECRET_KEY))// tokeni imzalamak için secret keyi kullanıyoruz
                .build()// tokeni parse etmek için build() metodunu çağırıyoruz
                .parseSignedClaims(token)// tokeni parse ediyoruz
                .getPayload()// tokenin payload alanını alıyoruz
                .get("role", String.class);// tokenin role alanını döndürüyoruz

    }

    public Date getExpirationDateFromToken( String token) {// tokenin son kullanma alanını almak için bir metod oluşturuyoruz

        return Jwts
                .parser()// tokeni parse etmek için parser() metodunu çağırıyoruz
                .verifyWith(getSigningKey(SECRET_KEY))// tokeni imzalamak için secret keyi kullanıyoruz
                .build()// tokeni parse etmek için build() metodunu çağırıyoruz
                .parseSignedClaims(token)// tokeni parse ediyoruz
                .getPayload()// tokenin payload alanını alıyoruz
                .getExpiration();// tokenin expiration alanını döndürüyoruz
    }

    public Boolean isExpiredToken(String token) {// tokenin son kullanma tarihini kontrol etmek için bir metod oluşturuyoruz
        if (getExpirationDateFromToken(token).before(new Date())) {
            return true;
        }
        return false;
    }

    public Boolean validateToken(String token,UserDetails userDetails) {
        final String username = getUsernameFromToken(token);// tokenin name alanını alıyoruz
        return (username.equals(userDetails.getUsername()) && !isExpiredToken(token));// tokenin name alanı ile userDetails nesnesinin name alanını karşılaştırıyoruz ve tokenin son kullanma tarihini kontrol ediyoruz
    }
}
