package com.mjoys.service.impl;

import com.mjoys.dao.TaskRepository;
import com.mjoys.po.Task;
import com.mjoys.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("taskService")
public class TaskServiceImpl implements ITaskService{
    @Autowired
    private TaskRepository taskRepository;
    @Override
    public void findAll() {
        List<Task> all = taskRepository.findAll();
        for (Task task : all){
            System.out.println(task);
        }
    }
}
