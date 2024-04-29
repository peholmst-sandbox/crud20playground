package org.vaadin.playground.crud20.sampledata;

import java.util.ArrayList;

public class Employee {
    private String name;
    private String role;
    private String profilePicUrl;

    public Employee(String name, String role, String profilePicUrl) {
        this.name = name;
        this.role = role;
        this.profilePicUrl = profilePicUrl;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public static ArrayList<Employee> createEmployees() {
        String[][] items = {
                {"Esther Howard", "Software Development Manager", "esther_howard.jpg"},
                {"Jane Cooper", "Project Manager", "jane_cooper.jpg"},
                {"Kristin Watson", "Team Leader", "kristin_watson.jpg"},
                {"Cody Fisher", "Scrum Master", "cody_fisher.jpg"},
                {"Wade Warren", "Ethical Hacker", "wade_warren.jpg"},
                {"Savannah Nguyen", "Software Developer", "savannah_nguyen.jpg"},
                {"Eleanor Pena", "Software Tester", "eleanor_pena.jpg"},
                {"Arlene McCoy", "Ethical Hacker", "arlene_mccoy.jpg"},
                {"Albert Flores", "UI/UX Designer", "albert_flores.jpg"},
                {"Ralph Edwards", "Software Tester", "ralph_edwards.jpg"},
                {"Darlene Robertson", "Software Development Manager", "darlene_robertson.jpg"},
                {"Kathryn Murphy", "Project Manager", "kathryn_murphy.jpg"},
                {"Annette Black", "Software Development Manager", "annette_black.jpg"},
                {"Theresa Webb", "Software Tester", "theresa_webb.jpg"},
                {"Cameron Williamson", "UI/UX Designer", "cameron_williamson.jpg"}
        };
        ArrayList<Employee> parties = new ArrayList<>();
        for (String item[] : items) {
            parties.add(new Employee( item[0],  item[1],  item[2]));
        }
        return parties;
    }
}

