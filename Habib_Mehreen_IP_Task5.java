    import java.sql.Connection;
	import java.sql.Statement;
	import java.util.Scanner;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.io.FileNotFoundException; 
	import java.io.FileReader;
	import java.io.FileWriter; 
	import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.*;
	import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;  
import java.io.FileWriter; 
	

	   

    public class Task5 {
		// Database credentials
		final static String HOSTNAME = "";
		final static String DBNAME = "";
		final static String USERNAME = "";
		final static String PASSWORD = "";
	 
		// Database connection string
		final static String URL = 
				String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
				HOSTNAME, DBNAME, USERNAME, PASSWORD);
	 
		// Query templates for each query 1-15
		// Every statement in each query got its own template
		// Note: some queries had to be split into multiple steps hence the notation: a, b, c
		final static String QUERY_TEMPLATE_1 = 
				"INSERT INTO Technical_staff " + 
				"VALUES (?, ?, ?, ?);" ;
		final static String QUERY_TEMPLATE_1a = 
				"INSERT INTO Quality_controller " + 
				"VALUES (?, ?, ?,? );" ;
		final static String QUERY_TEMPLATE_1b = 
				"INSERT INTO Worker " + 
				"VALUES (?, ?, ?, ?);";
		
		final static String QUERY_TEMPLATE_2 = 
				"INSERT INTO product " +
				"VALUES(?, ?, ?, ?, ?, ?);";
				
		final static String QUERY_TEMPLATE_3 = 
				"INSERT INTO customer " +
						"VALUES(?, ?);";
//				
		final static String QUERY_TEMPLATE_4 = 
				 "insert into account1 "+
				    "values (?,?,?);";
		final static String QUERY_TEMPLATE_4a = 
				 "insert into account2 "+
						 "values (?,?,?);";
		final static String QUERY_TEMPLATE_4b = 
						
				 "insert into account3 "+
						 "values (?,?,?);";
			
		
		final static String QUERY_TEMPLATE_5 = 
				"INSERT INTO Make " +
				"VALUES (?,?,?);";
		
		final static String QUERY_TEMPLATE_6 = 
				"insert into Got "+
						 "values (?,?,?);";
				
			
		final static String QUERY_TEMPLATE_6a = 
				"insert into GotAccident "+
						 "values (?,?,?);";
		
		final static String QUERY_TEMPLATE_7 = 
				"select date_produced, time_to_make"+" from product;";
		
		final static String QUERY_TEMPLATE_8 = 
				"select * "+"from product"+" where worker_name = ?;";
		
		
	
		
		final static String QUERY_TEMPLATE_9 = 
		
				"select count(*) as Total_errors from product, make  where  product.productid=make.productid AND product.qualitycontroller_name = ?;";
		
		final static String QUERY_TEMPLATE_10 = 
		"select  sum(cost) as TotalCost, qualitycontroller_name from account3, Record_cost3,product, product3 where account3.account3_num = Record_cost3.Account3_num and product3.productid = product.productid group by cost , qualitycontroller_name;";

		
		final static String QUERY_TEMPLATE_11 = 
				"select purchase.cname, product2.color from purchase, product2  where purchase.productid= product2.productid order by cname;";
		
		
		
		final static String QUERY_TEMPLATE_12 = 
//				
				
