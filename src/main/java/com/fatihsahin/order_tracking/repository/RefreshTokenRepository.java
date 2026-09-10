package com.fatihsahin.order_tracking.repository;

import com.fatihsahin.order_tracking.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUsernameAndIsUsed(String username , boolean isUsed);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now")
    void deleteExpiredTokens(LocalDateTime now);//eski token silinir

    @Modifying
    @Transactional
    void deleteByUsername(String username);//kullanıcı silindiğinde token silinir

    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken rt SET rt.isUsed = true WHERE rt.username = :username")//tüm tokenlar kullanıldı olarak işaret
    void markAllAsUsedByUsername(String username);

}

