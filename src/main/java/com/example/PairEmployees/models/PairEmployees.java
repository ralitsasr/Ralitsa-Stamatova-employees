package com.example.PairEmployees.models;

import java.util.HashMap;
public class PairEmployees {
    private long firstEmployeeId;
    private long secondEmployeeId;
    private long totalOverlapDays;
    private final HashMap<Long, Long> projectOverlapDurationMap = new HashMap<>();

    public PairEmployees(long firstEmployeeId, long secondEmployeeId, long totalOverlapDays) {
        this.setFirstEmployeeId(firstEmployeeId);
        this.setSecondEmployeeId(secondEmployeeId);
        this.setTotalOverlapDays(totalOverlapDays);
    }

    public long getFirstEmployeeId() {
        return this.firstEmployeeId;
    }
    public long getSecondEmployeeId() {
        return this.secondEmployeeId;
    }

    public long getTotalOverlapDays() {
        return totalOverlapDays;
    }

    public HashMap<Long, Long> getProjectOverlapDurationMap() {
        return this.projectOverlapDurationMap;
    }

    public void updateProjectIdToDuration(Long projectId, long daysOverlap) {
        this.projectOverlapDurationMap.put(projectId, daysOverlap);
        this.totalOverlapDays += daysOverlap;
    }

    public void setTotalOverlapDays(long totalOverlapDays) {
        this.totalOverlapDays = totalOverlapDays;
    }

    private void setSecondEmployeeId(long secondEmployeeId) {
        this.secondEmployeeId = secondEmployeeId;
    }

    private void setFirstEmployeeId(long firstEmployeeId) {
        this.firstEmployeeId = firstEmployeeId;
    }
}
