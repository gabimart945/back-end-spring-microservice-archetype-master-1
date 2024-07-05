package com.unir.hotels.model.db;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "hotel_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class HotelImage
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelImageId;

    @Column(name = "path")
    private String path;

    public HotelImage(String path) {
        this.path = path;
    }

    public void update(String path) {
        this.path = path;
    }

}