"(select technicalstaff_name from technical_staff where salary > ? )union (select qualitycontroller_name from quality_controller where quality_controller.qualitycontroller_salary > ? )union (select worker_name from worker where worker_salary > ?);";
		
		final static String QUERY_TEMPLATE_13 = 
				"select sum(days_lost)AS daysLost from accident where accident_num is not null;";
		
		final static String QUERY_TEMPLATE_14= 
			"((select avg (account1.cost) from account1  where DateEstablished >= ?) union (select avg (account2.cost) from account2 where DateEstablished > = ?) union (select avg (account3.cost) from account3 where DateEstablished > = ?) );";

				
		
		
		final static String QUERY_TEMPLATE_15= 
				
				"delete"+" from accident"+" where date_of_accident between ? and ?;";
		final static String QUERY_TEMPLATE_16= 
				"INSERT INTO Technical_staff " + 
						"VALUES (?, ?, ?, ?);" ;
				
				//import
		final static String QUERY_TEMPLATE_17= 
				"select purchase.cname, product2.color from purchase, product2  where purchase.productid= product2.productid order by cname;";
		

		
		
		
		// User input prompt//
		final static String PROMPT = 
				"(1) Enter a new employee \n" + 
				"(2) Enter a new product associated with the person who made the product, repaired the product if it is repaired, or checked the product \n" + 
				"(3) Enter a customer associated with some products \n" + 
				"(4) Create a new account associated with a product  \n" + 
				"(5) Enter a complaint associated with a customer and product  \n" + 
				"(6) Enter an accident associated with an appropriate employee and product\n" + 
				"(7) Retrieve the date produced and time spent to produce a particular product  \n" + 
				"(8) Retrieve all products made by a particular worker \n" + 
				"(9) Retrieve the total number of errors a particular quality controller made. This is the total number of products certified by this controller and got some complaints \n" + 
				"(10) Retrieve the total costs of the products in the product3 category which were repaired at the request of a particular quality controller \n" + 
				"(11) Retrieve all customers (in name order) who purchased all products of a particular color \n" + 
				"(12) Retrieve all employees whose salary is above a particular salary \n" + 
				"(13) Retrieve the total number of work days lost due to accidents in repairing the products which got complaints \n" + 
				"(14) Retrieve the average cost of all products made in a particular year\n" + 
				"(15) Delete all accidents whose dates are in some range \n" + 
				"(16) Import \n" + 
				"(17) Export \n" + 
				"(18) Quit";
		   
private static final String ANSI_Green_BACKGROUND = null;

	
	public static void main(String[] args) throws SQLException {
		System.out.println("WELCOME TO THE DATABASE SYSTEM OF MyProducts, Inc");
		final Scanner sc = new Scanner(System.in); // Scanner is used to collect the user input
		String option = ""; // Initialize user option selection as nothing
		
		while (!option.equals("18")) { // Ask user for options until options 16, 17, or 18 are selected
			System.out.println(PROMPT); // Print the available options
			option = sc.next(); // Read in the user option selection
		
			switch (option) { // Switch between different options
				case "1": // Insert a new Employee
					//Depending on the type of process, ask for different inputs and perform a different query
					System.out.println("Please enter Employee i.e. Technical staff or quality controller or worker:");
					sc.nextLine();
					 final String employe_type = sc.nextLine();		
					if(employe_type.equalsIgnoreCase("Technical_staff")) {
						// Collect the new data from the user
						System.out.println("Please enter Technical staff name:");
						final String Technicalstaff_name44 = sc.nextLine();
						System.out.println("Please enter Technical staff address:");
						final String address = sc.nextLine();
						System.out.println("Please enter technical position:");
						final String technical_position = sc.nextLine();
						System.out.println("Please enter Technical staff salary:");
						final float salary = sc.nextInt();
						
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_1)) {
								// Populate the query template with the data collected from the user
								statement.setString(1, Technicalstaff_name44);
								statement.setString(2, address);
								statement.setString(3,technical_position);
								statement.setFloat(4, salary);
								
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							
							}
						}
					}
					else if(employe_type.equalsIgnoreCase("quality_controller")) {

						System.out.println("Please enter quality controller name:");
						final String qualitycontroller_name5 = sc.nextLine();
	 
						System.out.println("Please enter quality controller address):");
						final String qualitycontroller_address = sc.nextLine();
						
						System.out.println("Please enter quality controller salary :");
						final Float qualitycontroller_salary = sc.nextFloat();
						System.out.println("Please enter max product type:");
						final int product_type = sc.nextInt();
						
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_1a)) {
								statement.setString(1, qualitycontroller_name5);
								statement.setString(2, qualitycontroller_address);
								statement.setFloat(3, qualitycontroller_salary);
								statement.setInt(4,product_type);
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}
						}
					}
					else if(employe_type.equalsIgnoreCase("worker")) {
						System.out.println("Please enter worker name:");
						final String worker_name = sc.nextLine();
						System.out.println("Please enter worker address:");
						final String worker_address = sc.nextLine();
						System.out.println("Please enter worker salary :");
						final Float worker_salary = sc.nextFloat();
						System.out.println("Please enter max products daily:");
						final int max_products_daily = sc.nextInt();
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_1b)) {
								// Populate the query template with the data collected from the user
								statement.setString(1, worker_name);
								statement.setString(2, worker_address);
								statement.setFloat(3, worker_salary);
								statement.setInt(4, max_products_daily);
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
								 
							}
						}
					}
					else {
						System.out.println("Error: Invalid type");
						break;
					}
 
	              break;

				case "2": // Insert into product.// Collect the new data from the user
					System.out.println("Please enter Product ID:");
					final int productid90 = sc.nextInt();
					System.out.println("Please enter date produced:");
					sc.nextLine();
					final String date_produced00 = sc.nextLine();
					System.out.println("Please enter time to make):");
					final String time_to_make8= sc.nextLine();
					System.out.println("Please enter worker name):");
				    final String Worker_name9 = sc.nextLine();
				    System.out.println("Please enter quality controller name):");
     			    final String qualitycontroller_name80 = sc.nextLine();
					System.out.println("Please enter technical staff name):");
                    final String technicalstaff_name80  = sc.nextLine();
					System.out.println("Connecting to the database...");
					// Get the database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_2)) {
						
							statement.setInt(1,productid90);
 						    statement.setString(2,date_produced00);
							statement.setString(3,time_to_make8);
							statement.setString(4,Worker_name9);
							statement.setString(5,qualitycontroller_name80);
							statement.setString(6,technicalstaff_name80);
							
							System.out.println("Dispatching the query...");
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
						}					
					}					
			       break;
				case "3": // Insert into customer
					// Collect the new data from the user
					System.out.println("Please enter customer name");
					sc.nextLine();
					final String cname = sc.nextLine();
					System.out.println("Please enter customer address:");
					final String caddress = sc.nextLine();
					System.out.println("Connecting to the database...");
					// Get the database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) {
						try (
							final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_3)) {
						
							statement.setString(1, cname);
							statement.setString(2, caddress);
							
							System.out.println("Dispatching the query...");
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
						}					
					}					
			       break;
