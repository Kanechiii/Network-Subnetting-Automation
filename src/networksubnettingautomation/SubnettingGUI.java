package networksubnettingautomation;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubnettingGUI extends JFrame 
    {
        // Input components
        private JComboBox<String> optionComboBox;
        private JLabel neededHostsLabel;
        private JTextField neededHostsField;
        private JLabel neededSubnetsLabel;
        private JTextField neededSubnetsField;
        private JLabel networkAddressLabel;
        private JTextField networkAddressField;
        private JButton calculateButton;
        private JButton clearButton;
        private JButton exitButton;

        // Output components - TABLES
        private JTable resultsTable;
        private JTable subnetTable;
        private JTable referenceTable;
        private JTable binaryTable;

        // Colors - Updated to #ED8B00 theme
        private final Color PRIMARY_COLOR = new Color(0xED, 0x8B, 0x00); // #ED8B00 - Orange
        private final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light Gray
        private final Color INPUT_BG_COLOR = Color.WHITE;
        private final Color BORDER_COLOR = new Color(220, 220, 220);
        private final Color DARK_TEXT_COLOR = Color.BLACK;
    
    public SubnettingGUI() 
        {
            initComponents();
            setTitle("NETWORK SUBNETTING AUTOMATION 1.0.0-beta.1 ");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Remove fixed size and make it fullscreen
            setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
            setUndecorated(false); // Keep window decorations (title bar, minimize/maximize/close buttons)

            setLocationRelativeTo(null);
            setResizable(true);
        }
    
    private void initComponents() 
        {
            // Main panel with BorderLayout
            JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
            mainPanel.setBackground(BACKGROUND_COLOR);

            // Top Panel - Header (Smaller)
            mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

            // Center Panel - Content
            mainPanel.add(createContentPanel(), BorderLayout.CENTER);

            // Bottom Panel - Buttons
            mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

            add(mainPanel);
        }
    
    private JPanel createHeaderPanel() 
        {
            JPanel headerPanel = new JPanel(new BorderLayout()) 
                {
                    @Override
                    protected void paintComponent(Graphics g) 
                        {
                            //built in method from JPanel that can be use to import gradient background
                            super.paintComponent(g);
                            Graphics2D g2d = (Graphics2D) g;

                            // Create gradient from top to bottom
                            Color startColor = PRIMARY_COLOR;
                            Color endColor = new Color(0xFF, 0xA5, 0x33); // Brighter orange

                            GradientPaint gradient = new GradientPaint
                                (
                                    0, 0, startColor,
                                    getWidth(), 0, endColor
                                );
                            g2d.setPaint(gradient);
                            g2d.fillRect(0, 0, getWidth(), getHeight());
                        }
                };

            headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

            // Title
            JLabel titleLabel = new JLabel("NETWORK SUBNETTING AUTOMATION");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Subtitle
            JLabel subtitleLabel = new JLabel("Automated Networking Subnetting Application 1.0.0-beta.1 | Powered By Apache Netbeans IDE 27");
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            subtitleLabel.setForeground(new Color(255, 245, 230));
            subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setOpaque(false); // Make transparent to show gradient
            titlePanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            titlePanel.add(titleLabel, BorderLayout.CENTER);
            titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

            headerPanel.add(titlePanel, BorderLayout.CENTER);

            return headerPanel;
        }
    
    private JPanel createContentPanel() 
        {
            JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
            contentPanel.setBackground(BACKGROUND_COLOR);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Left Panel - Input Section
            contentPanel.add(createInputPanel(), BorderLayout.WEST);

            // Right Panel - Output Section
            contentPanel.add(createOutputPanel(), BorderLayout.CENTER);

            return contentPanel;
        }
    
    private JPanel createInputPanel() 
        {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.WHITE);

            // Create a styled border
            TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                "INPUT PARAMETERS"
            );
            titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
            titledBorder.setTitleColor(PRIMARY_COLOR);
            titledBorder.setTitleJustification(TitledBorder.CENTER);
            panel.setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(10, 12, 10, 12) // Reduced vertical padding
            ));

            // Option selection
            JPanel optionPanel = createInputField("SELECT CALCULATION OPTION:");
            String[] options = 
                {
                    "1 - NUMBER OF NEEDED USABLE HOSTS ONLY",
                    "2 - NUMBER OF NEEDED SUBNETS ONLY",
                    "3 - BOTH HOSTS AND SUBNETS REQUIRED"
                };

            optionComboBox = new JComboBox<>(options);
            styleComboBox(optionComboBox);
            optionComboBox.addActionListener(e -> updateFieldVisibility());
            optionPanel.add(optionComboBox);
            optionComboBox.setFont(new Font("Segoe UI", Font.BOLD, 12));
            panel.add(optionPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Reduced spacing

            // Needed hosts
            neededHostsLabel = new JLabel("NEEDED USABLE HOSTS:");
            neededHostsField = new JTextField(15);
            JPanel hostsPanel = createInputField(neededHostsLabel, neededHostsField);
            panel.add(hostsPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 8))); // Reduced spacing

            // Needed subnets
            neededSubnetsLabel = new JLabel("NEEDED SUBNETS:");
            neededSubnetsField = new JTextField(15);
            JPanel subnetsPanel = createInputField(neededSubnetsLabel, neededSubnetsField);
            panel.add(subnetsPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 8))); // Reduced spacing

            // Network address
            networkAddressLabel = new JLabel("NETWORK ADDRESS:");
            networkAddressField = new JTextField(15);
            JPanel addressPanel = createInputField(networkAddressLabel, networkAddressField);
            panel.add(addressPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 12))); // Reduced spacing

            // Add buttons below network address
            JPanel buttonPanel = createButtonPanelForInput();
            panel.add(buttonPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Reduced spacing after buttons

            // IP Classification Reference Table
            panel.add(createIPClassificationTable());
            panel.add(Box.createRigidArea(new Dimension(0, 8))); // Reduced spacing

            // Hosts/Subnets Reference Table
            panel.add(createHostsSubnetsTable());

            updateFieldVisibility();

            // Set fixed width for input panel
            panel.setPreferredSize(new Dimension(400, 680)); // Reduced height

            return panel;
        }
    
    private JPanel createButtonPanelForInput() 
        {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5)); // Reduced horizontal spacing
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0)); // Reduced padding

            // Create orange buttons with white text
            calculateButton = createStyledButton("ENTER");
            calculateButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            calculateButton.addActionListener(new CalculateButtonListener());
            calculateButton.setPreferredSize(new Dimension(100, 35)); // Smaller

            clearButton = createStyledButton("CLEAR");
            clearButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            clearButton.addActionListener(e -> clearAll());
            clearButton.setPreferredSize(new Dimension(80, 35)); // Smaller

            exitButton = createStyledButton("EXIT");
            exitButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            exitButton.addActionListener(e -> System.exit(0));
            exitButton.setPreferredSize(new Dimension(80, 35)); // Smaller

            panel.add(calculateButton);
            panel.add(clearButton);
            panel.add(exitButton);

            return panel;
        }
    
    private JPanel createInputField(String labelText) 
        {
            JPanel panel = new JPanel(new BorderLayout(8, 5));
            panel.setBackground(Color.WHITE);

            JLabel label = new JLabel(labelText);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(DARK_TEXT_COLOR);

            panel.add(label, BorderLayout.NORTH);

            return panel;
        }
    
    private JPanel createInputField(JLabel label, JTextField field) 
        {
            JPanel panel = new JPanel(new BorderLayout(8, 5));
            panel.setBackground(Color.WHITE);

            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(DARK_TEXT_COLOR);

            styleTextField(field);

            panel.add(label, BorderLayout.NORTH);
            panel.add(field, BorderLayout.CENTER);

            return panel;
        }
    
    private void styleComboBox(JComboBox<String> comboBox) 
        {
            comboBox.setFont(new Font("Segoe UI", Font.BOLD, 11));
            comboBox.setBackground(INPUT_BG_COLOR);
            comboBox.setForeground(DARK_TEXT_COLOR);
            comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
            comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        }
    
    private void styleTextField(JTextField textField) 
        {
            textField.setFont(new Font("Segoe UI", Font.BOLD, 13));
            textField.setBackground(INPUT_BG_COLOR);
            textField.setForeground(DARK_TEXT_COLOR);
            textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        }
    
    private JPanel createIPClassificationTable() 
        {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(new Color(255, 250, 240));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 200, 150), 2), // ADDED: Orange border
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            // Title
            JLabel titleLabel = new JLabel("IP ADDRESS CLASSIFICATION");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            titleLabel.setForeground(PRIMARY_COLOR);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(titleLabel, BorderLayout.NORTH);

            // Create IP classification table
            String[] columnNames = {"CLASS", "RANGE", "NET BITS", "HOST BITS", "DEFAULT MASK"};
            Object[][] data = 
                {
                    {"A", "1-126", "8", "24", "255.0.0.0"},
                    {"B", "128-191", "16", "16", "255.255.0.0"},
                    {"C", "192-223", "24", "8", "255.255.255.0"}
                };

            referenceTable = new JTable(data, columnNames) 
                {
                    @Override
                    public boolean isCellEditable(int row, int column) 
                        {
                            return false;
                        }
                };

            referenceTable.setFont(new Font("Segoe UI", Font.BOLD, 11));
            referenceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
            referenceTable.setRowHeight(25);
            referenceTable.setShowGrid(true);
            referenceTable.setGridColor(new Color(220, 200, 180));
            referenceTable.setEnabled(false);
            referenceTable.setBackground(new Color(255, 250, 240));
            referenceTable.setForeground(DARK_TEXT_COLOR);

            // Style header
            referenceTable.getTableHeader().setBackground(new Color(255, 230, 200));
            referenceTable.getTableHeader().setForeground(DARK_TEXT_COLOR);
            referenceTable.getTableHeader().setReorderingAllowed(false);
            referenceTable.getTableHeader().setResizingAllowed(false);

            // Center align all cells
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            centerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 11));
            for (int i = 0; i < referenceTable.getColumnCount(); i++) 
                {
                    referenceTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }

            referenceTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // CLASS - slightly reduced
            referenceTable.getColumnModel().getColumn(1).setPreferredWidth(70);  // RANGE - slightly reduced
            referenceTable.getColumnModel().getColumn(2).setPreferredWidth(70);  // NET BITS - slightly reduced
            referenceTable.getColumnModel().getColumn(3).setPreferredWidth(70);  // HOST BITS - slightly reduced
            referenceTable.getColumnModel().getColumn(4).setPreferredWidth(110); // DEFAULT MASK - increased
            
            // Add table to panel without scrollbars
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.add(referenceTable.getTableHeader(), BorderLayout.NORTH);
            tablePanel.add(referenceTable, BorderLayout.CENTER);

            // Set fixed size
            referenceTable.setPreferredScrollableViewportSize(new Dimension(360, 85));
            referenceTable.setFillsViewportHeight(true);

            // Add inner border to the table panel
            tablePanel.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 150), 1)); // ADDED: Inner orange border

            panel.add(tablePanel, BorderLayout.CENTER);

            return panel;
        }
    
    private JPanel createHostsSubnetsTable() 
        {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(new Color(255, 250, 240));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 200, 150), 2),
                BorderFactory.createEmptyBorder(3, 5, 3, 5) // Reduced padding
            ));

            // Title
            JLabel titleLabel = new JLabel("NUMBER AND VALUE REFERENCE");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            titleLabel.setForeground(PRIMARY_COLOR);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(titleLabel, BorderLayout.NORTH);

            // Create landscape table with hosts and subnets
            String[] columnNames = {"HOSTS", "SUBNETS", "BINARY VALUES"};
            Object[][] data = 
                {
                    {"65536", "2", "128 64 32 16  8  4  2  1"},
                    {"32768", "4", "128 64 32 16  8  4  2  1"},
                    {"16384", "8", "128 64 32 16  8  4  2  1"},
                    {"8192", "16", "128 64 32 16  8  4  2  1"},
                    {"4096", "32", "128 64 32 16  8  4  2  1"},
                    {"2048", "64", "128 64 32 16  8  4  2  1"},
                    {"1024", "128", "128 64 32 16  8  4  2  1"},
                    {"512", "256", "128 64 32 16  8  4  2  1"},
                    {"256", "512", "128 64 32 16  8  4  2  1"},
                    {"128", "1024", "128 64 32 16  8  4  2  1"},
                    {"64", "2048", "128 64 32 16  8  4  2  1"},
                    {"32", "4096", "128 64 32 16  8  4  2  1"},
                    {"16", "8192", "128 64 32 16  8  4  2  1"},
                    {"8", "16384", "128 64 32 16  8  4  2  1"},
                    {"4", "32768", "128 64 32 16  8  4  2  1"},
                    {"2", "65536", "128 64 32 16  8  4  2  1"}
                };

            binaryTable = new JTable(data, columnNames) 
                {
                    @Override
                    public boolean isCellEditable(int row, int column) 
                        {
                            return false;
                        }
                };

            binaryTable.setFont(new Font("Consolas", Font.BOLD, 9)); // Smaller font
            binaryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11)); // Smaller
            binaryTable.setRowHeight(20); // Smaller row height
            binaryTable.setShowGrid(true);
            binaryTable.setGridColor(new Color(220, 200, 180));
            binaryTable.setEnabled(false);
            binaryTable.setBackground(new Color(255, 250, 240));
            binaryTable.setForeground(DARK_TEXT_COLOR);

            // Style header
            binaryTable.getTableHeader().setBackground(new Color(255, 230, 200));
            binaryTable.getTableHeader().setForeground(DARK_TEXT_COLOR);
            binaryTable.getTableHeader().setReorderingAllowed(false);
            binaryTable.getTableHeader().setResizingAllowed(false);

            // Custom cell renderer with orange theme
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() 
                {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
                        {
                            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                            setHorizontalAlignment(JLabel.CENTER);
                            setFont(new Font("Consolas", Font.BOLD, 11)); // Smaller font
                            setForeground(DARK_TEXT_COLOR);

                            // Alternate row colors in orange theme
                            if (row % 2 == 0) 
                                {
                                    setBackground(new Color(255, 245, 235));
                                } 
                            else 
                                {
                                    setBackground(new Color(255, 250, 240));
                                }

                            return this;
                        }
                };

            // Apply renderer to all columns
            for (int i = 0; i < binaryTable.getColumnCount(); i++) 
                {
                    binaryTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }

            // Set column widths
            binaryTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            binaryTable.getColumnModel().getColumn(1).setPreferredWidth(60);
            binaryTable.getColumnModel().getColumn(2).setPreferredWidth(170);

            // Create scrollable table with reduced height
            JScrollPane scrollPane = new JScrollPane(binaryTable);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 150), 1));
            scrollPane.setPreferredSize(new Dimension(370, 150)); // Reduced height
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            panel.add(scrollPane, BorderLayout.CENTER);

            // Add footer note
            JLabel footerLabel = new JLabel("NOTE: VALUES ARE POWER OF 2 PROGRESSION");
            footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10)); // Smaller font
            footerLabel.setForeground(new Color(150, 100, 50));
            footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            footerLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0)); // Reduced padding
            panel.add(footerLabel, BorderLayout.SOUTH);

            return panel;
        }
    
    private void updateFieldVisibility() 
        {
            int selected = optionComboBox.getSelectedIndex();

            // Reset all fields to visible
            neededHostsLabel.setVisible(true);
            neededHostsField.setVisible(true);
            neededSubnetsLabel.setVisible(true);
            neededSubnetsField.setVisible(true);

            if (selected == 0) 
                {   
                    // Hosts only
                    neededHostsField.setText("");
                    neededSubnetsField.setText("");
                    neededSubnetsLabel.setVisible(false);
                    neededSubnetsField.setVisible(false);
                    neededHostsLabel.setText("NEEDED USABLE HOSTS:");
                    neededHostsLabel.setForeground(DARK_TEXT_COLOR);
                } 
            else if (selected == 1) 
                { 
                    // Subnets only
                    neededHostsField.setText("");
                    neededHostsLabel.setVisible(false);
                    neededHostsField.setVisible(false);
                    neededSubnetsField.setText("");
                    neededSubnetsLabel.setText("NEEDED SUBNETS:");
                    neededSubnetsLabel.setForeground(DARK_TEXT_COLOR);
                }
            else 
                { 
                    // Both
                    neededHostsField.setText("");
                    neededSubnetsField.setText("");
                    neededHostsLabel.setText("NEEDED USABLE HOSTS:");
                    neededSubnetsLabel.setText("NEEDED SUBNETS:");
                    neededHostsLabel.setForeground(DARK_TEXT_COLOR);
                    neededSubnetsLabel.setForeground(DARK_TEXT_COLOR);
                }
        }
    
    private JPanel createOutputPanel() 
        {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BACKGROUND_COLOR);

            // Create split pane to separate results table and subnet table
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            splitPane.setResizeWeight(0.45);
            splitPane.setDividerSize(5);
            splitPane.setBackground(BACKGROUND_COLOR);

            // Top: Results Table
            JPanel resultsPanel = new JPanel(new BorderLayout());
            resultsPanel.setBackground(Color.WHITE);

            TitledBorder resultsBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                "CALCULATION RESULTS"
            );
            resultsBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
            resultsBorder.setTitleColor(PRIMARY_COLOR);
            resultsBorder.setTitleJustification(TitledBorder.CENTER);
            resultsPanel.setBorder(BorderFactory.createCompoundBorder(
                resultsBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            String[] resultsColumns = {"PARAMETER", "VALUE"};
            DefaultTableModel resultsModel = new DefaultTableModel(resultsColumns, 0) 
                {
                    @Override
                    public boolean isCellEditable(int row, int column) 
                        {
                            return false;
                        }
                };

            resultsTable = new JTable(resultsModel);
            resultsTable.setFont(new Font("Segoe UI", Font.BOLD, 14));
            resultsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
            resultsTable.setRowHeight(32);
            resultsTable.setShowGrid(true);
            resultsTable.setGridColor(new Color(180, 180, 180)); // Darker grid color
            resultsTable.setForeground(DARK_TEXT_COLOR);

            // Style header
            resultsTable.getTableHeader().setBackground(new Color(255, 240, 220));
            resultsTable.getTableHeader().setForeground(DARK_TEXT_COLOR);
            resultsTable.getTableHeader().setReorderingAllowed(false);

            // Use custom renderer for results table with alternating row colors
            resultsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() 
                {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
                        {
                            Component c = super.getTableCellRendererComponent(table, value, 
                                    isSelected, hasFocus, row, column);

                            // Set alignment
                            if (column == 0) 
                                {
                                    setHorizontalAlignment(JLabel.LEFT);
                                } 
                            else 
                                {
                                    setHorizontalAlignment(JLabel.CENTER);
                                }

                            setFont(new Font("Segoe UI", Font.BOLD, 14));
                            setForeground(DARK_TEXT_COLOR);

                            // Apply alternating row colors
                            if (!isSelected) 
                                {
                                    if (row % 2 == 0) 
                                        {
                                            c.setBackground(new Color(255, 250, 240)); // Light orange for even rows
                                        } 
                                    else 
                                        {
                                            c.setBackground(Color.WHITE); // White for odd rows
                                        }
                                }

                            return c;
                        }
                });

            JScrollPane resultsScroll = new JScrollPane(resultsTable);
            // Add thin border around the table
            resultsScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1), // Thin gray border
                BorderFactory.createEmptyBorder(1, 1, 1, 1) // Small padding
            ));
            resultsPanel.add(resultsScroll, BorderLayout.CENTER);

            // Bottom: Subnet Table
            JPanel subnetPanel = new JPanel(new BorderLayout());
            subnetPanel.setBackground(Color.WHITE);

            TitledBorder subnetBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                "SUBNET TABLE"
            );
            subnetBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
            subnetBorder.setTitleColor(PRIMARY_COLOR);
            subnetBorder.setTitleJustification(TitledBorder.CENTER);
            subnetPanel.setBorder(BorderFactory.createCompoundBorder(
                subnetBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            String[] subnetColumns = {"SUBNET NO.", "NETWORK ID/ADDRESS", "HOST RANGE", "BROADCAST ID/ADDRESS"};
            DefaultTableModel subnetModel = new DefaultTableModel(subnetColumns, 0) 
                {
                    @Override
                    public boolean isCellEditable(int row, int column) 
                        {
                            return false;
                        }
                };

            subnetTable = new JTable(subnetModel);
            subnetTable.setFont(new Font("Consolas", Font.BOLD, 13));
            subnetTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            subnetTable.setRowHeight(28);
            subnetTable.setShowGrid(true);
            subnetTable.setGridColor(new Color(180, 180, 180)); // Darker grid color
            subnetTable.setForeground(DARK_TEXT_COLOR);

            // Style header
            subnetTable.getTableHeader().setBackground(new Color(255, 240, 220));
            subnetTable.getTableHeader().setForeground(DARK_TEXT_COLOR);
            subnetTable.getTableHeader().setReorderingAllowed(false);

            // Use center renderer for subnet table with alternating row colors
            subnetTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() 
                {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
                        {
                            Component c = super.getTableCellRendererComponent(table, value, 
                                    isSelected, hasFocus, row, column);
                            setHorizontalAlignment(JLabel.CENTER);
                            setFont(new Font("Consolas", Font.BOLD, 13));
                            setForeground(DARK_TEXT_COLOR); // Black text

                            if (!isSelected) 
                                {
                                    if (row % 2 == 0) 
                                        {
                                            c.setBackground(new Color(255, 250, 240)); // Light orange for even rows
                                        } 
                                    else 
                                        {
                                            c.setBackground(Color.WHITE); // White for odd rows
                                        }
                                }
                            return c;
                        }
                });

            JScrollPane subnetScroll = new JScrollPane(subnetTable);
            // Add thin border around the table
            subnetScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1), // Thin gray border
                BorderFactory.createEmptyBorder(1, 1, 1, 1) // Small padding
            ));
            subnetPanel.add(subnetScroll, BorderLayout.CENTER);

            // Add both panels to split pane
            splitPane.setTopComponent(resultsPanel);
            splitPane.setBottomComponent(subnetPanel);

            panel.add(splitPane, BorderLayout.CENTER);

            return panel;
        }
    
    private JPanel createButtonPanel() 
        {
            // Create minimal panel for bottom
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
            panel.setBackground(BACKGROUND_COLOR);
            panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
            panel.setPreferredSize(new Dimension(0, 8)); // Minimal height

            return panel;
        }
    
    private JButton createStyledButton(String text) 
        {
            JButton button = new JButton(text);

            button.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Smaller font
            button.setBackground(Color.ORANGE); // Orange background
            button.setForeground(Color.BLACK); // White font
            button.setFocusPainted(false);

            // Simple border
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1),
                BorderFactory.createEmptyBorder(6, 15, 6, 15) // Reduced padding
            ));

            // Make button more visible with hover effect
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            button.addMouseListener(new java.awt.event.MouseAdapter() 
                {
                    public void mouseEntered(java.awt.event.MouseEvent evt) 
                        {
                            button.setBackground(PRIMARY_COLOR.brighter());
                        }

                    public void mouseExited(java.awt.event.MouseEvent evt) 
                        {
                            button.setBackground(PRIMARY_COLOR);
                        }
                });

            return button;
        }
    
    private class CalculateButtonListener implements ActionListener 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
                {
                    try 
                        {
                            if (!validateInputs()) 
                                {
                                    return;
                                }

                            int optChoice = optionComboBox.getSelectedIndex() + 1;
                            int neededHosts = -1;
                            int neededSubnet = -1;

                            switch (optChoice) 
                                {
                                    case 1:
                                        neededHosts = Integer.parseInt(neededHostsField.getText().trim());
                                        neededSubnet = 0;
                                        break;
                                    case 2:
                                        neededSubnet = Integer.parseInt(neededSubnetsField.getText().trim());
                                        neededHosts = 0;
                                        break;
                                    case 3:
                                        neededHosts = Integer.parseInt(neededHostsField.getText().trim());
                                        neededSubnet = Integer.parseInt(neededSubnetsField.getText().trim());
                                        break;
                                }

                            String netAddress = networkAddressField.getText().trim();

                            SubnetCalculator calculator = new SubnetCalculator();
                            calculator.performCalculations(optChoice, neededHosts, neededSubnet, netAddress);

                            String[][] subnetComponents = calculator.getSubnetComponents();
                            String[][] hostRange = calculator.getHostRange();

                            displayResultsInTable(subnetComponents, netAddress);
                            updateSubnetTable(hostRange, subnetComponents);

                        } 
                    catch (NumberFormatException ex) 
                        {
                            JOptionPane.showMessageDialog(SubnettingGUI.this,
                                "PLEASE ENTER VALID NUMBERS IN THE INPUT FIELDS.",
                                "INPUT ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    catch (Exception ex) 
                        {
                            JOptionPane.showMessageDialog(SubnettingGUI.this,
                                "ERROR: " + ex.getMessage().toUpperCase(),
                                "CALCULATION ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        }
                }
        }
    
    private boolean validateInputs() 
        {
            String address = networkAddressField.getText().trim();
            if (address.isEmpty()) 
                {
                    showError("PLEASE ENTER A NETWORK ADDRESS.");
                    return false;
                }

            String[] parts = address.split("\\.");
            if (parts.length != 4) 
                {
                    showError("INVALID IP ADDRESS FORMAT. USE FORMAT: XXX.XXX.XXX.XXX");
                    return false;
                }

            for (String part : parts) 
                {
                    try 
                        {
                            int num = Integer.parseInt(part);
                            if (num < 0 || num > 255) 
                                {
                                    showError("IP OCTET VALUE MUST BE BETWEEN 0 AND 255.");
                                    return false;
                                }
                        }
                    catch (NumberFormatException e) 
                        {
                            showError("IP ADDRESS MUST CONTAIN NUMBERS ONLY.");
                            return false;
                        }
                }

            int selected = optionComboBox.getSelectedIndex();
            try 
                {
                    switch (selected) 
                        {
                            case 0:
                                    if (neededHostsField.getText().trim().isEmpty()) 
                                        {
                                            showError("PLEASE ENTER NUMBER OF NEEDED HOSTS.");
                                            return false;
                                        }
                                    int hosts = Integer.parseInt(neededHostsField.getText().trim());
                                    if (hosts <= 0) 
                                        {
                                            showError("NUMBER OF HOSTS MUST BE GREATER THAN 0.");
                                            return false;
                                        }
                                    break;

                            case 1:
                                    if (neededSubnetsField.getText().trim().isEmpty()) 
                                        {
                                            showError("PLEASE ENTER NUMBER OF NEEDED SUBNETS.");
                                            return false;
                                        }
                                    int subnets = Integer.parseInt(neededSubnetsField.getText().trim());
                                    if (subnets <= 0) 
                                        {
                                            showError("NUMBER OF SUBNETS MUST BE GREATER THAN 0.");
                                            return false;
                                        }
                                    break;

                            case 2:
                                    if (neededHostsField.getText().trim().isEmpty() || neededSubnetsField.getText().trim().isEmpty()) 
                                            {
                                                showError("PLEASE ENTER BOTH NUMBER OF HOSTS AND SUBNETS.");
                                                return false;
                                            }
                                    int h = Integer.parseInt(neededHostsField.getText().trim());
                                    int s = Integer.parseInt(neededSubnetsField.getText().trim());
                                    if (h <= 0 || s <= 0) 
                                        {
                                            showError("BOTH HOSTS AND SUBNETS MUST BE GREATER THAN 0.");
                                            return false;
                                        }
                                    break;        
                        }
                } 
            catch (NumberFormatException e) 
                {
                    showError("PLEASE ENTER VALID NUMBERS.");
                    return false;
                }

            return true;
        }
    
    private void showError(String message) 
        {
            JOptionPane.showMessageDialog(this, 
                "<html><div style='padding:10px;font-weight:bold;'>" + message + "</div></html>", 
                "INPUT ERROR",
                JOptionPane.ERROR_MESSAGE);
        }
    
    private void displayResultsInTable(String[][] subnetComponents, String netAddress) 
        {
            DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();
            model.setRowCount(0);

            model.addRow(new Object[]{"IP ADDRESS CLASS", subnetComponents[0][0].toUpperCase()});
            model.addRow(new Object[]{"DEFAULT SUBNET MASK", subnetComponents[0][1].toUpperCase()});
            model.addRow(new Object[]{"CUSTOM SUBNET MASK", subnetComponents[0][2].toUpperCase()});
            model.addRow(new Object[]{"BITS BORROWED", subnetComponents[0][3].toUpperCase()});
            model.addRow(new Object[]{"TOTAL SUBNETS", subnetComponents[0][4].toUpperCase()});
            model.addRow(new Object[]{"TOTAL HOSTS PER SUBNET", subnetComponents[0][5].toUpperCase()});
            model.addRow(new Object[]{"USABLE HOSTS PER SUBNET", subnetComponents[0][6].toUpperCase()});
            model.addRow(new Object[]{"CIDR NOTATION", (netAddress + "/" + subnetComponents[0][7]).toUpperCase()});

            resultsTable.getColumnModel().getColumn(0).setPreferredWidth(250);
            resultsTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        }
    
    private void updateSubnetTable(String[][] hostRange, String[][] subnetComponents) 
        {
            DefaultTableModel model = (DefaultTableModel) subnetTable.getModel();
            model.setRowCount(0); // Clear existing rows

            int totalSubnets = Integer.parseInt(subnetComponents[0][4]);

            // Always show ALL rows, no matter how many
            for (int i = 0; i < totalSubnets; i++) 
                {
                    model.addRow(new Object[]{
                        i + 1,
                        hostRange[i][0].toUpperCase(),
                        hostRange[i][1].toUpperCase() + " - " + hostRange[i][2].toUpperCase(),
                        hostRange[i][3].toUpperCase()
                    });
                }

            for (int i = 0; i < subnetTable.getColumnCount(); i++) 
                {
                    subnetTable.getColumnModel().getColumn(i).setPreferredWidth(180);
                }
        }
    
    private void clearAll() 
        {
            optionComboBox.setSelectedIndex(0);
            neededHostsField.setText("");
            neededSubnetsField.setText("");
            networkAddressField.setText("");

            DefaultTableModel resultsModel = (DefaultTableModel) resultsTable.getModel();
            resultsModel.setRowCount(0);

            DefaultTableModel subnetModel = (DefaultTableModel) subnetTable.getModel();
            subnetModel.setRowCount(0);

            updateFieldVisibility();
        }
    
    // Inner class for center-aligned table cell rendering - ALL BLACK TEXT
    private class CenterTableCellRenderer extends DefaultTableCellRenderer 
        {
            public CenterTableCellRenderer() 
                {
                    setHorizontalAlignment(JLabel.CENTER);
                }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
                {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    setFont(new Font("Segoe UI", Font.BOLD, table == resultsTable ? 14 : 13));
                    setForeground(DARK_TEXT_COLOR); // ALL BLACK TEXT

                    return this;
                }
        }
    
    // Inner class for left-aligned table cell rendering - ALL BLACK TEXT
    private class LeftTableCellRenderer extends DefaultTableCellRenderer 
        {
            public LeftTableCellRenderer() 
                {
                    setHorizontalAlignment(JLabel.LEFT);
                }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
                {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setFont(new Font("Segoe UI", Font.BOLD, 14));
                    setForeground(DARK_TEXT_COLOR); // ALL BLACK TEXT
                    return this;
                }
        }
    
    public static void main(String[] args) 
        {
            try 
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                } 
            catch (Exception e) 
                {
                    e.printStackTrace();
                }

            SwingUtilities.invokeLater(() -> 
                {
                    SubnettingGUI gui = new SubnettingGUI();
                    gui.setVisible(true);
                });
        }
}