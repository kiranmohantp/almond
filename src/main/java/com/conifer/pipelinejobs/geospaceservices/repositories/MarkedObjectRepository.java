package com.conifer.pipelinejobs.geospaceservices.repositories;

import com.conifer.pipelinejobs.geospaceservices.entity.MarkedObject;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MarkedObjectRepository extends CrudRepository<MarkedObject, Long> {
    Optional<MarkedObject> findByName(String name);
}
