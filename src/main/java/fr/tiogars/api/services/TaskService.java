package fr.tiogars.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.tiogars.api.models.Task;

public interface TaskService {
    Task create(Task task);

    Task delete(Long id);

    Iterable<Task> findAll();

    Page<Task> findAll(Pageable pageable);

    Task findById(Long id);

    Task update(Long id, Task task);
}
