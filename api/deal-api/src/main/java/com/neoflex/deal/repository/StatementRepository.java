package com.neoflex.deal.repository;

import com.neoflex.deal.model.Statement;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatementRepository extends JpaRepository<Statement, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @NonNull
    Optional<Statement> findById(UUID statementId);
}