//			
			
					      

					case "4": // Insert into product
									//Depending on the type of process, ask for different inputs and perform a different query
							System.out.println("Please enter account type(account1, account2, or account3):");
							sc.nextLine();
							final String account_type = sc.nextLine();		
							if(account_type.equalsIgnoreCase("account1")) {
								// Collect the new data from the user
								System.out.println("Please enter account 1 number:");
								final int account_1_num  = sc.nextInt();
								sc.nextLine();
								System.out.println("Please enter date established):");
								final String date_established = sc.nextLine();
											
								System.out.println("Please enter product_cost:");
								final Float product_cost5= sc.nextFloat();
											
								System.out.println("Connecting to the database...");
								
								// Get a database connection and prepare a query statement
								try (final Connection connection = DriverManager.getConnection(URL)) {
									try (
										final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_4)) {
														// Populate the query template with the data collected from the user
										 statement.setInt(1, account_1_num);
										 statement.setString(2,date_established );
										 statement.setFloat(3, product_cost5);
										 final int rows_inserted = statement.executeUpdate();
										 System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
													
										}
									}
								}
							else if(account_type.equalsIgnoreCase("account2")) {

								 System.out.println("Please enter account 2 number:");
								 final int account_2_num1  = sc.nextInt();
								 sc.nextLine();	
								 System.out.println("Please enter date established):");
								 final String date_established1 = sc.nextLine();	
								 System.out.println("Please enter product_cost:");
								 final Float product_cost1 = sc.nextFloat();
										
													  
								 System.out.println("Connecting to the database...");
										// Get a database connection and prepare a query statement
								 try (final Connection connection = DriverManager.getConnection(URL)) {
								try (
									final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_4a)) {
												// Populate the query template with the data collected from the user
									 statement.setInt(1, account_2_num1);
									 statement.setString(2,date_established1);
									 statement.setFloat(3, product_cost1);
									 final int rows_inserted = statement.executeUpdate();
									 System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
								}
							}
						}
							 else if(account_type.equalsIgnoreCase("account3")) {
									System.out.println("Please enter account 3 number:");
									final int account_3_num1  = sc.nextInt();
					 
									System.out.println("Please enter date established):");
									sc.nextLine();
									final String date_established2 = sc.nextLine();
										
									System.out.println("Please enter product_cost:");
									final Float product_cost2 = sc.nextFloat();
										
									System.out.println("Connecting to the database...");
										// Get a database connection and prepare a query statement
									try (final Connection connection = DriverManager.getConnection(URL)) {
								        try (
											final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_4b)) {
												// Populate the query template with the data collected from the user
											statement.setInt(1, account_3_num1);
											statement.setString(2,date_established2);
											statement.setFloat(3, product_cost2);
											final int rows_inserted = statement.executeUpdate();
											System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
										}
									}
								}
									else {
										System.out.println("Error: Invalid type");
										break;
									}
				 
					       break;

				
					  case "5": 
						//"INSERT INTO Make " + "insert into complaint"
						// Collect the new data from the user
    					System.out.println("Please enter the customer name:");
						sc.nextLine();
						final String cname1 = sc.nextLine();
						System.out.println("Please enter the product id:");
						final int productid9 = sc.nextInt();
						System.out.println("Please enter the complaint id:");
						final int complaintid4 = sc.nextInt();
