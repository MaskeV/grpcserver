package com.apmosys.grpc_client.repository;

import com.apmosys.grpc_client.entity.StockeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<StockeEntity,Integer> {


   Optional<StockeEntity> findBySymbol(String symbol);
}
