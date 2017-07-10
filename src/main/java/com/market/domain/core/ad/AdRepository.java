package com.market.domain.core.ad;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;


public interface AdRepository extends PagingAndSortingRepository<Ad, Long> {

    @RestResource(rel = "published",path = "published")
    @Query("select ad from Ad ad where ad.status = 'PUBLISHED'")
    Page<Ad> findPublished(Pageable pageable);
}
