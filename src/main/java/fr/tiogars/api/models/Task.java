package fr.tiogars.api.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class Task {
    /**
     * The id of the task.
     */
    @Schema(example = "1", description = "The id of the task.")
    private Long id;

    /**
     * The name of the task.
     */
    @Schema(example = "task 1", description = "The name of the task.")
    private String name;

    /**
     * The description of the task.
     */
    @Schema(example = "description 1", description = "The description of the task.")
    private String description;

    /**
     * The done status of the task.
     */
    @Schema(example = "false", description = "The done status of the task.")
    private boolean done;

    public Task() {
    }

    public Task(Long id, String name, String description, boolean done) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.done = done;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", name=" + name + ", description=" + description + ", done=" + done + "]";
    }
}
