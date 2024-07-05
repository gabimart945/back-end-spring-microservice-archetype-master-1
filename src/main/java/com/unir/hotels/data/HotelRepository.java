package com.unir.hotels.data;


import com.unir.hotels.data.utils.SearchCriteria;
import com.unir.hotels.data.utils.SearchOperation;
import com.unir.hotels.data.utils.SearchStatement;
import com.unir.hotels.model.db.Hotel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@RequiredArgsConstructor
public class HotelRepository {

    private final HotelJpaRepository repository;

    public List<Hotel> getHotels() {
        return repository.findAll();
    }

    public Hotel getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Hotel save(Hotel hotel) {
        return repository.save(hotel);
    }

    public void delete(Hotel hotel) {
        repository.delete(hotel);
    }

    public List<Hotel> search(String name, String maxPrice, String minPrice, String maxStars, String minStars,
                              String maxOpinion, String minOpinion, String guests, String facilities) {
        SearchCriteria<Hotel> spec = new SearchCriteria<>();
        if (StringUtils.isNotBlank(name)) {
            spec.add(new SearchStatement("name", name, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(maxPrice)) {
            spec.add(new SearchStatement("price", maxPrice, SearchOperation.LESS_THAN_EQUAL));
        }

        if (StringUtils.isNotBlank(minPrice)) {
            spec.add(new SearchStatement("price", minPrice, SearchOperation.GREATER_THAN_EQUAL));
        }

        if (StringUtils.isNotBlank(maxStars)) {
            spec.add(new SearchStatement("stars", maxStars, SearchOperation.LESS_THAN_EQUAL));
        }

        if (StringUtils.isNotBlank(minStars)) {
            spec.add(new SearchStatement("stars", minStars, SearchOperation.GREATER_THAN_EQUAL));
        }

        if (StringUtils.isNotBlank(maxOpinion)) {
            spec.add(new SearchStatement("opinion", maxOpinion, SearchOperation.LESS_THAN_EQUAL));
        }

        if (StringUtils.isNotBlank(minOpinion)) {
            spec.add(new SearchStatement("opinion", minOpinion, SearchOperation.GREATER_THAN_EQUAL));
        }

        if (StringUtils.isNotBlank(guests)) {
            spec.add(new SearchStatement("maxGuests", guests, SearchOperation.GREATER_THAN_EQUAL));
        }

        if (StringUtils.isNotBlank(facilities)) {
            String[] facilityArray = facilities.split(",");
            for (String facility : facilityArray) {
                spec.add(new SearchStatement("facilities", facility.trim(), SearchOperation.MATCH));
            }
        }

        return repository.findAll(spec);
    }

}