//						
//						System.out.println("Please enter the complaint id:");
//						final int complaintid4 = sc.nextInt();
//						System.out.println("Please enter the date:");
//						sc.nextLine();
//						final String date1 = sc.nextLine();
//						System.out.println("Please enter the description:");
//						sc.nextLine();
//						final String description1 = sc.nextLine();
//						System.out.println("Please enter the expected treatement:");
//						final String expected_treatmentt = sc.nextLine();
						 														
						System.out.println("Connecting to the database...");
						// Get the database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5)) {
							// Populate the query template with the data collected from the user
								statement.setString(1, cname1);
							    statement.setInt(2, productid9);
							    statement.setInt(3, complaintid4);
//								statement.setInt(1, complaintid4);
//								statement.setString(2, date1);
//								statement.setString(3, description1);
//								statement.setString(4, expected_treatmentt);
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}					
						}	
						
				       break;
					case "6": // INSERT INTO got or gotaccident 
						System.out.println("Please enter Employee if it is Technical staff enter into got or if it is a worker then enter into got accident:");
						sc.nextLine();
						final String Employee_type = sc.nextLine();		
						if(Employee_type.equalsIgnoreCase("Technical_staff")) {
							// Collect the new data from the user
							System.out.println("Please enter accident num:");
					        final int accident_number = sc.nextInt();
							sc.nextLine();
							System.out.println("Please enter product id:");
							final int pid = sc.nextInt();
							sc.nextLine();
							System.out.println("Please enter technical staff name:");
							final String technicalstaff_namee = sc.nextLine();
										
							System.out.println("Connecting to the database...");
							
							// Get a database connection and prepare a query statement
							try (final Connection connection = DriverManager.getConnection(URL)) {
								try (
									final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_6)) {
													// Populate the query template with the data collected from the user
									statement.setInt(1, accident_number);
									statement.setInt(2, pid);
									statement.setString(3,technicalstaff_namee);
									 final int rows_inserted = statement.executeUpdate();
									 System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
												
									}
								}
							}
						else if(Employee_type.equalsIgnoreCase("worker")) {
							System.out.println("Please enter product id:");
							final int pidd = sc.nextInt();
							
							 sc.nextLine();	
							 System.out.println("Please enter worker name:");
//								sc.nextLine();
							 final String worker_namee = sc.nextLine();	
							 System.out.println("Please enter accident num:");
						      final int accident_numberr = sc.nextInt();
									
												  
							 System.out.println("Connecting to the database...");
									// Get a database connection and prepare a query statement
							 try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_6a)) {
											// Populate the query template with the data collected from the user
								statement.setInt(1, pidd);
								statement.setString(2,worker_namee);
								statement.setInt(3, accident_numberr);
								 final int rows_inserted = statement.executeUpdate();
								 System.out.println(String.format("Done. %d row(s) inserted.\n", rows_inserted));
							}
						}
					}
						
								else {
									System.out.println("Error: Invalid type");
									break;
								}
			 
				       break;

			
