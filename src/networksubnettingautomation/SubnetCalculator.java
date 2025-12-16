package networksubnettingautomation;
import java.lang.Math;

public class SubnetCalculator {
    private String[][] subnetComponents;
    private String[][] hostRange;
    
    private int[][] IPclassA = 
        {
            {16777216, 8388608, 4194304, 2097152, 1048576, 524288, 262144, 131072,
             65536, 32768, 16384, 8192, 4096, 2048, 1024, 512,
             256, 128, 64, 32, 16, 8, 4, 2},
            {2, 4, 8, 16, 32, 64, 128, 256,
             512, 1024, 2048, 4096, 8192, 16384, 32768, 65536,
             131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216},
            {128, 64, 32, 16, 8, 4, 2, 1, 128, 64, 32, 16, 8, 4, 2, 1, 128, 64, 32, 16, 8, 4, 2, 1}
        };
    
    private int[][] IPclassB = 
        {
            {65536, 32768, 16384, 8192, 4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2},
            {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536},
            {128, 64, 32, 16, 8, 4, 2, 1, 128, 64, 32, 16, 8, 4, 2, 1}
        };
    
    private int[][] IPclassC = 
        {
            {256, 128, 64, 32, 16, 8, 4, 2},
            {2, 4, 8, 16, 32, 64, 128, 256},
            {128, 64, 32, 16, 8, 4, 2, 1}
        };
    
    public void performCalculations(int optChoice, int neededHosts, int neededSubnet, String netAddress) 
        {
            subnetComponents = new String[1][8];
            String[] parts = netAddress.split("\\.");

            determineIPClass(parts);

            // Handle case when neededHosts or neededSubnet is 0 (from GUI)
            if (optChoice == 1) 
                {
                    neededSubnet = 0; // Not used for hosts-only calculation
                } 
            else if (optChoice == 2) 
                {
                    neededHosts = 0; // Not used for subnets-only calculation
                }

            int bitsBorrowed = calculateBitsBorrowed(optChoice, neededHosts, neededSubnet);
            calculateCustomMask(bitsBorrowed);
            calculateTotalSubnets(optChoice, neededHosts, neededSubnet, bitsBorrowed);
            calculateTotalHosts(bitsBorrowed);
            calculateUsableHosts();
            calculateCIDR(bitsBorrowed);
            calculateHostRanges(parts, bitsBorrowed);
        }
    
    private void determineIPClass(String[] parts) 
        {
            int firstOctet = Integer.parseInt(parts[0]);

            if (firstOctet <= 127) 
                {
                    subnetComponents[0][0] = "A";
                    subnetComponents[0][1] = "255.0.0.0";
                } 
            else if (firstOctet <= 191) 
                {
                    subnetComponents[0][0] = "B";
                    subnetComponents[0][1] = "255.255.0.0";
                } 
            else if (firstOctet <= 223) 
                {
                    subnetComponents[0][0] = "C";
                    subnetComponents[0][1] = "255.255.255.0";
                } 
            else 
                {
                    subnetComponents[0][0] = "Invalid";
                    subnetComponents[0][1] = "N/A";
                }
        }
    
    private int calculateBitsBorrowed(int optChoice, int neededHosts, int neededSubnet) 
        {
            String ipClass = subnetComponents[0][0];
            int bitsBorrowed = 0;

            if (ipClass.equals("A")) 
                {
                    bitsBorrowed = calculateForClass(optChoice, neededHosts, neededSubnet, IPclassA);
                }
            else if (ipClass.equals("B")) 
                {
                    bitsBorrowed = calculateForClass(optChoice, neededHosts, neededSubnet, IPclassB);
                }
            else if (ipClass.equals("C")) 
                {
                    bitsBorrowed = calculateForClass(optChoice, neededHosts, neededSubnet, IPclassC);
                }

            subnetComponents[0][3] = Integer.toString(bitsBorrowed);
            return bitsBorrowed;
        }
    
