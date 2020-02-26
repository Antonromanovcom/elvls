package com.antonromanov.elvl.repositoty;

import com.antonromanov.elvl.model.Elvls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ElvlsRepo extends JpaRepository<Elvls, Integer>{

	Optional<Elvls> findByIsin(String isin);
}
