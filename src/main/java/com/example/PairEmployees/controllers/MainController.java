package com.example.PairEmployees.controllers;

import com.example.PairEmployees.services.ApplicationService;
import com.example.PairEmployees.models.PairEmployees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {
    @Autowired
    private ApplicationService applicationService;

        @GetMapping("/upload")
        public String displayUploadForm() {
            return "upload";
        }

        @PostMapping("/upload")
        public String handleFileUpload(Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
            if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
                model.addAttribute("error", "Please upload a CSV file.");
                return "upload";
            }
                try {
                    PairEmployees pairEmployees = applicationService.run(file);
                    redirectAttributes.addFlashAttribute("pairEmployees", pairEmployees);
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("error", "Failed to process the file.");
                    return "redirect:/upload";
                }
                return "redirect:/upload/result";
        }

        @GetMapping("/upload/result")
        public String uploadResult() {
            return "uploadResult";
        }
}