//							
					 case "7":

						 try (final Connection connection = DriverManager.getConnection(URL)) {
						    System.out.println("Dispatching the query...");
						        try (
						           final Statement statement = connection.createStatement();
						           final ResultSet resultSet = statement.executeQuery(QUERY_TEMPLATE_7)) {
									// Populate the query template with the data collected from the user
														
									System.out.println("Contents of product table...");
						
									System.out.println(String.format("Date | Time to make")); 
									
									while (resultSet.next()) {
										System.out.println(String.format("%s | %s ",
												resultSet.getString(1),
												resultSet.getString(2)));
									}						
								}
							}
							
							
							break;
					 case "8":
					 
							System.out.println("Please enter worker name:");
							sc.nextLine();
							final String wn = sc.nextLine();
							//System.out.println("Connecting to the database...");
							// Get the database connection and prepare a query statement
							try (final Connection connection = DriverManager.getConnection(URL)) {
								try (
									final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_8)) {
									// Populate the query template with the data collected from the user
									statement.setString(1, wn);			
									System.out.println("Dispatching the query...");
									final ResultSet resultSet = statement.executeQuery();
									System.out.println(String.format("PId |Date | Time |worker name | QC name | TS name"));
									
									while (resultSet.next()) {
										System.out.println(String.format("%d | %s| %s | %s| %s |%s",
												resultSet.getInt(1),
												resultSet.getString(2),
												resultSet.getString(3),
												resultSet.getString(4),
												resultSet.getString(5),
												resultSet.getString(6)));
									}						
								}
							}
							
							
							break;
//			
//								
//						
//					
					case "9":
						System.out.println("Please enter quality controller name:");
						final String qcc= sc.nextLine();
					
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
									final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_9)) {
									// Populate the query template with the data collected from the user
									statement.setString(1, qcc);			
									System.out.println("Dispatching the query...");
									final ResultSet resultSet = statement.executeQuery();
														
									System.out.println("Contents of table after fetching data ...");
						
									System.out.println(String.format("Total_Errors")); 
									
									while (resultSet.next()) {
										System.out.println(String.format(" %s ",
												resultSet.getString(1)
												));
									}
									
						        }
						        
						}

						
						break;
					case "10":
						try (final Connection connection = DriverManager.getConnection(URL)) {
						    System.out.println("Dispatching the query...");
						        try (
						           final Statement statement = connection.createStatement();
						           final ResultSet resultSet = statement.executeQuery(QUERY_TEMPLATE_10)) {
									// Populate the query template with the data collected from the user
														
									System.out.println("Contents of  particular table...");
						
									System.out.println(String.format("Total_cost | qualitycontroller_name")); 
									
									while (resultSet.next()) {
										System.out.println(String.format("%s | %s ",
												resultSet.getString(1),
												resultSet.getString(2)));
									}						
								}
							}
							
							
						
						
						break;
					case "11":
						try (final Connection connection = DriverManager.getConnection(URL)) {
						    System.out.println("Dispatching the query...");
						        try (
						           final Statement statement = connection.createStatement();
						           final ResultSet resultSet = statement.executeQuery(QUERY_TEMPLATE_11)) {
									// Populate the query template with the data collected from the user
														
									System.out.println("Contents of customer with particular color ...");
						
									System.out.println(String.format("cname | color")); 
									
									while (resultSet.next()) {
										System.out.println(String.format("%s | %s ",
												resultSet.getString(1),
												resultSet.getString(2)));
									}						
								}
							}
							
							
							break;
						
						
					case "12":
						System.out.println("Please enter technical staff salary:");
						sc.nextLine();
						final Float s = sc.nextFloat();
						System.out.println("Please enter quality controller salary:");
						sc.nextLine();
						final Float q = sc.nextFloat();
						System.out.println("Please enter worker salary:");
						final Float w = sc.nextFloat();
						//System.out.println("Connecting to the database...");
						// Get the database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_12)) {
								// Populate the query template with the data collected from the user
								statement.setFloat(1, s);
								statement.setFloat(2, q);
								statement.setFloat(3, w);
								System.out.println("Dispatching the query...");
								final ResultSet resultSet = statement.executeQuery();
								System.out.println(String.format("Employees with salary greater than a particular salary "));
								
								while (resultSet.next()) {
									System.out.println(String.format("%s",
									
											resultSet.getString(1)));
								}						
							}
						}
						
						
						break;	
						
					case "13":
						try (final Connection connection = DriverManager.getConnection(URL)) {
						    System.out.println("Dispatching the query...");
						        try (
						           final Statement statement = connection.createStatement();
						           final ResultSet resultSet = statement.executeQuery(QUERY_TEMPLATE_13)) {
									// Populate the query template with the data collected from the user
														
									System.out.println("total number of workdays lost due to accidents in repairing the products ...");
						
									System.out.println(String.format("Total_days_lost")); 
									
									while (resultSet.next()) {
										System.out.println(String.format(" %s ",
												
												resultSet.getString(1)));
									}
									
						        }
						        
						}
						break;
					case "14":
