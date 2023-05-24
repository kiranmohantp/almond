package com.conifer.pipelinejobs.geospaceservices.models;

import lombok.Data;

@Data
public class GeoHash {
    private Long geoHashId;
    private Integer hashLevel;
    private Double minLat;
    private Double maxLat;
    private Double minLon;
    private Double maxLon;
}
