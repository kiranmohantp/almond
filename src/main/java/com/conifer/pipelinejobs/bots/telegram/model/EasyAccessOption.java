package com.conifer.pipelinejobs.bots.telegram.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EasyAccessOption {
    private String text;
    private String callBackIdentifier;

}
