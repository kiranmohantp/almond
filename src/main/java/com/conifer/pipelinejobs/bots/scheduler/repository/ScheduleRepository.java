package com.conifer.pipelinejobs.bots.scheduler.repository;

import com.conifer.pipelinejobs.bots.scheduler.entity.Schedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    Schedule findByJobName(String jobName);
}
