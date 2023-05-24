package com.conifer.pipelinejobs.bots.telegram.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InBoundMessage {
    private String message;
    private Long messageId;
    private Double latitude;
    private Double longitude;
}
