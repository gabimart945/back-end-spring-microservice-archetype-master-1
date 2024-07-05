package com.unir.hotels.data;

import com.unir.hotels.model.db.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

interface HotelJpaRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

	List<Hotel> findByName(String name);

}
