package org.ovirtChina.enginePlugin.isoUploaderPlugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.FileWriter;

public class CommandExecuter {

	private String lineAskingPassword = "Please provide the REST API password for the admin@internal oVirt Engine user (CTRL+D to abort):";

	public CommandExecuter() {}

  /**
  * Execute and return a Response for the command list
  */
  public void list(){
      System.out.println("Execute function ovirt-iso-uploader list");

      executeCommand("ovirt-iso-uploader list");

      System.out.println("End of the execution of the function ovirt-iso-uploader list");
  }

  /**
  * Execute and return a Response for the command upload
  */
  public void upload(){

  }

  /**
  * Execute command
  */
	private String executeCommand(String command) {

    System.out.println("Executing command: " + command);

		String[] commands = new String[]{"/bin/sh","-c",command};

		StringBuffer outputBuf = new StringBuffer();

		try {
			ProcessBuilder builder = new ProcessBuilder(commands);
			builder.redirectErrorStream(true);
			Process p = builder.start();

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

		  String line = "";

			line = reader.readLine();
			System.out.println(line);

			writer.write( "abc123\n" );
			writer.flush();

			while ((line = reader.readLine())!= null) {
				outputBuf.append(line + "\n");

				//System.out.println(line);
				System.out.println("New Output is: " + line);

				if (line.contains(lineAskingPassword)){
					System.out.println("CLI requests password");

					writer.write( "abc123\n" );
					writer.flush();
				}

			}



		// Process p;
		// try {
		// 	p = Runtime.getRuntime().exec(command);
		//
    //   Writer wr = new OutputStreamWriter(p.getOutputStream());
		//
		// 	//p.waitFor();
		// 	BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		//
    //   wr.write( "abc123\n" );
    //   wr.flush();
		//
    //   String line = "";
		// 	while ((line = reader.readLine())!= null) {
		// 		output.append(line + "\n");
		// 	}

		} catch (Exception e) {
			e.printStackTrace();
		}

    System.out.println("Output: " + outputBuf.toString().trim());
		return outputBuf.toString().trim();
	}

}
