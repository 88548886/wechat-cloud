package com.mjoys.service;

public interface ITaskService {
    void markTaskAsSubmitted(int taskId);

    void markTaskAsExecuted(int taskId);

    void updateTaskResult(int taskId, int result);
}
