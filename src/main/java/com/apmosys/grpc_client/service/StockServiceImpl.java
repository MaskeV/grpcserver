package com.apmosys.grpc_client.service;


import com.apmosys.grpc_client.entity.StockeEntity;
import com.apmosys.grpc_client.repository.StockRepository;
import com.proto.*;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@GrpcService
public class StockServiceImpl extends StockServiceGrpc.StockServiceImplBase {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void getStockbyId(StockReq request, StreamObserver<Stock> responseObserver) {
        java.lang.String symbol= request.getSymbol();
          StockeEntity stockeEntity =  stockRepository.findBySymbol(symbol).orElse(null);

          Stock stock = Stock.newBuilder().setId(stockeEntity.getId()).setSymbol(stockeEntity.getSymbol()).setPrice(stockeEntity.getPrice()).build();
        responseObserver.onNext(stock);
        responseObserver.onCompleted();

    }

    @Override
    public void addStock(AddStockReq request, StreamObserver<Stock> responseObserver) {
        StockeEntity stockeEntity = new StockeEntity();

        stockeEntity.setSymbol(request.getSymbol());
        stockeEntity.setPrice(request.getPrice());
        stockeEntity.setLastUpdated(Instant.now());


        stockRepository.save(stockeEntity);
        Stock stock = Stock.newBuilder().setId(stockeEntity.getId()).setSymbol(stockeEntity.getSymbol()).setPrice(stockeEntity.getPrice()).build();
        responseObserver.onNext(stock);
        responseObserver.onCompleted();
    }



    @Override
    public void getStocks(Empty request, StreamObserver<StockList> responseObserver) {
        List<StockeEntity> stockeEntities = stockRepository.findAll();

        List<Stock> stocks = new ArrayList<>();

        for(StockeEntity stockeEntity:stockeEntities){
            Stock stock = Stock.newBuilder().setId(stockeEntity.getId()).setSymbol(stockeEntity.getSymbol()).setPrice(stockeEntity.getPrice()).build();
            stocks.add(stock);
        }

        StockList stockList = StockList.newBuilder().addAllStocks(stocks).build();

        responseObserver.onNext(stockList);
        responseObserver.onCompleted();
    }


}
