package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.EmployeeDAO;
import org.example.dao.TransportCompanyDAO;
import org.example.dao.VehicleDAO;
import org.example.dao.TransportDAO;
import org.example.entity.Transport;
import org.example.entity.TransportCompany;
import org.example.entity.TransportEmployee;
import org.example.entity.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Console;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) throws IOException {
        SessionFactoryUtil.getSessionFactory().openSession();
        System.out.println("=== Transport Company CLI ===");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;

        try {
            while (running) {
                printMenu();
                System.out.flush();
                
                String input = reader.readLine();
                if (input == null) {
                    System.out.println("End of input stream detected, exiting.");
                    break;
                }

                input = input.trim();
                if (input.equalsIgnoreCase("q")) {
                    running = false;
                    continue;
                }

                handleChoice(input, reader);
            }
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("Error closing reader: " + e.getMessage());
            }
            SessionFactoryUtil.getSessionFactory().close();
            System.out.println("Exited.");
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Transport Company CLI ===");
        System.out.println("1 - Create transport company");
        System.out.println("2 - Edit transport company");
        System.out.println("3 - Delete transport company");
        System.out.println("4 - List all companies");
        System.out.println("5 - Create vehicle");
        System.out.println("6 - Edit vehicle");
        System.out.println("7 - Delete vehicle");
        System.out.println("8 - List vehicles by company");
        System.out.println("9 - Create employee");
        System.out.println("10 - Edit employee");
        System.out.println("11 - Delete employee");
        System.out.println("12 - List employees by company");
        System.out.println("13 - Create transport");
        System.out.println("14 - List transports by destination");
        System.out.println("15 - Export transports to CSV");
        System.out.println("q - Quit");
        System.out.print("Enter option: ");
    }

    private static void handleChoice(String choice, BufferedReader reader) {
        try {
            switch (choice) {
                case "1" -> createCompany(reader);
                case "2" -> editCompany(reader);
                case "3" -> deleteCompany(reader);
                case "4" -> listCompanies();
                case "5" -> createVehicle(reader);
                case "6" -> editVehicle(reader);
                case "7" -> deleteVehicle(reader);
                case "8" -> listVehiclesByCompany(reader);
                case "9" -> createEmployee(reader);
                case "10" -> editEmployee(reader);
                case "11" -> deleteEmployee(reader);
                case "12" -> listEmployeesByCompany(reader);
                case "13" -> createTransport(reader);
                case "14" -> listTransportsByDestination(reader);
                case "15" -> exportTransports(reader);
                default -> System.out.println("Invalid option");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Company handlers
    private static void createCompany(BufferedReader reader) throws IOException {
        System.out.print("Company name: ");
        System.out.flush();
        String name = reader.readLine().trim();
        TransportCompany company = new TransportCompany();
        company.setName(name);
        TransportCompanyDAO.saveCompany(company);
        System.out.println("Saved company with id: " + company.getId());
    }

    private static void editCompany(BufferedReader reader) throws IOException {
        long id = readId(reader, "Company id: ");
        TransportCompany company = TransportCompanyDAO.getCompanyById(id);
        if (company == null) {
            System.out.println("Company not found");
            return;
        }
        System.out.print("New name (current: " + company.getName() + "): ");
        System.out.flush();
        String name = reader.readLine().trim();
        if (!name.isEmpty()) company.setName(name);
        TransportCompanyDAO.updateCompany(company);
        System.out.println("Updated company");
    }

    private static void deleteCompany(BufferedReader reader) throws IOException {
        long id = readId(reader, "Company id: ");
        TransportCompany company = TransportCompanyDAO.getCompanyById(id);
        if (company == null) {
            System.out.println("Company not found");
            return;
        }
        TransportCompanyDAO.deleteCompany(company);
        System.out.println("Deleted company");
    }

    private static void listCompanies() {
        List<TransportCompany> companies = TransportCompanyDAO.getCompanies();
        if (companies.isEmpty()) {
            System.out.println("No companies found");
            return;
        }
        companies.forEach(c -> System.out.println(c.getId() + " - " + c.getName()));
    }

    // Vehicle handlers
    private static void createVehicle(BufferedReader reader) throws IOException {
        long companyId = readId(reader, "Company id: ");
        TransportCompany company = TransportCompanyDAO.getCompanyById(companyId);
        if (company == null) { System.out.println("Company not found"); return; }
        System.out.print("Vehicle type: ");
        System.out.flush();
        String type = reader.readLine().trim();
        Vehicle vehicle = new Vehicle();
        vehicle.setType(type);
        vehicle.setCompany(company);
        VehicleDAO.saveVehicle(vehicle);
        System.out.println("Saved vehicle with id: " + vehicle.getId());
    }

    private static void editVehicle(BufferedReader reader) throws IOException {
        long id = readId(reader, "Vehicle id: ");
        Vehicle vehicle = VehicleDAO.getVehicleById(id);
        if (vehicle == null) { System.out.println("Vehicle not found"); return; }
        System.out.print("New type (current: " + vehicle.getType() + "): ");
        System.out.flush();
        String type = reader.readLine().trim();
        if (!type.isEmpty()) vehicle.setType(type);
        VehicleDAO.updateVehicle(vehicle);
        System.out.println("Updated vehicle");
    }

    private static void deleteVehicle(BufferedReader reader) throws IOException {
        long id = readId(reader, "Vehicle id: ");
        Vehicle vehicle = VehicleDAO.getVehicleById(id);
        if (vehicle == null) { System.out.println("Vehicle not found"); return; }
        VehicleDAO.deleteVehicle(vehicle);
        System.out.println("Deleted vehicle");
    }

    private static void listVehiclesByCompany(BufferedReader reader) throws IOException {
        long companyId = readId(reader, "Company id: ");
        List<Vehicle> vehicles = VehicleDAO.getVehiclesByCompany(companyId);
        if (vehicles.isEmpty()) { System.out.println("No vehicles found"); return; }
        vehicles.forEach(v -> System.out.println(v.getId() + " - " + v.getType()));
    }

    // Employee handlers
    private static void createEmployee(BufferedReader reader) throws IOException {
        long companyId = readId(reader, "Company id: ");
        TransportCompany company = TransportCompanyDAO.getCompanyById(companyId);
        if (company == null) { System.out.println("Company not found"); return; }
        System.out.print("Employee name: ");
        System.out.flush();
        String name = reader.readLine().trim();
        TransportEmployee employee = new TransportEmployee();
        employee.setName(name);
        employee.setCompany(company);
        EmployeeDAO.saveEmployee(employee);
        System.out.println("Saved employee with id: " + employee.getId());
    }

    private static void editEmployee(BufferedReader reader) throws IOException {
        long id = readId(reader, "Employee id: ");
        TransportEmployee employee = EmployeeDAO.getEmployeeById(id);
        if (employee == null) { System.out.println("Employee not found"); return; }
        System.out.print("New name (current: " + employee.getName() + "): ");
        System.out.flush();
        String name = reader.readLine().trim();
        if (!name.isEmpty()) employee.setName(name);
        EmployeeDAO.updateEmployee(employee);
        System.out.println("Updated employee");
    }

    private static void deleteEmployee(BufferedReader reader) throws IOException {
        long id = readId(reader, "Employee id: ");
        TransportEmployee employee = EmployeeDAO.getEmployeeById(id);
        if (employee == null) { System.out.println("Employee not found"); return; }
        EmployeeDAO.deleteEmployee(employee);
        System.out.println("Deleted employee");
    }

    private static void listEmployeesByCompany(BufferedReader reader) throws IOException {
        long companyId = readId(reader, "Company id: ");
        List<TransportEmployee> employees = EmployeeDAO.getEmployeesByCompany(companyId);
        if (employees.isEmpty()) { System.out.println("No employees found"); return; }
        employees.forEach(e -> System.out.println(e.getId() + " - " + e.getName()));
    }

    // Transport handlers
    private static void createTransport(BufferedReader reader) throws IOException {
        long vehicleId = readId(reader, "Vehicle id: ");
        Vehicle vehicle = VehicleDAO.getVehicleById(vehicleId);
        if (vehicle == null) { System.out.println("Vehicle not found"); return; }

        long driverId = readId(reader, "Driver id: ");
        TransportEmployee driver = EmployeeDAO.getEmployeeById(driverId);
        if (driver == null) { System.out.println("Driver not found"); return; }

        System.out.print("Start point: ");
        System.out.flush();
        String startPoint = reader.readLine().trim();

        System.out.print("End point: ");
        System.out.flush();
        String endPoint = reader.readLine().trim();

        System.out.print("Departure date (yyyy-MM-dd HH:mm): ");
        System.out.flush();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime departureDate = LocalDateTime.parse(reader.readLine().trim(), formatter);

        System.out.print("Price: ");
        System.out.flush();
        BigDecimal price = new BigDecimal(reader.readLine().trim());

        Transport transport = new Transport();
        transport.setVehicle(vehicle);
        transport.setDriver(driver);
        transport.setStartPoint(startPoint);
        transport.setEndPoint(endPoint);
        transport.setDepartureDate(departureDate);
        transport.setPrice(price);

        TransportDAO.saveTransport(transport);
        System.out.println("Saved transport with id: " + transport.getId());
    }

    private static void listTransportsByDestination(BufferedReader reader) throws IOException {
        System.out.print("Destination: ");
        System.out.flush();
        String destination = reader.readLine().trim();
        List<Transport> transports = TransportDAO.getTransportsByDestination(destination);
        if (transports.isEmpty()) { System.out.println("No transports found"); return; }
        transports.forEach(t -> System.out.println(t.getId() + " - From: " + t.getStartPoint() + " To: " + t.getEndPoint() +
                " Date: " + t.getDepartureDate() + " Driver: " + t.getDriver().getName()));
    }

    private static void exportTransports(BufferedReader reader) throws IOException {
        long companyId = readId(reader, "Company id: ");
        List<Transport> transports = TransportDAO.getTransportsByCompany(companyId);
        if (transports.isEmpty()) { System.out.println("No transports found"); return; }
        TransportDAO.exportTransportsToCSV(transports, "transports.csv");
        System.out.println("Exported transports to transports.csv");
    }

    private static long readId(BufferedReader reader, String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            System.out.flush();
            String input = reader.readLine().trim();
            if (input.matches("\\d+")) {
                return Long.parseLong(input);
            }
            System.out.println("Warning: Please enter a valid number.");
        }
    }

    private static class InputEndedException extends RuntimeException {}
}