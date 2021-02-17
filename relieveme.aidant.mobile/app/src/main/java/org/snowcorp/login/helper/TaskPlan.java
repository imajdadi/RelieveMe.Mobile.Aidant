package org.snowcorp.login.helper;

public class TaskPlan {
    private  String TaskName;
    private String TaskDesc;
    private String TaskDate;
    private String TaskType;
    private String TaskRep;
    private String EndTime;



    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getTaskDesc() {
        return TaskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        TaskDesc = taskDesc;
    }

    public String getTaskDate() {
        return TaskDate;
    }

    public void setTaskDate(String taskDate) {
        TaskDate = taskDate;
    }

    public String getTaskType() {
        return TaskType;
    }

    public void setTaskType(String taskType) {
        TaskType = taskType;
    }

    public String getTaskRep() {
        return TaskRep;
    }

    public void setTaskRep(String taskRep) {
        TaskRep = taskRep;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
}