    private int calculateForClass(int optChoice, int neededHosts, int neededSubnet, int[][] ipClass) 
        {
            int bitsBorrowed = 0;

            if (optChoice == 1) 
                {
                    // Hosts only - find bits needed for given hosts
                    for (int col = ipClass[0].length - 1; col >= 0; col--) 
                        {
                            if (neededHosts < ipClass[0][col]) 
                                {
                                    bitsBorrowed = col;
                                    break;
                                }
                        }
                }
            else if (optChoice == 2) 
                {
                    // Subnets only - find bits needed for given subnets
                    for (int col = 0; col < ipClass[1].length; col++) 
                        {
                            if (neededSubnet <= ipClass[1][col]) 
                                {
                                    bitsBorrowed = col + 1;
                                    break;
                                }
                        }
                } 
            else if (optChoice == 3) 
                {
                    // Both hosts and subnets
                    for (int col = 0; col < ipClass[1].length; col++) 
                        {
                            int hostIndex = col + 1;
                            if (hostIndex >= ipClass[0].length) break;

                            if (neededSubnet <= ipClass[1][col] && neededHosts < ipClass[0][hostIndex]) 
                                {
                                    bitsBorrowed = hostIndex;
                                    break;
                                }
                        }
                }

            return bitsBorrowed;
        }
    
    private void calculateCustomMask(int bitsBorrowed) 
        {
            String ipClass = subnetComponents[0][0];
            int s = 0, t = 0, f = 0;

            if (ipClass.equals("A")) 
                {
                    for (int i = 0; i < bitsBorrowed; i++) 
                        {
                            if (i < 8) s += IPclassA[2][i];
                            else if (i < 16) t += IPclassA[2][i];
                            else f += IPclassA[2][i];
                        }
                    subnetComponents[0][2] = "255." + s + "." + t + "." + f;
                }
            else if (ipClass.equals("B")) 
                {
                    for (int i = 0; i < bitsBorrowed; i++) 
                        {
                            if (i < 8) t += IPclassB[2][i];
                            else f += IPclassB[2][i];
                        }
                    subnetComponents[0][2] = "255.255." + t + "." + f;
                }
            else if (ipClass.equals("C")) 
                {
                    for (int i = 0; i < bitsBorrowed; i++) 
                        {
                            f += IPclassC[2][i];
                        }
                    subnetComponents[0][2] = "255.255.255." + f;
                }
        }
    
    private void calculateTotalSubnets(int optChoice, int neededHosts, int neededSubnet, int bitsBorrowed) 
        {
            String ipClass = subnetComponents[0][0];

            if (ipClass.equals("A")) 
                {
                    handleClassA(optChoice, neededHosts, neededSubnet);
                } 
            else if (ipClass.equals("B")) 
                {
                    handleClassB(optChoice, neededHosts, neededSubnet, bitsBorrowed);
                } 
            else if (ipClass.equals("C")) 
                {
                    handleClassC(optChoice, neededHosts, neededSubnet);
                }
        }
    
    private void handleClassA(int optChoice, int neededHosts, int neededSubnet) {
        if (optChoice == 1) 
            {
                for (int col = IPclassA[0].length - 1; col >= 0; col--) 
                    {
                        if (neededHosts < IPclassA[0][col]) 
                            {
                                subnetComponents[0][4] = Integer.toString(col > 0 ? IPclassA[1][col - 1] : IPclassA[1][0]);
                                break;
                            }
                    }
            }
        else 
            {
                for (int col = 0; col < IPclassA[1].length; col++) 
                    {
                        if (neededSubnet <= IPclassA[1][col]) 
                            {
                                subnetComponents[0][4] = Integer.toString(IPclassA[1][col]);
                                break;
                            }
                    }
            }
    }
    
    private void handleClassB(int optChoice, int neededHosts, int neededSubnet, int bitsBorrowed) 
        {
            if (optChoice == 1) 
                {
                    for (int col = IPclassB[0].length - 1; col >= 0; col--) 
                        {
                            if (neededHosts < IPclassB[0][col]) 
                                {
                                    subnetComponents[0][4] = Integer.toString(col > 0 ? IPclassB[1][col - 1] : IPclassB[1][0]);
                                    break;
                                }
                        }
                } 
            else if (optChoice == 2) 
                {
                    for (int col = 0; col < IPclassB[1].length; col++) 
                        {
                            if (neededSubnet <= IPclassB[1][col]) 
                                {
                                    subnetComponents[0][4] = Integer.toString(IPclassB[1][col]);
                                    break;
                                }
                        }
                } 
            else 
                {
                    subnetComponents[0][4] = Integer.toString((int) Math.pow(2, bitsBorrowed));
                }
        }
    
