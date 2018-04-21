package com.mjoys.service.impl;

import com.mjoys.dao.TaskRepository;
import com.mjoys.po.Task;
import com.mjoys.service.ITaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TaskServiceImpl implements ITaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Override
    @Transactional
    public void markTaskAsSubmitted(int taskId) {
        Task task = taskRepository.getOne(taskId);
        if(null != task){
            task.setActionSubmitStatus(Task.ACTION_SUBMIT_STATUS_SUCCESSED);
            task.setTimeModified(new Date());
            taskRepository.save(task);
        }
    }

    @Override
    @Transactional
    public void markTaskAsExecuted(int taskId) {
        Task task = taskRepository.getOne(taskId);
        if(null != task){
            task.setActionExecutionStatus(Task.ACTION_EXECUTION_STATUS_SUCCESSED);
            task.setTimeModified(new Date());
            taskRepository.save(task);
        }
    }



    @Override
    @Transactional
    public void updateTaskResult(int taskId, int result) {
        Task task = taskRepository.getOne(taskId);
        if(null != task){
            task.setActionResultStatus(result);
            task.setTimeModified(new Date());
            taskRepository.save(task);
        }
    }
}
