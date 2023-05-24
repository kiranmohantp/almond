package com.conifer.pipelinejobs.geospaceservices.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class GeoHash {
    @Id
    private Long geoHashId; //HashLevel + integer
    private Double minLat;
    private Double maxLat;
    private Double minLon;
    private Double maxLon;
}
