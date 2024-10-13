package com.example.PairEmployees.services;

import com.example.PairEmployees.models.PairEmployees;
import com.example.PairEmployees.models.ProjectHistoryRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {
    public PairEmployees run(MultipartFile file) {
        List<ProjectHistoryRecord> allRecords = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
                    while((line = reader.readLine()) != null) {
                        ProjectHistoryRecord record = new ProjectHistoryRecord(line);
                        allRecords.add(record);
                    }
        } catch (IOException e) {
            e.printStackTrace();
        }

        PairMapGenerator generator = new PairMapGenerator();
        List<PairEmployees> allEmployeesWorkedTogether = generator.findPairsWorkedTogether(allRecords);

        return returnLongestWorkingPair(allEmployeesWorkedTogether);
    }

     private PairEmployees returnLongestWorkingPair(List<PairEmployees> pair) {
         PairEmployees longestWorkingPair = null;

         if (pair.size() != 0) {
             longestWorkingPair = getLongestWorkingPair(pair);
         }
         return longestWorkingPair;
     }
     private PairEmployees getLongestWorkingPair(List<PairEmployees> pair) {
         pair.sort((team1, team2) ->
                 (int) (team2.getTotalOverlapDays() - team1.getTotalOverlapDays()));
         return pair.get(0);
     }
}
