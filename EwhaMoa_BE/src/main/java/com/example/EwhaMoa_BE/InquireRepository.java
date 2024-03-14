package com.example.EwhaMoa_BE;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquireRepository extends JpaRepository<Inquire, Long> {
}