//				
						System.out.println("Please enter year for account1:");
						sc.nextLine();
						final String y1 = sc.nextLine();
						System.out.println("Please enter year for account2:");
						
						final String y2 = sc.nextLine();
						System.out.println("Please enter year for account3:");
						final String y3 = sc.nextLine();
						//System.out.println("Connecting to the database...");
						// Get the database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) {
							try (
								final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_14)) {
								// Populate the query template with the data collected from the user
								statement.setString(1, y1);	
								statement.setString(2, y2);	
								statement.setString(3, y3);	
								System.out.println("Dispatching the query...");
								final ResultSet resultSet = statement.executeQuery();
								System.out.println(String.format("Avg cost of product"));
								
								while (resultSet.next()) {
									System.out.println(String.format("%s",
											resultSet.getString(1)));
								}						
							}
						}
						
						
						break;
					case "15":
						System.out.println("Please enter the lower bound for accident date:");
						final String lower_date= sc.nextLine();
						sc.nextLine();
						System.out.println("Please enter the upper bound for accident date:");
						
						final String upper_date = sc.nextLine();
						
						System.out.println("Connecting to the database...");
						// Get the database connection, create statement and execute it right away, as no user input need be collected
						try (final Connection connection = DriverManager.getConnection(URL)) {
						
							try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_15)) {
								// Populate the query template with the data collected from the user
								statement.setString(1, lower_date);
								statement.setString(2, upper_date);
								System.out.println("Dispatching the query...");
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d row(s) updated.\n", rows_inserted));
//							}
//						}
//						
							}}
//					
							break;
				
					
					
					
					case "16":  
						try {
							  System.out.println("Please enter file path ");
							 
						      File myObj = new File("C:\\Users\\mehre\\Music\\Desktop\\file.csv");
						      Scanner myReader = new Scanner(myObj);
						      while (myReader.hasNextLine()) {
						        String data = myReader.nextLine();
						        System.out.println(data);
						      }
						      myReader.close();
						    } catch (FileNotFoundException e) {
						      System.out.println("An error occurred.");
						      e.printStackTrace();
						    }
						break;
					case "17": 
						   try {
							      FileWriter myWriter = new FileWriter("filename.txt");
							      myWriter.write("");
							      myWriter.close();
							      System.out.println("Successfully wrote to the file.");
							    } catch (IOException e) {
							      System.out.println("An error occurred.");
							      e.printStackTrace();
							    }
//					    
//	 
						break;
					case "18": // Do nothing, the while loop will terminate upon the next iteration
						System.out.println("Exiting! Good-bye!");
						
						break;
					default: // Unrecognized option, re-prompt the user for the correct one
						System.out.println(String.format(
								"Unrecognized option: %s\n" + 
								"Please try again!", 
								option));
						
						break;
				}
			}
	 
			sc.close(); // Close the scanner before exiting the application
		}

	}
	
	
		
		
