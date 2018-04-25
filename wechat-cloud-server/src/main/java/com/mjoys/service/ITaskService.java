package com.mjoys.service;

import com.mjoys.po.Task;
import com.mjoys.protocol.message.system.CommandExecutedAck;

import java.util.List;

public interface ITaskService {
    void markTaskAsSubmitted(int taskId);

    void markTaskAsExecuted(CommandExecutedAck commandExecutedAck);

    void updateTaskResult(int taskId, int result);

    List<Task> findAllByActionSubmitStatus(int actionSubmitStatus);
}
