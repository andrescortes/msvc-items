package com.app.items.services;

import com.app.items.clients.ProductFeignClient;
import com.app.items.models.Item;
import com.app.items.models.Product;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    final Random rand = new Random();
    private final ProductFeignClient client;

    @Override
    public List<Item> getItems() {
        return client.getProducts()
                .stream()
                .sorted(Comparator.comparing(product -> product.getName().substring(0, 2)))
                .map(product -> new Item(product, getRandomNumber()))
                .toList();
    }

    @Override
    public Optional<Item> getItem(Long id) {
        try {
            Product product = client.getProduct(id);
            return Optional.of(new Item(product, getRandomNumber()));
        } catch (FeignException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    private int getRandomNumber() {
        return rand.nextInt(100) + 1;
    }
}