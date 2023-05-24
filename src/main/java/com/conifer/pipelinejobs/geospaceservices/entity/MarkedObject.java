package com.conifer.pipelinejobs.geospaceservices.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkedObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double latitude;
    private Double longitude;
    private String name;
    @OneToOne
    private GeoHash geoHash;
}
