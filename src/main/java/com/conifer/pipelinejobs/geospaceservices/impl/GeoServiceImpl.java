package com.conifer.pipelinejobs.geospaceservices.impl;

import com.conifer.pipelinejobs.geospaceservices.GeoService;
import com.conifer.pipelinejobs.geospaceservices.models.GeoHash;
import com.conifer.pipelinejobs.geospaceservices.models.MarkedObject;
import com.conifer.pipelinejobs.geospaceservices.models.Position;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeoServiceImpl implements GeoService {


    @Override
    public GeoHash getGeoHashFromPosition(Position position) {
        return null;
    }

    @Override
    public List<MarkedObject> getMarkedObjectsInTheGeoHash(GeoHash geoHash) {
        return null;
    }

    @Override
    public void addMarkedObjectToGeoHash(MarkedObject markedObject) {

    }
}
