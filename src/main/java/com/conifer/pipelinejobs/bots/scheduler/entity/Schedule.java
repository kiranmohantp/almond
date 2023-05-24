package com.conifer.pipelinejobs.bots.scheduler.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;


@Data
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String jobName;
    private String description;
    private LocalTime startTime;
    private Long runAlwaysInThisMilliSeconds;
    private String deviceName;
    private DeviceOperation operation;
}
