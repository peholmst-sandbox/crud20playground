package org.vaadin.playground.crud20.sampledata;

import java.util.ArrayList;

public class Employee {
    private String name;
    private String role;

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public static ArrayList<Employee> createEmployees() {
        String[][] items = {
                {"Esther Howard", "Software Development Manager"},
                {"Jane Cooper", "Project Manager"},
                {"Kristin Watson", "Team Leader"},
                {"Cody Fisher", "Scrum Master"},
                {"Wade Warren", "Ethical Hacker"},
                {"Savannah Nguyen", "Software Developer"},
                {"Eleanor Pena", "Software Tester"},
                {"Arlene McCoy", "Ethical Hacker"},
                {"Albert Flores", "UI/UX Designer"},
                {"Ralph Edwards", "Software Tester"},
                {"Darlene Robertson", "Software Development Manager"},
                {"Kathryn Murphy", "Project Manager"},
                {"Annette Black", "Software Development Manager"},
                {"Theresa Webb", "Software Tester"},
                {"Cameron Williamson", "UI/UX Designer"}
        };
        ArrayList<Employee> parties = new ArrayList<>();
        for (String item[] : items) {
            parties.add(new Employee( item[0],  item[1]));
        }
        return parties;
    }
}

