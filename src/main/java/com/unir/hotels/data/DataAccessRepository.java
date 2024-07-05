package com.unir.hotels.data;


import com.unir.hotels.model.dto.AggregationDetails;
import com.unir.hotels.utils.Consts;
import com.unir.hotels.model.doc.HotelDoc;
import com.unir.hotels.model.dto.HotelDto;
import com.unir.hotels.model.dto.SearchDto;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DataAccessRepository {

    // Esta clase (y bean) es la unica que usan directamente los servicios para
    // acceder a los datos.
    private final HotelDocRepository hotelsRepository;
    private final ElasticsearchOperations elasticClient;

    private final String[] name_fields = {
            String.format("%s", Consts.FIELD_NAME),
            String.format("%s._2gram", Consts.FIELD_NAME),
            String.format("%s._3gram", Consts.FIELD_NAME)};

    private final String[] address_fields = {
            String.format("%s", Consts.FIELD_ADDRESS),
            String.format("%s._2gram", Consts.FIELD_ADDRESS),
            String.format("%s._3gram", Consts.FIELD_ADDRESS)};

    private final String[] description_fields = {
            String.format("%s", Consts.FIELD_DESCRIPTION),
            String.format("%s._2gram", Consts.FIELD_DESCRIPTION),
            String.format("%s._3gram", Consts.FIELD_DESCRIPTION)};

    private final String[] facilities_fields = {
            String.format("%s", Consts.FIELD_FACILITIES),
            String.format("%s._2gram", Consts.FIELD_FACILITIES),
            String.format("%s._3gram", Consts.FIELD_FACILITIES)};

    public SearchDto findProducts(
            String name,
            String address,
            String description,
            String maxPrice,
            String minPrice,
            List<String> priceValues,
            String maxStars,
            String minStars,
            List<String> starsValues,
            String maxOpinion,
            String minOpinion,
            List<String> opinionValues,
            String searchInput,
            List<String> facilities,
            String page) {

        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

        // Si el usuario ha seleccionado algun valor relacionado con el nombre, lo añadimos a la query
        if (!StringUtils.isEmpty(name)) {
            querySpec.must(QueryBuilders.multiMatchQuery(name, name_fields).type(MultiMatchQueryBuilder.Type.BOOL_PREFIX));
        }

        // Si el usuario ha seleccionado algun valor relacionado con la direccion, lo añadimos a la query
        if (!StringUtils.isEmpty(address)) {
            querySpec.must(QueryBuilders.multiMatchQuery(address, address_fields).type(MultiMatchQueryBuilder.Type.BOOL_PREFIX));
        }

        // Si el usuario ha seleccionado algun valor relacionado con la descripcion, lo añadimos a la query
        if (!StringUtils.isEmpty(description)) {
            querySpec.must(QueryBuilders.multiMatchQuery(description, description_fields).type(MultiMatchQueryBuilder.Type.BOOL_PREFIX));
        }

        //Si el usuario ha seleccionado algun valor relacionado con el precio maximo, lo añadimos a la query
        if (!StringUtils.isEmpty(maxPrice)) {
            querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_PRICE).to(maxPrice).includeUpper(true));
        }

        //Si el usuario ha seleccionado algun valor relacionado con el precio minimo, lo añadimos a la query
        if (!StringUtils.isEmpty(minPrice)) {
            querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_PRICE).from(minPrice).includeLower(true));
        }

        //Si el usuario ha seleccionado algun valor relacionado con las estrellas maximas, lo añadimos a la query
        if (!StringUtils.isEmpty(maxStars)) {
            querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_STARS).to(maxStars).includeUpper(true));
        }

        //Si el usuario ha seleccionado algun valor relacionado con las estrellas minimas, lo añadimos a la query
        if (!StringUtils.isEmpty(minStars)) {
            querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_STARS).from(minStars).includeLower(true));
        }

        //Si el usuario ha seleccionado algun valor relacionado con la opinion maxima, lo añadimos a la query
        if (!StringUtils.isEmpty(maxOpinion)) {
            querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_OPINION).to(maxOpinion).includeUpper(true));
        }

        //Si el usuario ha seleccionado algun valor relacionado con la opinion minima, lo añadimos a la query
        if (!StringUtils.isEmpty(minOpinion)) {
            querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_OPINION).from(minOpinion).includeLower(true));
        }

        // Si el usuario ha seleccionado algun valor en search input, lo añadimos a la query
        if (!StringUtils.isEmpty(searchInput)) {
            querySpec.should(QueryBuilders.multiMatchQuery(searchInput, name_fields).type(MultiMatchQueryBuilder.Type.BOOL_PREFIX));
            querySpec.should(QueryBuilders.multiMatchQuery(searchInput, address_fields).type(MultiMatchQueryBuilder.Type.BOOL_PREFIX));
        }

        // Si el usuario ha seleccionado algun valor relacionado con las facilities, lo añadimos a la query
        if (facilities != null && !facilities.isEmpty()) {
            facilities.forEach(facility -> querySpec.must(QueryBuilders.termQuery(Consts.FIELD_FACILITIES, facility)));
        }

        if (priceValues != null && !priceValues.isEmpty()) {
            priceValues.forEach(
                    price -> {
                        String[] priceRange = price != null && price.contains("-") ? price.split("-") : new String[]{};

                        if (priceRange.length == 2) {
                            if ("".equals(priceRange[0])) {
                                querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_PRICE).to(priceRange[1]).includeUpper(false));
                            } else {
                                querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_PRICE).from(priceRange[0]).to(priceRange[1]).includeUpper(false));
                            }
                        } if (priceRange.length == 1) {
                            querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_PRICE).from(priceRange[0]));
                        }
                    }
            );
        }

        if (starsValues != null && !starsValues.isEmpty()) {
            starsValues.forEach(stars -> {
                String[] starsRange = stars != null && stars.contains("-") ? stars.split("-") : new String[]{};
                if ("5".equals(stars)) {
                    querySpec.must(QueryBuilders.termQuery(Consts.FIELD_STARS, "5"));
                } else if (starsRange.length == 2) {
                    if ("".equals(starsRange[0])) {
                        querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_STARS).to(starsRange[1]).includeUpper(false));
                    } else {
                        querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_STARS).from(starsRange[0]).to(starsRange[1]).includeUpper(false));
                    }
                } else if (starsRange.length == 1) {
                    querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_STARS).from(starsRange[0]));
                }
            });
        }

        if (opinionValues != null && !opinionValues.isEmpty()) {
            opinionValues.forEach(
                    opinion -> {
                        String[] opinionRange = opinion != null && opinion.contains("-") ? opinion.split("-") : new String[]{};

                        if (opinionRange.length == 2) {
                            if ("".equals(opinionRange[0])) {
                                querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_OPINION).to(opinionRange[1]).includeUpper(false));
                            } else {
                                querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_OPINION).from(opinionRange[0]).to(opinionRange[1]).includeUpper(false));
                            }
                        } if (opinionRange.length == 1) {
                            querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_OPINION).from(opinionRange[0]));
                        }
                    }
            );
        }

        //Si no se ha seleccionado ningun filtro, se añade un filtro por defecto para que la query no sea vacia
        if(!querySpec.hasClauses()) {
            querySpec.must(QueryBuilders.matchAllQuery());
        }

        //Construimos la query
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec);

        nativeSearchQueryBuilder.addAggregation(
                AggregationBuilders.range(Consts.AGG_KEY_RANGE_STARS)
                        .field(Consts.FIELD_STARS)
                        .addUnboundedTo(Consts.AGG_KEY_RANGE_STARS_0, 2)
                        .addRange(Consts.AGG_KEY_RANGE_STARS_1, 2, 4)
                        .addUnboundedFrom(Consts.AGG_KEY_RANGE_STARS_2, 4)
                        .addRange(Consts.AGG_KEY_RANGE_STARS_3, 5, 5)
        );



        nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                .range(Consts.AGG_KEY_RANGE_PRICE)
                .field(Consts.FIELD_PRICE)
                .addUnboundedTo(Consts.AGG_KEY_RANGE_PRICE_0,50)
                .addRange(Consts.AGG_KEY_RANGE_PRICE_1, 50, 100)
                .addUnboundedFrom(Consts.AGG_KEY_RANGE_PRICE_2, 100));

        nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                .range(Consts.AGG_KEY_RANGE_OPINION)
                .field(Consts.FIELD_OPINION)
                .addUnboundedTo(Consts.AGG_KEY_RANGE_OPINION_0,5)
                .addRange(Consts.AGG_KEY_RANGE_OPINION_1, 5, 8)
                .addUnboundedFrom(Consts.AGG_KEY_RANGE_OPINION_2, 8));

        nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                .terms(Consts.AGG_KEY_TERM_FACILITIES)
                .field(Consts.FIELD_FACILITIES).size(1000));

        //Se establece un maximo de 5 resultados, va acorde con el tamaño de la pagina
        nativeSearchQueryBuilder.withMaxResults(5);

        //Podemos paginar los resultados en base a la pagina que nos llega como parametro
        //El tamaño de la pagina es de 5 elementos (pero el propio llamante puede cambiarlo si se habilita en la API)
        int pageInt = Integer.parseInt(page);
        if (pageInt >= 0) {
            nativeSearchQueryBuilder.withPageable(PageRequest.of(pageInt,5));
        }

        //Se construye la query
        Query query = nativeSearchQueryBuilder.build();
        // Se realiza la busqueda
        SearchHits<HotelDoc> result = elasticClient.search(query, HotelDoc.class);

        return new SearchDto(getResponseHotels(result), getResponseAggregations(result));
    }

    /**
     * Metodo que convierte los resultados de la busqueda en una lista de hoteles.
     * @param result Resultados de la busqueda.
     * @return Lista de hoteles.
     */
    private List<HotelDto> getResponseHotels(SearchHits<HotelDoc> result) {
        return result.getSearchHits().stream().map(hit -> new HotelDto(hit.getContent())).toList();
    }

    private Map<String, List<AggregationDetails>> getResponseAggregations(SearchHits<HotelDoc> result){
        Map<String, List<AggregationDetails>> responseAggregations = new HashMap<>();

        if (result.hasAggregations()){
            Map<String, Aggregation> aggs = result.getAggregations().asMap();

            aggs.forEach((key, value) -> {

                if(!responseAggregations.containsKey(key)) {
                    responseAggregations.put(key, new LinkedList<>());
                }

                if (value instanceof ParsedStringTerms parsedStringTerms){
                    parsedStringTerms.getBuckets().forEach(bucket -> {
                        responseAggregations.get(key).add(new AggregationDetails(bucket.getKey().toString(), (int) bucket.getDocCount()));
                    });
                }

                if (value instanceof ParsedRange parsedRange){
                    parsedRange.getBuckets().forEach(bucket -> {
                        responseAggregations.get(key).add(new AggregationDetails(bucket.getKeyAsString(), (int) bucket.getDocCount()));
                    });
                }
            });
        }

        return responseAggregations;
    }


}
