# Network Subnetting Automation
A Java Swing desktop application for automated network subnetting calculations with an intuitive graphical interface and comprehensive subnet analysis tools.

<img width="1366" height="720" alt="image" src="https://github.com/user-attachments/assets/66a64e98-e9c0-4cf4-95f2-7cb6cd585065" />

## Features

### Core Functionality

- **Automated Subnet Calculations:** Calculate subnet masks, host ranges, and network parameters based on your requirements.
- **Three Calculation Modes:**
  - **Hosts Only:** Calculate based on the number of needed usable hosts.
  - **Subnets Only:** Calculate based on the number of needed subnets.
  - **Both Hosts and Subnets:** Calculate when both requirements are specified.
- **Comprehensive Results Display:** View all calculated parameters including custom subnet mask, bits borrowed, total subnets, and CIDR notation.
- **Subnet Table Generation:** Automatically generate complete subnet tables with network IDs, host ranges, and broadcast addresses.
- **Subnet Table Generation:** Automatically generate complete subnet tables with network IDs, host ranges, and broadcast addresses.
- **Reference Tables:** Built-in IP classification and binary value reference tables for quick lookup.
- **Full-Screen Interface:** Modern orange-themed interface designed for maximum visibility and ease of use.
- **Input Validation:** Comprehensive validation with user-friendly error messages.

## Calculation

1. **Number of Needed Usable Hosts Only:** Calculate the optimal subnetting configuration when you know how many hosts you need per subnet.
2. **Number of Needed Subnets Only:** Calculate subnetting when you know how many subnets you need to create.
3. **Both Hosts and Subnets Required:** Calculate when you have specific requirements for both number of subnets and hosts per subnet.

## Requirements

- **Java 8** _or_ **Higher**
- **Apache NetBeans IDE 27** _(for development) or any Java-compatible IDE_
- **Swing GUI Toolkit** _(included with Java)_

## Installation and Usage

**Compile the Application**
```bash
# Navigate to the project directory
cd network-subnetting-automation

# Compile all Java source files
javac networksubnettingautomation/*.java
```

**Run the Application**
```bash
# Run the main GUI application
java networksubnettingautomation.SubnettingGUI
```

**Compile and Run in One Command**
```bash
javac networksubnettingautomation/*.java && java networksubnettingautomation.SubnettingGUI
```

**For Development** _(Using Apache NetBeans)_
```bash
# Open the project in NetBeans IDE 27
# or use command line with netbeans
netbeans networksubnettingautomation/
```

**Clean Compiled Classes**
```bash
# Remove all .class files
rm networksubnettingautomation/*.class
```

**Complete Build and Run Script Example**
Create a file named `run.sh:`
```bash
#!/bin/bash

echo "Compiling Network Subnetting Automation..."
javac networksubnettingautomation/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Starting application..."
    java networksubnettingautomation.SubnettingGUI
else
    echo "Compilation failed!"
    exit 1
fi
```

Make it executable:
```bash
chmod +x run.sh
./run.sh
```

## Customization

**Changing Colors**
Edit in `SubnettingGUI.java:`
```java
private final Color PRIMARY_COLOR = new Color(0xED, 0x8B, 0x00); // #ED8B00 Orange
private final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light Gray
```

**Adjusting Layout**
- Modify `setPreferredSize()` calls for different component sizes.
- Adjust split pane divider location.
- Change font sizes in `setFont()` methods.

## Validation Features

- **IP Format Validation** (XXX.XXX.XXX.XXX)
- **Octet Range Validation** (0-255)
- **Positive Integer Validation for hosts/subnets**
- **Context-Sensitive Field Validation**
- **User-Friendly Error Messages**

## Technical Details

**Algorithm Overview**
```text
1. Determine IP class from network address
2. Calculate bits to borrow based on requirements
3. Generate custom subnet mask
4. Calculate total subnets and hosts
5. Generate subnet ranges and broadcast addresses
```

**File Structure**
```test
network-subnetting-automation/
├── networksubnettingautomation/
│   ├── SubnetCalculator.java     # Core calculation logic
│   ├── SubnettingGUI.java        # Main GUI interface
│   └── (Supporting files)
├── resources/                    # Images and assets
├── README.md                     # This documentation
├── .gitignore                    # Git ignore rules
└── LICENSE                       # MIT License
```

## GUI Features

- Gradient header with orange theme
- Responsive layout with split pane for results
- Alternating row colors in tables for better readability
- Hover effects on buttons
- Full-screen capability
- Center-aligned table cells for consistent display

## Contribution 

We welcome contributions! Here's how:
1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/amazing-feature`
3. **Commit** your changes: `git commit -m 'Add amazing feature'`
4. **Push** to the branch: `git push origin feature/amazing-feature`
5. **Open** a Pull Request

## Areas for Improvement

- IPv6 subnetting support
- Network diagram visualization
- Export to CSV/PDF functionality
- Multi-language support
- Advanced reporting features

## Contributors 

- **@shann-shannn**
- **@Kurokibara**
- **@Kanechiii**

## 🪪 License

This project is licensed under **Apache License 2.0** - See [This License](http://www.apache.org/licenses/LICENSE-2.0) for details. 
> [!NOTE]
> This tool is for educational and planning purposes. Always test in controlled environments before production deployment.

## ⚠️ Disclaimer 

#### IMPORTANT LEGAL NOTICE ####
This tool is designed for educational and network planning purposes. Always verify subnetting calculations in a test environment before implementing in production networks. The developers are not responsible for any network issues arising from the use of this tool.

**Built with** 🫶🏻 | **Visit me on** [Kanechiii](https://github.com/Kanechiii)
