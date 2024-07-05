package com.unir.hotels.data;

import com.unir.hotels.model.doc.HotelDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface HotelDocRepository extends ElasticsearchRepository<HotelDoc, String> {
	
	List<HotelDoc> findAll();
}
