package com.maksi.learnspringboot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class CourseController {
    String pathToDB;
    JSONArray dbContent;
    public CourseController() throws IOException, ParseException {
        this.pathToDB = "db/db.json";
        this.dbContent = this.getFileContent();
    }

    // Read from "DB"
//    private String extractData() throws IOException {
//        try  {
//            String content = new String(Files.readAllBytes(this.file.toPath()));
//            return content;
//        } catch (IOException e){
//            e.printStackTrace();
//            throw e;
//        }
//    }

    // Get a Path to a "DB" file
    private JSONArray getFileContent() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader fileReader = new FileReader(this.pathToDB);
        return (JSONArray) jsonParser.parse(fileReader);
    }

    // Handling GET Request to /courses endpoint (Getting all the courses in the DB)
    @GetMapping("/courses")
    public String retrieveCourses() throws IOException {
        return this.dbContent.toJSONString();
    }

    // Handling POST Request to /courses endpoint (adding course to DB)
    @PostMapping(value = "/courses")
    public void postCourse(@RequestBody Course course) throws ParseException, IOException {
        JSONArray parsed = this.dbContent;


        int id = parsed.size()+1;
        Map<String, Object> courseAsMap = new HashMap<>() {{
            put("Id", id);
            put("Name", course.getName());
            put("Author", course.getAuthor());
        }};

        JSONObject courseObject = new JSONObject(courseAsMap);
        parsed.add(courseObject);
        System.out.println(parsed);
        this.WriteToDb(parsed);
    }

    // Writing result to the "DB" file
    private void WriteToDb(JSONArray lstOfCourses) throws IOException {
        String jsonString = lstOfCourses.toJSONString();
        FileWriter fileWriter = new FileWriter(this.pathToDB);
        fileWriter.write(jsonString);
        fileWriter.flush();
    }

    @GetMapping("/courses/{id}")
    public String retrieveSpecificCourse(@PathVariable Long id) throws IOException, NotFoundException {
        JSONArray content = this.dbContent;
        for (Object o : content) {
            JSONObject obj = (JSONObject) o;
            if (Objects.equals((Long) obj.get("Id"), id)) {
                return obj.toJSONString();
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }

    @DeleteMapping("/courses/{id}")
    public String deleteSpecificCourse(@PathVariable Long id) throws IOException, NotFoundException {
        JSONArray content = this.dbContent;
        for (int i = 0; i<content.size(); i++) {
            JSONObject obj = (JSONObject) content.get(i);
            if (Objects.equals((Long) obj.get("Id"), id)) {
                content.remove(i);
                this.WriteToDb(content);
                return obj.toJSONString();
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }

}