    private void handleClassC(int optChoice, int neededHosts, int neededSubnet) 
        {
            if (optChoice == 1) 
                {
                    for (int col = IPclassC[0].length - 1; col >= 0; col--) 
                        {
                            if (neededHosts < IPclassC[0][col]) 
                                {
                                    subnetComponents[0][4] = Integer.toString(col > 0 ? IPclassC[1][col - 1] : IPclassC[1][0]);
                                    break;
                                }
                        }
                } 
            else 
                {
                    for (int col = 0; col < IPclassC[1].length; col++) 
                        {
                            if (neededSubnet <= IPclassC[1][col]) 
                                {
                                    subnetComponents[0][4] = Integer.toString(IPclassC[1][col]);
                                    break;
                                }
                        }
                }
        }
    
    private void calculateTotalHosts(int bitsBorrowed) 
        {
            String ipClass = subnetComponents[0][0];
            int hostBits = 0;

            if (ipClass.equals("A")) hostBits = 24 - bitsBorrowed;
            else if (ipClass.equals("B")) hostBits = 16 - bitsBorrowed;
            else if (ipClass.equals("C")) hostBits = 8 - bitsBorrowed;

            subnetComponents[0][5] = Integer.toString((int) Math.pow(2, hostBits));
        }
    
    private void calculateUsableHosts() 
        {
            int totalHosts = Integer.parseInt(subnetComponents[0][5]);
            subnetComponents[0][6] = Integer.toString(totalHosts - 2);
        }
    
    private void calculateCIDR(int bitsBorrowed) 
        {
            String ipClass = subnetComponents[0][0];
            int networkBits = 0;

            if (ipClass.equals("A")) networkBits = 8;
            else if (ipClass.equals("B")) networkBits = 16;
            else if (ipClass.equals("C")) networkBits = 24;

            subnetComponents[0][7] = Integer.toString(networkBits + bitsBorrowed);
        }
    
    private void calculateHostRanges(String[] parts, int bitsBorrowed) 
        {
            int totalSubnets = Integer.parseInt(subnetComponents[0][4]);
            hostRange = new String[totalSubnets][4];

            int b1 = Integer.parseInt(parts[0]);
            int b2 = Integer.parseInt(parts[1]);
            int b3 = Integer.parseInt(parts[2]);
            int b4 = Integer.parseInt(parts[3]);

            String[] mask = subnetComponents[0][2].split("\\.");
            int subnetOctet = (!mask[3].equals("0")) ? 4 : (!mask[2].equals("0")) ? 3 : 2;
            int blockSize = 256 - Integer.parseInt(mask[subnetOctet - 1]);

            int o2 = b2, o3 = b3, o4 = b4;

            for (int i = 0; i < totalSubnets; i++) 
                {
                    hostRange[i][0] = b1 + "." + o2 + "." + o3 + "." + o4;
                    hostRange[i][1] = b1 + "." + o2 + "." + o3 + "." + (o4 + 1);
                    hostRange[i][2] = b1 + "." + o2 + "." + o3 + "." + (o4 + blockSize - 2);
                    hostRange[i][3] = b1 + "." + o2 + "." + o3 + "." + (o4 + blockSize - 1);

                    if (subnetOctet == 4) 
                        {
                            o4 += blockSize;
                            if (o4 >= 256) 
                                { 
                                    o4 = 0; o3++; 
                                }
                        } 
                    else if (subnetOctet == 3) 
                        {
                            o3 += blockSize;
                            if (o3 >= 256) 
                                { 
                                    o3 = 0; o2++; 
                                }
                        } 
                    else 
                        {
                            o2 += blockSize;
                        }
                }
        }
    
    public String[][] getSubnetComponents() 
        {
            return subnetComponents;
        }
    
    public String[][] getHostRange() 
        {
            return hostRange;
        }
}