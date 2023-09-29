package com.alejo.photodownloader.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class HouseListingResponse {
    List<HouseListing> houses;
}
