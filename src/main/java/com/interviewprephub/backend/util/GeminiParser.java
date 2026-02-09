package com.interviewprephub.backend.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class GeminiParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private GeminiParser() {
        // utility class
    }

    // ---------------- COMMON CLEANUP ----------------

    /**
     * Removes markdown code fences and trims whitespace.
     */
    private static String sanitize(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replaceAll("(?s)```json", "")
                .replaceAll("(?s)```", "")
                .trim();
    }

    // ---------------- QUESTIONS PARSER ----------------

    /**
     * Expected JSON:
     * {
     *   "questions": [
     *     { "question": "..." }
     *   ]
     * }
     */
    public static Map<String, String> parseQuestions(String responseText) {

        Map<String, String> result = new HashMap<>();

        try {
            String cleanJson = sanitize(responseText);
            JsonNode root = objectMapper.readTree(cleanJson);

            JsonNode questionsNode = null;

            // Case 1: { "questions": [...] }
            if (root.has("questions")) {
                questionsNode = root.get("questions");
            }

            // Case 2: { "result": { "questions": [...] } }
            else if (root.has("result") && root.get("result").has("questions")) {
                questionsNode = root.get("result").get("questions");
            }

            // Case 3: questions as array of strings
            if (questionsNode != null && questionsNode.isArray()) {
                int index = 1;

                for (JsonNode q : questionsNode) {
                    if (q.isTextual()) {
                        result.put("q" + index++, q.asText());
                    } else if (q.has("question")) {
                        result.put("q" + index++, q.get("question").asText());
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to parse questions JSON. Raw response:\n" + responseText,
                    e
            );
        }

        return result;
    }


    // ---------------- FEEDBACK PARSER ----------------

    /**
     * Expected JSON:
     * {
     *   "feedback": "...",
     *   "actualanswer": "..."
     * }
     */
    public static Map<String, String> parseFeedback(String responseText) {

        Map<String, String> result = new HashMap<>();

        try {
            String cleanJson = sanitize(responseText);
            JsonNode root = objectMapper.readTree(cleanJson);

            // -------- feedback (object -> string) --------
            JsonNode feedbackNode = root.path("feedback");
            if (!feedbackNode.isMissingNode()) {
                result.put(
                        "feedback",
                        feedbackNode.toPrettyString()
                );
            } else {
                result.put("feedback", "");
            }

            // -------- correct answer --------
            result.put(
                    "actualAnswer",
                    root.path("correct_answer").asText("")
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to parse feedback JSON. Raw response:\n" + responseText,
                    e
            );
        }

        return result;
    }

}
