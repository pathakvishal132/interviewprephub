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

    private static String sanitize(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replaceAll("(?s)```json", "")
                .replaceAll("(?s)```", "")
                .trim();
    }

    public static Map<String, String> parseQuestions(String responseText) {
        Map<String, String> result = new HashMap<>();
        try {
            String cleanJson = sanitize(responseText);
            JsonNode root = objectMapper.readTree(cleanJson);
            JsonNode questionsNode = null;
            if (root.has("questions")) {
                questionsNode = root.get("questions");
            }
            else if (root.has("result") && root.get("result").has("questions")) {
                questionsNode = root.get("result").get("questions");
            }
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
    public static String sanitizeMarkdown(String input) {

        return input
                .replaceAll("(?m)^#{1,6}\\s*", "")   // remove headings ##
                .replaceAll("\\*\\*(.*?)\\*\\*", "$1") // remove bold **
                .replaceAll("\\*(.*?)\\*", "$1")       // remove italic *
                .replaceAll("`", "")                   // remove backticks
                .replaceAll("\\|", "")                 // remove table pipes
                .replaceAll("\\n{2,}", "\n\n")         // normalize spacing
                .trim();
    }
    public static Map<String, String> parseFeedback(String responseText) {
        Map<String, String> result = new HashMap<>();
        try {
            String cleanJson = sanitize(responseText);
            System.out.println(cleanJson);
            JsonNode root = objectMapper.readTree(cleanJson);
            JsonNode feedbackNode = root.path("feedback");
            if (!feedbackNode.isMissingNode()) {
                result.put(
                        "feedback",
                        feedbackNode.toPrettyString()
                );
            } else {
                result.put("feedback", "");
            }
            result.put(
                    "actualAnswer",sanitizeMarkdown(
                    root.path("actualanswer").asText(""))
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
