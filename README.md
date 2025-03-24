# Prescription App

A Java-based application designed to streamline the process of generating and managing medical prescriptions.

## Features

- **Doctor Information Management**: Store and manage doctor details, including name, specialty, and contact information.
- **Patient Information Management**: Maintain comprehensive records of patients, encompassing personal details and medical history.
- **Prescription Generation**: Create detailed prescriptions in PDF format, facilitating easy sharing and printing.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven 3.6.0 or higher

### Installation

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/metharda/prescription-app.git
   ```

2. **Navigate to the Project Directory**:

   ```bash
   cd prescription-app
   ```

3. **Build the Project Using Maven**:

   ```bash
   mvn clean install
   ```

### Running the Application

After building the project:

1. **Navigate to the Target Directory**:

   ```bash
   cd target
   ```

2. **Run the Application**:

   ```bash
   java -jar prescription-app-1.0.0.jar
   ```

## Usage

- **Doctor Information**: Input and manage doctor details using the `doctor_info.txt` file.
- **Patient Information**: Maintain patient records through the `patient_info.txt` file.
- **Generate Prescription**: Use the application interface to create and export prescriptions as PDFs.

## License

This project is licensed under the MIT License.
