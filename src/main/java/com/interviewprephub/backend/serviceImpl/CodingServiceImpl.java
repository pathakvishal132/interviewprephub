package com.interviewprephub.backend.serviceImpl;

import com.interviewprephub.backend.config.RateLimiter;
import com.interviewprephub.backend.dto.*;
import com.interviewprephub.backend.dto.SubmitCodeResponse.TestCaseResult;
import com.interviewprephub.backend.entity.*;
import com.interviewprephub.backend.entity.CodeSubmission.Status;
import com.interviewprephub.backend.repository.*;
import com.interviewprephub.backend.service.CodingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class CodingServiceImpl implements CodingService {

    @Autowired
    private CodingQuestionRepository codingQuestionRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private CodeSubmissionRepository codeSubmissionRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private final RateLimiter rateLimiter = new RateLimiter(10, 1, TimeUnit.MINUTES);

    private static final int QUESTION_PAGE_SIZE = 10;
    private static final long EXECUTION_TIMEOUT_SECONDS = 10;

    @Value("${coding.temp.dir:/tmp/code-execution}")
    private String tempDir;

    @Override
    public Map<String, Object> getCodingQuestionsByCompany(Long companyId, int page) {
        int safePage = Math.max(page, 1);
        Pageable pageable = PageRequest.of(safePage - 1, QUESTION_PAGE_SIZE);
        Page<CodingQuestion> questionPage =
                codingQuestionRepository.findByCompanies_Id(companyId, pageable);

        List<CodingQuestionListDTO> questions = questionPage.getContent().stream().map(q -> {
            CodingQuestionListDTO dto = new CodingQuestionListDTO();
            dto.setId(q.getId());
            dto.setTitle(q.getTitle());
            dto.setDifficulty(q.getDifficulty().name());
            dto.setDateOfCreation(q.getDateOfCreation());
            dto.setCompanies(q.getCompanies().stream()
                    .map(Company::getName)
                    .collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("questions", questions);
        response.put("total_pages", questionPage.getTotalPages());
        response.put("current_page", safePage);
        response.put("total_questions", questionPage.getTotalElements());
        return response;
    }

    @Override
    public Map<String, Object> getCodingQuestionDetail(Long questionId) {
        CodingQuestion q = codingQuestionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Coding question not found"));

        List<TestCase> testCases = testCaseRepository
                .findByCodingQuestion_IdOrderByOrderIndexAsc(questionId);

        CodingQuestionDetailDTO dto = new CodingQuestionDetailDTO();
        dto.setId(q.getId());
        dto.setTitle(q.getTitle());
        dto.setDescription(q.getDescription());
        dto.setDifficulty(q.getDifficulty().name());
        dto.setStarterCodeJava(q.getStarterCodeJava());
        dto.setStarterCodePython(q.getStarterCodePython());
        dto.setStarterCodeCpp(q.getStarterCodeCpp());
        dto.setStarterCodeJavascript(q.getStarterCodeJavascript());
        dto.setDateOfCreation(q.getDateOfCreation());

        List<TestCaseDTO> tcDTOs = testCases.stream().map(tc -> {
            TestCaseDTO tcdto = new TestCaseDTO();
            tcdto.setId(tc.getId());
            tcdto.setInputData(tc.getInputData());
            tcdto.setExpectedOutput(tc.getExpectedOutput());
            tcdto.setIsHidden(tc.getIsHidden());
            tcdto.setOrderIndex(tc.getOrderIndex());
            tcdto.setExplanation(tc.getExplanation());
            return tcdto;
        }).collect(Collectors.toList());
        dto.setTestCases(tcDTOs);

        Map<String, Object> response = new HashMap<>();
        response.put("question", dto);
        return response;
    }

    @Override
    public Map<String, Object> createCodingQuestion(CreateCodingQuestionRequest request) {
        CodingQuestion question = new CodingQuestion();
        question.setTitle(request.getTitle());
        question.setDescription(request.getDescription());

        if (request.getDifficulty() != null) {
            question.setDifficulty(CodingQuestion.Difficulty.valueOf(request.getDifficulty().toUpperCase()));
        }

        if (request.getCompanyIds() != null && !request.getCompanyIds().isEmpty()) {
            Set<Company> companies = new HashSet<>(companyRepository.findAllById(request.getCompanyIds()));
            question.setCompanies(companies);
        }

        question.setStarterCodeJava(request.getStarterCodeJava());
        question.setStarterCodePython(request.getStarterCodePython());
        question.setStarterCodeCpp(request.getStarterCodeCpp());
        question.setStarterCodeJavascript(request.getStarterCodeJavascript());

        CodingQuestion saved = codingQuestionRepository.save(question);

        if (request.getTestCases() != null) {
            List<TestCase> testCases = new ArrayList<>();
            for (int i = 0; i < request.getTestCases().size(); i++) {
                CreateCodingQuestionRequest.CreateTestCase tcReq = request.getTestCases().get(i);
                TestCase tc = new TestCase();
                tc.setCodingQuestion(saved);
                tc.setInputData(tcReq.getInputData());
                tc.setExpectedOutput(tcReq.getExpectedOutput());
                tc.setIsHidden(tcReq.getIsHidden() != null ? tcReq.getIsHidden() : false);
                tc.setOrderIndex(tcReq.getOrderIndex() != null ? tcReq.getOrderIndex() : i);
                tc.setExplanation(tcReq.getExplanation());
                testCases.add(tc);
            }
            testCaseRepository.saveAll(testCases);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", saved.getId());
        response.put("title", saved.getTitle());
        response.put("message", "Coding question created successfully");
        return response;
    }

    @Override
    public Map<String, Object> getSubmissionStats(Long userId, int days) {
        String userIdStr = String.valueOf(userId);
        LocalDateTime since = LocalDateTime.now().minusDays(days);

        List<CodeSubmission> submissions = codeSubmissionRepository
                .findByUserIdAndSubmittedAtAfterOrderBySubmittedAtAsc(userIdStr, since);

        Map<LocalDate, Long> dateCounts = submissions.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getSubmittedAt().toLocalDate(),
                        Collectors.counting()
                ));

        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        for (int i = days; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            dates.add(date.toString());
            counts.add(dateCounts.getOrDefault(date, 0L).intValue());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("dates", dates);
        response.put("submission_counts", counts);
        return response;
    }

    @Override
    public void deleteCodingQuestion(Long questionId) {
        CodingQuestion q = codingQuestionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Coding question not found"));
        testCaseRepository.deleteByCodingQuestion_Id(questionId);
        codingQuestionRepository.delete(q);
    }

    @Override
    public SubmitCodeResponse executeCode(Long userId, SubmitCodeRequest request, boolean isSubmission) {
        if (!rateLimiter.isAllowed("user:" + userId)) {
            SubmitCodeResponse errorResp = new SubmitCodeResponse();
            errorResp.setStatus("RATE_LIMITED");
            errorResp.setErrorMessage("Too many submissions. Please wait before submitting again.");
            return errorResp;
        }

        CodingQuestion question = codingQuestionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        List<TestCase> allTestCases = testCaseRepository
                .findByCodingQuestion_IdOrderByOrderIndexAsc(request.getQuestionId());

        List<TestCase> testCasesToRun;
        if (isSubmission) {
            testCasesToRun = allTestCases;
        } else {
            testCasesToRun = allTestCases.stream()
                    .filter(tc -> !tc.getIsHidden())
                    .collect(Collectors.toList());
        }

        long startTime = System.currentTimeMillis();
        List<TestCaseResult> results = new ArrayList<>();
        int passed = 0;

        for (TestCase tc : testCasesToRun) {
            TestCaseResult result = new TestCaseResult();
            result.setTestCaseId(tc.getId());
            result.setExpectedOutput(tc.getExpectedOutput());
            result.setIsHidden(tc.getIsHidden());

            try {
                String actualOutput = runSingleTestCase(request.getLanguage(), request.getCode(),
                        tc.getInputData(), tc.getExpectedOutput());
                result.setActualOutput(actualOutput);

                boolean isPassed = compareOutput(actualOutput, tc.getExpectedOutput());
                result.setPassed(isPassed);
                if (isPassed) passed++;

            } catch (Exception e) {
                result.setPassed(false);
                result.setActualOutput("");
                result.setIsHidden(tc.getIsHidden());
            }
            results.add(result);
        }

        long executionTime = System.currentTimeMillis() - startTime;

        boolean allPassed = passed == testCasesToRun.size();
        Status submissionStatus = allPassed ? Status.PASS : Status.FAIL;

        CodeSubmission submission = new CodeSubmission();
        submission.setUserId(String.valueOf(userId));
        submission.setCodingQuestionId(request.getQuestionId());
        submission.setLanguage(request.getLanguage());
        submission.setCode(request.getCode());
        submission.setStatus(submissionStatus);
        submission.setTestCasesPassed(passed);
        submission.setTotalTestCases(testCasesToRun.size());
        submission.setExecutionTimeMs(executionTime);
        codeSubmissionRepository.save(submission);

        SubmitCodeResponse response = new SubmitCodeResponse();
        response.setStatus(submissionStatus.name());
        response.setTestCasesPassed(passed);
        response.setTotalTestCases(testCasesToRun.size());
        response.setExecutionTimeMs(executionTime);
        response.setTestCaseResults(results);

        return response;
    }

    private String runSingleTestCase(String language, String code, String input, String expectedOutput) throws Exception {
        Path tempWorkDir = Files.createTempDirectory(Path.of(tempDir), "codeexec_");
        try {
            String output;
            switch (language.toLowerCase()) {
                case "java":
                    output = executeJava(code, input, tempWorkDir);
                    break;
                case "python":
                case "python3":
                    output = executePython(code, input, tempWorkDir);
                    break;
                case "javascript":
                case "js":
                case "node":
                    output = executeJavaScript(code, input, tempWorkDir);
                    break;
                case "cpp":
                case "c++":
                    output = executeCpp(code, input, tempWorkDir);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported language: " + language);
            }
            return output != null ? output.trim() : "";
        } finally {
            deleteDirectory(tempWorkDir);
        }
    }

    private String executeJava(String code, String input, Path workDir) throws Exception {
        Files.writeString(workDir.resolve("Solution.java"), code);

        StringBuilder runnerCode = new StringBuilder();
        runnerCode.append("import java.util.*;\n");
        runnerCode.append("public class Main {\n");
        runnerCode.append("    public static void main(String[] args) {\n");
        runnerCode.append("        Scanner sc = new Scanner(System.in);\n");
        runnerCode.append("        StringBuilder sb = new StringBuilder();\n");
        runnerCode.append("        while (sc.hasNextLine()) {\n");
        runnerCode.append("            String line = sc.nextLine();\n");
        runnerCode.append("            if (line.isEmpty()) break;\n");
        runnerCode.append("            sb.append(line).append(\"\\n\");\n");
        runnerCode.append("        }\n");
        runnerCode.append("        sc.close();\n");
        runnerCode.append("        String input = sb.toString().trim();\n");
        runnerCode.append("        String result = new Solution().solve(input);\n");
        runnerCode.append("        System.out.print(result);\n");
        runnerCode.append("    }\n");
        runnerCode.append("}\n");
        Files.writeString(workDir.resolve("Main.java"), runnerCode.toString());

        ProcessBuilder pb = new ProcessBuilder("javac", "Solution.java", "Main.java");
        pb.directory(workDir.toFile());
        pb.redirectErrorStream(true);
        Process compileProcess = pb.start();
        boolean compiled = compileProcess.waitFor(30, TimeUnit.SECONDS);
        if (!compiled) {
            compileProcess.destroyForcibly();
            throw new RuntimeException("Compilation timeout");
        }
        if (compileProcess.exitValue() != 0) {
            String error = new String(compileProcess.getInputStream().readAllBytes());
            throw new RuntimeException("Compilation error:\n" + error);
        }

        return runProcessWithInput(workDir, input, "java", "Main");
    }

    private String executePython(String code, String input, Path workDir) throws Exception {
        StringBuilder fullCode = new StringBuilder(code);
        fullCode.append("\n\nimport sys\n");
        fullCode.append("if __name__ == '__main__':\n");
        fullCode.append("    input_str = sys.stdin.read().strip()\n");
        fullCode.append("    solver = Solution()\n");
        fullCode.append("    result = solver.solve(input_str)\n");
        fullCode.append("    print(result, end='')\n");

        Files.writeString(workDir.resolve("solution.py"), fullCode.toString());
        return runProcessWithInput(workDir, input, "python3", "solution.py");
    }

    private String executeJavaScript(String code, String input, Path workDir) throws Exception {
        StringBuilder fullCode = new StringBuilder(code);
        fullCode.append("\n\nconst readline = require('readline');\n");
        fullCode.append("const rl = readline.createInterface({ input: process.stdin });\n");
        fullCode.append("let inputLines = [];\n");
        fullCode.append("rl.on('line', (line) => { inputLines.push(line); });\n");
        fullCode.append("rl.on('close', () => {\n");
        fullCode.append("    const solver = new Solution();\n");
        fullCode.append("    const result = solver.solve(inputLines.join('\\n'));\n");
        fullCode.append("    process.stdout.write(result);\n");
        fullCode.append("});\n");

        Files.writeString(workDir.resolve("solution.js"), fullCode.toString());
        return runProcessWithInput(workDir, input, "node", "solution.js");
    }

    private String executeCpp(String code, String input, Path workDir) throws Exception {
        Files.writeString(workDir.resolve("solution.cpp"), code);

        ProcessBuilder pb = new ProcessBuilder("g++", "-std=c++17", "-o", "solution", "solution.cpp");
        pb.directory(workDir.toFile());
        pb.redirectErrorStream(true);
        Process compileProcess = pb.start();
        boolean compiled = compileProcess.waitFor(30, TimeUnit.SECONDS);
        if (!compiled) {
            compileProcess.destroyForcibly();
            throw new RuntimeException("Compilation timeout");
        }
        if (compileProcess.exitValue() != 0) {
            String error = new String(compileProcess.getInputStream().readAllBytes());
            throw new RuntimeException("Compilation error:\n" + error);
        }

        return runProcessWithInput(workDir, input, "./solution");
    }

    private String runProcessWithInput(Path workDir, String input, String... command) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workDir.toFile());
        pb.redirectErrorStream(true);

        Process process = pb.start();

        if (input != null && !input.isEmpty()) {
            try (var os = process.getOutputStream()) {
                os.write(input.getBytes());
                os.flush();
            }
        }

        boolean finished = process.waitFor(EXECUTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            throw new RuntimeException("Time Limit Exceeded");
        }

        String output = new String(process.getInputStream().readAllBytes()).trim();
        if (process.exitValue() != 0) {
            throw new RuntimeException("Runtime error (exit code " + process.exitValue() + "):\n" + output);
        }
        return output;
    }

    private boolean compareOutput(String actual, String expected) {
        if (actual == null && expected == null) return true;
        if (actual == null || expected == null) return false;
        String a = actual.trim().replaceAll("\\s+", " ");
        String e = expected.trim().replaceAll("\\s+", " ");
        return a.equals(e);
    }

    private void deleteDirectory(Path path) {
        try {
            if (Files.exists(path)) {
                try (var walk = Files.walk(path)) {
                    walk.sorted(Comparator.reverseOrder())
                            .map(java.nio.file.Path::toFile)
                            .forEach(File::delete);
                }
            }
        } catch (IOException ignored) {}
    }
}
