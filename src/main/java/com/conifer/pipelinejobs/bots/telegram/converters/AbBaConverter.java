package com.conifer.pipelinejobs.bots.telegram.converters;

public interface AbBaConverter<A, B> {
    default B convertFromAtoB(A inputObject) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    default A convertFromBtoA(B inputObject) {
        throw new UnsupportedOperationException("Method not implemented");
    }

}
