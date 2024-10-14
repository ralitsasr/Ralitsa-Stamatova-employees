package com.example.PairEmployees.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

public class ProjectHistoryRecord {
    private long employeeId;
    private long projectId;
    private LocalDate startDate;
    private LocalDate endDate;

    public ProjectHistoryRecord(String line) {
        String[] arguments = line.split(", ");
        if(arguments.length != 4 ) {
            throw new IllegalArgumentException("Number of arguments is incorrect");
        }
        setEmployeeId(arguments[0]);
        setProjectId(arguments[1]);
        setStartDate(arguments[2]);
        setEndDate(arguments[3]);
    }

    public long getEmployeeId() {
        return this.employeeId;
    }

    public long getProjectId() {
        return this.projectId;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }
    private void setEmployeeId(String employeeIdAsString) {
        this.employeeId = validateAndParseId(employeeIdAsString, "Employee ID");
    }

    private void setProjectId(String projectIdAsString) {
        this.projectId = validateAndParseId(projectIdAsString, "Project ID");
    }
    private void setStartDate(String startDateString) {
        if (startDateString == null || startDateString.trim().isEmpty()) {
            throw new IllegalArgumentException("Start date cannot be empty.");
        }

        try {
            this.startDate = dateFormatter(startDateString);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Start date format is incorrect.");
        }

        if (this.startDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the future.");
        }
    }

    private void setEndDate(String endDateAsString) {
        if (endDateAsString.equals("NULL")) {
            this.endDate = LocalDate.now();
        } else {
            try {
                this.endDate = dateFormatter(endDateAsString);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("End date format is incorrect.");
            }

            if (this.endDate.isBefore(this.startDate)) {
                throw new IllegalArgumentException("End date cannot be before start date.");
            }
        }
    }

    private Long validateAndParseId(String idAsString, String idType) {
        if(idAsString == null || idAsString.trim().isEmpty()) {
            throw new IllegalArgumentException(idType + " cannot be null or empty");
        }

        try {
            long id = Long.parseLong(idAsString);
            if (id <= 0) {
                throw new IllegalArgumentException(idType + " must be a positive number");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(idType + " format is wrong");
        }
    }

    private LocalDate dateFormatter(String dateString) {
        DateTimeFormatter formatter = generateDateTimeFormatter();

            LocalDate date = parseDate(dateString, formatter);
            if (date != null) {
                return date;
            } else {
                throw new IllegalArgumentException("This date format is not supported: " + dateString);
            }
        }

    private LocalDate parseDate(String dateString, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    private DateTimeFormatter generateDateTimeFormatter() {
        return new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .appendOptional(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyyMMdd"))
                .appendOptional(DateTimeFormatter.ofPattern("yy/MM/dd"))
                .appendOptional(DateTimeFormatter.ofPattern("d-MMM-yy"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                .appendOptional(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                .toFormatter();
    }

}
