package com.interviewprephub.backend.config;

import com.interviewprephub.backend.entity.*;
import com.interviewprephub.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CodingQuestionRepository codingQuestionRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (codingQuestionRepository.count() > 0) {
            return;
        }

        Map<String, Company> companies = ensureCompanies();

        seedTwoSum(companies);
        seedValidParentheses(companies);
        seedLongestSubstring(companies);
        seedThreeSum(companies);
        seedMergeSortedLists(companies);
        seedWordSearch(companies);
    }

    private Map<String, Company> ensureCompanies() {
        String[] names = {"Google", "Amazon", "Meta", "Microsoft", "Apple", "Netflix"};
        Map<String, Company> map = new HashMap<>();
        for (String name : names) {
            Company company = companyRepository.findByNameContainingIgnoreCase(name, null)
                    .stream().findFirst().orElseGet(() -> {
                        Company c = new Company();
                        c.setName(name);
                        return companyRepository.save(c);
                    });
            map.put(name, company);
        }
        return map;
    }

    private void seedTwoSum(Map<String, Company> companies) {
        CodingQuestion q = new CodingQuestion();
        q.setTitle("Two Sum");
        q.setDifficulty(CodingQuestion.Difficulty.EASY);
        q.setDescription(
            "<h3>Given an array of integers <code>nums</code> and an integer <code>target</code>, return indices of the two numbers that add up to <code>target</code>.</h3>"
            + "<br/><h4>Input Format:</h4>"
            + "<p>The first line contains an integer N, the size of the array.</p>"
            + "<p>The second line contains N space-separated integers.</p>"
            + "<p>The third line contains the target integer.</p>"
            + "<br/><h4>Output Format:</h4>"
            + "<p>Return the two indices as space-separated integers in ascending order.</p>"
            + "<br/><h4>Constraints:</h4>"
            + "<ul>"
            + "<li>2 ≤ N ≤ 10<sup>4</sup></li>"
            + "<li>-10<sup>9</sup> ≤ nums[i] ≤ 10<sup>9</sup></li>"
            + "<li>-10<sup>9</sup> ≤ target ≤ 10<sup>9</sup></li>"
            + "<li>Exactly one valid solution exists.</li>"
            + "</ul>"
        );
        q.setCompanies(Set.of(companies.get("Google"), companies.get("Amazon")));
        q.setStarterCodeJava(
            "import java.util.*;\n\npublic class Solution {\n    public String solve(String input) {\n        // Parse input and return indices\n        return \"\";\n    }\n}"
        );
        q.setStarterCodePython(
            "class Solution:\n    def solve(self, input_str: str) -> str:\n        # Parse input and return indices\n        return \"\"\n"
        );
        q.setStarterCodeCpp(
            "#include <string>\nusing namespace std;\n\nclass Solution {\npublic:\n    string solve(string input) {\n        // Parse input and return indices\n        return \"\";\n    }\n};\n"
        );
        q.setStarterCodeJavascript(
            "class Solution {\n    solve(input) {\n        // Parse input and return indices\n        return \"\";\n    }\n}\n"
        );
        codingQuestionRepository.save(q);

        testCaseRepository.saveAll(List.of(
            tc(q, "4\n2 7 11 15\n9",     "0 1",       false, 0, "2 + 7 = 9"),
            tc(q, "3\n3 2 4\n6",         "1 2",       false, 1, "2 + 4 = 6"),
            tc(q, "2\n3 3\n6",           "0 1",       false, 2, "3 + 3 = 6"),
            tc(q, "5\n1 5 3 9 2\n11",    "1 3",       true,  3, null),
            tc(q, "4\n-1 -2 -3 -4\n-5",  "1 2",       true,  4, null),
            tc(q, "6\n0 4 3 0 7 8\n0",   "0 3",       true,  5, null)
        ));
    }

    private void seedValidParentheses(Map<String, Company> companies) {
        CodingQuestion q = new CodingQuestion();
        q.setTitle("Valid Parentheses");
        q.setDifficulty(CodingQuestion.Difficulty.EASY);
        q.setDescription(
            "<h3>Given a string containing just the characters <code>(){}[]</code>, determine if the input string is valid.</h3>"
            + "<br/><p>A string is valid if:</p>"
            + "<ul>"
            + "<li>Open brackets must be closed by the same type of brackets.</li>"
            + "<li>Open brackets must be closed in the correct order.</li>"
            + "<li>Every close bracket has a corresponding open bracket of the same type.</li>"
            + "</ul>"
            + "<br/><h4>Input Format:</h4>"
            + "<p>A single line containing the bracket string.</p>"
            + "<br/><h4>Output Format:</h4>"
            + "<p>Return <code>true</code> if valid, <code>false</code> otherwise.</p>"
        );
        q.setCompanies(Set.of(companies.get("Meta"), companies.get("Microsoft")));
        q.setStarterCodeJava(
            "import java.util.*;\n\npublic class Solution {\n    public String solve(String input) {\n        // Return \"true\" or \"false\"\n        return \"\";\n    }\n}"
        );
        q.setStarterCodePython(
            "class Solution:\n    def solve(self, input_str: str) -> str:\n        # Return \"true\" or \"false\"\n        return \"\"\n"
        );
        q.setStarterCodeCpp(
            "#include <string>\nusing namespace std;\n\nclass Solution {\npublic:\n    string solve(string input) {\n        // Return \"true\" or \"false\"\n        return \"\";\n    }\n};\n"
        );
        q.setStarterCodeJavascript(
            "class Solution {\n    solve(input) {\n        // Return \"true\" or \"false\"\n        return \"\";\n    }\n}\n"
        );
        codingQuestionRepository.save(q);

        testCaseRepository.saveAll(List.of(
            tc(q, "()[]{}",      "true",  false, 0, "All pairs in order"),
            tc(q, "([)]",        "false", false, 1, "Wrong order"),
            tc(q, "{[]}",        "true",  false, 2, "Nested correctly"),
            tc(q, "(]",          "false", true,  3, null),
            tc(q, "([{}])",      "true",  true,  4, null),
            tc(q, "((((",        "false", true,  5, null),
            tc(q, "",            "true",  true,  6, null)
        ));
    }

    private void seedLongestSubstring(Map<String, Company> companies) {
        CodingQuestion q = new CodingQuestion();
        q.setTitle("Longest Substring Without Repeating Characters");
        q.setDifficulty(CodingQuestion.Difficulty.MEDIUM);
        q.setDescription(
            "<h3>Given a string, find the length of the longest substring without repeating characters.</h3>"
            + "<br/><h4>Input Format:</h4>"
            + "<p>A single line containing the input string.</p>"
            + "<br/><h4>Output Format:</h4>"
            + "<p>Return the length as a string.</p>"
            + "<br/><h4>Constraints:</h4>"
            + "<ul><li>0 ≤ len(s) ≤ 5 × 10<sup>4</sup></li>"
            + "<li>s consists of English letters, digits, symbols and spaces.</li></ul>"
            + "<br/><h4>Example:</h4>"
            + "<p>Input: <code>abcabcbb</code></p>"
            + "<p>Output: <code>3</code> (substring is <code>abc</code>)</p>"
        );
        q.setCompanies(Set.of(companies.get("Amazon"), companies.get("Google")));
        q.setStarterCodeJava(
            "import java.util.*;\n\npublic class Solution {\n    public String solve(String input) {\n        // Return length as string\n        return \"\";\n    }\n}"
        );
        q.setStarterCodePython(
            "class Solution:\n    def solve(self, input_str: str) -> str:\n        # Return length as string\n        return \"\"\n"
        );
        q.setStarterCodeCpp(
            "#include <string>\nusing namespace std;\n\nclass Solution {\npublic:\n    string solve(string input) {\n        // Return length as string\n        return \"\";\n    }\n};\n"
        );
        q.setStarterCodeJavascript(
            "class Solution {\n    solve(input) {\n        // Return length as string\n        return \"\";\n    }\n}\n"
        );
        codingQuestionRepository.save(q);

        testCaseRepository.saveAll(List.of(
            tc(q, "abcabcbb",      "3", false, 0, "abc"),
            tc(q, "bbbbb",         "1", false, 1, "b"),
            tc(q, "pwwkew",        "3", false, 2, "wke"),
            tc(q, "",              "0", true,  3, null),
            tc(q, "au",            "2", true,  4, null),
            tc(q, "dvdf",          "3", true,  5, null),
            tc(q, "abcdef",        "6", true,  6, null)
        ));
    }

    private void seedThreeSum(Map<String, Company> companies) {
        CodingQuestion q = new CodingQuestion();
        q.setTitle("3Sum");
        q.setDifficulty(CodingQuestion.Difficulty.MEDIUM);
        q.setDescription(
            "<h3>Given an integer array nums, return all unique triplets <code>[nums[i], nums[j], nums[k]]</code> such that <code>i != j != k</code> and <code>nums[i] + nums[j] + nums[k] == 0</code>.</h3>"
            + "<br/><h4>Input Format:</h4>"
            + "<p>First line: integer N (size of array).</p>"
            + "<p>Second line: N space-separated integers.</p>"
            + "<br/><h4>Output Format:</h4>"
            + "<p>For each triplet, print the three numbers separated by spaces, one triplet per line. Triplets should be in ascending order (both within each triplet and across triplets). If no triplet, return \"No triplet\".</p>"
        );
        q.setCompanies(Set.of(companies.get("Meta"), companies.get("Apple")));
        q.setStarterCodeJava(
            "import java.util.*;\n\npublic class Solution {\n    public String solve(String input) {\n        // Return triplets or \"No triplet\"\n        return \"\";\n    }\n}"
        );
        q.setStarterCodePython(
            "class Solution:\n    def solve(self, input_str: str) -> str:\n        # Return triplets or \"No triplet\"\n        return \"\"\n"
        );
        q.setStarterCodeCpp(
            "#include <string>\nusing namespace std;\n\nclass Solution {\npublic:\n    string solve(string input) {\n        // Return triplets or \"No triplet\"\n        return \"\";\n    }\n};\n"
        );
        q.setStarterCodeJavascript(
            "class Solution {\n    solve(input) {\n        // Return triplets or \"No triplet\"\n        return \"\";\n    }\n}\n"
        );
        codingQuestionRepository.save(q);

        testCaseRepository.saveAll(List.of(
            tc(q, "6\n-1 0 1 2 -1 -4",   "-1 -1 2\n-1 0 1", false, 0, "Two unique triplets"),
            tc(q, "3\n0 0 0",            "0 0 0",            false, 1, "Single triplet"),
            tc(q, "3\n1 2 3",            "No triplet",       false, 2, null),
            tc(q, "5\n-2 0 0 2 2",       "-2 0 2",           true,  3, null),
            tc(q, "4\n0 0 0 0",          "0 0 0",            true,  4, null)
        ));
    }

    private void seedMergeSortedLists(Map<String, Company> companies) {
        CodingQuestion q = new CodingQuestion();
        q.setTitle("Merge K Sorted Lists");
        q.setDifficulty(CodingQuestion.Difficulty.HARD);
        q.setDescription(
            "<h3>Given K sorted linked lists, merge them into one sorted list and return its head.</h3>"
            + "<br/><h4>Input Format:</h4>"
            + "<p>First line: integer K (number of lists).</p>"
            + "<p>Next K lines: each line contains space-separated integers representing a sorted list. A list may be empty.</p>"
            + "<br/><h4>Output Format:</h4>"
            + "<p>Return the merged sorted list as space-separated integers. If empty, return \"empty\".</p>"
        );
        q.setCompanies(Set.of(companies.get("Microsoft"), companies.get("Amazon")));
        q.setStarterCodeJava(
            "import java.util.*;\n\npublic class Solution {\n    public String solve(String input) {\n        // Return merged sorted list\n        return \"\";\n    }\n}"
        );
        q.setStarterCodePython(
            "class Solution:\n    def solve(self, input_str: str) -> str:\n        # Return merged sorted list\n        return \"\"\n"
        );
        q.setStarterCodeCpp(
            "#include <string>\nusing namespace std;\n\nclass Solution {\npublic:\n    string solve(string input) {\n        // Return merged sorted list\n        return \"\";\n    }\n};\n"
        );
        q.setStarterCodeJavascript(
            "class Solution {\n    solve(input) {\n        // Return merged sorted list\n        return \"\";\n    }\n}\n"
        );
        codingQuestionRepository.save(q);

        testCaseRepository.saveAll(List.of(
            tc(q, "3\n1 4 5\n1 3 4\n2 6",           "1 1 2 3 4 4 5 6", false, 0, null),
            tc(q, "2\n1 2 3\n4 5 6",                 "1 2 3 4 5 6",     false, 1, null),
            tc(q, "2\n\n1",                          "1",               false, 2, "One empty list"),
            tc(q, "1\n",                             "empty",           true,  3, null),
            tc(q, "3\n1\n2\n3",                      "1 2 3",           true,  4, null)
        ));
    }

    private void seedWordSearch(Map<String, Company> companies) {
        CodingQuestion q = new CodingQuestion();
        q.setTitle("Word Search");
        q.setDifficulty(CodingQuestion.Difficulty.MEDIUM);
        q.setDescription(
            "<h3>Given an M x N grid of characters and a word, determine if the word exists in the grid.</h3>"
            + "<br/><p>The word can be constructed from sequentially adjacent cells (horizontally or vertically adjacent). The same cell may not be used more than once.</p>"
            + "<br/><h4>Input Format:</h4>"
            + "<p>First line: M N (dimensions).</p>"
            + "<p>Next M lines: each line is a string of N characters representing a row.</p>"
            + "<p>Last line: the word to search for.</p>"
            + "<br/><h4>Output Format:</h4>"
            + "<p>Return <code>true</code> if the word exists, <code>false</code> otherwise.</p>"
        );
        q.setCompanies(Set.of(companies.get("Google"), companies.get("Netflix")));
        q.setStarterCodeJava(
            "import java.util.*;\n\npublic class Solution {\n    public String solve(String input) {\n        // Return \"true\" or \"false\"\n        return \"\";\n    }\n}"
        );
        q.setStarterCodePython(
            "class Solution:\n    def solve(self, input_str: str) -> str:\n        # Return \"true\" or \"false\"\n        return \"\"\n"
        );
        q.setStarterCodeCpp(
            "#include <string>\nusing namespace std;\n\nclass Solution {\npublic:\n    string solve(string input) {\n        // Return \"true\" or \"false\"\n        return \"\";\n    }\n};\n"
        );
        q.setStarterCodeJavascript(
            "class Solution {\n    solve(input) {\n        // Return \"true\" or \"false\"\n        return \"\";\n    }\n}\n"
        );
        codingQuestionRepository.save(q);

        testCaseRepository.saveAll(List.of(
            tc(q, "3 4\nABCE\nSFCS\nADEE\nABCCED", "true",  false, 0, "ABCCED exists"),
            tc(q, "3 4\nABCE\nSFCS\nADEE\nSEE",    "true",  false, 1, "SEE exists"),
            tc(q, "3 4\nABCE\nSFCS\nADEE\nABCD",   "false", false, 2, "ABCD does not exist"),
            tc(q, "1 1\nA\nA",                     "true",  true,  3, null),
            tc(q, "2 2\nAB\nCD\nAC",               "false", true,  4, null)
        ));
    }

    private TestCase tc(CodingQuestion q, String input, String expected, boolean hidden, int order, String explanation) {
        TestCase tc = new TestCase();
        tc.setCodingQuestion(q);
        tc.setInputData(input);
        tc.setExpectedOutput(expected);
        tc.setIsHidden(hidden);
        tc.setOrderIndex(order);
        tc.setExplanation(explanation);
        return tc;
    }
}
