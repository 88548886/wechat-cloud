package com.mjoys.dao;

import com.mjoys.po.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    //@Param(value = "status") int status
}
