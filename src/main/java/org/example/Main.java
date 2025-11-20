package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.EmployeeDAO;
import org.example.dao.TransportCompanyDAO;
import org.example.dao.VehicleDAO;
import org.example.dao.TransportDAO;
import org.example.dao.TransportTypeDAO;
import org.example.dao.QualificationDAO;
import org.example.dto.TransportDto;
import org.example.dto.TransportCompanyDto;
import org.example.dto.TransportEmployeeDto;
import org.example.dto.VehicleDto;
import org.example.dto.TransportTypeDto;
import org.example.dto.QualificationDto;
import org.example.dto.DriverTransportCountDto;
import org.example.dto.CompanyRevenueDto;
import org.example.dto.DriverRevenueDto;
import org.example.entity.Transport;
import org.example.entity.TransportCompany;
import org.example.entity.TransportEmployee;
import org.example.entity.TransportType;
import org.example.entity.Vehicle;
import org.example.entity.Qualification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SessionFactoryUtil.getSessionFactory().openSession();

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
        System.out.println("1 - Companies");
        System.out.println("2 - Qualifications");
        System.out.println("3 - Transport Types");
        System.out.println("4 - Employees");
        System.out.println("5 - Vehicles");
        System.out.println("6 - Transports");
        System.out.println("7 - Reports");
        System.out.println("q - Quit");
        System.out.print("Enter option: ");
    }

    private static void handleChoice(String choice, BufferedReader reader) throws IOException {
        switch (choice) {
            case "1" -> companyMenu(reader);
            case "2" -> qualificationMenu(reader);
            case "3" -> transportTypeMenu(reader);
            case "4" -> employeeMenu(reader);
            case "5" -> vehicleMenu(reader);
            case "6" -> transportMenu(reader);
            case "7" -> reportsMenu(reader);
            default -> System.out.println("Invalid option");
        }
    }

    // Submenu for Companies
    private static void companyMenu(BufferedReader reader) throws IOException {
        boolean inSubmenu = true;
        while (inSubmenu) {
            System.out.println("\n--- Companies Menu ---");
            System.out.println("1 - Create company");
            System.out.println("2 - List all companies");
            System.out.println("3 - Edit company");
            System.out.println("4 - Delete company");
            System.out.println("5 - Order companies by name");
            System.out.println("6 - Order companies by revenue");
            System.out.println("b - Back to main menu");
            System.out.print("Enter option: ");
            System.out.flush();
            
            String input = reader.readLine();
            if (input == null) {
                System.out.println("End of input stream detected, exiting.");
                return;
            }
            input = input.trim();
            
            try {
                switch (input) {
                    case "1" -> createCompany(reader);
                    case "2" -> listCompanies();
                    case "3" -> editCompany(reader);
                    case "4" -> deleteCompany(reader);
                    case "5" -> listCompaniesByName();
                    case "6" -> listCompaniesByRevenue();
                    case "b" -> inSubmenu = false;
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Submenu for Qualifications
    private static void qualificationMenu(BufferedReader reader) throws IOException {
        boolean inSubmenu = true;
        while (inSubmenu) {
            System.out.println("\n--- Qualifications Menu ---");
            System.out.println("1 - Create qualification");
            System.out.println("2 - List all qualifications");
            System.out.println("3 - Edit qualification");
            System.out.println("4 - Delete qualification");
            System.out.println("b - Back to main menu");
            System.out.print("Enter option: ");
            System.out.flush();
            
            String input = reader.readLine();
            if (input == null) {
                System.out.println("End of input stream detected, exiting.");
                return;
            }
            input = input.trim();
            
            try {
                switch (input) {
                    case "1" -> createQualification(reader);
                    case "2" -> listQualifications();
                    case "3" -> editQualification(reader);
                    case "4" -> deleteQualification(reader);
                    case "b" -> inSubmenu = false;
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Submenu for Vehicles
    private static void vehicleMenu(BufferedReader reader) throws IOException {
        boolean inSubmenu = true;
        while (inSubmenu) {
            System.out.println("\n--- Vehicles Menu ---");
            System.out.println("1 - Create vehicle");
            System.out.println("2 - List vehicles by company");
            System.out.println("3 - Edit vehicle");
            System.out.println("4 - Delete vehicle");
            System.out.println("b - Back to main menu");
            System.out.print("Enter option: ");
            System.out.flush();
            
            String input = reader.readLine();
            if (input == null) {
                System.out.println("End of input stream detected, exiting.");
                return;
            }
            input = input.trim();
            
            try {
                switch (input) {
                    case "1" -> createVehicle(reader);
                    case "2" -> listVehiclesByCompany(reader);
                    case "3" -> editVehicle(reader);
                    case "4" -> deleteVehicle(reader);
                    case "b" -> inSubmenu = false;
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Submenu for Employees
    private static void employeeMenu(BufferedReader reader) throws IOException {
        boolean inSubmenu = true;
        while (inSubmenu) {
            System.out.println("\n--- Employees Menu ---");
            System.out.println("1 - Create employee");
            System.out.println("2 - List employees by company");
            System.out.println("3 - Edit employee");
            System.out.println("4 - Delete employee");
            System.out.println("5 - Manage employee qualifications");
            System.out.println("6 - Order employees by qualifications count");
            System.out.println("7 - Order employees by revenue created");
            System.out.println("b - Back to main menu");
            System.out.print("Enter option: ");
            System.out.flush();
            
            String input = reader.readLine();
            if (input == null) {
                System.out.println("End of input stream detected, exiting.");
                return;
            }
            input = input.trim();
            
            try {
                switch (input) {
                    case "1" -> createEmployee(reader);
                    case "2" -> listEmployeesByCompany(reader);
                    case "3" -> editEmployee(reader);
                    case "4" -> deleteEmployee(reader);
                    case "5" -> manageEmployeeQualifications(reader);
                    case "6" -> listEmployeesOrderedByQualifications();
                    case "7" -> listEmployeesOrderedByRevenue();
                    case "b" -> inSubmenu = false;
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Submenu for Transport Types
    private static void transportTypeMenu(BufferedReader reader) throws IOException {
        boolean inSubmenu = true;
        while (inSubmenu) {
            System.out.println("\n--- Transport Types Menu ---");
            System.out.println("1 - Create transport type");
            System.out.println("2 - List transport types");
            System.out.println("3 - Edit transport type");
            System.out.println("4 - Delete transport type");
            System.out.println("b - Back to main menu");
            System.out.print("Enter option: ");
            System.out.flush();
            
            String input = reader.readLine();
            if (input == null) {
                System.out.println("End of input stream detected, exiting.");
                return;
            }
            input = input.trim();
            
            try {
                switch (input) {
                    case "1" -> createTransportType(reader);
                    case "2" -> listTransportTypes();
                    case "3" -> editTransportType(reader);
                    case "4" -> deleteTransportType(reader);
                    case "b" -> inSubmenu = false;
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Submenu for Transports
    private static void transportMenu(BufferedReader reader) throws IOException {
        boolean inSubmenu = true;
        while (inSubmenu) {
            System.out.println("\n--- Transports Menu ---");
            System.out.println("1 - Create transport");
            System.out.println("2 - List transports by destination");
            System.out.println("3 - Order transports by destination");
            System.out.println("b - Back to main menu");
            System.out.print("Enter option: ");
            System.out.flush();
            
            String input = reader.readLine();
            if (input == null) {
                System.out.println("End of input stream detected, exiting.");
                return;
            }
            input = input.trim();
            
            try {
                switch (input) {
                    case "1" -> createTransport(reader);
                    case "2" -> listTransportsByDestination(reader);
                    case "3" -> listTransportsOrderedByDestination();
                    case "b" -> inSubmenu = false;
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
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
        List<org.example.dto.TransportCompanyDto> companies = TransportCompanyDAO.getCompaniesDto();
        if (companies.isEmpty()) {
            System.out.println("No companies found");
            return;
        }
        companies.forEach(c -> System.out.println(c.getId() + " - " + c.getName()));
    }

    private static void listCompaniesByName() {
        List<org.example.dto.TransportCompanyDto> companies = TransportCompanyDAO.getCompaniesDtoOrderedByName();
        if (companies.isEmpty()) {
            System.out.println("No companies found");
            return;
        }
        System.out.println("\n=== Companies Ordered by Name ===");
        companies.forEach(c -> System.out.println(c.getId() + " - " + c.getName()));
    }

    private static void listCompaniesByRevenue() {
        List<org.example.dto.CompanyRevenueDto> companies = TransportCompanyDAO.getCompaniesDtoOrderedByRevenue();
        if (companies.isEmpty()) {
            System.out.println("No companies found");
            return;
        }
        System.out.println("\n=== Companies Ordered by Revenue (highest to lowest) ===");
        companies.forEach(c -> System.out.println(c.getCompanyId() + " - " + c.getCompanyName() + 
                " (Transports: " + c.getTransportCount() + ", Revenue: $" + c.getTotalRevenue() + ")"));
    }

    // Vehicle handlers
    private static void createVehicle(BufferedReader reader) throws IOException {
        // Check if transport types exist
        List<TransportTypeDto> types = TransportTypeDAO.getAllDto();
        if (types.isEmpty()) {
            System.out.println("Cannot create vehicle: No transport types exist in database.");
            System.out.println("Please create transport types first (Main Menu -> Transport Types).");
            return;
        }
        
        long companyId = readId(reader, "Company id: ");
        TransportCompany company = TransportCompanyDAO.getCompanyById(companyId);
        if (company == null) { System.out.println("Company not found"); return; }
        
        System.out.println("Available transport types:");
        types.forEach(t -> System.out.println(t.getId() + " - " + t.getName() +
                ((t.getDescription() != null && !t.getDescription().isBlank()) ? (" (" + t.getDescription() + ")") : "")));
        
        long typeId = readId(reader, "Select transport type ID: ");
        TransportTypeDto selectedType = TransportTypeDAO.getByIdDto(typeId);
        if (selectedType == null) {
            System.out.println("Transport type not found");
            return;
        }
        
        Vehicle vehicle = new Vehicle();
        vehicle.setType(selectedType.getName());
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
        List<org.example.dto.VehicleDto> vehicles = VehicleDAO.getVehiclesByCompanyDto(companyId);
        if (vehicles.isEmpty()) { System.out.println("No vehicles found"); return; }
        vehicles.forEach(v -> System.out.println(v.getId() + " - " + v.getType()));
    }

    // Employee handlers
    private static void createEmployee(BufferedReader reader) throws IOException {
        // Check if qualifications exist
        List<QualificationDto> qualifications = QualificationDAO.getQualificationsDto();
        if (qualifications.isEmpty()) {
            System.out.println("Cannot create employee: No qualifications exist in database.");
            System.out.println("Please create qualifications first (Main Menu -> Qualifications).");
            return;
        }
        
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
        System.out.println("Note: Use 'Manage employee qualifications' to assign qualifications.");
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
        List<org.example.dto.TransportEmployeeDto> employees = EmployeeDAO.getEmployeesByCompanyDto(companyId);
        if (employees.isEmpty()) { System.out.println("No employees found"); return; }
        employees.forEach(e -> System.out.println(e.getId() + " - " + e.getName()));
    }

    private static void listEmployeesOrderedByQualifications() {
        List<org.example.dto.TransportEmployeeDto> employees = EmployeeDAO.getEmployeesDtoOrderedByQualifications();
        if (employees.isEmpty()) {
            System.out.println("No employees found");
            return;
        }
        System.out.println("\n=== Employees Ordered by Qualifications Count (highest to lowest) ===");
        employees.forEach(e -> System.out.println(e.getId() + " - " + e.getName() + " (" + e.getCompanyName() + ")"));
    }

    private static void listEmployeesOrderedByRevenue() {
        List<org.example.dto.DriverRevenueDto> employees = EmployeeDAO.getEmployeesDtoOrderedByRevenue();
        if (employees.isEmpty()) {
            System.out.println("No employees found");
            return;
        }
        System.out.println("\n=== Employees Ordered by Revenue Created (highest to lowest) ===");
        employees.forEach(e -> System.out.println(e.getDriverId() + " - " + e.getDriverName() + 
                " (" + e.getCompanyName() + ", Transports: " + e.getTransportCount() + ", Revenue: $" + e.getTotalRevenue() + ")"));
    }

    // Transport handlers
    private static void createTransport(BufferedReader reader) throws IOException {
        long vehicleId = readId(reader, "Vehicle id: ");
        Vehicle vehicle = VehicleDAO.getVehicleById(vehicleId);
        if (vehicle == null) { System.out.println("Vehicle not found"); return; }

        long driverId = readId(reader, "Driver id: ");
        TransportEmployee driver = EmployeeDAO.getEmployeeByIdWithQualifications(driverId);
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
        // Resolve TransportType from Vehicle type name, or ask the user to choose
        TransportType tt = TransportTypeDAO.findByNameWithQualifications(vehicle.getType());
        if (tt == null) {
            System.out.println("No TransportType found matching vehicle type '" + vehicle.getType() + "'.");
            List<TransportTypeDto> allTypes = TransportTypeDAO.getAllDto();
            if (allTypes.isEmpty()) {
                System.out.println("No TransportTypes exist. Please create transport types in the database first.");
                return;
            }
            System.out.println("Available transport types:");
            allTypes.forEach(t -> System.out.println(t.getId() + " - " + t.getName()));
            long ttId = readId(reader, "Transport type id: ");
            tt = TransportTypeDAO.getByIdWithQualifications(ttId);
            if (tt == null) { System.out.println("Transport type not found"); return; }
        }
        
        transport.setTransportType(tt);
        
        // Ask for cargo weight if transport type is Truck
        Float cargoWeight = null;
        if (tt.getName().equalsIgnoreCase("Truck")) {
            System.out.print("Cargo weight (kg): ");
            System.out.flush();
            String cargoInput = reader.readLine().trim();
            if (!cargoInput.isEmpty()) {
                try {
                    cargoWeight = Float.parseFloat(cargoInput);
                    transport.setCargoWeight(cargoWeight);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid cargo weight, setting to null");
                    cargoWeight = null;
                }
            }
        }
        
        // Check if driver has "Truck" qualification when cargo weight is provided
        if (tt.getName().equalsIgnoreCase("Truck") && cargoWeight != null && cargoWeight > 0) {
            boolean hasTruckQualification = driver.getQualifications().stream()
                .anyMatch(q -> q.getName().equalsIgnoreCase("Truck"));
            
            if (!hasTruckQualification) {
                System.out.println("Error: Driver must have 'Truck' qualification to transport cargo.");
                System.out.println("Driver's current qualifications:");
                if (driver.getQualifications().isEmpty()) {
                    System.out.println("  (none)");
                } else {
                    driver.getQualifications().forEach(q -> 
                        System.out.println("  - " + q.getName()));
                }
                return;
            }
        }
        
        TransportDAO.saveTransport(transport);
        System.out.println("Saved transport with id: " + transport.getId());
    }

    private static void listTransportsByDestination(BufferedReader reader) throws IOException {
        System.out.print("Destination: ");
        System.out.flush();
        String destination = reader.readLine().trim();
        List<org.example.dto.TransportDto> transports = TransportDAO.getTransportsByDestinationDto(destination);
        if (transports.isEmpty()) { System.out.println("No transports found"); return; }
        transports.forEach(t -> System.out.println(t.getId() + " - From: " + t.getStartPoint() + " To: " + t.getEndPoint() +
                " Date: " + t.getDepartureDate() + " Driver: " + t.getDriverName()));
    }

    private static void listTransportsOrderedByDestination() {
        List<org.example.dto.TransportDto> transports = TransportDAO.getTransportsDtoOrderedByDestination();
        if (transports.isEmpty()) {
            System.out.println("No transports found");
            return;
        }
        System.out.println("\n=== Transports Ordered by Destination ===");
        transports.forEach(t -> System.out.println(t.getId() + " - From: " + t.getStartPoint() + " To: " + t.getEndPoint() +
                " Date: " + t.getDepartureDate() + " Driver: " + t.getDriverName()));
    }

    private static void exportTransports(BufferedReader reader) throws IOException {
        long companyId = readId(reader, "Company id: ");
        System.out.print("Enter file name (e.g., transports.csv): ");
        String fileName = reader.readLine().trim();
        
        // Use default filename if empty
        if (fileName.isEmpty()) {
            fileName = "transports_company_" + companyId + ".csv";
            System.out.println("Using default filename: " + fileName);
        }
        
        List<TransportDto> transports = TransportDAO.getTransportsByCompanyDto(companyId);
        
        if (transports.isEmpty()) {
            System.out.println("No transports found for company ID: " + companyId);
            return;
        }
        
        try {
            TransportDAO.exportTransportsDtoToCSV(transports, fileName);
            System.out.println("Successfully exported " + transports.size() + " transports to " + fileName);
        } catch (IOException e) {
            System.err.println("Error exporting transports: " + e.getMessage());
        }
    }

    // TransportType handlers
    private static void listTransportTypes() {
        List<TransportTypeDto> types = TransportTypeDAO.getAllDto();
        if (types.isEmpty()) {
            System.out.println("No transport types found");
            return;
        }
        types.forEach(t -> System.out.println(t.getId() + " - " + t.getName() +
                ((t.getDescription() != null && !t.getDescription().isBlank()) ? (" (" + t.getDescription() + ")") : "")));
    }

    private static void createTransportType(BufferedReader reader) throws IOException {
        System.out.print("Transport type name: ");
        System.out.flush();
        String name = reader.readLine().trim();
        if (name.isEmpty()) { System.out.println("Name is required"); return; }
        TransportTypeDto existing = TransportTypeDAO.findByNameDto(name);
        if (existing != null) { System.out.println("Type already exists with id: " + existing.getId()); return; }
        System.out.print("Description (optional): ");
        System.out.flush();
        String desc = reader.readLine();
        TransportType type = new TransportType();
        type.setName(name);
        if (desc != null && !desc.isBlank()) type.setDescription(desc.trim());
        TransportTypeDAO.saveTransportType(type);
        System.out.println("Saved transport type with id: " + type.getId());
    }

    private static void editTransportType(BufferedReader reader) throws IOException {
        long id = readId(reader, "Transport type id: ");
        TransportType type = TransportTypeDAO.getById(id);
        if (type == null) { System.out.println("Transport type not found"); return; }
        System.out.print("New name (current: " + type.getName() + "): ");
        System.out.flush();
        String name = reader.readLine().trim();
        if (!name.isEmpty()) {
            TransportTypeDto dup = TransportTypeDAO.findByNameDto(name);
            if (dup != null && dup.getId() != type.getId()) { System.out.println("Another type with that name exists"); return; }
            type.setName(name);
        }
        String currentDesc = type.getDescription() == null ? "" : type.getDescription();
        System.out.print("New description (current: " + currentDesc + "): ");
        System.out.flush();
        String desc = reader.readLine();
        if (desc != null && !desc.isBlank()) type.setDescription(desc.trim());
        TransportTypeDAO.updateTransportType(type);
        System.out.println("Updated transport type");
    }

    private static void deleteTransportType(BufferedReader reader) throws IOException {
        long id = readId(reader, "Transport type id: ");
        TransportType type = TransportTypeDAO.getById(id);
        if (type == null) { System.out.println("Transport type not found"); return; }
        TransportTypeDAO.deleteTransportType(type);
        System.out.println("Deleted transport type");
    }

    // Qualification handlers
    private static void listQualifications() {
        List<QualificationDto> qualifications = QualificationDAO.getQualificationsDto();
        if (qualifications.isEmpty()) {
            System.out.println("No qualifications found");
            return;
        }
        qualifications.forEach(q -> System.out.println(q.getId() + " - " + q.getName() +
                ((q.getDescription() != null && !q.getDescription().isBlank()) ? (" (" + q.getDescription() + ")") : "")));
    }

    private static void createQualification(BufferedReader reader) throws IOException {
        System.out.print("Qualification name: ");
        System.out.flush();
        String name = reader.readLine().trim();
        if (name.isEmpty()) { System.out.println("Name is required"); return; }
        System.out.print("Description (optional): ");
        System.out.flush();
        String desc = reader.readLine();
        Qualification qualification = new Qualification();
        qualification.setName(name);
        if (desc != null && !desc.isBlank()) qualification.setDescription(desc.trim());
        QualificationDAO.saveQualification(qualification);
        System.out.println("Saved qualification with id: " + qualification.getId());
    }

    private static void editQualification(BufferedReader reader) throws IOException {
        long id = readId(reader, "Qualification id: ");
        Qualification qual = QualificationDAO.getQualificationById(id);
        if (qual == null) { System.out.println("Qualification not found"); return; }
        System.out.print("New name (current: " + qual.getName() + "): ");
        System.out.flush();
        String name = reader.readLine().trim();
        if (!name.isEmpty()) qual.setName(name);
        String currentDesc = qual.getDescription() == null ? "" : qual.getDescription();
        System.out.print("New description (current: " + currentDesc + "): ");
        System.out.flush();
        String desc = reader.readLine();
        if (desc != null && !desc.isBlank()) qual.setDescription(desc.trim());
        QualificationDAO.updateQualification(qual);
        System.out.println("Updated qualification");
    }

    private static void deleteQualification(BufferedReader reader) throws IOException {
        long id = readId(reader, "Qualification id: ");
        Qualification qual = QualificationDAO.getQualificationById(id);
        if (qual == null) { System.out.println("Qualification not found"); return; }
        QualificationDAO.deleteQualification(qual);
        System.out.println("Deleted qualification");
    }

    // Manage employee qualifications
    private static void manageEmployeeQualifications(BufferedReader reader) throws IOException {
        long employeeId = readId(reader, "Employee id: ");
        TransportEmployee employee = EmployeeDAO.getEmployeeByIdWithQualifications(employeeId);
        if (employee == null) {
            System.out.println("Employee not found");
            return;
        }
        
        System.out.println("\nEmployee: " + employee.getName());
        System.out.println("Current qualifications:");
        if (employee.getQualifications().isEmpty()) {
            System.out.println("  (none)");
        } else {
            employee.getQualifications().forEach(q -> 
                System.out.println("  " + q.getId() + " - " + q.getName()));
        }
        
        System.out.println("\n1 - Add qualification");
        System.out.println("2 - Remove qualification");
        System.out.println("b - Back");
        System.out.print("Enter option: ");
        System.out.flush();
        
        String choice = reader.readLine();
        if (choice == null) return;
        choice = choice.trim();
        
        switch (choice) {
            case "1" -> addQualificationToEmployee(reader, employeeId);
            case "2" -> removeQualificationFromEmployee(reader, employeeId);
            case "b" -> {}
            default -> System.out.println("Invalid option");
        }
    }

    private static void addQualificationToEmployee(BufferedReader reader, long employeeId) throws IOException {
        List<QualificationDto> qualifications = QualificationDAO.getQualificationsDto();
        if (qualifications.isEmpty()) {
            System.out.println("No qualifications available. Please create qualifications first.");
            return;
        }
        
        System.out.println("\nAvailable qualifications:");
        qualifications.forEach(q -> System.out.println(q.getId() + " - " + q.getName()));
        
        long qualId = readId(reader, "Select qualification id to add: ");
        QualificationDto qual = QualificationDAO.getQualificationByIdDto(qualId);
        if (qual == null) {
            System.out.println("Qualification not found");
            return;
        }
        
        EmployeeDAO.addQualificationToEmployee(employeeId, qualId);
        System.out.println("Qualification added successfully");
    }

    private static void removeQualificationFromEmployee(BufferedReader reader, long employeeId) throws IOException {
        TransportEmployee employee = EmployeeDAO.getEmployeeByIdWithQualifications(employeeId);
        if (employee == null || employee.getQualifications().isEmpty()) {
            System.out.println("Employee has no qualifications to remove");
            return;
        }
        
        System.out.println("\nEmployee's current qualifications:");
        employee.getQualifications().forEach(q -> 
            System.out.println(q.getId() + " - " + q.getName()));
        
        long qualId = readId(reader, "Select qualification id to remove: ");
        EmployeeDAO.removeQualificationFromEmployee(employeeId, qualId);
        System.out.println("Qualification removed successfully");
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

    // Reports Menu
    private static void reportsMenu(BufferedReader reader) throws IOException {
        boolean inSubmenu = true;
        while (inSubmenu) {
            System.out.println("\n--- Reports Menu ---");
            System.out.println("1 - Total transports count");
            System.out.println("2 - Total revenue from all transports");
            System.out.println("3 - Transports per driver report");
            System.out.println("4 - Company revenue for period");
            System.out.println("5 - Revenue per driver report");
            System.out.println("6 - Export transports to CSV");
            System.out.println("b - Back to main menu");
            System.out.print("Enter option: ");
            System.out.flush();

            String input = reader.readLine();
            if (input == null) {
                System.out.println("End of input stream detected, exiting.");
                return;
            }
            input = input.trim();

            try {
                switch (input) {
                    case "1" -> reportTotalTransports();
                    case "2" -> reportTotalRevenue();
                    case "3" -> reportTransportsPerDriver();
                    case "4" -> reportCompanyRevenueForPeriod(reader);
                    case "5" -> reportRevenuePerDriver();
                    case "6" -> exportTransports(reader);
                    case "b" -> inSubmenu = false;
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.err.println("Error generating report: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Report: Total number of transports
    private static void reportTotalTransports() {
        Long count = TransportDAO.getTotalTransportsCount();
        System.out.println("\n=== Total Transports Report ===");
        System.out.println("Total transports: " + count);
    }

    // Report: Total revenue from all transports
    private static void reportTotalRevenue() {
        BigDecimal total = TransportDAO.getTotalRevenue();
        System.out.println("\n=== Total Revenue Report ===");
        System.out.println("Total revenue: $" + total);
    }

    // Report: List of drivers and how many transports each has completed
    private static void reportTransportsPerDriver() {
        List<DriverTransportCountDto> results = TransportDAO.getTransportsPerDriver();

        System.out.println("\n=== Transports Per Driver Report ===");
        System.out.printf("%-10s %-30s %s%n", "Driver ID", "Driver Name", "Transport Count");
        System.out.println("=".repeat(60));
        
        for (DriverTransportCountDto dto : results) {
            System.out.printf("%-10d %-30s %d%n", dto.getDriverId(), dto.getDriverName(), dto.getTransportCount());
        }
        
        if (results.isEmpty()) {
            System.out.println("No transports found.");
        }
    }

    // Report: Company revenue for a specific time period
    private static void reportCompanyRevenueForPeriod(BufferedReader reader) throws IOException {
        System.out.print("Enter start date (yyyy-MM-dd HH:mm): ");
        String startStr = reader.readLine();
        System.out.print("Enter end date (yyyy-MM-dd HH:mm): ");
        String endStr = reader.readLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endStr, formatter);

        List<CompanyRevenueDto> results = TransportDAO.getCompanyRevenueForPeriod(startDate, endDate);

        System.out.println("\n=== Company Revenue Report ===");
        System.out.println("Period: " + startDate + " to " + endDate);
        System.out.printf("%-12s %-30s %-15s %s%n", "Company ID", "Company Name", "Transports", "Revenue");
        System.out.println("=".repeat(80));
        
        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (CompanyRevenueDto dto : results) {
            totalRevenue = totalRevenue.add(dto.getTotalRevenue());
            System.out.printf("%-12d %-30s %-15d $%.2f%n", 
                dto.getCompanyId(), dto.getCompanyName(), dto.getTransportCount(), dto.getTotalRevenue());
        }
        
        if (!results.isEmpty()) {
            System.out.println("=".repeat(80));
            System.out.printf("%-58s $%.2f%n", "TOTAL:", totalRevenue);
        } else {
            System.out.println("No transports found in this period.");
        }
    }

    // Report: Revenue per driver
    private static void reportRevenuePerDriver() {
        List<DriverRevenueDto> results = TransportDAO.getRevenuePerDriver();

        System.out.println("\n=== Revenue Per Driver Report ===");
        System.out.printf("%-10s %-25s %-25s %-12s %s%n", 
                "Driver ID", "Driver Name", "Company", "Transports", "Revenue");
        System.out.println("=".repeat(100));
        
        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (DriverRevenueDto dto : results) {
            totalRevenue = totalRevenue.add(dto.getTotalRevenue());
            System.out.printf("%-10d %-25s %-25s %-12d $%.2f%n", 
                    dto.getDriverId(), dto.getDriverName(), dto.getCompanyName(), 
                    dto.getTransportCount(), dto.getTotalRevenue());
        }
        
        if (!results.isEmpty()) {
            System.out.println("=".repeat(100));
            System.out.printf("%-73s $%.2f%n", "TOTAL:", totalRevenue);
        } else {
            System.out.println("No transports found.");
        }
    }

}