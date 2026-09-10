package com.fatihsahin.order_tracking.security;

import com.fatihsahin.order_tracking.dto.TokenPair;
import com.fatihsahin.order_tracking.entities.RefreshToken;
import com.fatihsahin.order_tracking.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 15;//jwt tokeninin son kullanma tarihi
    private final long REFRESH_TOKEN_VALIDITY = 7;//Refresh tokeninin son kullanma tarihi
    private final RefreshTokenRepository refreshTokenRepository;

    private final String secretKey;
    public JwtService(
            @Value("${jwt.secret-key}") String secretKey,
            RefreshTokenRepository refreshTokenRepository) {

        this.secretKey = secretKey;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    //JWT token oluşturma
    public String generateToken(UserDetails userDetails) {

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();// kullanıcı yetkilerini alıyoruz
        String role = authorities.stream().map(validator -> validator.getAuthority()).findFirst().orElse(null);// kullanıcı yetkilerini alıyoruz ve ilk yetkiyi role değişkenine atıyoruz
        return Jwts
                .builder()
                .subject(userDetails.getUsername())// tokenin subject alanına kullanıcı adını ekliyoruz
                .signWith(getSigningKey(secretKey))// tokeni imzalamak için secret keyi kullanıyoruz
                .issuedAt(new Date(System.currentTimeMillis()))// tokenin oluşturulma tarihini belirliyoruz
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY)) // tokenin son kullanma tarihini belirliyoruz.15 dakika geçerli olacak
                .claim("role", role)// tokenin claim alanına kullanıcı yetkisini ekliyoruz
                .claim("type", "access")
                .compact();// tokeni oluşturuyoruz
    }


    public RefreshToken generateRefreshToken(String username) {
        //kullanıcının eski refresh kodunu geçersiz kıl
        refreshTokenRepository.markAllAsUsedByUsername(username);

        //yeni refresh token oluştur
        String refreshTokenValue = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(REFRESH_TOKEN_VALIDITY);
        RefreshToken refreshToken = new RefreshToken(
                refreshTokenValue, username, expiryDate
        );
        return refreshTokenRepository.save(refreshToken);


    }

    //login anında token oluştur //token çifti oluşturma(Access + Refresh)
    public TokenPair generateTokenPair(UserDetails userDetails) {
        String accessToken = generateToken(userDetails);
        RefreshToken refreshToken = generateRefreshToken(userDetails.getUsername());
        return new TokenPair(accessToken, refreshToken.getToken());
    }

    //login anında değil amaç sonradan refresh token ile yeni access token elde etmek
    public String refreshAccessToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue).orElseThrow(() -> new RuntimeException("refresh token bulunamadı"));
        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("refresh token geçerli değil");
        }
        if (refreshToken.isUsed()) {
            throw new RuntimeException("refresh token kullanıldı");
        }
        refreshToken.setUsed(true);//tokenı kullanıldı olarak işaretle
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getUsername();

    }

    //refresh token doğrulama
    public boolean validateRefreshToken(String refreshTokenValue) {
        return refreshTokenRepository.findByToken(refreshTokenValue)
                .map(token -> !token.isExpired() && !token.isUsed())
                .orElse(false);
    }

    //kullanıcının tüm refresh tokenlarını sil(logout)
    public void revokeAllRefreshTokens(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }

    //süresi dolmuş tokenları temizle (Scheduled task için)
    public void cleanupExpriedTokenns() {
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }


    private SecretKey getSigningKey(String secretKey) { // secret keyi decode ederek SecretKey nesnesi oluşturuyoruz
        byte[] decode = Decoders.BASE64.decode(secretKey);//    Base64 ile decode ediyoruz
        SecretKey secretKey1 = Keys.hmacShaKeyFor(decode);//    decode edilmiş byte dizisini kullanarak SecretKey nesnesi oluşturuyoruz
        return secretKey1; // SecretKey nesnesini döndürüyoruz
    }

    public String getUsernameFromToken(String token) {// tokenin name alanını almak için bir metod oluşturuyoruz
        return Jwts
                .parser()// tokeni parse etmek için parser() metodunu çağırıyoruz
                .verifyWith(getSigningKey(secretKey))// tokeni imzalamak için secret keyi kullanıyoruz
                .build()// tokeni parse etmek için build() metodunu çağırıyoruz
                .parseSignedClaims(token)// tokeni parse ediyoruz
                .getPayload()// tokenin payload alanını alıyoruz
                .getSubject();// tokenin subject alanını döndürüyoruz

    }

    public String getUserRoleFromToken(String token) {// tokenin role alanını almak için bir metod oluşturuyoruz
        return Jwts
                .parser()// tokeni parse etmek için parser() metodunu çağırıyoruz
                .verifyWith(getSigningKey(secretKey))// tokeni imzalamak için secret keyi kullanıyoruz
                .build()// tokeni parse etmek için build() metodunu çağırıyoruz
                .parseSignedClaims(token)// tokeni parse ediyoruz
                .getPayload()// tokenin payload alanını alıyoruz
                .get("role", String.class);// tokenin role alanını döndürüyoruz

    }

    public Date getExpirationDateFromToken(String token) {// tokenin son kullanma alanını almak için bir metod oluşturuyoruz

        return Jwts
                .parser()// tokeni parse etmek için parser() metodunu çağırıyoruz
                .verifyWith(getSigningKey(secretKey))// tokeni imzalamak için secret keyi kullanıyoruz
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

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);// tokenin name alanını alıyoruz
        return (username.equals(userDetails.getUsername()) && !isExpiredToken(token));// tokenin name alanı ile userDetails nesnesinin name alanını karşılaştırıyoruz ve tokenin son kullanma tarihini kontrol ediyoruz
    }
}
