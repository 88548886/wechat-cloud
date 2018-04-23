package com.mjoys.service;

import com.mjoys.po.Task;

import java.util.List;

public interface ITaskService {
    void markTaskAsSubmitted(int taskId);

    void markTaskAsExecuted(int taskId);

    void updateTaskResult(int taskId, int result);

    List<Task> findAllByActionSubmitStatus(int actionSubmitStatus);

}
