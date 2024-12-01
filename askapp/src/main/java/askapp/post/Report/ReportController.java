package askapp.post.Report;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/report")
@RestController

@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ReportRepository reportRepository;


    @PostMapping("/create")
    public ResponseEntity<RepINFO> createReport(@RequestBody Reprequest report) {
        return new ResponseEntity<RepINFO>(reportService.addReport(report), HttpStatus.CREATED);
    }

    @GetMapping("/reports")
    public ResponseEntity<List<RepINFO>> getReports() {
        List<RepINFO> reports = reportService.getAll();
        if (reports.isEmpty()) {
            System.out.println("No reports found.");
        }
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }


    @PutMapping("/ReportValide/{reportId}")
    public ResponseEntity<String> ValiderReport(@PathVariable int reportId) {


        try {
            reportRepository.findById(reportId)
                    .orElseThrow(() -> new EntityNotFoundException("Report with ID " + reportId + " not found"));

            reportService.ValiderReport(reportId);

            return ResponseEntity.ok("Report with ID " + reportId + " has been successfully set to Traité and post visibility set to false.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Report with ID " + reportId + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
    }


  @PutMapping("/ReportNonValider/{reportId}")
    public ResponseEntity<String> NonValiderReport(@PathVariable int reportId) {
        try {
            Report report = reportRepository.findById(reportId)
                    .orElseThrow(() -> new EntityNotFoundException("Report with ID " + reportId + " not found"));
            reportService.NonValiderReport(reportId);
            return ResponseEntity.ok("Report with ID " + reportId + " has been successfully set to Traité and post visibility set to false.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Report with ID " + reportId + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
    }

    @DeleteMapping("/DeleteReport/{reportId}")
    public ResponseEntity<String> DeleteReport(@PathVariable int reportId) {
        try {
            Report report = reportRepository.findById(reportId)
                    .orElseThrow(() -> new EntityNotFoundException("Report with ID " + reportId + " not found"));
            reportRepository.delete(report);
            return ResponseEntity.ok("Report with ID " + reportId + " has been successfully deleted");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Report with ID " + reportId + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
    }

}
