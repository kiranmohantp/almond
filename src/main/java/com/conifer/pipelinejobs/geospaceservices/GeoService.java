package com.conifer.pipelinejobs.geospaceservices;

import com.conifer.pipelinejobs.geospaceservices.models.GeoHash;
import com.conifer.pipelinejobs.geospaceservices.models.MarkedObject;
import com.conifer.pipelinejobs.geospaceservices.models.Position;

import java.util.List;

public interface GeoService {
    GeoHash getGeoHashFromPosition(Position position);

    List<MarkedObject> getMarkedObjectsInTheGeoHash(GeoHash geoHash);

    void addMarkedObjectToGeoHash(MarkedObject markedObject);
}
