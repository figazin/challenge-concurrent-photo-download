package com.alejo.photodownloader.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HouseListing {
    private Long id;
    private String address;
    private String homeowner;
    private Long price;
    private String photoURL;
}
