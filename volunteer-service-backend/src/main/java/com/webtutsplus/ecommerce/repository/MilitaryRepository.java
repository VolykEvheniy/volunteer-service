package com.webtutsplus.ecommerce.repository;

import com.webtutsplus.ecommerce.model.Military;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MilitaryRepository extends JpaRepository<Military, Integer> {

    //Military findByEmail(String email);
}
