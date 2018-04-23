package com.mjoys.dao;

import com.mjoys.po.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByActionSubmitStatus(@Param(value = "actionSubmitStatus") int actionSubmitStatus);
}
