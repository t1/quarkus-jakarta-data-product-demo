package com.github.t1.products;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
interface ProductsRepository extends CrudRepository<Product, Long> {}
