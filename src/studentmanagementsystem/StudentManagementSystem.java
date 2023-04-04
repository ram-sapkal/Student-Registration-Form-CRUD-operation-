/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentmanagementsystem;

/**
 *
 * @author jagdish jadhav
 */
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class StudentManagementSystem extends JFrame implements ActionListener {
    JLabel titleLabel, nameLabel, ageLabel, courseLabel, idLabel;
    JTextField nameTextField, ageTextField, courseTextField, idTextField;
    JButton addButton, removeButton, updateButton, fetchButton;
    JTextArea outputTextArea;

    Connection conn;
    Statement stmt;

    public StudentManagementSystem() throws ClassNotFoundException {
        super("Student Management System");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        titleLabel = new JLabel("Student Management System");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(titleLabel);

        nameLabel = new JLabel("Name:");
        add(nameLabel);
        nameTextField = new JTextField(20);
        add(nameTextField);

        ageLabel = new JLabel("Age:");
        add(ageLabel);
        ageTextField = new JTextField(20);
        add(ageTextField);

        courseLabel = new JLabel("Course:");
        add(courseLabel);
        courseTextField = new JTextField(20);
        add(courseTextField);

        idLabel = new JLabel("ID:");
        add(idLabel);
        idTextField = new JTextField(20);
        add(idTextField);

        addButton = new JButton("Add");
        addButton.addActionListener(this);
        add(addButton);

        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);
        add(removeButton);

        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        add(updateButton);

        fetchButton = new JButton("Fetch");
        fetchButton.addActionListener(this);
        add(fetchButton);

        outputTextArea = new JTextArea(10, 40);
        outputTextArea.setEditable(false);
        add(outputTextArea);

        try {
            
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/sample/", "app", "app");
            stmt = conn.createStatement();
            //stmt.executeUpdate("CREATE TABLE IF NOT EXISTS students (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(50), age INT, course VARCHAR(50), PRIMARY KEY (id))");
        } catch (SQLException ex) {
            outputTextArea.setText("Error: " + ex.getMessage());
        }

        pack();
        setVisible(true);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        new StudentManagementSystem();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            try {
                String name = nameTextField.getText();
                int age = Integer.parseInt(ageTextField.getText());
                String course = courseTextField.getText();

                String sql = "INSERT INTO students (name, age, course) VALUES ('" + name + "', " + age + ", '" + course + "')";
                int affectedRows = stmt.executeUpdate(sql);

                if (affectedRows > 0) {
                    outputTextArea.setText("Student added.");
                } else {
                    outputTextArea.setText("Student not added.");
                }
            } catch (NumberFormatException ex) {
                outputTextArea.setText("Invalid age.");
            } catch (SQLException ex) {
                outputTextArea.setText("Error: " + ex.getMessage());
            }
        } else if (e.getSource() == removeButton) {
            try {
                int id = Integer.parseInt(idTextField.getText());

                String sql = "DELETE FROM students WHERE id = " + id;
                int affectedRows = stmt.executeUpdate(sql);

                if (affectedRows > 0) {
                    outputTextArea.setText("Student removed.");
                    nameTextField.setText("");
                    ageTextField.setText("");
                    courseTextField.setText("");
                } else {
                    outputTextArea.setText("Student not found.");
                }
            } catch (SQLException ex) {
                outputTextArea.setText("Error: " + ex.getMessage());
            }
        } else if (e.getSource() == updateButton) {
            try {
                int id = Integer.parseInt(idTextField.getText());
                String name = nameTextField.getText();
                int age = Integer.parseInt(ageTextField.getText());
                String course = courseTextField.getText();

                String sql = "UPDATE students SET name = '" + name + "', age = " + age + ", course = '" + course + "' WHERE id = " + id;
                int affectedRows = stmt.executeUpdate(sql);

                if (affectedRows > 0) {
                    outputTextArea.setText("Student updated.");
                } else {
                    outputTextArea.setText("Student not found.");
                }
            } catch (NumberFormatException ex) {
                outputTextArea.setText("Invalid age or ID.");
            } catch (SQLException ex) {
                outputTextArea.setText("Error: " + ex.getMessage());
            }
        } else if (e.getSource() == fetchButton) {
            try {
                String sql = "SELECT * FROM students";
                ResultSet rs = stmt.executeQuery(sql);

                outputTextArea.setText("");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String course = rs.getString("course");
                    outputTextArea.append("ID: " + id + "\nName: " + name + "\nAge: " + age + "\nCourse: " + course + "\n\n");
                }
            } catch (SQLException ex) {
                outputTextArea.setText("Error: " + ex.getMessage());
            }
        }
    }
}