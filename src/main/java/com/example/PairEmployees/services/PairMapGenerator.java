package com.example.PairEmployees.services;


import com.example.PairEmployees.models.PairEmployees;
import com.example.PairEmployees.models.ProjectHistoryRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class PairMapGenerator {
    public List<PairEmployees> findPairsWorkedTogether(List<ProjectHistoryRecord> allRecords) {
        List<PairEmployees> employeePairs = new ArrayList<>();
        for (int i = 0; i < allRecords.size() - 1; i++) {
            ProjectHistoryRecord firstEmployee = allRecords.get(i);

            for (int j = i + 1; j < allRecords.size(); j++) {
                ProjectHistoryRecord secondEmployee = allRecords.get(j);

                if (firstEmployee.getEmployeeId() != secondEmployee.getEmployeeId() &&
                        firstEmployee.getProjectId() == secondEmployee.getProjectId() &&
                        doDatesOverlap(firstEmployee, secondEmployee)) {
                    long overlapDays = calculateOverlapDays(firstEmployee, secondEmployee);

                    if (overlapDays > 0) {
                        addPair(employeePairs, firstEmployee, secondEmployee, overlapDays);
                    }
                }
            }
        }
        return employeePairs;
    }

    private boolean doDatesOverlap(ProjectHistoryRecord firstEmployee, ProjectHistoryRecord secondEmployee) {

        return  (firstEmployee.getStartDate().isBefore(secondEmployee.getEndDate()) || firstEmployee.getStartDate().isEqual(secondEmployee.getEndDate())
                &&
                (firstEmployee.getEndDate().isAfter(secondEmployee.getStartDate()) || firstEmployee.getEndDate().isEqual(secondEmployee.getStartDate())));
    }

    private long calculateOverlapDays(ProjectHistoryRecord firstEmployee, ProjectHistoryRecord secondEmployee) {
        LocalDate overlapStartDate;
        LocalDate overlapEndDate;
        if (firstEmployee.getStartDate().isBefore(secondEmployee.getStartDate())) {
            overlapStartDate = secondEmployee.getStartDate();
        } else {
            overlapStartDate = firstEmployee.getStartDate();
        }

        if (firstEmployee.getEndDate().isBefore(secondEmployee.getEndDate())) {
            overlapEndDate = firstEmployee.getEndDate();
        } else {
            overlapEndDate = secondEmployee.getEndDate();
        }

        return Math.abs(ChronoUnit.DAYS.between(overlapStartDate, overlapEndDate));
    }


    private void addPair(List<PairEmployees> pairs, ProjectHistoryRecord firstEmployee, ProjectHistoryRecord secondEmployee, long overlapDays) {
        Boolean isPresent = false;
        int index = 0;

        while (index < pairs.size()) {
            PairEmployees pair = pairs.get(index);
            if (pairExists(pair, firstEmployee.getEmployeeId(), secondEmployee.getEmployeeId())) {
                pair.updateProjectIdToDuration(firstEmployee.getProjectId(), overlapDays);
                isPresent = true;
                break;
            }
            index++;
        }

        if (!isPresent) {
            PairEmployees newPair = new PairEmployees(firstEmployee.getEmployeeId(), secondEmployee.getEmployeeId(), overlapDays);
            newPair.updateProjectIdToDuration(firstEmployee.getProjectId(), overlapDays);
            pairs.add(newPair);
        }
    }


    private boolean pairExists(PairEmployees pair, long firstEmployeeId, long secondEmployeeId) {
        return (pair.getFirstEmployeeId() == firstEmployeeId && pair.getSecondEmployeeId() == secondEmployeeId) ||
                (pair.getFirstEmployeeId() == secondEmployeeId && pair.getSecondEmployeeId() == firstEmployeeId);
    }
}
