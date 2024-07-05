package com.unir.hotels.model.doc;
import com.unir.hotels.utils.Consts;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "hotels", createIndex = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class HotelDoc {

    @Id
    @Field(type = FieldType.Integer, name = Consts.FIELD_ID)
    private Long id;

    @Field(type = FieldType.Search_As_You_Type, name = Consts.FIELD_NAME)
    private String name;

    @Field(type = FieldType.Search_As_You_Type, name = Consts.FIELD_ADDRESS)
    private String address;

    @Field(type = FieldType.Search_As_You_Type, name = Consts.FIELD_DESCRIPTION)
    private String description;

    @Field(type = FieldType.Double, name = Consts.FIELD_PRICE)
    private Double price;

    @Field(type = FieldType.Integer, name = Consts.FIELD_STARS)
    private Integer stars;

    @Field(type = FieldType.Double, name = Consts.FIELD_OPINION)
    private Double opinion;

    @Field(type = FieldType.Integer, name = Consts.FIELD_MAX_ROOMS)
    private Integer maxRooms;

    @Field(type = FieldType.Keyword, name = Consts.FIELD_FACILITIES)
    private List<String> facilities;

    @Field(type = FieldType.Text, name = Consts.FIELD_CONTACT_MAIL)
    private String contactMail;

    @Field(type = FieldType.Text, name = Consts.FIELD_CONTACT_NUMBER)
    private String contactNumber;

    @Field(type = FieldType.Double, name = Consts.FIELD_LATITUDE)
    private Double latitude;

    @Field(type = FieldType.Double, name = Consts.FIELD_LONGITUDE)
    private Double longitude;

    @Field(type = FieldType.Text, name = Consts.FIELD_IMAGES)
    private List<String> images;
}
