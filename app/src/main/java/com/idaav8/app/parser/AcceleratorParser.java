package com.idaav8.app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.idaav8.app.dto.AccelInfoAux;



@Component
public class AcceleratorParser {

    private static final Pattern PATTERN = Pattern.compile(
        "^(\\S+)\\s+(.*?)\\s+(Deployed|Not-Deployed)(?:\\s+(\\S+)\\s+(\\S+))?$"
    );

    public List<AccelInfoAux> parseAccelsInfo(String output) {

        List<AccelInfoAux> result = new ArrayList<>();

        String[] lines = output.split("\\R");

        // Saltar encabezado
        for (int i = 1; i < lines.length; i++) {

            String line = lines[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            Matcher matcher = PATTERN.matcher(line);

            if (matcher.matches()) {

                result.add(
                    new AccelInfoAux(
                        matcher.group(1), // system
                        matcher.group(2), // assignment
                        matcher.group(3), // deploymentStatus
                        matcher.group(4), // dns
                        matcher.group(5)  // ip
                    )
                );
            }
        }

        return result;
    }
}
