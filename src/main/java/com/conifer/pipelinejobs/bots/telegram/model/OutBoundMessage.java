package com.conifer.pipelinejobs.bots.telegram.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class OutBoundMessage {
    @Setter
    private String message;
    private Long messageId;
    private List<EasyAccessOption> easyAccessOptions;
}
